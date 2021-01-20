package io.github.dryrum.bumpversion

import io.github.dryrum.bumpversion.Constants.TASK_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project

class BumpVersionCodePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extFilePath = project.extensions.create("versionCodePropPath", BumpVersionCodeExt::class.java)
        project.tasks.create(TASK_NAME, BumpVersionCodeTask::class.java, extFilePath)
    }
}
