package io.github.dryrum.bumpversion

import io.github.dryrum.bumpversion.Constants.GROUP
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.* // ktlint-disable
import javax.inject.Inject

open class BumpVersionCodeTask @Inject constructor(
    private val propertyFile: BumpVersionCodeExt
) : DefaultTask() {

    init {
        group = GROUP
    }

    @TaskAction
    fun execute() {
        val file = File("${project.projectDir}/${propertyFile.path}")
        if (!file.exists()) throw GradleException(
            """
            gradle.properties [${file.path}] doesn't exist!!!
            Create a gradle.properties file into the following dir [${project.projectDir}]
            """.trimIndent()
        )
        updateVersionCode(file)
    }

    private fun updateVersionCode(file: File) {

        val versionProps = Properties()
        versionProps.load(file.inputStream())

        if (!versionProps.containsKey("VERSION_CODE"))throw GradleException(
            """
            Property VERSION_CODE doesn't exist!!!
            Insert a property VERSION_CODE file into the gradle.properties file. Ex:
            VERSION_CODE=10
            """.trimIndent()
        )

        versionProps.getProperty("VERSION_CODE").toIntOrNull() ?: throw GradleException(
            """
            VERSION_CODE doesn't contain a number!!!
            Replace the VERSION_CODE value with a number
            """.trimIndent()
        )

        val bumpedVersionCode = versionProps.getProperty("VERSION_CODE").toLong().inc()
        versionProps.setProperty("VERSION_CODE", "$bumpedVersionCode")
        versionProps.store(file.writer(), null)
        println(file.readText())
    }
}
