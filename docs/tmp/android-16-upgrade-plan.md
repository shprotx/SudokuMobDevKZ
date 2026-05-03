# Android 16 (API 36) Upgrade Plan

## Overview

Upgrade the Sudoku project from AGP 7.4.2 / compileSdk 33 / Gradle 7.5 to AGP 8.13 / compileSdk 36 / Gradle 8.13, with all dependencies updated to their latest stable versions. The project stays as pure Java with Groovy build scripts (AGP 8.x fully supports both).

## Target Dependency Versions

| Dependency | Current | Target |
|---|---|---|
| AGP | 7.4.2 | 8.13 |
| Gradle wrapper | 7.5 | 8.13 |
| compileSdk / targetSdk | 33 | 36 |
| minSdk | 24 | 24 (unchanged) |
| Java source/target | 1.8 | 17 |
| Room | 2.5.0 | 2.8.4 |
| Navigation | 2.5.3 | 2.9.7 |
| AppCompat | 1.6.1 | 1.7.1 |
| Material | 1.8.0 | 1.13.0 |
| ConstraintLayout | 2.1.4 | 2.2.1 |
| RxJava2 | 2.2.9 | 2.2.21 (latest 2.x) |
| RxAndroid | 2.0.1 | 2.1.1 |
| Room RxJava2 | 2.5.0 | 2.8.4 |
| AdMob (play-services-ads) | 21.5.0 | 23.6.0 |
| Neumorphism | 0.3.2 | 0.3.2 (unchanged, last release) |
| SSP | 1.1.0 | 1.1.1 |
| SDP | 1.1.0 | 1.1.1 |
| Play Review | 2.0.1 | 2.0.2 |
| Espresso | 3.5.1 | 3.6.1 |
| JUnit ext | 1.1.5 | 1.2.1 |
| JUnit | 4.13.2 | 4.13.2 (unchanged) |

## Files to Modify

- `gradle/wrapper/gradle-wrapper.properties` - Gradle distribution URL
- `build.gradle` (root) - AGP plugin version, remove deprecated `clean` task
- `app/build.gradle` - compileSdk, targetSdk, Java version, all dependency versions, namespace config
- `gradle.properties` - JVM args, AGP 8.x required properties
- `app/src/main/AndroidManifest.xml` - Android 16 behavior change adaptations
- `app/src/main/res/values/themes.xml` - Edge-to-edge status/nav bar color handling
- `app/src/main/res/values-night/themes.xml` - Same edge-to-edge changes
- `app/src/main/java/ru/shprot/sudokumobdevkz/FirstActivity.java` - Remove deprecated `setDecorCaptionShade` call
- `app/src/main/java/ru/shprot/sudokumobdevkz/MainActivity.java` - Edge-to-edge inset handling
- `app/src/main/java/ru/shprot/sudokumobdevkz/model/database/SudokuDatabase.java` - Room API update for `fallbackToDestructiveMigration`

## Implementation Steps

### Step 1: Upgrade Gradle Wrapper

- **Where:** `gradle/wrapper/gradle-wrapper.properties`
- **What:** Change distribution URL from `gradle-7.5-bin.zip` to `gradle-8.13-bin.zip`
- **How:** Update `distributionUrl` to `https\://services.gradle.org/distributions/gradle-8.13-bin.zip`

### Step 2: Update gradle.properties

- **Where:** `gradle.properties`
- **What:** Update JVM args for JDK 17 compatibility and add AGP 8.x properties
- **How:**
  - Keep `android.useAndroidX=true` and `android.nonTransitiveRClass=true`
  - Ensure `org.gradle.jvmargs` is suitable (current `-Xmx2048m` is fine)
  - No additional properties needed; `android.useAndroidX` and `android.nonTransitiveRClass` are already set

### Step 3: Update Root build.gradle

- **Where:** `build.gradle` (root)
- **What:** Bump AGP version from `7.4.2` to `8.13`
- **How:**
  - Change `id 'com.android.application' version '7.4.2' apply false` to `id 'com.android.application' version '8.13' apply false`
  - Change `id 'com.android.library' version '7.4.2' apply false` to `id 'com.android.library' version '8.13' apply false`
  - Replace the deprecated `clean` task block (`task clean(type: Delete) { delete rootProject.buildDir }`) -- in Gradle 8.x `buildDir` is deprecated; replace with: `tasks.register('clean', Delete) { delete rootProject.layout.buildDirectory }`

### Step 4: Update app/build.gradle SDK and Java Versions

