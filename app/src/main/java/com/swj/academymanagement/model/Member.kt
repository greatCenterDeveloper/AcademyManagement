package com.swj.academymanagement.model

class Member (
    var authority:String,
    var profile:String,
    var emailId:String,
    var password:String,
    var name:String,
    var courseArr:MutableList<String>,
    var call:String
) {
    var course:String = ""
}