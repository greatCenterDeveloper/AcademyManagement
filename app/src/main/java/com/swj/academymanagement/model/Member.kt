package com.swj.academymanagement.model

import java.io.Serializable

data class Member (
    var authority:String,
    var profile:String,
    var emailId:String,
    var password:String,
    var name:String,
    var courseArr:MutableList<String>,
    var call:String
):Serializable