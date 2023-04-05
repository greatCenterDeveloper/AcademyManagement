package com.swj.academymanagement.model

data class CourseScheduleTeacher(
    val date:String,
    val day:String,
    val course:String,
    val period:String,
    val room:String,
    val students:MutableList<Member>
)