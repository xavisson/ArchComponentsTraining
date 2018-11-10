package com.xavisson.archcomponentstraining.data.remote


data class ExchangesResponse(
		val success: Boolean,
		val quotes: Map<String, Double>
)
