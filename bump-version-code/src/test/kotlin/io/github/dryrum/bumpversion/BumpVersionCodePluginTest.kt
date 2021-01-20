package io.github.dryrum.bumpversion

import io.github.dryrum.bumpversion.Constants.TASK_NAME
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class BumpVersionCodePluginTest {

    @Rule
    @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()

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

    @Test
    fun `GIVEN a gradle properties file into app INCREASE the VERSION_CODE`() {

        buildFile.appendText(
            "\n" + """
            versionCodePropPath{
                path = "app/gradle.properties"
            }
            """.trimIndent()
        )

        testProjectDir.run {
            newFolder("app")
            newFile("app/gradle.properties")
                .appendText("gradle.properties".readFileContent())
        }

        val res = gradleRunner
            .withArguments(TASK_NAME)
            .build()

        Assert.assertTrue(res.output.contains("VERSION_CODE=2"))
        Assert.assertEquals(TaskOutcome.SUCCESS, res.task(":$TASK_NAME")!!.outcome)
    }

    @Test
    fun `GIVEN a gradle properties file into the root project INCREASE the VERSION_CODE`() {
        buildFile.appendText(
            "\n" + """
            versionCodePropPath{
                path = "gradle.properties"
            }
            """.trimIndent()
        )

        testProjectDir
            .newFile("gradle.properties")
            .appendText("gradle.properties".readFileContent())

        val res = gradleRunner
            .withArguments(TASK_NAME)
            .build()

        Assert.assertTrue(res.output.contains("VERSION_CODE=2"))
        Assert.assertEquals(TaskOutcome.SUCCESS, res.task(":$TASK_NAME")!!.outcome)
    }
}
