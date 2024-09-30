import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.future.getd"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.future.getd"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ndk {
            // 设置支持的SO库架构
//            abiFilters "armeabi", "armeabi-v7a", "arm64-v8a", "x86_64", "x86"
            abiFilters.addAll(mutableSetOf("armeabi", "armeabi-v7a", "arm64-v8a", "x86_64", "x86"))
        }
    }
    signingConfigs {
        create("packJKS"){
            keyAlias = "key0" // 别名
            keyPassword = "123456" // 密码
//            storeFile = file("${rootDir.absolutePath}/keystore/key.jks") // 存储keystore或者是jks文件的路径
            storeFile = file("./../key.jks") // 存储keystore或者是jks文件的路径
            storePassword = "123456" // 存储密码
        }
//        releaseConfig {
//            keyAlias 'key0'
//            keyPassword '123456'
//            storeFile file('./../key.jks')
//            storePassword '123456'
//        }
//        debugConfig {
//            keyAlias 'key0'
//            keyPassword '123456'
//            storeFile file('./../key.jks')
//            storePassword '123456'
//        }
    }

    buildTypes {
        // 通过前面配置的签名信息对应的标识符：packJKS拿到签名的配置信息
        // 保存在mySignConfig中，分别在debug和release中配置上就行了
        val mySignConfig = signingConfigs.getByName("packJKS")
        release {
//            isMinifyEnabled = false  //开启混淆
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 配置release 的签名信息
            signingConfig = mySignConfig
        }

        debug {
//            isMinifyEnabled = false //开启混淆
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 配置debug的签名信息
            signingConfig = mySignConfig
        }
    }

    android.applicationVariants.all {
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                // 生成当前时间戳
                val timestamp = SimpleDateFormat("yyyy-MM-dd HHmmss", Locale.getDefault()).format(Date())
                // 设置新的 APK 文件名
                val fileName = "GetD Smart ${this.name}-V${versionName}-${timestamp}.apk"
                this.outputFileName = fileName
            }
        }
    }

    sourceSets.all{
        jniLibs.srcDir("libs")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(files("libs/jl-component-lib_V1.2.3-debug.aar"))
    implementation(files("libs/jl_bluetooth_rcsp_V3.2.0_30202-release.aar"))
    implementation(files("libs/jldecryption_v0.1-release.aar"))
    implementation(files("libs/fastjson-1.1.38.jar"))
//    implementation(files("libs/Msc.jar"))
    // 添加所有jar文件依赖
//    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(files("libs\\Java-WebSocket-1.3.4.jar"))
    implementation(files("libs\\commons-codec-1.9.jar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

//    implementation("com.geyifeng.immersionbar:immersionbar:3.2.2")
    implementation("com.guolindev.permissionx:permissionx:1.7.1")
    implementation("androidx.room:room-runtime:2.3.0")
    annotationProcessor("androidx.room:room-compiler:2.3.0")
    implementation(libs.rxandroid)
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation("com.squareup.okio:okio:2.8.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.google.code.gson:gson:2.8.9")

//    implementation 'com.gyf.immersionbar:immersionbar:3.0.0'
//    implementation 'com.gyf.immersionbar:immersionbar-components:3.0.0'
//    implementation 'com.gyf.immersionbar:immersionbar-ktx:3.0.0'
//    implementation("com.geyifeng.immersionbar:immersionbar:latest.release")
    //RecyclerView通用适配器模板库
//    implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4")
    implementation ("io.github.cymchad:BaseRecyclerViewAdapterHelper4:4.1.4")
    //图片加载库
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    //定位功能
//    implementation ("com.amap.api:location:latest.integration")
    // Google Maps Location Services
    implementation ("com.google.android.gms:play-services-location:18.0.0")
    implementation ("com.google.android.gms:play-services-maps:17.0.0")
    implementation ("com.google.maps.android:android-maps-utils:1.2.1")
    //谷歌健康
    implementation ("com.google.android.gms:play-services-fitness:21.0.0")
    //谷歌登陆
    implementation ("com.google.android.gms:play-services-auth:16.0.1")
    implementation("androidx.credentials:credentials:1.5.0-alpha05")
    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0-alpha05")
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.0.0")
    //bugly
    implementation ("com.tencent.bugly:crashreport:latest.release")

}