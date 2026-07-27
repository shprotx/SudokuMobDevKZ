export const meta = {
  name: 'sudoku-wave',
  description: 'Волна GitHub issues параллельно: exec в worktree → adversarial review → security → draft PR',
  whenToUse: 'Прогон волны независимых issues SudokuMobDevKZ параллельно (замена scripts/execution-loop.sh: worktree + draft PR вместо прямых коммитов). args: {issues:[111,112], base:"develop", maxParallel:3, includeDiscussion:false}',
  phases: [
    { title: 'Exec', detail: 'sonnet в git worktree: реализация по issue, assembleDebug', model: 'sonnet' },
    { title: 'Review', detail: 'fable: adversarial-ревью по гейтам issue + конвенции' },
    { title: 'Security', detail: 'fable: security-пасс по диффу (Android/Firebase чеклист)' },
    { title: 'Finalize', detail: 'sonnet: фиксы findings, push, draft PR, коммент в issue', model: 'sonnet' },
  ],
}

const REPO = 'shprotx/SudokuMobDevKZ'
const MAIN = '/Users/artur/StudioProjects/SudokuMobDevKZ'
const WT_ROOT = '/Users/artur/StudioProjects/SudokuMobDevKZ-wt'

const parsedArgs = typeof args === 'string' ? JSON.parse(args) : (args || {})
const BASE = parsedArgs.base || 'develop'
const MAX_PARALLEL = parsedArgs.maxParallel || 3
const INCLUDE_DISCUSSION = !!parsedArgs.includeDiscussion
const rawIssues = Array.isArray(parsedArgs.issues) ? parsedArgs.issues : []
if (!rawIssues.length) {
  return { error: 'args.issues пуст — передай волну номеров, например {"issues":[111,112]}' }
}
const issues = rawIssues.map((x) => (typeof x === 'number' ? { n: x, extra: '' } : { n: x.n, extra: x.extra || '' }))

const EXEC_SCHEMA = {
  type: 'object',
  required: ['skipped', 'summary'],
  properties: {
    skipped: { type: 'boolean' },
    skip_reason: { type: 'string' },
    branch: { type: 'string' },
    base_sha: { type: 'string' },
    worktree_path: { type: 'string' },
    summary: { type: 'string' },
    files: { type: 'array', items: { type: 'string' } },
    build_passed: { type: 'boolean' },
    tests_passed: { type: 'boolean' },
    caveats: { type: 'string' },
  },
}

const VERDICT_SCHEMA = {
  type: 'object',
  required: ['verdict', 'findings'],
  properties: {
    verdict: { type: 'string', enum: ['pass', 'fix_needed'] },
    findings: {
      type: 'array',
      items: {
        type: 'object',
        required: ['severity', 'problem', 'fix'],
        properties: {
          severity: { type: 'string', enum: ['critical', 'major', 'minor'] },
          file: { type: 'string' },
          problem: { type: 'string' },
          fix: { type: 'string' },
        },
      },
    },
    acceptance_unmet: { type: 'array', items: { type: 'string' } },
    notes: { type: 'string' },
  },
}

const FINAL_SCHEMA = {
  type: 'object',
  required: ['pr_url', 'status'],
  properties: {
    pr_url: { type: 'string' },
    status: { type: 'string', enum: ['clean', 'with_warnings', 'failed'] },
    notes: { type: 'string' },
  },
}

