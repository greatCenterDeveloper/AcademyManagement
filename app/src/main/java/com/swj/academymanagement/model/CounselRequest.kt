package com.swj.academymanagement.model

data class CounselRequest(
    val date:String,
    val startTime:String,
    val endTime:String,
    val teacher:String,
    val content:String
)