- **Where:** `app/build.gradle`
- **What:** Update compileSdk, targetSdk, and Java compatibility
- **How:**
  - Change `compileSdk 33` to `compileSdk 36`
  - Change `targetSdk 33` to `targetSdk 36`
  - Change `sourceCompatibility JavaVersion.VERSION_1_8` to `sourceCompatibility JavaVersion.VERSION_17`
  - Change `targetCompatibility JavaVersion.VERSION_1_8` to `targetCompatibility JavaVersion.VERSION_17`
  - Remove the duplicate `viewBinding` block (lines 29-31 duplicate lines 33-35); keep only the `buildFeatures { viewBinding true }` block

### Step 5: Update All Dependency Versions in app/build.gradle

- **Where:** `app/build.gradle`
- **What:** Bump every dependency to its target version
- **How:** Replace each dependency line:
  - `androidx.appcompat:appcompat:1.6.1` -> `1.7.1`
  - `com.google.android.material:material:1.8.0` -> `1.13.0`
  - `androidx.constraintlayout:constraintlayout:2.1.4` -> `2.2.1`
  - `nav_version = "2.5.3"` -> `"2.9.7"`
  - `room_version = "2.5.0"` -> `"2.8.4"`
  - `androidx.room:room-rxjava2:2.5.0` -> `2.8.4` (use the `room_version` variable)
  - `io.reactivex.rxjava2:rxjava:2.2.9` -> `2.2.21`
  - `io.reactivex.rxjava2:rxandroid:2.0.1` -> `2.1.1`
  - `com.google.android.gms:play-services-ads:21.5.0` -> `23.6.0`
  - `com.intuit.ssp:ssp-android:1.1.0` -> `1.1.1`
  - `com.intuit.sdp:sdp-android:1.1.0` -> `1.1.1`
  - `com.google.android.play:review:2.0.1` -> `2.0.2`
  - `androidx.test.ext:junit:1.1.5` -> `1.2.1`
  - `androidx.test.espresso:espresso-core:3.5.1` -> `3.6.1`
  - `com.github.fornewid:neumorphism:0.3.2` stays at `0.3.2` (no newer release exists)

### Step 6: Handle Room API Changes

- **Where:** `app/src/main/java/ru/shprot/sudokumobdevkz/model/database/SudokuDatabase.java`
- **What:** Room 2.8.x changed `fallbackToDestructiveMigration()` -- the no-arg overload was deprecated in favor of `fallbackToDestructiveMigration(boolean)` where the boolean indicates whether to allow destructive migration on downgrade. Verify at compile time: if the no-arg method is removed, replace `.fallbackToDestructiveMigration()` with `.fallbackToDestructiveMigration(true)`.

### Step 7: Handle Android 16 Edge-to-Edge Enforcement

Starting with API 36, edge-to-edge display is mandatory. The system bars (status bar and navigation bar) become transparent and content draws behind them. The existing `android:statusBarColor` and `android:navigationBarColor` theme attributes are **ignored** on API 36.

- **Where:** `app/src/main/res/values/themes.xml`, `app/src/main/res/values-night/themes.xml`
- **What:** The `android:statusBarColor` and `android:navigationBarColor` attributes will be ignored on API 36+ devices. They can remain for backward compatibility with older API levels but will have no effect on 36+.
- **Where:** `app/src/main/java/ru/shprot/sudokumobdevkz/MainActivity.java`
- **What:** Add `WindowCompat.setDecorFitsSystemWindows(getWindow(), false)` and apply `WindowInsetsCompat` padding to the root view (or the `NavHostFragment` container) so content does not overlap system bars.
- **How:**
  - In `onCreate`, after `setContentView`, use `ViewCompat.setOnApplyWindowInsetsListener` on the root view to apply padding for `WindowInsetsCompat.Type.systemBars()`.
  - Reference the AndroidX `core` library's `WindowCompat` and `ViewCompat` APIs (already available transitively through `appcompat:1.7.1`).
  - Apply the same pattern in `FirstActivity.java`.
  - Check all fragment layouts -- if any use `android:fitsSystemWindows="true"`, that attribute will still work, but manual inset handling is more reliable.

### Step 8: Handle Android 16 Orientation Restriction Changes

- **Where:** `app/src/main/AndroidManifest.xml`
- **What:** On API 36+, `android:screenOrientation="portrait"` is **ignored on displays with smallest width >= 600dp** (tablets, foldables). On phones it still works. This is a behavioral change, not a compile error.
- **How:**
  - If the app must remain portrait-only on large screens, add to each `<activity>` element: `<property android:name="android.window.PROPERTY_COMPAT_ALLOW_RESTRICTED_RESIZABILITY" android:value="true" />`
  - This is a temporary opt-out; it will be removed in API 37. Long-term, the app should support landscape layouts on large screens.

### Step 9: Remove Deprecated API Usage in FirstActivity

