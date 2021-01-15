package io.github.ciriti.changelog

import io.github.ciriti.changelog.Constants.TASK_NAME
import io.github.ciriti.changelog.Constants.EXTENSION_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project

class ChangeLogUpdatePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create(EXTENSION_NAME, ChangeLogUpdateExt::class.java)
        project.tasks.create(TASK_NAME, ChangeLogUpdateTask::class.java, extension)
    }
}
