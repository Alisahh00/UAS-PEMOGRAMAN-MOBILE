plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.apk.menualisah"
    // Tetap di 36 sesuai settingan lo
    compileSdk = 36

    defaultConfig {
        applicationId = "com.apk.menualisah"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // 1. Tambahan untuk layout CardView (opsional tapi bagus buat UI)
    implementation("androidx.cardview:cardview:1.0.0")

    // 2. WAJIB: Tambahkan ini agar Splash Screen lo gak "double" di Android 12 ke atas
    implementation("androidx.core:core-splashscreen:1.0.1")

    // 3. Tambahan biar Emoji Bendera nampil sempurna di HP lama
    implementation("androidx.emoji2:emoji2:1.4.0")
    implementation("androidx.emoji2:emoji2-bundled:1.4.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // Library untuk koneksi internet simpel
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.android.volley:volley:1.2.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}