const HARD_RULES = `ЖЁСТКИЕ ПРАВИЛА КОДА (SudokuMobDevKZ, нарушение = блокер ревью; полный свод — CLAUDE.md в корне worktree):
- НИКАКИХ комментариев в Kotlin-коде (// и /* */), никаких TODO/FIXME. Код самодокументируемый.
- Не оставлять пустую строку в конце файла.
- Все user-facing строки ТОЛЬКО из strings.xml. НОВЫЕ строки добавлять во ВСЕ 8 локалей (values, -ru, -kk, -de, -es, -fr, -pt, -uk) с реальными переводами — английские заглушки в не-en локалях ЗАПРЕЩЕНЫ.
- Никаких хардкод dp/цветов: только AppTheme.colors.* / AppTheme.paddings.* / AppTheme.sizes.* / AppTheme.typography.*. Запрещены Android Color.* константы.
- Trailing comma на всех многострочных списках параметров.
- Один public class / composable на файл (исключение: sealed с наследниками). private composable запрещён — использовать internal.
- UIState — иммутабельный data class, только immutable-коллекции + copy(...).
- Screen/ScreenContent split: Screen подписывается на state+effects (collectAsState/collectAsStateWithLifecycle + LaunchedEffect(viewModel.effect)); ScreenContent — чистый UI, принимает ТОЛЬКО (uiState, onEvent), без ViewModel и nav-колбэков.
- Навигация ТОЛЬКО через UIEffect, обрабатывается в LaunchedEffect внутри Screen. Nav args — savedStateHandle.toRoute<RouteClass>().
- Диалоги через флаги UIState + UIEvent dismiss (не rememberSaveable).
- ViewModel наследует BaseViewModel<Event, State, Effect>; setEvent/updateState/setEffect; ошибки через exceptionHandler.
- handleUIEvent: сначала ветки data object, потом data class (is X.Foo).
- Hilt constructor injection; модули в di/.
- Никакого Scaffold в экранах: Box/Column + .background(); statusBarsPadding() сверху, navigationBarsPadding() снизу.
- Снекбары через SnackbarManager.show(res).
- Изменения темы (statusBarColor, windowBackground и т.п.) — синхронно во все 4 файла themes.xml (values, values-night, values-v31, values-night-v31).
- Room: schema-изменения ТОЛЬКО с ручной Migration (ALTER TABLE) и bump version — НЕ полагаться на fallbackToDestructiveMigration, он снесёт данные юзеров.
- Modifier — первый параметр. Без Spacer — padding/arrangement.`

