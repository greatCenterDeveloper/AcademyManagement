package com.swj.academymanagement.model

import java.io.Serializable

class Member (
    var authority:String,
    var profile:String,
    var emailId:String,
    var password:String,
    var name:String,
    var courseArr:MutableList<String>,
    var call:String
):Serializable {
    var course:String = ""
}