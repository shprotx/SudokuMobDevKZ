# ============================================================
# Sudoku — ProGuard / R8 rules
# ============================================================

# --- Debugging: keep source file names and line numbers for crash reports ---
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# --- Keep annotations used by Room, RxJava, etc. ---
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# ============================================================
# Room Database
# ============================================================
# Keep Entity classes (fields are mapped to DB columns by name)
-keep class ru.shprot.sudokumobdevkz.model.game.Square { *; }
-keep class ru.shprot.sudokumobdevkz.model.game.GameState { *; }
-keep class ru.shprot.sudokumobdevkz.model.game.Statistic { *; }

# Keep DAOs (Room generates implementations at compile time)
-keep interface ru.shprot.sudokumobdevkz.model.database.** { *; }

# Keep TypeConverters
-keep class ru.shprot.sudokumobdevkz.model.database.DraftsVisibilityConverter { *; }

# Keep Room database class
-keep class ru.shprot.sudokumobdevkz.model.database.SudokuDatabase { *; }

# ============================================================
# Parcelable
# ============================================================
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# ============================================================
# RxJava 2
# ============================================================
-dontwarn io.reactivex.**
-keep class io.reactivex.** { *; }
-keepclassmembers class io.reactivex.** { *; }

# ============================================================
# Neumorphism (JitPack library, reflection-based custom views)
# ============================================================
-keep class soup.neumorphism.** { *; }

# ============================================================
# Google Play In-App Review
# ============================================================
-keep class com.google.android.play.core.review.** { *; }

# ============================================================
# General Android
# ============================================================
# Keep custom Views (inflated from XML by name)
-keep class ru.shprot.sudokumobdevkz.model.game.utils.SquareCardView { *; }
-keep class ru.shprot.sudokumobdevkz.model.game.utils.SquareCardLayout { *; }
-keep class ru.shprot.sudokumobdevkz.model.game.utils.MyCardLayout { *; }