function execPrompt(it, attempt, prevFail) {
  const retryNote = attempt > 1 ? `
ЭТО ПОВТОРНАЯ ПОПЫТКА (${attempt}/2). Прошлая упала: ${prevFail}. Worktree и ветку пересоздай с нуля, причину прошлого фейла устрани в первую очередь.` : ''
  const extra = it.extra ? `
ДОП. УКАЗАНИЯ ПО ЭТОМУ ISSUE:
${it.extra}` : ''
  return `Ты — имплементатор. Задача: ПОЛНОСТЬЮ реализовать GitHub issue #${it.n} репозитория ${REPO} (Android-судоку: 100% Kotlin + Jetpack Compose + Hilt + MVI + Room + Retrofit/Firebase REST, feature-based модули, package ru.shprot.sudokumobdevkz).
${retryNote}
ШАГ 0 — ГЕЙТЫ (до любой работы):
  gh issue view ${it.n} -R ${REPO} --json state,labels,title,body
  - state не OPEN → верни skipped=true, skip_reason="issue closed", БОЛЬШЕ НИЧЕГО НЕ ДЕЛАЙ.
  ${INCLUDE_DISCUSSION ? '- Гейт needs-discussion отключён волной (includeDiscussion=true).' : '- В labels есть needs-discussion → верни skipped=true, skip_reason="needs-discussion gate", БОЛЬШЕ НИЧЕГО НЕ ДЕЛАЙ.'}

РАБОЧАЯ КОПИЯ — изолированный git worktree ПЕРВЫМ ДЕЛОМ:
  WT="${WT_ROOT}/issue-${it.n}"
  git -C "${MAIN}" worktree remove --force "$WT" 2>/dev/null; git -C "${MAIN}" branch -D wave/issue-${it.n} 2>/dev/null
  git -C "${MAIN}" worktree add "$WT" -b wave/issue-${it.n} ${BASE}
  cp "${MAIN}/local.properties" "$WT/local.properties"
  cd "$WT"
Дальше работай ТОЛЬКО внутри "$WT" — основную копию ${MAIN} НЕ трогать и НЕ собирать.
base_sha = git rev-parse ${BASE} (зафиксируй до начала).

ПОИСК ПО КОДУ:
  Сначала выполни в "$WT": ast-index update (инициализация индекса worktree). Затем используй ast-index (symbol/class/usages/callers/refs/outline/file) для ВСЕЙ навигации по символам. Если ast-index в worktree не заработал — выполняй ast-index команды из ${MAIN} (пути 1:1, читай файлы по ним в "$WT"). grep — только для строковых литералов/XML/JSON.

КОНТЕКСТ (прочитай внутри "$WT"):
  1. Issue целиком — секции «Корень», «Что сделать», «Поведение», «Реализация», «Гейт», «Out of scope» (если есть). Разложенные в issue file:line-якоря — проверяй актуальность, код мог сдвинуться.
  2. CLAUDE.md в корне worktree — архитектура (MVI, Screen/ScreenContent, BaseViewModel) и антипаттерны обязательны.

ПОРЯДОК:
  1. Реализуй ВСЕ пункты раздела «Что сделать». «Out of scope» — жёсткая граница.
  2. Напиши юнит-тесты, которые issue требует в «Гейте».
  3. Верификация из "$WT":
     ./gradlew assembleDebug --max-workers=4  — обязан пройти.
     ./gradlew testDebugUnitTest --max-workers=4  — обязан пройти (юнит-тесты гоняются всегда).
     Инструментальные и ручную проверку на эмуляторе — пропусти (отметь в caveats).
  4. Пройди «Гейт» issue как чеклист; невыполнимое локально — в caveats, НЕ имитируй.

${HARD_RULES}

GIT (в worktree разрешено и нужно коммитить):
- Новые файлы — git add сразу после создания.
- Коммиты логическими шагами В СВОЕЙ ветке, формат: "feat(#${it.n}): описание по-русски" (fix/refactor по смыслу). Без Generated-подписей.
- НЕ push. НЕ pr create. НЕ трогать ветку ${BASE} и основную копию.
${extra}
Верни СТРОГО структурированный результат (если не skipped): skipped=false, branch, base_sha, worktree_path (вывод pwd), summary (3–6 предложений), files (ключевые), build_passed, tests_passed, caveats.`
}

function reviewPrompt(it, ex) {
  return `Adversarial-ревью реализации issue #${it.n} (${REPO}). Ты НЕ правишь код — только вердикт.

Рабочая копия автора: ${ex.worktree_path} — все команды через git -C и пути оттуда. База диффа: ${ex.base_sha}.

1. gh issue view ${it.n} -R ${REPO} --json title,body — прочитай. Ключевые секции: «Что сделать», «Поведение», «Гейт», «Out of scope».
2. Дифф: git -C ${ex.worktree_path} diff ${ex.base_sha}..HEAD и log --oneline.
3. Прогони САМ (не верь отчёту автора), из ${ex.worktree_path}:
   ./gradlew assembleDebug --max-workers=4
   ./gradlew testDebugUnitTest --max-workers=4
4. Пройди КАЖДЫЙ пункт «Гейта» — невыполненные перечисли в acceptance_unmet (кроме явно ручных/эмуляторных — их в notes).
5. Проверь, что файлы вне зоны issue не тронуты.
6. Конвенции (нарушение = major):
${HARD_RULES}
7. Ищи реальные дефекты: корректность Room-миграций (данные юзеров НЕ теряются), сохранение/восстановление состояния игры, гонки в корутинах, забытые ветки when, регрессы соседних фич (меню, настройки, daily challenge, статистика), локализация всех 8 локалей без EN-заглушек.
8. Заявление автора: "${ex.summary}". Caveats: "${ex.caveats || 'нет'}". Проверь честность.

ast-index доступен из worktree (или из ${MAIN} с путями 1:1) — используй для cross-reference (usages/callers) вместо grep.

Вердикт pass — ТОЛЬКО если нет critical/major и acceptance_unmet пуст. Каждый finding: severity, file, problem (конкретно, со строкой), fix. Не выдумывай проблем ради количества.`
}

