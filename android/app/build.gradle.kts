plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

import java.util.Properties
import java.io.FileInputStream

android {
    namespace = "com.marotoweb.cajuscan_app"
    // compileSdk = flutter.compileSdkVersion
    // ndkVersion = flutter.ndkVersion
    // Define sdk and ndk version for reprodutible build
    compileSdk = 36
    ndkVersion = "27.0.12077973"
    buildToolsVersion = "36.0.0-rc1"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "com.marotoweb.cajuscan_app"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        minSdk = flutter.minSdkVersion
        //targetSdk = flutter.targetSdkVersion
        targetSdk = 36
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }
    
    // Create a variable called keystorePropertiesFile, and initialize it to your
    // keystore.properties file, in the rootProject folder.
    val keystorePropertiesFile = rootProject.file("key.properties")

    // Initialize a new Properties() object called keystoreProperties.
    val keystoreProperties = Properties()
    
    // Load your keystore.properties file into the keystoreProperties object.
    val hasKeyProperties = keystorePropertiesFile.exists()
    if (hasKeyProperties) {
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
    }

    signingConfigs {
        // Configuração de assinatura dinâmica
        create("release") {
            if (hasKeyProperties) {
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
                storeFile = keystoreProperties.getProperty("storeFile")?.let { file(it) }
                storePassword = keystoreProperties.getProperty("storePassword")
            }
        }
    }

    buildTypes {
        getByName("release") {
            // Só tenta assinar se as propriedades existirem, caso contrário usa debug
            signingConfig = if (hasKeyProperties) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
            
            // Ofuscação e a remoção de código não usado
            isMinifyEnabled = true
            isShrinkResources = true
            
            // Mantém as definições padrão de ficheiros de regras, 
            // mas como o minify está false, elas não farão nada.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packagingOptions {
        jniLibs {
            // Enabling flag to compress JNI Libs to reduce APK size Ref: https://developer.android.com/topic/performance/reduce-apk-size?hl=zh-cn#extract-false
            useLegacyPackaging = true
        }
    }

    // Required by F-Droid
    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }
}

flutter {
    source = "../.."
}

dependencies {}