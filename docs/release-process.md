# Релизный процесс

## Версионирование

Схема — `MAJOR.MINOR.PATCH[-prerelease.N]` в `versionName`. `versionCode` всегда монотонно растёт.

| Назначение | `versionName` | `versionCode` |
|---|---|---|
| Internal testing | `2.6.0-internal.1`, `.2`, `.3`, … | +1 на каждый аплоад |
| Closed/Open beta (опц.) | `2.6.0-beta.1`, `.2`, … | +1 |
| Production | `2.6.0` | +1 |
| Hotfix internal | `2.6.1-internal.1` | +1 |
| Hotfix production | `2.6.1` | +1 |

`BuildConfig.IS_PRE_RELEASE` автоматически становится `true`, если `versionName` содержит `-`. Используется в коде для условного включения debug-индикаторов в pre-release-сборках.

## Конфигурация

Service Account JSON-ключ для Play Console читается из `local.properties` (или env-переменной `PLAY_SERVICE_ACCOUNT_FILE`):

```
PLAY_SERVICE_ACCOUNT_FILE=/абсолютный/путь/к/keyfile.json
```

`local.properties` в `.gitignore`. JSON-ключ должен лежать **вне** репозитория. Права на файл: `chmod 600`.

Если `PLAY_SERVICE_ACCOUNT_FILE` не задан — `play{}` блок устанавливает `enabled.set(false)`, и плагин не ломает обычную сборку.

## Подготовка релиза

1. В `app/build.gradle` поднять `versionCode` (+1) и обновить `versionName` согласно схеме.
2. Локально проверить:
   ```bash
   ./gradlew clean assembleDebug
   ./gradlew testDebugUnitTest
   ```
3. Закоммитить bump одним коммитом: `BUMP: 2.6.0-internal.2 (versionCode 30) — описание изменений`.

## Загрузка в Internal Testing

```bash
./gradlew publishReleaseBundle
```

`track=internal` и `releaseStatus=DRAFT` уже прописаны в `play{}` блоке `app/build.gradle`, явно указывать не нужно. AAB загружается в Internal Testing как черновик — нужно будет промоутить в Active внутри Play Console (или скриптом ниже).

## Активация (Draft → Active в Internal)

После `publishReleaseBundle` релиз остаётся в статусе `DRAFT`. Чтобы тестеры увидели обновление:

- Play Console → Testing → Internal testing → Releases → найти черновик → **Review release** → **Start rollout to Internal testing**.

Это страховка от случайной публикации.

## Промоут из Internal в Production

После того как версия отстоялась в Internal и проверена тестерами:

```bash
./gradlew promoteArtifact \
    --from-track=internal \
    --promote-track=production \
    --release-status=draft
```

Опять же — статус `draft`, дальше вручную в Play Console «Start rollout to Production».

При полной уверенности можно `--release-status=completed` — релиз пойдёт в production сразу после прохода Google review.

## Hotfix-flow

1. Создать ветку `hotfix/X.Y.Z-internal.1` от текущей production-версии.
2. Бамп: `versionName 2.6.1-internal.1`, `versionCode +1`.
3. `./gradlew publishReleaseBundle` → Internal.
4. После проверки → промоут в production: `./gradlew promoteArtifact --from-track=internal --promote-track=production`.

## Откат

API не позволяет «откатить» релиз — только опубликовать новую версию с фиксом. В Play Console можно остановить раскатку (Halt rollout) для активного релиза. Но уже установленные пользователям версии назад не вернутся.

Поэтому **не пропускать internal**: каждый production-релиз должен сначала отстояться в Internal Testing минимум 24-48 часов.

## Безопасность

- Никогда не коммитить SA-JSON-ключ.
- Раз в 6-12 месяцев пересоздавать SA-ключ в Google Cloud Console.
- Минимальный набор прав в Play Console: `Manage testing track releases` + опционально `Manage production releases`. Никогда не выдавать SA роль `Admin`.

## Доступные таски GPP

```
./gradlew tasks --all | grep -i publish
```

Основные:
- `publishReleaseBundle` — заливает AAB в Internal track (через `play{ track = "internal" }`).
- `publishApk` — то же самое, но APK (не используем, у нас AAB).
- `promoteArtifact` — перенос релиза между треками.
- `bootstrapListing` — скачать текущие listing-метаданные с Play Console (известный баг GPP 3.x: падает на deprecated `inappproducts` endpoint, для нашего use case не нужен).
