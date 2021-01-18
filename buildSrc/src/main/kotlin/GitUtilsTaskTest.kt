import org.codehaus.groovy.runtime.ProcessGroovyMethods
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

open class GitUtilsTaskTest @Inject constructor(
) : DefaultTask() {

    init {
        group = "versioning"
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

        userName = System.getenv(KEY_USERNAME) ?: fail(KEY_USERNAME)
        userEmail = System.getenv(KEY_EMAIL) ?: fail(KEY_EMAIL)
        val content = project
            .subprojects
            .map { "${project.rootDir.path}/${it.name}/README.md" }
            .toMutableList()
            .apply {
                add("${project.rootDir.path}/CHANGELOG.md")
                add("${project.rootDir.path}/README.md")
            }
            .map { File(it) }
        addCommitAndPush(content.toList(), userEmail, userName)
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
        val error = ErrorAppend(log)
        "echo ==================================================".runCommand(error = error)
        "echo userEmail: [$pUserEmail] - userName: [$pUserName]".runCommand(error = error)
        "echo ==================================================".runCommand(error = error)
        "git config user.email $pUserEmail".runCommand(error = error)
        "git config user.name $pUserName".runCommand(error = error)
        "git pull --ff-only".runCommand(error = error)
        files
            .forEach {
                checkFile(it)
                "echo ============= > $it".runCommand(error = error)
                "git add $it".runCommand(error = error)
            }
        "git commit -m \"committed files $files\"".runCommand(error = error)
        "git push".runCommand(error = error)

        "echo ======================".runCommand(error = error)
        "echo ${log.joinToString(separator = "\"n\"")}".runCommand(error = error)
        "echo ======================".runCommand(error = error)
        if (log.contains("[rejected]")) {
            throw GradleException(log.toString())
        }
        return "Success!!!"
    }

    private fun String.runCommand(workingDir: File = project.rootDir, error: Appendable) {
        val process: Process = ProcessBuilder(split("\\s(?=(?:[^'\"`]*(['\"`])[^'\"`]*\\1)*[^'\"`]*$)".toRegex()))
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        process.waitForProcessOutput(System.out, error)
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

    class ErrorAppend(private val log: MutableList<String>) : Appendable {

        override fun append(csq: CharSequence?): java.lang.Appendable {
            csq?.let {
                if (it.isNotBlank() && it.isNotEmpty()) {
                    log.add("${it.trim()}")
                }
            }
            return System.err
        }

        override fun append(csq: CharSequence?, start: Int, end: Int): java.lang.Appendable {
            csq?.let {
                if (it.isNotBlank() && it.isNotEmpty()) {
                    log.add("${it.trim()}")
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
