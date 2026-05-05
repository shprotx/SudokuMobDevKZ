# Auto-Update Feature Implementation Plan

## Overview

On every app launch, `FirstActivity` (splash screen) makes an async call to the GitHub Releases API to check whether a newer APK version is available. If the remote `tag_name` is greater than the running `versionName`, a `BottomSheetDialog` is shown with the release notes, an "Update" button, and a progress bar. The user can either start the download or dismiss the sheet and continue into the app. Downloading fetches the APK to the app's external cache dir, then installs it via `FileProvider` + `ACTION_INSTALL_PACKAGE`. On any API failure or if the versions match, the existing 1 000 ms splash timer fires normally and the app continues.

## Existing Patterns Used as Reference

- **Network call pattern** — `FirebaseSync.java` (`/app/src/main/java/ru/shprot/sudokumobdevkz/model/FirebaseSync.java`) uses raw `HttpURLConnection`, an `ExecutorService`, a `Handler(Looper.getMainLooper())` callback, and `org.json.JSONObject` for JSON parsing. `UpdateChecker` will follow exactly this pattern.
- **Background-to-main-thread hand-off** — `FirebaseSync.fetchPercentile` calls `executor.execute(() -> { ... mainHandler.post(() -> callback.onResult(...)); })`. `ApkDownloader` will use the same shape.
- **Splash navigation** — `FirstActivity` posts a `Runnable` via `handler.postDelayed(navigateRunnable, 1000)` and removes it in `onStop`. The update check must cancel this runnable while the sheet is visible and re-post it on dismiss.
- **Bottom-sheet dialogs** — The project does not yet have a `BottomSheetDialogFragment`; the closest existing dialog pattern is `fragment_upvote_dialog.xml` + `MaterialCardView`. The update sheet will use `BottomSheetDialog` directly (not a Fragment) to keep it lightweight, consistent with the no-DI, no-Fragment-manager-required approach of `FirstActivity`.
- **Shared constants** — All string keys live in `Library.java`; the `UPDATE_CHECK_PREFS` key will be added there.

## Files to Modify

| File | Change |
|---|---|
| `app/src/main/AndroidManifest.xml` | Add `REQUEST_INSTALL_PACKAGES` permission; add `<provider>` for `FileProvider` |
| `app/src/main/java/ru/shprot/sudokumobdevkz/FirstActivity.java` | Inject update-check flow into `onCreate`, cancel/restore the splash `Runnable` |
| `app/src/main/java/ru/shprot/sudokumobdevkz/model/game/utils/Library.java` | Add `UPDATE_PREFS`, `UPDATE_SKIPPED_VERSION` constants |
| `app/src/main/res/values/strings.xml` | Add update-related strings |
| `app/proguard-rules.pro` | No changes needed — `JSONObject` is part of the Android SDK and not shrunk; `HttpURLConnection` is the same |

## Files to Create

| File | Purpose |
|---|---|
| `app/src/main/java/ru/shprot/sudokumobdevkz/model/update/UpdateChecker.java` | Calls GitHub API on a background thread, parses JSON, returns `UpdateInfo` via callback on main thread |
| `app/src/main/java/ru/shprot/sudokumobdevkz/model/update/UpdateInfo.java` | Plain data holder: `tagName`, `releaseNotes`, `downloadUrl` |
| `app/src/main/java/ru/shprot/sudokumobdevkz/model/update/ApkDownloader.java` | Downloads the APK from `downloadUrl` to `getExternalCacheDir()`, reports progress, triggers install intent |
| `app/src/main/res/layout/bottom_sheet_update.xml` | BottomSheet UI: title, version badge, release-notes `ScrollView`, progress bar, "Update" + "Later" buttons |
| `app/src/main/res/xml/file_paths.xml` | FileProvider paths config (`external-cache-path` for the downloaded APK) |

## Dependencies

No new Gradle dependencies. Everything is available:

