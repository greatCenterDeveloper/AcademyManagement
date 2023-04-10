package com.swj.academymanagement.model

import com.google.gson.annotations.SerializedName

data class CourseScheduleTeacher(
    val date:String,
    val day:String,
    val course:String,
    val period:String,
    val room:String,
    //@SerializedName("studentArr")
    var studentArr:MutableList<Member> = mutableListOf()
)