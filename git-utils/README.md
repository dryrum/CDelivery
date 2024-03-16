[![Plugin portal publication](https://img.shields.io/bintray/v/ciriti/c-delivery/gitutils-plugin?color=blue&label=Gradle%20Portal%20Git%20Utils)](https://plugins.gradle.org/plugin/io.github.dryrum.git-utils)

# Git utils

This is a Gradle plugin for pushing file into your repo programmatically.

## Setup

In your `build.gradle.kts` add:

- Kotlin

    ```kotlin
    plugins {
      id("io.github.dryrum.git-utils") version "0.6.6"
    }
    ```

In your `build.gradle` add:

- Groovy

    ```groovy
    plugins {
      id "io.github.dryrum.git-utils" version "0.6.6"
    }
    ```

## Config

List the file that you want to add, commit and push:

- Kotlin

    ```kotlin
    addCommitPushConfig {
        fileList = listOf(
    	    "${project.rootDir.path}/CHANGELOG.md",
                "${project.rootDir.path}/README.md",
    	    "${project.rootDir.path}/app/gradle.properties",
    	    "${project.rootDir.path}/release_note.txt"
            
        )
    }
    ```

- Groovy

    ```groovy
    addCommitPushConfig {
        fileList = [
                rootDir.path + '/CHANGELOG.md', 
                rootDir.path + '/README.md', 
                rootDir.path + '/app/gradle.properties',
                rootDir.path + '/release_note.txt'
        ]
    }
    ```

## Add, commit and pus

```bash
./gradlew addCommitPush
```

# CICD

## Configure your environment

Create 2 env variables like below

```
GIT_EMAIL=user@email.com
GIT_USERNAME=username
```

- GIT_EMAIL is your git email
- GIT_USERNAME is your git username

### GitHub Actions config example

On GitHub, you should create the env variable int the workflow where you are planning to use this plugin

```yaml
- name: Add, commit and push step
  env:
    GIT_USERNAME: ${{ secrets.GIT_USERNAME }}
    GIT_EMAIL: ${{ secrets.GIT_EMAIL }}
  run: ./gradlew addCommitPush
```