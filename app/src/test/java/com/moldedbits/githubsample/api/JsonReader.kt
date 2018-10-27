package com.moldedbits.githubsample.api

import java.io.File

class JsonReader {

    fun readJsonFile(jsonFile: String): String {
        return File("${basePath()}/$jsonFile").readLines().joinToString(separator = "\n")
    }

    private fun basePath(): String? {
        val classLoader: ClassLoader = JsonReader::class.java.classLoader
        return classLoader.getResource("json/")?.path
    }
}