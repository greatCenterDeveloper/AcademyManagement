package com.swj.academymanagement.model

data class NaverSearchApiResponse(val total:Int, val display:Int, val items:MutableList<ShoppingItem>)

// 아이템 1개의 클래스
data class ShoppingItem(
    val title:String,
    val link:String,
    val image:String,
    val lprice:String,
    val hprice:String,
    val mallName:String
)