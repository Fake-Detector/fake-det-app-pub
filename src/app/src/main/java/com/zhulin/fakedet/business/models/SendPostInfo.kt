package com.zhulin.fakedet.business.models

import fake.detection.post.bridge.api.Bridge.RequestResult

data class SendPostInfo(
    val result: RequestResult
)
