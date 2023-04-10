package com.swj.academymanagement.model

data class CounselRequest(
    val date:String,
    val startTime:String,
    val endTime:String,
    val content:String,
    val studentId:String,
    var counselRequestCode:String = ""
)
