package io.github.ciriti.replaceinfile

import io.github.ciriti.replaceinfile.ReplaceInFileTask.Companion.updateDocument
import org.gradle.api.GradleException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class ReplaceInFileTaskTest {

    @Rule
    @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()

    private val readme: File by lazy {
        testProjectDir.newFile("README.md")
    }

    @Before
    fun setup() {
        readme.appendText("README.txt".readFileContent())
    }

    @Test
    fun `GIVEN a README file REPLACE the first dependency`() {

        val version = "(\\d)+\\.(\\d)+\\.(\\d)+"

        val sut = updateDocument(
            readme,
            "io.github.carmelo:test-lib:$version",
            "io.github.carmelo:test-lib:2.0.0"
        )
        sut.assertEquals(expectedRes1)
    }

    @Test
    fun `GIVEN a README file REPLACE the second dependency`() {

        val sut = updateDocument(
            readme,
            "io.github.fabio:android-lib:0.0.9",
            "io.github.fabio:android-lib:0.1.1"
        )
        sut.assertEquals(expectedRes2)
    }

    @Test(expected = GradleException::class)
    fun `GIVEN a missing file VERIFY that it throws an exception`() {

        val sut = updateDocument(
            File("INVALID.path"),
            "io.github.fabio:android-lib:0.0.9",
            "io.github.fabio:android-lib:0.1.1"
        )
        sut.assertEquals(expectedRes2)
    }

    private val expectedRes1 = """
        # Library
        ## Install

        ```java
                io.github.carmelo:test-lib:2.0.0
                io.github.fabio:android-lib:0.0.9
                io.github.iriti:java-lib:0.2.0
        ```
    """.trimIndent()

    private val expectedRes2 = """
        # Library
        ## Install

        ```java
                io.github.carmelo:test-lib:1.0.0
                io.github.fabio:android-lib:0.1.1
                io.github.iriti:java-lib:0.2.0
        ```
    """.trimIndent()
}