- `org.json.JSONObject` — Android SDK built-in
- `java.net.HttpURLConnection` — Java standard library
- `androidx.core.content.FileProvider` — part of `androidx.appcompat:appcompat` already in `build.gradle`
- `com.google.android.material.bottomsheet.BottomSheetDialog` — part of `com.google.android.material:material:1.13.0` already in `build.gradle`
- `java.util.concurrent.Executors` — Java standard library

## Step-by-Step Implementation

### Step 1 — Create `UpdateInfo.java`

Path: `app/src/main/java/ru/shprot/sudokumobdevkz/model/update/UpdateInfo.java`

A simple POJO with three fields:
```
String tagName        // e.g. "v1.4.0"
String releaseNotes   // body field from GitHub API
String downloadUrl    // assets[0].browser_download_url
```
No annotations, no Room, no Parcelable required.

### Step 2 — Create `UpdateChecker.java`

Path: `app/src/main/java/ru/shprot/sudokumobdevkz/model/update/UpdateChecker.java`

Public interface:
```java
public interface Callback {
    void onUpdateAvailable(UpdateInfo info);
    void onNoUpdate();
    void onError();
}

public static void check(String currentVersion, ExecutorService executor,
                         Handler mainHandler, Callback callback)
```

Implementation mirrors `FirebaseSync.fetchPercentile`:
1. `executor.execute(() -> { ... })` — background thread
2. `HttpURLConnection` GET to `https://api.github.com/repos/shprotx/SudokuMobDevKZ/releases/latest`
   - `connectTimeout = 5000`, `readTimeout = 5000`
   - `setRequestProperty("Accept", "application/vnd.github+json")`
3. Read response into `StringBuilder` with `BufferedReader`
4. Parse `JSONObject`: extract `tag_name`, `body`, `assets[0].browser_download_url`
5. Compare versions: strip leading `"v"` from `tag_name`, split by `"."`, compare int arrays component by component
6. Call back `onUpdateAvailable(info)`, `onNoUpdate()`, or `onError()` via `mainHandler.post()`

Version comparison logic (avoids depending on `BuildConfig` inside the model layer):
```java
private static boolean isNewer(String remote, String current) {
    // strip "v", split by ".", parse ints, lexicographic int comparison
}
```

### Step 3 — Create `ApkDownloader.java`

Path: `app/src/main/java/ru/shprot/sudokumobdevkz/model/update/ApkDownloader.java`

Public interface:
```java
public interface Callback {
    void onProgress(int percent);
    void onSuccess(File apkFile);
    void onError();
}

public static void download(Context context, String url, ExecutorService executor,
                            Handler mainHandler, Callback callback)
```

Implementation:
1. `executor.execute(() -> { ... })`
2. Destination: `new File(context.getExternalCacheDir(), "update.apk")`
3. `HttpURLConnection` GET, follow redirects (`setInstanceFollowRedirects(true)`)
4. Read `Content-Length` header for progress calculation
5. Copy `InputStream` → `FileOutputStream` in 8 KB chunks; after each chunk call `mainHandler.post(() -> callback.onProgress(percent))`
6. On completion: `mainHandler.post(() -> callback.onSuccess(apkFile))`
7. On any exception: `mainHandler.post(() -> callback.onError())`

Install trigger method (called from `FirstActivity` on `onSuccess`):
```java
public static void installApk(Context context, File apkFile) {
    Uri apkUri = FileProvider.getUriForFile(context,
        context.getPackageName() + ".fileprovider", apkFile);
    Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
    intent.setData(apkUri);
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
}
```

### Step 4 — Create `file_paths.xml`

Path: `app/src/main/res/xml/file_paths.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-cache-path name="apk_downloads" path="." />
</paths>
```

`external-cache-path` maps to `context.getExternalCacheDir()`. No `READ_EXTERNAL_STORAGE` permission is required because the app owns this directory.

### Step 5 — Update `AndroidManifest.xml`

