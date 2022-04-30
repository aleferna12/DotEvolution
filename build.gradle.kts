@file:Suppress("PropertyName")

plugins {
    kotlin("jvm") version "1.3.72"
    application
}
group = "com.aleferna.dotevolution"
version = "1.0"

val tornadofx_version: String by rootProject

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.aleferna.dotevolution.MainKt")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("no.tornado:tornadofx:$tornadofx_version")
    implementation("org.apache.commons:commons-math3:3.6.1")
    testImplementation(kotlin("test-junit"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}