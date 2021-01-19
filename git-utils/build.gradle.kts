plugins {
    id("java")
    id("java-gradle-plugin")
    id("org.jetbrains.kotlin.jvm")
    id("com.gradle.plugin-publish")
    id("maven")
    id("maven-publish")
    id("jacoco")
    id("org.gradle.kotlin.kotlin-dsl")
}

apply(plugin = "jacoco")
apply(from = "${rootDir.path}/buildfile/publish.gradle")
apply(from = rootDir.path + "/buildfile/ktlint_utils.gradle")

group = project.property("GROUP_ID") as String
version = rootProject.extra.get("VERSION_NAME") as String

repositories {
    mavenCentral()
}

// to configure both compileKotlin and compileTestKotlin
// https://kotlinlang.org/docs/reference/using-gradle.html#compiler-options
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

// https://docs.gradle.org/current/userguide/kotlin_dsl.html?_ga=2.260720329.512580604.1602165526-681461519.1596023720#type-safe-accessors
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(project(":process-ext"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("junit:junit:4.12")
}

jacoco {
    toolVersion = "0.8.5"
    reportsDir = file("$buildDir/customJacocoReportDir")
}

tasks.jacocoTestReport {
    reports.html.isEnabled = true
    executionData.setFrom(fileTree(buildDir).include("/jacoco/*.exec"))
}

pluginBundle {
    website = project.property("WEB_SITE_URL") as String
    vcsUrl = project.property("POM_VCS_URL") as String
    tags = listOf("kotlin", "cicd", "bump version code", "utility")
}

gradlePlugin {
    plugins {

        val pluginName = project.property("PLUGIN_NAME") as String
        val implClass = project.property("PLUGIN_IMPLEMENTATION_CLASS") as String
        
        create(pluginName) {
            id = project.property("GRADLE_PLUGIN_ID") as String
            description = project.property("POM_DESCRIPTION") as String
            implementationClass = implClass
            displayName = project.property("GRADLE_DISPLAY_NAME") as String
        }
    }
}
