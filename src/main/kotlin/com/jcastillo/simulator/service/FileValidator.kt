package com.jcastillo.simulator.service

import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class FileValidator(private val config: UploadFileConfiguration) {

    fun validate(file: Resource): FileValidationResult =
        try {
            when {
                !file.exists() ->
                    FileValidationResult.Invalid("File does not exist")

                file.contentLength() == 0L ||  file.contentLength() > config.maxSize  ->
                    FileValidationResult.Invalid("Invalid size")

                !file.isReadable ->
                    FileValidationResult.Invalid("File is not readable")

                isInvalidExtension(file) -> FileValidationResult.Invalid(
                    "File must have one of these extensions: ${config.allowedExtensions.joinToString(", ")}"
                )
                else -> FileValidationResult.Valid
            }
        } catch (e: IOException) {
            FileValidationResult.Invalid("Error validating file: ${e.message}")
        }

    private fun isInvalidExtension(file: Resource): Boolean = !config.allowedExtensions.any {
        file.filename?.endsWith(".$it", ignoreCase = true) ?: false
    }
}

sealed class FileValidationResult {
    object Valid : FileValidationResult()
    data class Invalid(val message: String) : FileValidationResult()
}
