
plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlinSerialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.serialization.json)
    implementation(libs.androidx.annotation)
    implementation(libs.retrofit.adapters.result)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    api(libs.okhttp)
}