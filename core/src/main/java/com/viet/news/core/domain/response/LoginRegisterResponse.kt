package com.viet.news.core.domain.response

import com.google.gson.annotations.SerializedName
import com.viet.news.core.api.HttpResponse

class LoginRegisterResponse : HttpResponse<LoginRegisterResponse.LoginRegister>() {

     data class LoginRegister(
            @SerializedName("user_id") var userId: String = "",
            @SerializedName("role_id") var roleId: String = "",
            @SerializedName("nick_name") var nickName: String = "",
            @SerializedName("phone_number") var phoneNumber: String = "",
            @SerializedName("token") var token: String = "",
            var imageUrl: String = ""
    )
}
