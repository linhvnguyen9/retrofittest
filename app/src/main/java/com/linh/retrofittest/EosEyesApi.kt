package com.linh.retrofittest

import retrofit2.http.GET
import retrofit2.http.Query

interface EosEyesApi {
    @GET("ram/kline")
    suspend fun getRamOhlcData(
        @Query("unit") unit: String,
        @Query("start") start: Long,
        @Query("end") end: Long
    ): RamOhlcResponse
}

data class RamOhlcResponse(
    val code: Int? = 0,
    val `data`: List<Data?>? = listOf(),
    val message: String? = ""
) {
    data class Data(
        val close: Double? = 0.0,
        val date: Long? = 0,
        val high: Double? = 0.0,
        val low: Double? = 0.0,
        val `open`: Double? = 0.0,
        val usd: Double? = 0.0,
        val volume: Long? = 0
    )
}