@file:JvmName("TestUtils")
@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package io.github.ciriti.gitutils

import org.gradle.internal.impldep.org.junit.Assert
import org.junit.rules.TemporaryFolder
import java.io.File

/**
 * Receive file.json and return the content as string
 */
fun String.readFileContent(): String = Thread.currentThread()
    .contextClassLoader
    .getResourceAsStream(this)
    .bufferedReader().use { it.readText() }
    .trimIndent()

fun TemporaryFolder.createNestedFolder(fileName: String, vararg folders: String): File {
    newFolder(folders.first())
    (1 until folders.size).forEach {
        newFolder(folders[it - 1], folders[it])
    }
    val path = folders.drop(1).fold(folders.first()) { acc, s -> "$acc/$s" }
    return this.newFile("$path/$fileName")
}

fun String.getFolderByName(): File =
    File(
        Thread.currentThread()
            .contextClassLoader
            .getResource(this)
            .file
    )

fun String.getFileByName(): File =
    File(
        Thread.currentThread()
            .contextClassLoader
            .getResource(this)
            .file
    )

infix fun <T> T.assertEquals(t: T) = apply { Assert.assertEquals(t, this) }
fun <T : Boolean> T.assertTrue() = apply { Assert.assertTrue(this) }
fun <T> T?.assertNotNull(): T = apply { Assert.assertNotNull(this) }!!

fun TemporaryFolder.createSrcFile(name: String, content: String): File {

    return File("${javaSrc.path}/$name")
        .also {
            it.createNewFile()
            it.appendText(text = content)
        }
}
fun TemporaryFolder.createKtSrcFile(name: String, content: String): File {

    return File("${kotlinSrc.path}/$name")
        .also {
            it.createNewFile()
            it.appendText(text = content)
        }
}

fun TemporaryFolder.getSrcFile(name: String): File = javaSrc.resolve("$kotlinSrc/$name")

val TemporaryFolder.kotlinSrc: File
    get() {
        val dir = File("${this.root}/src/main/kotlin")
        return when (dir.exists()) {
            true -> dir
            false -> this.newFolder("src", "main", "kotlin")
        }
    }

val TemporaryFolder.javaSrc: File
    get() {
        val dir = File("${this.root}/src/main/java")
        return when (dir.exists()) {
            true -> dir
            false -> this.newFolder("src", "main", "java")
        }
    }
