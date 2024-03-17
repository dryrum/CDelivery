// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        mavenCentral()
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.+")
        classpath ("org.jetbrains.dokka:dokka-gradle-plugin:1.4.0-rc")
        classpath ("io.codearte.gradle.nexus:gradle-nexus-staging-plugin:0.22.0") // original
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
    id("io.github.gradle-nexus.publish-plugin") version ("1.1.0")

    id("io.github.dryrum.update-changelog") version ("0.6.1")
    id("io.github.dryrum.replace-in-file") version ("0.6.1")
    id("io.github.dryrum.git-utils") version ("0.6.1")
}

apply(plugin = "io.codearte.nexus-staging")

allprojects {
    repositories {
        mavenCentral()
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
            add("${project.rootDir.path}/build.gradle.kts")
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
            find = "\\*\\s+version\\s+\\d+\\.\\d+\\.\\d+\n"
            replaceWith = "* version $versionName\n"
        }
        create("update-changelog") {
            path = project.buildFile.path
            find = "id\\(\"io.github.dryrum.update-changelog\"\\) version \\(\"\\d+\\.\\d+\\.\\d+\"\\)"
            replaceWith = "id\\(\"io.github.dryrum.update-changelog\"\\) version \\(\"$versionName\")"
        }
        create("replace-in-file") {
            path = project.buildFile.path
            find = "id\\(\"io.github.dryrum.replace-in-file\"\\) version \\(\"\\d+\\.\\d+\\.\\d+\"\\)"
            replaceWith = "id\\(\"io.github.dryrum.replace-in-file\"\\) version \\(\"$versionName\")"
        }
        create("git-utils") {
            path = project.buildFile.path
            find = "id\\(\"io.github.dryrum.git-utils\"\\) version \\(\"\\d+\\.\\d+\\.\\d+\"\\)"
            replaceWith = "id\\(\"io.github.dryrum.git-utils\"\\) version \\(\"$versionName\")"
        }
    }
}

changeLogConfig {
    val versionName = rootProject.extra.get("VERSION_NAME") as String
    changeLogPath = rootDir.path + "/CHANGELOG.md"
    content = file(rootDir.path + "/release_note.txt").readText()
    version = versionName
}