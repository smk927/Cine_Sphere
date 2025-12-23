package com.example.cinesphere.data.dto

fun WatchProviderDto.toOttPlatform(): String? {
    return results["IN"]?.flatrate?.firstOrNull()?.providerName
}