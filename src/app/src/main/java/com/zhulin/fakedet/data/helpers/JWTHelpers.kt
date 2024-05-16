package com.zhulin.fakedet.data.helpers

import com.google.gson.Gson
import java.util.Base64


object JWTHelpers {
    data class JWTClaims(val login: String)

    fun String.getJWTClaim(): String {
        val split = this.split(".")
        if (split.size < 2) return ""
        val jsonClaims = getJson(split[1])
        val gson = Gson()
        val claims = gson.fromJson(jsonClaims, JWTClaims::class.java)
        return claims.login
    }

    private fun getJson(strEncoded: String): String {
        val decodedBytes: ByteArray = Base64.getDecoder().decode(strEncoded)
        return String(decodedBytes, charset("UTF-8"))
    }
}