package de.koandesign.gibsgif.api.gfycat.service

import okhttp3.Request

fun Request.Builder.addAuthToken(token: String): Request.Builder =
    addHeader("Authorization", "Bearer $token")