Add inside `<manifest>` before `<application>`:
```xml
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
```

Add inside `<application>` (after the existing `<activity>` entries):
```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

Note: `INTERNET` permission already exists.

### Step 6 — Create `bottom_sheet_update.xml`

Path: `app/src/main/res/layout/bottom_sheet_update.xml`

Structure (LinearLayout vertical, `wrap_content` height):
- `MaterialTextView` — title: "New version available" + version name badge
- `MaterialDivider`
- `ScrollView` containing `MaterialTextView` for release notes (max 160 dp height)
- `ProgressBar` (horizontal, style `Widget.AppCompat.ProgressBar.Horizontal`) — initially `GONE`
- `soup.neumorphism.NeumorphButton` — "Update" button (same `@style/NeoButtons` as upvote dialog)
- `MaterialButton` style `OutlinedButton` — "Later" / "Not now"

Follow the theming attributes already used in `fragment_upvote_dialog.xml`:
- `android:background="?attr/pageBackground"`
- `android:fontFamily="?attr/fontFamily"`
- Use `@dimen/` SDP values for spacing

### Step 7 — Add strings to `strings.xml`

```xml
<string name="update_available_title">New version available</string>
<string name="update_button">Update</string>
<string name="update_later">Later</string>
<string name="update_downloading">Downloading…</string>
<string name="update_error">Download failed. Try again later.</string>
```

### Step 8 — Update `Library.java`

Add constants at the bottom of the constants block:
```java
public static final String UPDATE_PREFS = "updatePrefs";
public static final String UPDATE_SKIPPED_VERSION = "skippedVersion";
```

`UPDATE_SKIPPED_VERSION` lets us remember if the user tapped "Later" for a specific tag — so we don't re-show the sheet on the next launch for the same version. On next launch, compare `tag_name` to the stored skipped version; if equal, skip the sheet.

### Step 9 — Update `FirstActivity.java`

The existing flow:
```
onCreate → handler.postDelayed(navigateRunnable, 1000)
onStop   → handler.removeCallbacks(navigateRunnable)
```

New flow:
```
onCreate
  └─ handler.removeCallbacks(navigateRunnable)   // cancel the 1 000 ms timer
  └─ UpdateChecker.check(versionName, executor, mainHandler, new Callback() {
        onUpdateAvailable(info) → showUpdateSheet(info)
        onNoUpdate()            → handler.postDelayed(navigateRunnable, 300)
        onError()               → handler.postDelayed(navigateRunnable, 300)
     })
