package com.nanoporetech.scainter.ui.fake

import com.nanoporetech.scainter.notification.DeviceTokenRegistrar

class FakeDeviceTokenRegistrar : DeviceTokenRegistrar {
    var lastRegisteredUserId: String? = null

    override suspend fun registerDeviceToken(userId: String) {
        lastRegisteredUserId = userId
    }
}