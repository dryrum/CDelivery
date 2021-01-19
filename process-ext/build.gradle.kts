plugins{
    id("java-library")
    id("java-gradle-plugin")
    id("org.jetbrains.kotlin.jvm")
}

apply(from = "${rootDir.path}/buildfile/publish.gradle")
apply(from = rootDir.path + "/buildfile/ktlint_utils.gradle")

group = project.property("GROUP_ID") as String
version = rootProject.extra.get("VERSION_NAME") as String

dependencies {

    implementation (fileTree(mapOf("dir" to "libs", "include" to "*.jar")))
    // we need compile otherwise the resulting jar won't work
    implementation (Libs.kotlin_stdlib_jdk7)
}

// to configure both compileKotlin and compileTestKotlin
// https://kotlinlang.org/docs/reference/using-gradle.html#compiler-options
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

// https://docs.gradle.org/current/userguide/kotlin_dsl.html?_ga=2.260720329.512580604.1602165526-681461519.1596023720#type-safe-accessors
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.getByName<Jar>("jar") {

    val dirsList: Provider<List<Any>> = configurations
        .compile
        .map { conf ->
            conf
                .files
                .map {
                    when(it.isDirectory) {
                        true -> it
                        false -> zipTree(it)
                    }
                }
        }
    from(dirsList)
}