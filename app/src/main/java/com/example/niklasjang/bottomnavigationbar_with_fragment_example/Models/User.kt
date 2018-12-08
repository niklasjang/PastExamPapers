package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val username: String, val profileImageUrl: String="") : Parcelable {
    constructor() : this("", "", "")
}


