package io.github.ciriti.replaceinfile

import io.github.ciriti.replaceinfile.Constants.GROUP
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

open class ReplaceInFileTask @Inject constructor(
    private val ext: ReplaceInFileExt
) : DefaultTask() {

    init {
        group = GROUP
    }

    @TaskAction
    fun execute() {
        val sb = StringBuilder()
        val list: NamedDomainObjectContainer<Doc> = ext.docs
        println(list)
        list
            .forEach {
                val content = updateDocument(
                    file = File(it.path),
                    oldText = it.find,
                    newText = it.replaceWith
                )
                sb.append(content + "\n")
            }
        println(sb.toString())
    }

    companion object {
        fun updateDocument(file: File, oldText: String, newText: String): String {
            if (!file.exists()) throw GradleException(
                """
                ${file.name} [${file.path}] doesn't exist!!!
                """.trimIndent()
            )
            val readmeContent = file.readText(charset = Charsets.UTF_8)
            val updateReadme = readmeContent.replace(oldText.toRegex(), newText)
            // README.md content updated
            file.writeText(text = updateReadme, charset = Charsets.UTF_8)
            return file.readText()
        }
    }
}
