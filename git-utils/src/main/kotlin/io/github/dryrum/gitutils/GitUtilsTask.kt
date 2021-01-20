package io.github.dryrum.gitutils

import io.github.dryrum.gitutils.Constants.GROUP
import org.codehaus.groovy.runtime.ProcessGroovyMethods
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

open class GitUtilsTask @Inject constructor(
    private val ext: GitUtilsExt
) : DefaultTask() {

    init {
        group = GROUP
    }

    companion object {
        const val KEY_USERNAME = "GIT_USERNAME"
        const val KEY_EMAIL = "GIT_EMAIL"
    }

    @get:Input
    var userName: String = ""

    @get:Input
    var userEmail: String = ""

    @TaskAction
    fun execute() {

        userName = System.getenv(KEY_USERNAME) ?: fail(
            KEY_USERNAME
        )
        userEmail = System.getenv(KEY_EMAIL) ?: fail(
            KEY_EMAIL
        )
        val content = ext
            .fileList
            .map { File(it) }
            .toList()
            .run { addCommitAndPush(this, userEmail, userName) }
        println(content)
    }

    private fun fail(key: String): Nothing {
        throw GradleException(
            """
            The environment variable [$key] is null!!!
            Please set [GIT_USERNAME] and [GIT_EMAIL] variables in you system:
            ...
            GIT_USERNAME=<your github username>
            GIT_EMAIL=<your github email>
            ...
            """.trimIndent()
        )
    }

    private fun addCommitAndPush(
        files: List<File>,
        pUserEmail: String = userEmail,
        pUserName: String = userName
    ): String {
        val log = mutableListOf<String>()
        "echo ==================================================".runCommand(workingDir = project.rootDir, outputList = log)
        "echo userEmail: [$pUserEmail] - userName: [$pUserName]".runCommand(workingDir = project.rootDir, outputList = log)
        "echo ==================================================".runCommand(workingDir = project.rootDir, outputList = log)
        "git config user.email $pUserEmail".runCommand(workingDir = project.rootDir, outputList = log)
        "git config user.name $pUserName".runCommand(workingDir = project.rootDir, outputList = log)
        "git pull --ff-only".runCommand(workingDir = project.rootDir, outputList = log)
        files
            .forEach {
                checkFile(it)
                "echo =============> git add $it".runCommand(workingDir = project.rootDir, outputList = log)
                "git add $it".runCommand(workingDir = project.rootDir, outputList = log)
            }
        "git commit -m \"committed files $files\"".runCommand(workingDir = project.rootDir, outputList = log)
        "git push".runCommand(workingDir = project.rootDir, outputList = log)

        "echo ========= start output ========".runCommand(workingDir = project.rootDir, outputList = log)
        log.forEach { "echo $it".runCommand(workingDir = project.rootDir, outputList = log) }
        "echo =========  end output  ==============".runCommand(workingDir = project.rootDir, outputList = log)
        if (log.contains("[rejected]") || log.contains("fatal: not in a git directory")) {
            throw GradleException(log.toString())
        }
        return "Success!!!"
    }

    private fun String.runCommand(workingDir: File, outputList: MutableList<String>): List<String> {
        val error: AppendableErrorOutput = AppendableErrorOutputImpl(outputList)
        val process: Process = ProcessBuilder(split("\\s(?=(?:[^'\"`]*(['\"`])[^'\"`]*\\1)*[^'\"`]*$)".toRegex()))
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        process.waitForProcessOutput(System.out, error)
        return error.output
    }

    private fun Process.waitForProcessOutput(
        output: Appendable,
        error: Appendable
    ) {
        val tout = ProcessGroovyMethods.consumeProcessOutputStream(this, output)
        val terr = ProcessGroovyMethods.consumeProcessErrorStream(this, error)
        tout.join()
        terr.join()
        this.waitFor()
        ProcessGroovyMethods.closeStreams(this)
    }

    interface AppendableErrorOutput : Appendable {
        val output: List<String>
    }

    fun createAppendableLog(list: MutableList<String>): AppendableErrorOutput {
        return AppendableErrorOutputImpl(list)
    }

    private class AppendableErrorOutputImpl(val list: MutableList<String>) : AppendableErrorOutput {
        override val output: List<String> = list
        override fun append(csq: CharSequence?): java.lang.Appendable {
            csq?.let {
                if (it.isNotBlank() && it.isNotEmpty()) {
                    list.add("${it.trim()}")
                }
            }
            return System.err
        }

        override fun append(csq: CharSequence?, start: Int, end: Int): java.lang.Appendable {
            csq?.let {
                if (it.isNotBlank() && it.isNotEmpty()) {
                    list.add("${it.trim()}")
                }
            }
            return System.err
        }

        override fun append(c: Char): java.lang.Appendable {
            return System.err
        }
    }

    private fun checkFile(file: File) {
        if (!file.exists()) {
            throw GradleException(
                """
                the file [${file.path}] does not exist!!!
                """.trimIndent()
            )
        }
    }
}
