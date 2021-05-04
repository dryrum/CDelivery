package io.github.dryrum.changelog

import io.github.dryrum.changelog.Constants.GROUP
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.text.SimpleDateFormat
import java.util.* // ktlint-disable
import javax.inject.Inject

open class ChangeLogUpdateTask @Inject constructor(
    private val ext: ChangeLogUpdateExt
) : DefaultTask() {

    init {
        group = GROUP
    }

    @TaskAction
    fun execute() {
        val content =
            updateChangelogByContent(
                File(ext.changeLogPath),
                ext.content,
                ext.title,
                ext.version
            )
        println(content)
    }

    companion object {
        fun updateChangelogByContent(changeLog: File, content: String?, pTitle: String?, version: String): String {
            val date: String = SimpleDateFormat("MMMM, dd, YYYY").format(Date())
            val title: String = pTitle ?: "## $version ($date)"

            // CHANGELOG.md
            if (!changeLog.exists()) throw GradleException(
                """
                ${changeLog.name} [${changeLog.path}] doesn't exist!!!
                """.trimIndent()
            )
            val changeLogContent = changeLog.readText(charset = Charsets.UTF_8)
            val releaseNoteContent: String = content ?: throw GradleException(noConfigClosureErrorMessage)
            val updatedChangeLog =
                "$title\n${releaseNoteContent.trim()}\n\n$changeLogContent".trimMargin()
            changeLog.writeText(text = updatedChangeLog, charset = Charsets.UTF_8)
            return changeLog.readText()
        }

        val noConfigClosureErrorMessage = """
            the [content] field cannot be null!!
            Please add the config field. Ex:
            ...
            changeLogConfig{
                content = ""
            }
            ...
        """.trimIndent()
    }
}
