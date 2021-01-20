package io.github.dryrum.gitutils

import io.github.dryrum.gitutils.Constants.TASK_NAME
import io.github.dryrum.processext.runCommand
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.time.Instant

class GitUtilsPluginTest {

    @Rule
    @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()

    @Rule
    @JvmField
    val testProjectDirErrorCase: TemporaryFolder = TemporaryFolder()

    private val buildFile: File by lazy {
        testProjectDir.newFile("build.gradle")
    }

    private val gradleRunner: GradleRunner by lazy {
        GradleRunner.create()
            .withDebug(true)
            .withPluginClasspath()
            .withProjectDir(testProjectDir.root)
            .withTestKitDir(testProjectDir.newFolder())
    }

    @Before
    fun setup() {
        buildFile.appendText("build.gradle.txt".readFileContent())
    }

    @Test(expected = Throwable::class)
    fun `GIVEN a folder without git CATCH an UnexpectedBuildFailure`() {

        val testRepo = "https://github.com/dryrum/test.git"
        val original = testProjectDir.newFolder("original")

        val log = mutableListOf<String>()
        "git clone $testRepo".runCommand(original, log)
        val readmeOriginal = File("$original/test/README.md")
        readmeOriginal.exists().assertTrue()

        val timestamp = "timestamp[${Instant.now()}]"

        readmeOriginal.appendText(timestamp)

        buildFile.appendText(
            "\n" + """
                addCommitPushConfig {
                    fileList = ['${readmeOriginal.path}']
                }
            """.trimIndent()
        )

        gradleRunner
            .withArguments(TASK_NAME)
            .build()
    }

    @Test
    fun `GIVEN an edited file PUSH the changes onto the test repo`() {
        val testRepo = "https://github.com/dryrum/test.git"
        val log = mutableListOf<String>()
        "git init".runCommand(testProjectDir.root, log)
        "git remote add origin $testRepo".runCommand(testProjectDir.root, log)
        "git fetch --all --prune".runCommand(testProjectDir.root, log)
        "git checkout main".runCommand(testProjectDir.root, log)

        val readmeOriginal = File("${testProjectDir.root}/README.md")
        readmeOriginal.exists().assertTrue()

        val timestamp = "timestamp[${Instant.now()}]\n"

        readmeOriginal.appendText(timestamp)

        buildFile.appendText(
            "\n" + """
                addCommitPushConfig {
                    fileList = ['${readmeOriginal.path}']
                }
            """.trimIndent()
        )

        val res = gradleRunner
            .withArguments(TASK_NAME)
            .build()

        Assert.assertEquals(TaskOutcome.SUCCESS, res.task(":$TASK_NAME")!!.outcome)

        val sut = testProjectDir.newFolder("sut")
        "git clone $testRepo".runCommand(sut, log.apply { clear() })
        val readmeSut = File("$sut/test/README.md")
        readmeSut.readText().contains(timestamp).assertTrue()
    }

    @Test(expected = Throwable::class)
    fun `GIVEN a wrong file path CATCH a GradleException`() {
        val tmpDir = testProjectDirErrorCase.root
        val testRepo = "https://github.com/dryrum/test.git"
        val log = mutableListOf<String>()
        "git init".runCommand(tmpDir, log)
        "git remote add origin $testRepo".runCommand(tmpDir, log)
        "git fetch --all --prune".runCommand(tmpDir, log)
        "git checkout main".runCommand(tmpDir, log)

        val readmeOriginal = File("$tmpDir/README.md")
        readmeOriginal.exists().assertTrue()

        val timestamp = "timestamp[${Instant.now()}]\n"

        readmeOriginal.appendText(timestamp)

        val buildFile = testProjectDirErrorCase.newFile("build.gradle").apply {
            appendText("build.gradle.txt".readFileContent())
            appendText(
                "\n" + """
                addCommitPushConfig {
                    fileList = ['$tmpDir/wrong_dir/README.md']
                }
                """.trimIndent()
            )
        }

        GradleRunner.create()
            .withDebug(true)
            .withPluginClasspath()
            .withProjectDir(testProjectDirErrorCase.root)
            .withTestKitDir(testProjectDirErrorCase.newFolder())
            .withArguments(TASK_NAME)
            .build()
    }
}
