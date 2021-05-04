package io.github.dryrum.changelog

import io.github.dryrum.changelog.ChangeLogUpdateTask.Companion.updateChangelogByContent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.text.SimpleDateFormat
import java.util.* // ktlint-disable

class ChangeLogUpdateTaskTest {

    @Rule
    @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()

    private val changelogFile: File by lazy {
        testProjectDir.newFile("CHANGELOG.md")
    }

    @Before
    fun setup() {
        changelogFile.appendText("CHANGELOG.txt".readFileContent())
    }

    @Test
    fun `GIVEN an extension with title==null VERIFY the CHANGELOG content`() {

        val content = """
            * test
            * test
        """.trimIndent()

        val sut = updateChangelogByContent(
            File(changelogFile.path), content, null, "1.0.1"
        )
        sut.assertEquals(expectedRes1)
    }

    @Test
    fun `GIVEN an extension with title!=null VERIFY the CHANGELOG content`() {

        val content = """
            * test
            * test
        """.trimIndent()

        val sut = updateChangelogByContent(
            File(changelogFile.path), content, "## my template", "1.0.1"
        )
        sut.assertEquals(expectedRes2)
    }

    private val expectedRes1 = """
        ## 1.0.1 (${SimpleDateFormat("MMMM, dd, YYYY").format(Date())})
        * test
        * test

        ## 0.0.0 (January, 1, 2021)
        * first feature
        * second feature
    """.trimIndent()

    private val expectedRes2 = """
        ## my template
        * test
        * test

        ## 0.0.0 (January, 1, 2021)
        * first feature
        * second feature
    """.trimIndent()
}
