package com.swj.academymanagement.model

data class StudentAttendance(
    val course:String,
    val teacher:String,
    val attendanceDate:String,
    val student:String,
    val attendanceTime:String,
    val gohomeTime:String
)