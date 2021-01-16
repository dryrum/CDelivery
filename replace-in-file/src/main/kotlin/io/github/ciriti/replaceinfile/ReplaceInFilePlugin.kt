package io.github.ciriti.replaceinfile

import io.github.ciriti.replaceinfile.Constants.EXTENSION_NAME
import io.github.ciriti.replaceinfile.Constants.TASK_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project

class ReplaceInFilePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension: ReplaceInFileExt = project
            .extensions
            .create(EXTENSION_NAME, ReplaceInFileExt::class.java, project)

        project.tasks.create(TASK_NAME, ReplaceInFileTask::class.java, extension)
    }
}
