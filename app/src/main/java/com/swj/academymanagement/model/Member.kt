package com.swj.academymanagement.model

data class Member (
    var authority:String,
    var profile:String,
    var id:String,
    var password:String,
    var name:String,
    var courseArr:MutableList<String>,
    var call:String
)