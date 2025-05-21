import com.jcastillo.simulator.service.FileValidationResult
import com.jcastillo.simulator.service.FileValidator
import com.jcastillo.simulator.service.UploadFileConfiguration
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.core.io.Resource

class FileValidatorTest {
    private lateinit var config: UploadFileConfiguration
    private lateinit var validator: FileValidator
    private lateinit var resource: Resource

    @BeforeEach
    fun setup() {
        config = UploadFileConfiguration().apply {
            maxSize = 1024 * 1024 // 1MB
            allowedExtensions = listOf("json")
            allowedContentTypes = listOf("application/json")
        }
        validator = FileValidator(config)
        resource = mockk<Resource>()
    }

    @Test
    fun `validate should return invalid when file does not exist`() {
        every { resource.exists() } returns false

        val result = validator.validate(resource)

        assertInstanceOf<FileValidationResult.Invalid>(result, "File doe not exist")
    }

    @Test
    fun `validate should return invalid when file is empty`() {
        every { resource.exists() } returns true
        every { resource.contentLength() } returns 0L

        val result = validator.validate(resource)

        assertInstanceOf<FileValidationResult.Invalid>(result, "Invalid file size: 0 bytes")
    }

    @Test
    fun `validate should return invalid when file exceeds max size`() {
        every { resource.exists() } returns true
        every { resource.contentLength() } returns config.maxSize + 1

        val result = validator.validate(resource)

        assertInstanceOf<FileValidationResult.Invalid>(result, "Invalid file size: ${config.maxSize + 1} bytes")
    }

    @Test
    fun `validate should return invalid when file is not readable`() {
        every { resource.exists() } returns true
        every { resource.contentLength() } returns 100L
        every { resource.isReadable } returns false

        val result = validator.validate(resource)

        assertInstanceOf<FileValidationResult.Invalid>(result, "File is not readable")
    }

    @ParameterizedTest
    @ValueSource(strings = ["test.txt", "data.xml", "file.yaml", "document"])
    fun `validate should return invalid for non-allowed extensions`(filename: String) {
        // Given
        every { resource.exists() } returns true
        every { resource.contentLength() } returns 100L
        every { resource.isReadable } returns true
        every { resource.filename } returns filename

        // When
        val result = validator.validate(resource)

        assertInstanceOf<FileValidationResult.Invalid>(result, "Invalid extension: $filename. Must be one of: json")
    }

    @ParameterizedTest
    @ValueSource(strings = ["test.JSON", "data.json", "file.JsOn"])
    fun `validate should accept files with allowed extensions regardless of case`(filename: String) {
        every { resource.exists() } returns true
        every { resource.contentLength() } returns 100L
        every { resource.isReadable } returns true
        every { resource.filename } returns filename

        val result = validator.validate(resource)

        assertInstanceOf< FileValidationResult.Valid>(result)
    }

    @Test
    fun `validate should return valid for valid file`() {
        every { resource.exists() } returns true
        every { resource.contentLength() } returns 100L
        every { resource.isReadable } returns true
        every { resource.filename } returns "test.json"

        val result = validator.validate(resource)

        assertInstanceOf< FileValidationResult.Valid>(result)
    }
}