function securityPrompt(it, ex) {
  return `Security-пасс по диффу issue #${it.n} (${REPO} — Android-судоку, Google Play, Firebase REST + Play Games Services). Только вердикт, кода не править.

Дифф: git -C ${ex.worktree_path} diff ${ex.base_sha}..HEAD

CRITICAL:
- Реальные секреты в коде/гите (keystore-пароли, PLAY_SERVICE_ACCOUNT json, Firebase-ключи вне BuildConfig/local.properties).
- SQL-инъекции в Room (SimpleSQLiteQuery со строковой конкатенацией).
- Небезопасная десериализация недоверенного JSON (kotlinx polymorphic без allowlist).
- Cleartext HTTP в OkHttp/Retrofit или networkSecurityConfig вне localhost.
- Room-миграция, теряющая данные юзеров (destructive fallback на изменённой схеме).

HIGH:
- PII в логах (никнеймы, leaderboard id, email).
- Чувствительные данные плейнтекстом в DataStore/Preferences (токены, session).
- Exported Activity/Service/Receiver в манифесте без permission.
- Firebase-пути, позволяющие писать чужие данные (leaderboard/статистика без скоупа юзера).

MEDIUM:
- Verbose-логирование без гейта BuildConfig.DEBUG; тестовые URL в release-путях; неограниченные размеры при чтении в память; сервисные JSON, случайно добавленные в git.

Вердикт: pass / fix_needed + findings (severity, file, problem, fix). Только реальные проблемы ИЗ ДИФФА.`
}

function finalizePrompt(it, acc) {
  const allFindings = [...(acc.review.findings || []), ...(acc.security.findings || [])]
  const unmet = acc.review.acceptance_unmet || []
  return `Финализация issue #${it.n} (${REPO}). Рабочая копия: ${acc.exec.worktree_path}, ветка ${acc.exec.branch}, база ${acc.exec.base_sha}.

Findings ревью+security (JSON): ${JSON.stringify(allFindings)}
Невыполненные пункты гейта: ${JSON.stringify(unmet)}

1. Каждый critical/major finding и каждый невыполненный пункт гейта — ИСПРАВЬ в worktree. Minor — исправь если дёшево (< ~15 строк), иначе перечисли в PR как known issues. Соблюдай те же жёсткие правила кода, что у автора (CLAUDE.md в worktree).
2. После правок: ./gradlew assembleDebug --max-workers=4 + testDebugUnitTest — зелёные. Коммит: "fix(#${it.n}): правки по ревью".
3. git -C ${acc.exec.worktree_path} push -u origin ${acc.exec.branch}
4. Draft PR: gh pr create -R ${REPO} --draft --base ${BASE} --head ${acc.exec.branch} --title "<type>(#${it.n}): <суть по-русски>" --body с секциями: "Refs #${it.n}" (НЕ Closes — issue закрываем после мержа), Summary, Review verdict (${acc.review.verdict}) и Security verdict (${acc.security.verdict}) с таблицей findings (исправлено/known), Tests, Caveats: ${JSON.stringify(acc.exec.caveats || 'нет')}.
5. Комментарий в issue: gh issue comment ${it.n} -R ${REPO} --body "PR: <url> — sudoku-wave (exec=sonnet, review/security=fable). Вердикты: review=${acc.review.verdict}, security=${acc.security.verdict}."
6. ТОЛЬКО после успешного push: git -C "${MAIN}" worktree remove --force ${acc.exec.worktree_path} (ветка живёт в origin). Push не удался → worktree НЕ удалять.

status: clean (всё исправлено, сборка зелёная) / with_warnings (known minors или невыполнимые локально пункты) / failed (critical не исправлен — причина в notes). Верни pr_url, status, notes.`
}

