package at.ac.tuwien.sepm.individual.assignment.vehiclerental

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.lang.IllegalArgumentException
import java.lang.System.getProperty
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import kotlin.experimental.and

@Service
class FileService(
    @Value("\${fileService.storageLocation}")
    fileStoreLocation: Path
) {

    companion object {
        const val SHA1 = "SHA-1"
        const val USER_HOME_SHORTCUT = "~/"
        val USER_HOME_PATH = Paths.get(getProperty("user.home"))!!
    }

    private val fileStoreLocation: Path

    init {
        this.fileStoreLocation = cleanupHomeDirectory(fileStoreLocation)
        with(this.fileStoreLocation.toFile()) {
            if (!exists() && !mkdirs()) throw IllegalArgumentException("fileStoreLocation '$fileStoreLocation' does not exist")
            if (!canWrite()) throw IllegalArgumentException("fileStoreLocation '$fileStoreLocation' is not write able")
        }
    }

    private fun cleanupHomeDirectory(fileStoreLocation: Path): Path {
        if (fileStoreLocation.startsWith(USER_HOME_SHORTCUT))
            return USER_HOME_PATH.resolve(fileStoreLocation.subpath(1, fileStoreLocation.count()))
        return fileStoreLocation
    }

    fun storeFile(file: File): String {
        val generatedFileHash = generateFileHash(file)
        logger.debug { "Storing file '${file.name}' with hash code '$generatedFileHash'" }
        val targetFile = fileStoreLocation.resolve(generatedFileHash).toFile()
        if (targetFile.exists())
            logger.debug { "File '${file.name}' with hash code '$generatedFileHash' already existed" }
        else
            file.copyTo(targetFile, false)
        return generatedFileHash
    }

    fun retrieveFile(hash: String): File {
        val hash = hash.toUpperCase()
        with(fileStoreLocation.resolve(hash).toFile()) {
            if (!exists()) throw FileNotFoundException("No file with hash '$hash' exists")
            return this
        }
    }

    fun generateFileHash(file: File): String {
        FileInputStream(file).use {
            return MessageDigest.getInstance(SHA1).digest(it.readAllBytes()).toHexString()
        }
    }

}