package io.github.dryrum.replaceinfile

import io.github.dryrum.replaceinfile.Constants.TASK_NAME
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class ReplaceInFilePluginTest {

    @Rule
    @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()

    private val buildFile: File by lazy {
        testProjectDir.newFile("build.gradle")
    }

    private val readme: File by lazy {
        testProjectDir.newFile("README.md")
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
        readme.appendText("README.txt".readFileContent())
    }

    @Test
    fun `GIVEN 2 changes to apply to a file VERIFY the output`() {

        buildFile.appendText(
            "\n" + """
            replaceInFile{
                docs{
                    doc{
                        path = "${readme.path}"
                        find = "io.github.carmelo:test-lib:(\\d)+\\.(\\d)+\\.(\\d)+"
                        replaceWith = "io.github.carmelo:test-lib:2.0.0"
                    }
                    doc1{
                        path = "${readme.path}"
                        find = "io.github.fabio:android-lib:0.0.9"
                        replaceWith = "io.github.fabio:android-lib:0.1.1"
                    }
                }
            }
            """.trimIndent()
        )

        val res = gradleRunner
            .withArguments(TASK_NAME)
            .build()

        Assert.assertEquals(TaskOutcome.SUCCESS, res.task(":$TASK_NAME")!!.outcome)
        readme.readText().assertEquals(expectedRes1)
    }

    private val expectedRes1 = """
        # Library
        ## Install

        ```java
                io.github.carmelo:test-lib:2.0.0
                io.github.fabio:android-lib:0.1.1
                io.github.iriti:java-lib:0.2.0
        ```
    """.trimIndent()
}
