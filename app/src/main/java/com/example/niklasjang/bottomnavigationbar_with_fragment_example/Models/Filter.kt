package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models

import android.os.Parcel
import android.os.Parcelable

class Filter(
    var fltClassName: String? = null, var fltProfessorName: String? = null, var fltYear: Int? = null,
    var fltSemester: Int? = null, var fltTest: Int? = null
) : Parcelable {
    /*
        var fltClassName: String? = null

        var fltProfessorName: String? = null

        var fltYear: Int? = null //1: 1학년, 2: 2학년, 3:3학년, 4 : 4학년

        var fltSemester: Int? = null //1 : 1학기, 2 : 2학기, 3: 모든 학기 개설 과목

        var fltTest: Int? = null //1 : 중간고사, 2: 기말고사, 3: 모든 시험
    */
    override fun toString(): String {
        return "fltClassName : ${fltClassName}, fltProfessorName : ${fltProfessorName}, fltTest : ${fltTest}"
    }

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(fltClassName)
        writeString(fltProfessorName)
        writeValue(fltYear)
        writeValue(fltSemester)
        writeValue(fltTest)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Filter> = object : Parcelable.Creator<Filter> {
            override fun createFromParcel(source: Parcel): Filter =
                Filter(source)

            override fun newArray(size: Int): Array<Filter?> = arrayOfNulls(size)
        }
    }
}