- **Where:** `app/src/main/java/ru/shprot/sudokumobdevkz/FirstActivity.java`
- **What:** Line 69 calls `getWindow().setDecorCaptionShade(Window.DECOR_CAPTION_SHADE_LIGHT)` inside a `Build.VERSION.SDK_INT >= 27` check. This API is for freeform/multi-window caption styling and is not harmful, but verify it compiles against API 36. If it causes issues, remove it -- it has no visible effect on standard phone/tablet usage.

### Step 10: Handle Deprecated updateConfiguration Calls

- **Where:** `MainActivity.java` (line 69), `SettingsFragment.java` (line 150)
- **What:** `Resources.updateConfiguration()` has been deprecated since API 25. While it still works, on API 36 it may behave unexpectedly.
- **How:** Both calls are currently commented out or in an unused code path (`updateLocale()` in MainActivity is commented out at line 39). If locale switching is needed in the future, replace with `context.createConfigurationContext()` or the `AppCompatDelegate.setApplicationLocales()` API from AppCompat 1.7+.

### Step 11: Verify Neumorphism Library Compatibility

- **Where:** Build output
- **What:** `com.github.fornewid:neumorphism:0.3.2` was last released in February 2022 and compiled against an older SDK. It is distributed via JitPack as an AAR.
- **How:** After completing all version bumps, build the project and check for compilation errors from this library. Common issues:
  - If the library's AAR has a `compileSdk` lower than 36, it should still work as a dependency (AGP handles this).
  - If there are `resource` or `attribute` conflicts with Material 1.13.0, the build will fail with merge errors. In that case, add `tools:replace` attributes in the manifest or resource files as needed.
  - If the library is fundamentally broken, consider forking it or replacing neumorphism views with standard `MaterialCardView` with custom shadow drawables.

### Step 12: Verify AdMob SDK Compatibility

- **Where:** Build output, runtime
- **What:** AdMob 23.6.0 requires updated consent/privacy handling (UMP SDK). The upgrade from 21.x to 23.x includes breaking changes in ad initialization.
- **How:**
  - Check if `MobileAds.initialize()` is called somewhere in the codebase (search for it). If not, AdMob 23.x may require explicit initialization.
  - Review the `AdHolder.java` utility class for any deprecated API usage from the 21.x SDK.
  - The test ad application ID in `AndroidManifest.xml` (`ca-app-pub-3940256099942544~3347511713`) is fine for debug builds.

### Step 13: Clean Build and Fix Compilation Errors

- **What:** After all changes, run `./gradlew clean assembleDebug` and address any remaining errors.
- **How:** Common issues to expect:
  - **Namespace declaration:** AGP 8+ requires `namespace` in `build.gradle` (already present at line 32 of `app/build.gradle`, so this is fine).
  - **R8/ProGuard:** If `minifyEnabled` is set to `true` for release builds (currently `false`), ensure ProGuard rules are up to date.
  - **Build cache:** Run `./gradlew clean` first to clear any stale caches from the old Gradle/AGP version.
  - **Gradle JDK:** Ensure the project JDK in Android Studio / `JAVA_HOME` is set to JDK 17 or higher. AGP 8.13 requires JDK 17 minimum.

## Dependencies and APIs

- **AGP 8.13**: [Release notes](https://developer.android.com/build/releases/agp-8-13-0-release-notes). Requires Gradle 8.13, JDK 17+. Supports compileSdk up to 36. Groovy build scripts are fully supported.
- **Room 2.8.4**: [Release page](https://developer.android.com/jetpack/androidx/releases/room). Room 2.x remains in maintenance mode; Java annotation processor (`annotationProcessor`) still works. Room 3.0 is KSP-only/Kotlin-only -- do not migrate to it for this Java project.
- **Navigation 2.9.7**: [Release page](https://developer.android.com/jetpack/androidx/releases/navigation). Drop-in replacement for 2.5.3 in Java projects.
- **Material 1.13.0**: [GitHub releases](https://github.com/material-components/material-components-android/releases). Adds new components; existing `Theme.MaterialComponents.DayNight.NoActionBar` parent theme remains supported.
- **AdMob 23.6.0**: [Release notes](https://developers.google.com/admob/android/rel-notes). Major version jump from 21.x; review migration guide for any API changes in ad loading/displaying.
- **Android 16 behavior changes**: [Official documentation](https://developer.android.com/about/versions/16/behavior-changes-16). Key items: mandatory edge-to-edge, orientation restrictions relaxed on large screens.
- **Neumorphism 0.3.2**: [GitHub](https://github.com/fornewid/neumorphism). Unmaintained since 2022. No update available; monitor for compile/runtime issues with API 36.
