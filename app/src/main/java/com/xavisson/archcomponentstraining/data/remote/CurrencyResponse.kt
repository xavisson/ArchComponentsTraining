/**
 * Copyright 2017 Erik Jhordan Rey.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xavisson.archcomponentstraining.data.remote

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
        @SerializedName(RemoteContract.SUCCESS) val isSuccess: Boolean,
        @SerializedName(RemoteContract.QUOTES) val currencyQuotes: Map<String, Double>
)
