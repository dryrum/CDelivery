[![Bintray](https://img.shields.io/bintray/v/ciriti/cdelivery/gitutils-plugin?color=blue&label=Bintray%20Git%20Utils)](https://bintray.com/ciriti/cdelivery/gitutils-plugin)
[![Bintray](https://img.shields.io/bintray/v/ciriti/cdelivery/gitutils-plugin?color=blue&label=Gradle%20Portal%20Git%20Utils)](https://plugins.gradle.org/plugin/io.github.git-utils)

# Git utils

## Install

```groovy
plugins {
  id "io.github.git-utils" version "0.5.0"
}
```
```kotlin
plugins {
  id("io.github.git-utils") version "0.5.0"
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