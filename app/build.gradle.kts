plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.travel_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.travel_app"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.annotations)
    implementation(libs.picasso)
    implementation(fileTree(mapOf(
        "dir" to "C:\\Users\\DELL\\Desktop\\ZaloPayLib",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
	implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
	implementation("com.squareup.okhttp3:okhttp:4.6.0")
    implementation("commons-codec:commons-codec:1.14")
    implementation("com.google.firebase:firebase-auth")
    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Converter JSON -> Java object
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Optional: Logging (giúp debug các request)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

}