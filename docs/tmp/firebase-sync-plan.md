# Firebase REST Sync + Percentile Comparison — Implementation Plan

## Overview

Sync local Room statistics to Firebase Realtime Database via REST API (no SDK), identify user by `Settings.Secure.ANDROID_ID`, and show percentile comparison ("You're faster than X% of players") on the statistics screen when 10+ players exist.

## Firebase Realtime Database Structure

```
/stats/{ANDROID_ID}/{difficulty}
    averageTime: int (seconds)
    bestTime: int (seconds)
    gamesWon: int
    gamesStarted: int
```

- `difficulty`: 1, 2, or 3 (matches Room `Statistic.difficulty`)
- REST endpoint: `https://<PROJECT_ID>.firebaseio.com/stats/{androidId}/{difficulty}.json`
- All data: `https://<PROJECT_ID>.firebaseio.com/stats.json`

## Firebase Security Rules (for user to configure in Firebase Console)

```json
{
  "rules": {
    "stats": {
      ".read": true,
      "$uid": {
        "$difficulty": {
          ".write": true,
          ".validate": "$difficulty.matches(/^[1-3]$/) && newData.hasChildren(['averageTime','bestTime','gamesWon','gamesStarted']) && newData.child('averageTime').isNumber() && newData.child('bestTime').isNumber() && newData.child('gamesWon').isNumber() && newData.child('gamesStarted').isNumber()"
        }
      }
    }
  }
}
```

## Files to Modify

- `app/src/main/AndroidManifest.xml` — add INTERNET permission
- `app/src/main/java/.../model/game/utils/Library.java` — add Firebase URL constant
- `app/src/main/java/.../model/Repository.java` — add Firebase sync and percentile fetch methods
- `app/src/main/java/.../viewmodel/GameViewModel.java` — call sync after statistic insert
- `app/src/main/java/.../viewmodel/StatisticViewModel.java` — add percentile fetch method
- `app/src/main/java/.../ui/ItemStatisticFragment.java` — display percentile text
- `app/src/main/res/layout/fragment_item_statistic.xml` — add percentile TextView
- `app/src/main/res/values/strings.xml` — add percentile string resources
- `app/src/main/res/values-ru/strings.xml` — add Russian translations

## New Files to Create

- `app/src/main/java/.../model/FirebaseSync.java` — HTTP client for Firebase REST API

## Implementation Steps

### Step 1: Create feature branch

```
git checkout -b feature/firebase-stats-sync
```

### Step 2: Add INTERNET permission

- Where: `app/src/main/AndroidManifest.xml`
- What: Add `<uses-permission android:name="android.permission.INTERNET" />` before `<application>`

### Step 3: Add Firebase URL constant

- Where: `Library.java`
- What: Add a new constants section:
```java
/**
 * Firebase
 */
public static final String FIREBASE_DB_URL = "https://YOUR_PROJECT_ID.firebaseio.com";
```
- The URL remains a placeholder until the Firebase project is created. When created, replace `YOUR_PROJECT_ID`.

### Step 4: Create `FirebaseSync.java`

- Where: `app/src/main/java/ru/shprot/sudokumobdevkz/model/FirebaseSync.java`
- What: Utility class with static methods for Firebase REST operations.
- Fields:
  - Uses `Library.FIREBASE_DB_URL` for base URL
  - Uses existing `ExecutorService` pattern from `Repository`
- Methods:

#### `static String getDeviceId(Context context)`
- Returns `Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)`

#### `static void uploadStatistic(Context context, Statistic statistic)`
- Builds URL: `FIREBASE_DB_URL + "/stats/" + getDeviceId(context) + "/" + statistic.getDifficulty() + ".json"`
- Constructs JSON manually (no library needed — only 4 fields):
  ```
  {"averageTime":N,"bestTime":N,"gamesWon":N,"gamesStarted":N}
  ```
- Makes HTTP PUT request via `HttpURLConnection`:
  - Method: PUT
  - Content-Type: application/json
  - Write JSON body to output stream
  - Check response code is 200
- Fire-and-forget, errors logged with `Log.e`
- Must run on background thread (caller provides executor)

#### `static void fetchPercentile(Context context, int difficulty, int userAverageTime, PercentileCallback callback, ExecutorService executor, Handler mainHandler)`
- Callback interface: `interface PercentileCallback { void onResult(int percentile, int totalPlayers); }`
- Builds URL: `FIREBASE_DB_URL + "/stats.json?shallow=false"`
- Makes HTTP GET request
- Parses JSON response manually:
  - Iterate over each user ID in the response object
  - For each user, check if they have data for the given `difficulty`
  - If yes, extract `averageTime` and compare with `userAverageTime`
  - Count total players with data for this difficulty, count those with worse (higher) averageTime
  - Ignore users with `averageTime == 0` (no wins yet) and the user's own ID
