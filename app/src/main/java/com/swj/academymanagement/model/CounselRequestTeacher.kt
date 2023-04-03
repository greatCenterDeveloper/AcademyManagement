package com.swj.academymanagement.model

data class CounselRequestTeacher(
    val studentId:String,
    val name:String,
    val date:String,
    val counselDate:String,
    val counselStartTime:String,
    val counselEndTime:String,
    val counselContent:String
)
