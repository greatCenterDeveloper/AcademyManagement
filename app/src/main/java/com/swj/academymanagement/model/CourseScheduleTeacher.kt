package com.swj.academymanagement.model

data class CourseScheduleTeacher(
    val date:String,
    val day:String,
    val course:String,
    val period:String,
    val room:String,
    var students:MutableList<Member> = mutableListOf()
)