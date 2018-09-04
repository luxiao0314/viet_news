package com.viet.news.core.domain.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginRegisterResponse(
        var nick: String = "",
        @SerializedName("phone_no") var phoneNo: String = "",
        @SerializedName("set_password") var set_password: Boolean = false,
        @SerializedName("image_url") var imageUrl: String = "",
        @SerializedName("expires_in_milliseconds") var expiresInMilliseconds: Long = 0L,
        @SerializedName("wallet_user_id") var walletUserId: String = "",
        var token: String = "",
        @SerializedName("user_state") var user_state: String = "",
        @Transient
        var zoneCode: String = ""
):Serializable
