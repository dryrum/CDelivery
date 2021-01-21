[![Bintray](https://img.shields.io/bintray/v/ciriti/c-delivery/gitutils-plugin?color=blue&label=Bintray%20Git%20Utils)](https://bintray.com/ciriti/c-delivery/gitutils-plugin)
[![Bintray](https://img.shields.io/bintray/v/ciriti/c-delivery/gitutils-plugin?color=blue&label=Gradle%20Portal%20Git%20Utils)](https://plugins.gradle.org/plugin/io.github.dryrum.git-utils)

# Git utils

## Install

```groovy
plugins {
  id "io.github.git-utils" version "0.6.1"
}
```
```kotlin
plugins {
  id("io.github.git-utils") version "0.6.1"
}
```

## Config

Create 2 env variables like below

```
GIT_EMAIL=user@email.com
GIT_USERNAME=username
```

* GIT_EMAIL is your git email

* GIT_USERNAME is your git username

### GitHub Actions config example

On GitHub, you should create the env variable int the workflow where you are planning to use this plugin

```yaml
      - name: Add, commit and push step
        env:
          GIT_USERNAME: ${{ secrets.GIT_USERNAME }}
          GIT_EMAIL: ${{ secrets.GIT_EMAIL }}
        run: ./gradlew addCommitPush
```