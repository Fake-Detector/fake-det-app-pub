package com.zhulin.fakedet.business.models

import fake.detection.auth.Auth.ErrorResponse

data class AuthInfo(
    val result: Boolean = false,
    var errorStatus: ErrorResponse = ErrorResponse.Unexpected,
    val userName: String? = null,
    var token: String? = null
)