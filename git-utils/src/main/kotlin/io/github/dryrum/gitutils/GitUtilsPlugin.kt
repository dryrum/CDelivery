package io.github.dryrum.gitutils

import io.github.dryrum.gitutils.Constants.EXTENSION_NAME
import io.github.dryrum.gitutils.Constants.TASK_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project

class GitUtilsPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create(EXTENSION_NAME, GitUtilsExt::class.java)
        project.tasks.create(TASK_NAME, GitUtilsTask::class.java, extension)
    }
}
