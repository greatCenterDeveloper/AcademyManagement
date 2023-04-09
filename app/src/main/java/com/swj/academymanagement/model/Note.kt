package com.swj.academymanagement.model

data class Note(
    val num:Int = 0,
    val kind:String,
    val title:String,
    val date:String,
    val content:String,
    val authority:String
)
