package io.github.dryrum.gitutils

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class GitUtilsPluginTest {

    @Rule
    @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()

    @Rule
    @JvmField
    val testProjectDirErrorCase: TemporaryFolder = TemporaryFolder()

    @Test
    fun `GIVEN a folder without git CATCH an UnexpectedBuildFailure`() {

//        val buildFile: File by lazy {
//            testProjectDir.newFile("build.gradle")
//        }
//
//        buildFile.appendText("build.gradle.txt".readFileContent())
//
//        val testRepo = "https://github.com/dryrum/test.git"
//        val log = mutableListOf<String>()
//        "git init".runCommand(testProjectDir.root, log)
//        "git config user.email ciriti@gmail.com".runCommand(testProjectDir.root, log)
//        "git config user.name ciriti".runCommand(testProjectDir.root, log)
//        "git remote add origin $testRepo".runCommand(testProjectDir.root, log)
//        "git fetch --all --prune".runCommand(testProjectDir.root, log)
//        "git checkout main".runCommand(testProjectDir.root, log)
//
//        val readmeOriginal = File("${testProjectDir.root}/README.md")
//        readmeOriginal.exists().assertTrue()
//
//        val timestamp = "timestamp[${Instant.now()}]\"n\""
//
//        readmeOriginal.appendText(timestamp)

//        val output = GitUtilsTask.addCommitAndPush(
//            listOf(readmeOriginal),
//            "ciriti@gmail.com",
//            "ciriti",
//            testProjectDir.root
//        )

//        "git push \"https://ciriti:${System.getenv("GITHUB_TOKEN")}@github.com/test.git\" main".runCommand(testProjectDir.root, log)

//        println(log.joinToString(separator = "\n"))

        /**
         * In the test env I have this error:
         * fatal: could not read Username for 'https://github.com': Device not configured
         */
    }
}
