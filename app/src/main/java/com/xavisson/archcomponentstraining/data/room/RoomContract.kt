package com.xavisson.archcomponentstraining.data.room

class RoomContract {
	companion object {
		const val DATABASE_CURRENCY = "currency.db"
		const val TABLE_CURRENCIES = "currencies"
		private const val SELECT_FROM = "SELECT * FROM "
		const val SELECT_CURRENCIES = SELECT_FROM + TABLE_CURRENCIES
	}
}
