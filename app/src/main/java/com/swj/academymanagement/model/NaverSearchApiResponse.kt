package com.swj.academymanagement.model

data class NaverSearchApiResponse(var total:Int, var display:Int, var items:MutableList<ShoppingItem>)

// 아이템 1개의 클래스
data class ShoppingItem(
    var title:String,
    var link:String,
    var image:String,
    var lprice:String,
    var hprice:String,
    var mallName:String
)