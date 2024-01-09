plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.memo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.memo"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }



}

dependencies {
    val lifecycle_version = "2.6.2"
    val room_version = "2.6.0"
    val fragment_version = "1.6.2"
    val nav_version = "2.7.5"


    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.appcompat:appcompat-resources:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.github.bumptech.glide:glide:4.16.0") {
        exclude(group = "androidx.support")
    }
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")


    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("io.grpc:grpc-okhttp:1.50.2")
    implementation("io.grpc:grpc-stub:1.50.2")
    implementation("io.grpc:grpc-protobuf:1.50.2")

    //recyclerview
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("jp.wasabeef:recyclerview-animators:4.0.2")

    //viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")

    //liveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    //Annotation Processor
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")

    //Room
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version") //컴파일러
    implementation("androidx.room:room-ktx:$room_version")

    //FAB
    implementation("com.github.clans:fab:1.6.4")

    //easy permission
    implementation("pub.devrel:easypermissions:3.0.0")

    implementation("com.intuit.sdp:sdp-android:1.1.0")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")

    //Nav
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")

    //material Components
    implementation("com.google.android.material:material:1.11.0-beta01")

    //color picker library
    implementation("com.thebluealliance:spectrum:0.7.1")

    implementation("io.github.yahiaangelo.markdownedittext:markdownedittext:1.1.3")
    implementation("io.noties.markwon:core:4.6.2")
}