```

`showUpdateSheet(UpdateInfo info)`:
1. Inflate `bottom_sheet_update.xml` with `getLayoutInflater()`
2. Create `BottomSheetDialog(this)`, set the inflated view
3. Set `dialog.setCancelable(false)` while downloading; `true` while idle
4. "Later" button: store skipped version in SharedPreferences, dismiss, `handler.postDelayed(navigateRunnable, 0)`
5. "Update" button:
   - Disable button, hide "Later", show `ProgressBar`
   - Call `ApkDownloader.download(...)`:
     - `onProgress(p)` → update `ProgressBar.setProgress(p)`
     - `onSuccess(file)` → `ApkDownloader.installApk(this, file)` + dismiss sheet (user sees system installer)
     - `onError()` → re-enable button, show toast, re-enable dismiss
6. `dialog.setOnCancelListener(...)` → `handler.postDelayed(navigateRunnable, 0)`

`ExecutorService` should be a field: `private final ExecutorService executor = Executors.newSingleThreadExecutor();`

Override `onDestroy` to call `executor.shutdownNow()`.

Skipped-version check before showing the sheet:
```java
SharedPreferences prefs = getSharedPreferences(Library.UPDATE_PREFS, MODE_PRIVATE);
String skipped = prefs.getString(Library.UPDATE_SKIPPED_VERSION, "");
if (skipped.equals(info.getTagName())) {
    handler.postDelayed(navigateRunnable, 0);
    return;
}
```

### Step 10 — Verify ProGuard

No additional rules are required:
- `JSONObject` / `HttpURLConnection` are part of the Android SDK and not subject to R8 shrinking.
- `FileProvider` is an AndroidX class kept automatically by the AAR consumer rules bundled with `appcompat`.
- `BottomSheetDialog` is similarly kept by material's consumer rules.
- The new `model/update/` classes are app code; R8 will inline/rename them, but since they are only called by name from `FirstActivity` (not via reflection), no `-keep` rule is needed.

## Testing Strategy

### Unit tests (JVM)

Create `UpdateCheckerTest.java` in `app/src/test/`:
- Mock `HttpURLConnection` response to a known JSON string, assert `onUpdateAvailable` fires with correct fields.
- Test `isNewer()` with same, older, newer, and malformed version strings.

### Manual / smoke tests

1. Build debug APK with `versionName "1.2.0"` (older than the live GitHub release).
   - Expect: sheet appears with correct title and release notes.
   - Tap "Later": sheet dismisses, app continues to `MainActivity`.
   - Re-launch: sheet does NOT appear (skipped version stored).

2. Build with current `versionName "1.3.0"` matching the live release.
   - Expect: no sheet, app continues normally after ~300 ms.

3. Force `onError` by disabling network.
   - Expect: no sheet, app continues normally after ~300 ms.

4. Tap "Update" with network available.
   - Expect: progress bar fills, system installer appears after download.
   - Install the APK, verify the app updates.

5. Tap "Update" then background the app mid-download.
   - Expect: `onStop` removes `navigateRunnable`, `onDestroy` shuts down executor. No crash.

### Instrumented tests

`FirstActivityTest.java` using `ActivityScenario`:
- Stub `UpdateChecker` with a fake executor that immediately calls `onUpdateAvailable` on main thread.
- Assert `BottomSheetDialog` is displayed.
- Click "Later", assert `MainActivity` is launched.

## Risks & Open Questions

1. **GitHub API rate limit** — Unauthenticated requests are limited to 60/hour per IP. For an individual APK distributed outside Play Store this is almost certainly never an issue, but if the app grows large, consider adding a `Cache-Control` check or caching the last response in SharedPreferences with a TTL.

2. **HTTPS redirect on asset URL** — GitHub release asset `browser_download_url` redirects through `objects.githubusercontent.com`. `HttpURLConnection` follows redirects by default (`setInstanceFollowRedirects` defaults to `true`), but cross-protocol redirects (HTTP → HTTPS) are NOT automatically followed. The asset URL is already HTTPS, so this should not be a problem in practice. Still worth verifying.

3. **External cache dir availability** — `getExternalCacheDir()` returns `null` if external storage is not mounted. `ApkDownloader.download` must null-check and call `onError()` in that case, falling back gracefully.

4. **Screen rotation during download** — `FirstActivity` has `android:configChanges="keyboardHidden|screenSize"` so portrait orientation changes are handled, but if the user somehow rotates (e.g. landscape tablet), the activity would recreate and the download reference would be lost. Since `screenOrientation="portrait"` is set, this is not a real risk, but the executor shutdown in `onDestroy` handles it safely anyway.

5. **API 26+ requirement for `REQUEST_INSTALL_PACKAGES`** — `minSdk 24` means the install intent works for API 24–25 via the legacy `ACTION_INSTALL_PACKAGE` without the permission being relevant. The manifest permission is still declared for API 26+ compatibility. No runtime permission request is needed for this permission (it is an install-time permission, not a dangerous permission).

6. **`external-cache-path` vs `files-path`** — External cache is preferred because it is world-readable (necessary for the system installer on some OEMs). Internal files path (`files-path`) can trigger `SecurityException` on some devices when the installer tries to read the URI even with `FLAG_GRANT_READ_URI_PERMISSION`.
