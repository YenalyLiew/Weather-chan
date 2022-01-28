package com.yenaly.weatherchan.logic.model

data class CurrentIpResponse(
    val status: String,
    val province: String,
    val city: String,
    val rectangle: String
)
