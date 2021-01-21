// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        mavenCentral()
//        mavenLocal()
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.+")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version ("0.12.0")
    id("org.jetbrains.kotlin.jvm") version (Versions.kotlinVersion)
    id("maven-publish")
    id("org.gradle.kotlin.kotlin-dsl") version ("1.4.2")

    val cdVersion = "0.6.1"
    id("io.github.dryrum.update-changelog") version (cdVersion)
    id("io.github.dryrum.replace-in-file") version (cdVersion)
    id("io.github.dryrum.git-utils") version (cdVersion)
}

apply(plugin = "io.github.dryrum.update-changelog")
apply(plugin = "io.github.dryrum.replace-in-file")
apply(plugin = "io.github.dryrum.git-utils")

allprojects {
    repositories {
        mavenCentral()
//        mavenLocal()
        google()
        jcenter()
    }
}

tasks.register("versionTxt") {
    group = "versioning"
    doLast {
        val version = rootProject.extra.get("VERSION_NAME") as String
        File(projectDir, "version.txt").writeText(version)
    }
}

addCommitPushConfig {
    fileList = project
        .subprojects
        .map { "${project.rootDir.path}/${it.name}/README.md" }
        .toMutableList()
        .apply {
            add("${project.rootDir.path}/CHANGELOG.md")
            add("${project.rootDir.path}/README.md")
        }
}

replaceInFile {
    val versionName = rootProject.extra.get("VERSION_NAME") as String
    docs {
        project.subprojects.forEachIndexed { index, project ->
            create("doc$index") {
                path = "${rootDir.path}/${project.name}/README.md"
                find = "version \"(\\d)+\\.(\\d)+\\.(\\d)+\""
                replaceWith = "version \"$versionName\""
            }
        }
        create("release_note") {
            path = "${rootDir.path}/release_note.txt"
            find = "version \"(\\d)+\\.(\\d)+\\.(\\d)+\""
            replaceWith = "version \"$versionName\""
        }
    }
}

changeLogConfig {
    val versionName = rootProject.extra.get("VERSION_NAME") as String
    changeLogPath = rootDir.path + "/CHANGELOG.md"
    content = file(rootDir.path + "/release_note.txt").readText()
    version = versionName
}