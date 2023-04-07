package com.swj.academymanagement.model

class Member (
    val authority:String,
    val profile:String,
    val id:String,
    val password:String,
    val name:String,
    var courseArr:MutableList<String> = mutableListOf(),
    val call_number:String
) {
    var course:String = ""
}