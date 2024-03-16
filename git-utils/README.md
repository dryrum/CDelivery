[![Plugin portal publication](https://img.shields.io/gradle-plugin-portal/v/io.github.dryrum.git-utils
)](https://plugins.gradle.org/plugin/io.github.dryrum.git-utils)

# Git utils

The `gitutils` plugin automates Git operations within your Gradle projects. It allows you to add, commit, 
and push files directly through Gradle tasks, streamlining your version control workflow in automation pipelines.

It can be particularly useful in automation scenarios, such as continuous integration and deployment pipelines, 
where changes need to be programmatically committed and pushed to a repository. It simplifies the process of version 
control management within Gradle tasks, making it a valuable tool for developers looking to streamline their development 
workflows with Git operations.

## Getting Started

### Setup

To integrate the `gitutils` plugin into your project, add the following to your build script:

**For Kotlin DSL (`build.gradle.kts`):**
```kotlin
    plugins {
        id("io.github.dryrum.git-utils") version "0.7.0"
    }
```

**For Groovy DSL (`build.gradle`):**
```groovy
    plugins {
        id "io.github.dryrum.git-utils" version "0.7.0"
    }
```

### Config

Configure the plugin by listing the files you intend to add, commit, and push:

**For Kotlin DSL (`build.gradle.kts`):**

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

**For Groovy DSL (`build.gradle`):**

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

## Usage

```bash
./gradlew addCommitPush
```

## CI/CD Integration

### Environment Configuration

For the plugin to operate correctly, define the following environment variables:

- GIT_EMAIL: Your Git email address.
- GIT_USERNAME: Your Git username.

### GitHub Actions Example
Configure your GitHub Actions workflow by setting the necessary environment variables:

```yaml
- name: Execute Git Operations
  env:
    GIT_USERNAME: ${{ secrets.GIT_USERNAME }}
    GIT_EMAIL: ${{ secrets.GIT_EMAIL }}
  run: ./gradlew addCommitPush

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

## License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE.txt) file for details.