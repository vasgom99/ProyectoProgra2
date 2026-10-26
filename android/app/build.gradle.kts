plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "garage_coffee.app"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "garage_coffee.app"
        minSdk = 34
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }

    buildTypes {
        debug {
            // conecta el emulador con spring.
            buildConfigField("String", "API_BASE_URL", "\"http://127.0.0.1:8080\"")
        }
        release {
            isMinifyEnabled = false
            // configura la dirección del servidor.
            buildConfigField("String", "API_BASE_URL", "\"\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
}
