package com.anwera64.peruapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Token(
    @SerializedName("access_token")
    var accessToken: String,
    @SerializedName("token_type")
    var tokenType: String,
    @SerializedName("refresh_token")
    var refreshToken: String,
    var scope: String
) : Serializable