package io.github.ciriti.changelog

import io.github.ciriti.changelog.Constants.GROUP
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

open class ChangeLogUpdateTask @Inject constructor(
    private val ext: ChangeLogUpdateExt
) : DefaultTask() {

    init {
        group = GROUP
    }

    @TaskAction
    fun execute() {
        updateChangelogByFile(ext)
        updateChangelogByContent(ext)
    }

    private fun updateChangelogByContent(extension: ChangeLogUpdateExt) {
        // CHANGELOG.md
        val changeLog = File(extension.changeLogPath)
        if (!changeLog.exists()) changeLog.createNewFile()
        val changeLogContent = changeLog.readText(charset = Charsets.UTF_8)
        val releaseNoteContent = extension.content
        val updatedChangeLog =
            "## ${extension.title} \n${releaseNoteContent.trim()} \n\n$changeLogContent".trimMargin()
        changeLog.writeText(text = updatedChangeLog, charset = Charsets.UTF_8)
    }

    private fun updateChangelogByFile(extension: ChangeLogUpdateExt) {
        // CHANGELOG.md
        val changeLog = File(extension.changeLogPath)
        if (!changeLog.exists()) changeLog.createNewFile()
        val changeLogContent = changeLog.readText(charset = Charsets.UTF_8)
        // release_note.txt
        val releaseNote = File(extension.releaseNotePath)
        val releaseNoteContent = releaseNote.readText(charset = Charsets.UTF_8)
        // January, 09, 2021
        val date: String = SimpleDateFormat("MMMM, DD, YYYY").format(Date())
        // CHANGELOG.md content updated
        val updatedChangeLog =
            "## ${extension.version} ($date) \n${releaseNoteContent.trim()} \n\n$changeLogContent".trimMargin()
        changeLog.writeText(text = updatedChangeLog, charset = Charsets.UTF_8)
    }
}