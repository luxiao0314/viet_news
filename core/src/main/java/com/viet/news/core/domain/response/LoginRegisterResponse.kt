package com.viet.news.core.domain.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginRegisterResponse(@SerializedName("fans_count") var fansCount: Int = 0,
                            @SerializedName("user_id") var userId: String? = "",
                            @SerializedName("role_id") var roleId: String? = "",
                            @SerializedName("follow_count") var followCount: Int = 0,
                            @SerializedName("nick_name") var nickName: String? = "",
                            @SerializedName("phone_number") var phoneNumber: String? = "",
                            @SerializedName("token") var token: String? = "",
                            var avatar: String? = "") : Serializable