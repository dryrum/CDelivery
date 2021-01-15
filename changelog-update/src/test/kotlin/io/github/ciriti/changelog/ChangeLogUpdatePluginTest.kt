package io.github.ciriti.changelog

import io.github.ciriti.changelog.Constants.TASK_NAME
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

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
    fun `GIVEN a default config CHECK the default output`() {

        val res = gradleRunner
            .withArguments(TASK_NAME)
            .build()

        Assert.assertTrue(res.output.contains("VERSION_CODE=2"))
        Assert.assertEquals(TaskOutcome.SUCCESS, res.task(":$TASK_NAME")!!.outcome)
    }

}
