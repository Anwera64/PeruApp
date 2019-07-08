package com.anwera64.peruapp.data.model

import com.google.gson.annotations.SerializedName

class Pageable<T>(var content: ArrayList<T>,
                  @SerializedName("last") var isLast: Boolean,
                  @SerializedName("number") var page: Int)
