[![Bintray](https://img.shields.io/gradle-plugin-portal/v/io.github.dryrum.replace-in-file
)](https://plugins.gradle.org/plugin/io.github.dryrum.replace-in-file)

# Replace In File Gradle Plugin

The `replaceInFile` plugin for Gradle automates the process of finding and replacing text within files in your project. It's particularly useful for updating configurations, version numbers, or any other text across multiple files automatically, ideal for CI/CD pipelines and automating repetitive tasks.

## Features

- Replace specific text patterns within files automatically.
- Configure multiple files with different find-and-replace patterns.
- Streamline versioning and configuration management in your project.

## Getting Started

### Prerequisites

This plugin requires Gradle. Ensure you have a compatible version of Gradle installed in your project.

### Adding Plugin to Your Project

**For Kotlin DSL (`build.gradle.kts`):**

```kotlin
plugins {
    id("io.github.replace-in-file") version "0.7.0"
}
```

**For Groovy DSL (`build.gradle`):**

```groovy
plugins {
  id "io.github.replace-in-file" version "0.7.0"
}
```

## Usage
### Configuration
To use the replaceInFile plugin effectively, you must configure it in your Gradle build script. 
This involves specifying the files you want to modify and the text patterns you wish to find and replace. 
You can configure multiple files, each with its own unique find-and-replace settings.

**For Kotlin DSL (`build.gradle.kts`):**

```kotlin
replaceInFile {
    docs {
        register("docName") {
            path = "path/to/your/file.ext"
            find = "textToFind"
            replaceWith = "replacementText"
        }

        // Add more documents as needed
        register("anotherDoc") {
            path = "path/to/another/file.ext"
            find = "anotherTextToFind"
            replaceWith = "anotherReplacementText"
        }
    }
}

```

**For Groovy DSL (`build.gradle`):**

```groovy
replaceInFile {
    docs {
        create("docName") {
            path = 'path/to/your/file.ext'
            find = 'textToFind'
            replaceWith = 'replacementText'
        }

        // Configure additional documents as needed
        create("anotherDoc") {
            path = 'path/to/another/file.ext'
            find = 'anotherTextToFind'
            replaceWith = 'anotherReplacementText'
        }
    }
}

```

Replace "docName" with a unique identifier for each document you wish to configure. Adjust path, find, and replaceWith 
accordingly for each file and text pattern you intend to modify.

### Notes
- **path**: The relative or absolute path to the file you want to modify.
- **find**: The text pattern or string you want to find in the file. Regular expressions are supported, allowing for flexible search patterns.
replaceWith: The text that will replace each occurrence of the find pattern in the file.

## License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE.txt) file for details.