async function runExec(it) {
  let prevFail = ''
  for (let attempt = 1; attempt <= 2; attempt++) {
    const ex = await agent(execPrompt(it, attempt, prevFail), {
      label: `exec:#${it.n}${attempt > 1 ? ':retry' : ''}`, phase: 'Exec', model: 'sonnet',
      schema: EXEC_SCHEMA,
    })
    if (!ex) { prevFail = 'агент умер без результата'; continue }
    if (ex.skipped) return ex
    if (ex.build_passed) return ex
    prevFail = `build_passed=false: ${ex.summary} / caveats: ${ex.caveats || '-'}`
    log(`#${it.n} exec attempt ${attempt} провален: ${prevFail.slice(0, 160)}`)
  }
  return null
}

function chunk(arr, size) {
  const out = []
  for (let i = 0; i < arr.length; i += size) out.push(arr.slice(i, i + size))
  return out
}

log(`Волна: ${issues.length} issues (${issues.map((i) => '#' + i.n).join(', ')}), base=${BASE}, параллельность=${MAX_PARALLEL}, конвейер 4 стадий`)

const results = []
for (const batch of chunk(issues, MAX_PARALLEL)) {
  const batchResults = await pipeline(
    batch,
    (it) => runExec(it),
    async (ex, it) => {
      if (!ex) throw new Error(`exec #${it.n} провалился дважды`)
      if (ex.skipped) return { skipped: true, reason: ex.skip_reason }
      const review = await agent(reviewPrompt(it, ex), {
        label: `review:#${it.n}`, phase: 'Review', model: 'fable', effort: 'max', schema: VERDICT_SCHEMA,
      })
      if (!review) throw new Error(`review #${it.n} не вернул вердикт`)
      log(`#${it.n} review: ${review.verdict} (${review.findings.length} findings, unmet: ${(review.acceptance_unmet || []).length})`)
      return { exec: ex, review }
    },
    async (acc, it) => {
      if (!acc || acc.skipped) return acc
      const security = await agent(securityPrompt(it, acc.exec), {
        label: `security:#${it.n}`, phase: 'Security', model: 'fable', effort: 'high', schema: VERDICT_SCHEMA,
      })
      if (!security) throw new Error(`security #${it.n} не вернул вердикт`)
      log(`#${it.n} security: ${security.verdict} (${security.findings.length} findings)`)
      return { ...acc, security }
    },
    async (acc, it) => {
      if (!acc || acc.skipped) return { issue: it.n, status: 'skipped', notes: acc ? acc.reason : 'exec failed' }
      const fin = await agent(finalizePrompt(it, acc), {
        label: `finalize:#${it.n}`, phase: 'Finalize', model: 'sonnet', schema: FINAL_SCHEMA,
      })
      if (!fin) throw new Error(`finalize #${it.n} не вернул результат`)
      log(`#${it.n} → ${fin.status}: ${fin.pr_url}`)
      return {
        issue: it.n,
        pr_url: fin.pr_url,
        status: fin.status,
        review: acc.review.verdict,
        security: acc.security.verdict,
        findings_total: acc.review.findings.length + acc.security.findings.length,
        acceptance_unmet: acc.review.acceptance_unmet || [],
        notes: fin.notes || '',
        branch: acc.exec.branch,
      }
    },
  )
  results.push(...batchResults)
}

const done = results.filter(Boolean)
const failed = issues.filter((it, i) => !results[i]).map((it) => it.n)
if (failed.length) log(`НЕ завершены (упали в конвейере): ${failed.map((n) => '#' + n).join(', ')}`)
return { completed: done, failed, base: BASE }
