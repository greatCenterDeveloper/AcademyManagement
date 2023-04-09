package com.swj.academymanagement.model


data class StudentManagementCourse(
    val course:String,
    val profile:String,
    val teacher:String,
    val attendance:String = "0",
    val absence:String = "0",
    val studentId:String,
    val studentName:String
)
