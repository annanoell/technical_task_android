package com.example.myapplication.model

import com.google.gson.annotations.SerializedName


data class User(

    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("gender") var gender: String,
    @SerializedName("status") var status: String,
    @SerializedName("created_at") var created_at: String,
    @SerializedName("updated_at") var updated_at: String
) {
    constructor() : this(
        0, "",
        "", "", "",
        "", ""
    )
}