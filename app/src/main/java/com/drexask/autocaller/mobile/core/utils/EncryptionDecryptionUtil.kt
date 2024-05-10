package com.drexask.autocaller.mobile.core.utils

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object EncryptionDecryptionUtil {

    fun encrypt(secret: String, data: String): String {
        val secretByteArray = secret.toByteArray()
        val dataByteArray = data.toByteArray()

        val secretKey = SecretKeySpec(secretByteArray.copyOf(16), "AES")
        val cipher = Cipher.getInstance("AES")

        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val cipherText = cipher.doFinal(dataByteArray)
        return Base64.getEncoder().encodeToString(cipherText)
    }

    fun decrypt(secret: String, data: String): String {
        val secretByteArray = secret.toByteArray()
        val dataByteArray = data.toByteArray()

        val secretKey = SecretKeySpec(secretByteArray.copyOf(16), "AES")
        val cipher = Cipher.getInstance("AES")

        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val readCipherText = Base64.getDecoder().decode(dataByteArray)
        return cipher.doFinal(readCipherText).decodeToString()
    }

}
