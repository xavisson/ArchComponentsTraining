package com.xavisson.archcomponentstraining.data.remote

import com.xavisson.archcomponentstraining.BuildConfig

class RemoteContract {
	companion object {
		const val BASE_API_LAYER = "http://apilayer.net/api/"
		const val LIVE = "live"
		const val ACCESS_KEY = "access_key"
		const val CURRENCIES = "currencies"
		const val FORMAT = "format"
		const val SUCCESS = "success"
		const val QUOTES = "quotes"

		const val ACCESS_KEY_API_LAYER = BuildConfig.PublicApiKey
		const val FORMAT_TYPE = "1"
	}
}
