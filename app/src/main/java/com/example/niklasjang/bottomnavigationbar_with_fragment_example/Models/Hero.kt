package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models

class Hero(val id: String ,val name: String, val rating:String, var check: Int=0, val hashID: String) {

    constructor() : this("","","",0,""){

    }
}