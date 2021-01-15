package io.github.ciriti.changelog

open class ChangeLogUpdateExt(
    var changeLogPath: String = "CHANGELOG.md",
    var releaseNotePath: String = "release_note.txt",
    var title : String,
    var content : String,
    var version : String
)