- Calculate percentile: `(slowerCount * 100) / totalPlayers`
- If totalPlayers < 10: call `callback.onResult(-1, totalPlayers)` (signal to hide the comparison)
- Post result to main thread via `mainHandler`

### Step 5: Update `Repository.java`

- Where: `Repository.java`
- What: Add two methods that delegate to `FirebaseSync`:

```java
public void syncStatisticToFirebase(Context context, Statistic statistic) {
    executor.execute(() -> FirebaseSync.uploadStatistic(context, statistic));
}

public void fetchPercentile(Context context, int difficulty, int userAverageTime, FirebaseSync.PercentileCallback callback) {
    FirebaseSync.fetchPercentile(context, difficulty, userAverageTime, callback, executor, mainHandler);
}
```

### Step 6: Update `GameViewModel.java`

- Where: `GameViewModel.java`, method `insertStatistic(Statistic statistic)`
- What: After `repository.insertStatistic(statistic)`, add:
```java
repository.syncStatisticToFirebase(getApplication(), statistic);
```
- This ensures every game completion syncs stats to Firebase (fire-and-forget).

### Step 7: Update `StatisticViewModel.java`

- Where: `StatisticViewModel.java`
- What: Add method:
```java
public void getPercentile(int difficulty, int userAverageTime, FirebaseSync.PercentileCallback callback) {
    repository.fetchPercentile(getApplication(), difficulty, userAverageTime, callback);
}
```

### Step 8: Add percentile string resources

- Where: `app/src/main/res/values/strings.xml`
- What: Add:
```xml
<string name="faster_than">You\'re faster than %d%% of players</string>
```

- Where: `app/src/main/res/values-ru/strings.xml`
- What: Add:
```xml
<string name="faster_than">Вы быстрее %d%% игроков</string>
```

### Step 9: Add percentile TextView to statistic layout

- Where: `app/src/main/res/layout/fragment_item_statistic.xml`
- What: Add a new `TextView` after the first `NeumorphCardView` (time card) and before the second one (games card). Example placement — inside a new simple card or standalone:
```xml
<TextView
    android:id="@+id/percentileTextView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:gravity="center"
    android:padding="12dp"
    android:textSize="14sp"
    android:textStyle="italic"
    android:visibility="gone" />
```
- Starts as `gone`, shown only when 10+ players exist.

### Step 10: Update `ItemStatisticFragment.java`

- Where: `ItemStatisticFragment.java`, method `loadData(Statistic statistic)`
- What: After loading local data, fetch the percentile:
```java
if (statistic.getAverageTime() > 0) {
    viewModel.getPercentile(difficulty, statistic.getAverageTime(), (percentile, totalPlayers) -> {
        if (isAdded() && percentile >= 0) {
            binding.percentileTextView.setText(getString(R.string.faster_than, percentile));
            binding.percentileTextView.setVisibility(View.VISIBLE);
        }
    });
}
```
- The `percentile >= 0` check handles the "less than 10 players" case (step 4 returns -1).
- Also in `clearStatistic()`: hide percentile view — `binding.percentileTextView.setVisibility(View.GONE)`.

## JSON Parsing Notes

Since the project has 0 JSON dependencies, manual parsing is needed. The Firebase response for GET `/stats.json` looks like:
```json
{
  "abc123def": {
    "1": {"averageTime":300,"bestTime":200,"gamesWon":5,"gamesStarted":8},
    "3": {"averageTime":600,"bestTime":400,"gamesWon":3,"gamesStarted":10}
  },
  "xyz789ghi": {
    "1": {"averageTime":450,"bestTime":350,"gamesWon":2,"gamesStarted":4}
  }
}
```

Use `org.json.JSONObject` (included in Android SDK, no dependency needed) to parse:
```java
JSONObject root = new JSONObject(responseString);
Iterator<String> userIds = root.keys();
while (userIds.hasNext()) {
    String uid = userIds.next();
    JSONObject userData = root.getJSONObject(uid);
    if (userData.has(String.valueOf(difficulty))) {
        JSONObject diffData = userData.getJSONObject(String.valueOf(difficulty));
        int avgTime = diffData.getInt("averageTime");
        // compare...
    }
}
```

## Dependencies

None. Uses only:
- `java.net.HttpURLConnection` — HTTP requests
- `org.json.JSONObject` — JSON parsing (Android SDK built-in)
- `android.provider.Settings.Secure` — device ID
- Existing `ExecutorService` + `Handler` pattern from `Repository`
