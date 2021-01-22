package io.github.dryrum.gitutils

import io.github.dryrum.gitutils.GitUtilsTask.Companion.runCommand
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.time.Instant

class GitUtilsPluginTest {

    @Rule
    @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()

    @Test
    fun `GIVEN a folder without git CATCH an UnexpectedBuildFailure`() {
        val log = mutableListOf<String>()

        val buildFile: File by lazy {
            testProjectDir.newFile("build.gradle")
        }

        buildFile.appendText("build.gradle.txt".readFileContent())

        val testRepo = "https://github.com/dryrum/test.git"
        "git clone --bare $testRepo".runCommand(testProjectDir.root, log)

        "git init".runCommand(testProjectDir.root, log)
        "git remote add origin test".runCommand(testProjectDir.root, log)
        "git fetch --all --prune".runCommand(testProjectDir.root, log)
        "git checkout main".runCommand(testProjectDir.root, log)

        val readme = File("${testProjectDir.root}/README.md")

        readme.exists().assertTrue()

        val timestamp = "timestamp[${Instant.now()}]\"n\""

        readme.appendText(timestamp)

        buildFile.appendText(
            "\n" + """
                addCommitPushConfig {
                    fileList = ['${readme.path}']
                }
            """.trimIndent()
        )

        val res = GradleRunner.create()
            .withDebug(true)
            .withPluginClasspath()
            .withProjectDir(testProjectDir.root)
            .withTestKitDir(testProjectDir.newFolder())
            .withArguments(Constants.TASK_NAME)
            .build()

        res.output

        res.output.contains("Success!!!").assertTrue()
    }
}
