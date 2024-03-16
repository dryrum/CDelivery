# ProcessExt Utility

The `processext` package offers a Kotlin extension to execute system commands within a given working directory and capture their output. It simplifies the interaction with the system's command line from within Kotlin applications, making it easier to run external processes and handle their outputs or errors directly in Kotlin code.

## Features

- **Execute System Commands**: Run commands as if you were in the system's command line.
- **Capture Output**: Collects both the standard output and error streams of the executed command.
- **Custom Working Directory**: Allows specifying the directory from which the command should be executed.

## Getting Started

### Prerequisites

- Kotlin project setup
- Familiarity with running external commands from Kotlin

### Installation

This utility is packaged as part of the `io.github.dryrum.processext` library. Ensure it's included in your project's dependencies. As of now, manual inclusion of the source file might be necessary.

### Usage

Here's a simple example of how to use `runCommand` to execute a command and capture its output:

```kotlin
import io.github.dryrum.processext.runCommand
import java.io.File

fun main() {
    val command = "echo Hello, world!"
    val workingDir = File(System.getProperty("user.dir"))
    
    val outputList: MutableList<String> = mutableListOf()
    command.runCommand(workingDir, outputList)
    
    println("Command output:")
    outputList.forEach { println(it) }
}
```

## License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE.txt) file for details.