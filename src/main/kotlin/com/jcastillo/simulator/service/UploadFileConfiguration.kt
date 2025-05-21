package com.jcastillo.simulator.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "upload.file")
class UploadFileConfiguration {
    var maxSize: Long = 1024 * 1024 // 1MB default
    var allowedExtensions: List<String> = listOf("json")
    var allowedContentTypes: List<String> = listOf("application/json")

}