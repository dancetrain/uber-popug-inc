package com.uberpopug.tasktracker

import io.ktor.network.tls.certificates.*
import java.io.File

object CertificateGenerator {
  @JvmStatic
  fun main(args: Array<String>) {
    val jksFile = File("build/temporary.jks").apply {
      parentFile.mkdirs()
    }

    if (!jksFile.exists()) {
      generateCertificate(jksFile) // Generates the certificate
    }
  }
}
