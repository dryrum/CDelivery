package io.github.dryrum.changelog

import io.github.dryrum.changelog.Constants.TASK_NAME
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.text.SimpleDateFormat
import java.util.* // ktlint-disable

class ChangeLogUpdatePluginTest {

    @Rule
    @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()

    private val buildFile: File by lazy {
        testProjectDir.newFile("build.gradle")
    }

    private val changelogFile: File by lazy {
        testProjectDir.newFile("CHANGELOG.md")
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
        changelogFile.appendText("CHANGELOG.txt".readFileContent())
    }

    @Test
    fun `GIVEN an config block with title==null VERIFY the CHANGELOG content`() {

        buildFile.appendText(
            "\n" + """
            changeLogConfig{
                changeLogPath = "${changelogFile.path}"
                content = "$content"
                version = "1.0.1"
            }
            """.trimIndent()
        )

        val res = gradleRunner
            .withArguments(TASK_NAME)
            .build()

        Assert.assertEquals(TaskOutcome.SUCCESS, res.task(":$TASK_NAME")!!.outcome)
        changelogFile.readText().assertEquals(expectedRes1)
    }

    @Test
    fun `GIVEN an config block with title!=null VERIFY the CHANGELOG content`() {

        buildFile.appendText(
            "\n" + """
            changeLogConfig{
                changeLogPath = "${changelogFile.path}"
                title = "## my template"
                content = "$content"
                version = "1.0.1"
            }
            """.trimIndent()
        )

        val res = gradleRunner
            .withArguments(TASK_NAME)
            .build()

        Assert.assertEquals(TaskOutcome.SUCCESS, res.task(":$TASK_NAME")!!.outcome)
        changelogFile.readText().assertEquals(expectedRes2)
    }

    private val content = """
            * test
    """.trimIndent()

    private val expectedRes1 = """
        ## 1.0.1 (${SimpleDateFormat("MMMM, DD, YYYY").format(Date())})
        * test

        ## 0.0.0 (January, 1, 2021)
        * first feature
        * second feature
    """.trimIndent()

    private val expectedRes2 = """
        ## my template
        * test

        ## 0.0.0 (January, 1, 2021)
        * first feature
        * second feature
    """.trimIndent()
}
