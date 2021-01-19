import org.codehaus.groovy.runtime.ProcessGroovyMethods
import java.io.File

fun String.runCommand(workingDir: File, outputList: MutableList<String>): List<String> {
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
