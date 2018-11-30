package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Post(
    var lectureName: String,
    var professorName: String,
    var year: Int,
    var test: Int,
    var service: Int,
    var reward: Double,
    var vote: Int,
    var uid: String,
    var contents : String
) : Parcelable {
    constructor() : this("", "",-1,-1,-1,-1.0,0,"","")
}

/*
    Parameter 설명
    uid : 현재 로그인한 사용자의 UID
    lectureName
    professorName
    year :  //1 : 1학년, 2: 2학년, 3: 3학년, 4:4학년, 0 : 전체
    test : //1 : 중간, 2: 기말, 0: 중간/기말
    service // 1: 질문답변 2.지식공유
    contents //입력한 내용
    reward
    vote

    */
