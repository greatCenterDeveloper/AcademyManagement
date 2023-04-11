package com.swj.academymanagement.model

// 네이버 오픈 API로 가져온 도서 클래스
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