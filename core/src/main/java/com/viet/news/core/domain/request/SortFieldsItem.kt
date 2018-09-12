package com.viet.news.core.domain.request

import com.google.gson.annotations.SerializedName

data class SortFieldsItem(@SerializedName("direction")
                          val direction: String = "",
                          @SerializedName("field_name")
                          val fieldName: String = "")