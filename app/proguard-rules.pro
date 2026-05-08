# ============================================================
# Sudoku — ProGuard / R8 rules
# ============================================================

# --- Debugging: keep source file names and line numbers for crash reports ---
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# --- Keep annotations used by Room, Hilt, Serialization ---
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# ============================================================
# Room Database
# ============================================================
# Keep Entity classes (fields are mapped to DB columns by name)
-keep class ru.shprot.sudokumobdevkz.core.base.data.database.entity.** { *; }

# Keep DAOs (Room generates implementations at compile time)
-keep interface ru.shprot.sudokumobdevkz.core.base.data.database.dao.** { *; }

# Keep Room database class
-keep class ru.shprot.sudokumobdevkz.core.base.data.database.SudokuComposeDatabase { *; }

# ============================================================
# KotlinX Serialization
# ============================================================
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep @Serializable classes
-keep,includedescriptorclasses class ru.shprot.sudokumobdevkz.core.base.data.remote.**$$serializer { *; }
-keepclassmembers class ru.shprot.sudokumobdevkz.core.base.data.remote.** {
    *** Companion;
    *** serializer(...);
}
-keep,includedescriptorclasses class ru.shprot.sudokumobdevkz.core.base.data.repository.**$$serializer { *; }
-keepclassmembers class ru.shprot.sudokumobdevkz.core.base.data.repository.** {
    *** Companion;
    *** serializer(...);
}

# ============================================================
# Retrofit
# ============================================================
-keepattributes Exceptions
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**
-keep interface ru.shprot.sudokumobdevkz.core.base.data.remote.FirebaseApi { *; }

# ============================================================
# OkHttp
# ============================================================
-dontwarn okhttp3.**
-dontwarn okio.**

# ============================================================
# Navigation Compose (type-safe routes)
# ============================================================
-keep class ru.shprot.sudokumobdevkz.feature.**.navigation.** { *; }
-keep class ru.shprot.sudokumobdevkz.core.base.presentation.navigation.** { *; }

# ============================================================
# Hilt
# ============================================================
-dontwarn dagger.hilt.**

# ============================================================
# Google Play In-App Review
# ============================================================
-keep class com.google.android.play.core.review.** { *; }

# Annotation referenced by play-services-tasks shim, missing in runtime classpath
-dontwarn com.google.android.gms.common.annotation.NoNullnessRewrite
