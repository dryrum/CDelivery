[![Plugin portal publication](https://img.shields.io/gradle-plugin-portal/v/io.github.dryrum.update-changelog
)](https://plugins.gradle.org/plugin/io.github.dryrum.update-changelog)

# ChangeLogUpdatePlugin

The `ChangeLogUpdatePlugin` is a Gradle plugin that automates the process of updating a project's changelog file (`CHANGELOG.md`). It is designed to facilitate the inclusion of new entries with version information, changes content, and an optional custom title, streamlining the maintenance of a well-documented changelog as part of your CI/CD pipeline.

## Features

- Automatically prepend new entries to your project's changelog.
- Customize the path to your changelog file.
- Define version-specific titles and content for each new entry.
- Use the current date in the default entry title.

## Getting Started

### Prerequisites

Ensure you have Gradle installed in your project. This plugin is compatible with Gradle projects.

### Installation

To use the `ChangeLogUpdatePlugin`, add the following lines to your `build.gradle` or `build.gradle.kts` file to include the plugin in your project:

**For Kotlin DSL (`build.gradle.kts`):**

```kotlin
plugins {
    id("io.github.update-changelog") version "0.7.0"
}
```

**For Groovy DSL (`build.gradle`):**

```groovy
plugins {
  id "io.github.update-changelog" version "0.7.0"
}
```

### Configuration
Configure the plugin in your build script to specify the changelog file path, title, content, and version of the upcoming entry:

**For Kotlin DSL (`build.gradle.kts`):**

```kotlin
changeLogConfig {
    changeLogPath = "CHANGELOG.md" // Default value
    title = "Feature Release" // Optional
    content = "Description of the new features and bug fixes." // Required
    version = "1.1.0" // Default is "X.Y.Z"
}
// or 
changeLogConfig {
    val versionName = project.property("VERSION_NAME") as String
    changeLogPath = rootDir.path + "/CHANGELOG.md"
    content = file(  "${rootDir.path}/${project.name}/release_note.txt").readText()
    version = versionName
}
```

**For Groovy DSL (`build.gradle`):**

```groovy
changeLogConfig {
    changeLogPath = 'CHANGELOG.md' // Default value
    title = 'Feature Release' // Optional
    content = 'Description of the new features and bug fixes.' // Required
    version = '1.1.0' // Default is "X.Y.Z"
}
// or
changeLogConfig {
    def versionName = project.property("VERSION_NAME")
    changeLogPath = "${rootDir.path}/CHANGELOG.md"
    content = new File("${rootDir.path}/${project.name}/release_note.txt").text
    version = versionName
}

```

#### Notes
- **changeLogPath**: Path to the changelog file. Defaults to "CHANGELOG.md".
- **title**: Custom title for the changelog entry. If not provided, a default title is generated using the version and current date.
- **content**: The content describing changes in the new version. This field is required.
- **version**: The version number for the changelog entry. Defaults to "X.Y.Z".


### Usage
To update your changelog with the configured entry, run the changeLogUpdate task:

```shell
./gradlew changeLogUpdate
```
## License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE.txt) file for details.