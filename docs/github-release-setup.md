# Настройка автоматического релиза APK на GitHub

## Как работает

При пуше тега `v*` (например `v1.2.0`) GitHub Actions автоматически:
1. Собирает release APK с подписью
2. Создаёт GitHub Release с APK для скачивания

Workflow уже создан: `.github/workflows/release.yml`

## Что нужно настроить

GitHub → Settings → Secrets and variables → Actions → New repository secret

| Secret | Значение |
|---|---|
| `KEYSTORE_BASE64` | Keystore в base64 (см. ниже) |
| `KEYSTORE_PASS` | Пароль от keystore |
| `KEYSTORE_ALIAS` | Alias ключа |
| `KEYSTORE_ALIAS_PASS` | Пароль от alias |

### Как получить KEYSTORE_BASE64

```bash
base64 -i путь/к/keystore.jks | pbcopy
```

Результат из буфера обмена вставить в секрет `KEYSTORE_BASE64`.

## Как выпустить релиз

```bash
git tag v1.2.0
git push origin v1.2.0
```

APK появится на странице Releases репозитория.
