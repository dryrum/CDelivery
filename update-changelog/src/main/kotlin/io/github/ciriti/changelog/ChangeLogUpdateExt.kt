package io.github.ciriti.changelog

open class ChangeLogUpdateExt(
    var changeLogPath: String = "CHANGELOG.md",
    var title: String? = null,
    var content: String? = null,
    var version: String = "X.Y.Z"
)
