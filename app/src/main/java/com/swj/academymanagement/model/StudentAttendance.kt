package com.swj.academymanagement.model

data class StudentAttendance(
    val attendanceDate:String,
    val student:String,
    val attendanceTime:String,
    val gohomeTime:String
)