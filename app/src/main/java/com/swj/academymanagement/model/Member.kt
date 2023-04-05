package com.swj.academymanagement.model

class Member (
    val authority:String,
    val profile:String,
    val id:String,
    val password:String,
    val name:String,
    val courseArr:MutableList<String>,
    val call:String
) {
    var course:String = ""
}