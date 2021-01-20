package io.github.dryrum.replaceinfile

import groovy.lang.Closure
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

open class ReplaceInFileExt(project: Project) {

    val docs = project.container(Doc::class.java) { name ->
        Doc(name, project)
    }

    /**
     * Configures the server instances registered in the plugin.
     *
     * @param [config] lambda to configure the server instances.
     */
    fun docs(config: DocInstanceConfigurationContainer.() -> Unit) {
        docs.configure(object : Closure<Unit>(this, this) {
            fun doCall() {
                @Suppress("UNCHECKED_CAST")
                (delegate as? DocInstanceConfigurationContainer)?.let {
                    config(it)
                }
            }
        })
    }

    /**
     * Configures the server instances registered in the plugin.
     *
     * @param [config] Groovy closure to configure the server instances.
     */
    fun docs(config: Closure<Unit>) {
        docs.configure(config)
    }
}

internal typealias DocInstanceConfigurationContainer =
    NamedDomainObjectContainer<Doc>

open class Doc(
    val name: String,
    project: Project
) {
    var path by GradleProperty(project, String::class.java, "")
    var find by GradleProperty(project, String::class.java, "")
    var replaceWith by GradleProperty(project, String::class.java, "")
}
