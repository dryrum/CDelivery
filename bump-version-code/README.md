[![Plugin portal publication](https://img.shields.io/gradle-plugin-portal/v/io.github.dryrum.bump-version-code
)](https://plugins.gradle.org/plugin/io.github.dryrum.bump-version-code)

# BumpVersionCode Gradle Plugin

## Getting Started

### Prerequisites

Ensure you have Gradle installed in your project. This plugin is compatible with Gradle version 6.0 and above. (Specify the correct version according to your plugin's compatibility.)

### Installation

This plugin is available on the Gradle Plugin Portal. To apply it to your project, add the following to your `build.gradle` or `build.gradle.kts` file:

**Groovy (build.gradle)**
```groovy
plugins {
  id "io.github.bump-version-code" version "0.7.0"
}
```

**Kotlin (build.gradle.kts)**
```kotlin
plugins {
  id("io.github.bump-version-code") version "0.7.0"
}
```

### Configuration
By default, the plugin operates on the gradle.properties file at the root of your project. If your version code is stored in a different properties file, configure the plugin as follows:

**Groovy (build.gradle)**
```groovy
versionCodePropPath {
    path = "path/to/your/properties/<file>.properties"
}
```

**Kotlin (build.gradle.kts)**
```kotlin
configure<io.github.dryrum.bumpversion.BumpVersionCodeExt> {
    path = "path/to/your/properties/<file>.properties"
}
```

### Usage
To bump the version code, execute the bumpVersionCode task from the terminal:
```shell
./gradlew bumpVersionCode
```
This will increment the VERSION_CODE in the specified properties file by 1.

## Contributing
Contributions are welcome! Please feel free to submit pull requests or open issues to improve the plugin or address bugs.

Pull Requests: For major changes, please open an issue first to discuss what you would like to change.
Issues: If you find a bug or have a suggestion, please open an issue with a clear title and description.

## License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE.txt) file for details.