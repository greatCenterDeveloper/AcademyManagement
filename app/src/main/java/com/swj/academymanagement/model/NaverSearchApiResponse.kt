package com.swj.academymanagement.model

// 네이버 오픈 API로 가져온 도서 클래스
data class NaverSearchApiResponse(
    val total:Int,                          // 검색 결과 전체 개수
    val display:Int,                        // 화면에 보여질 개수
    val items:MutableList<ShoppingItem>     // 도서 한권 정보
)

// 도서 1권의 클래스
data class ShoppingItem(
    val title:String,       // 제목
    val link:String,        // 쇼핑몰 이동 링크
    val image:String,       // 이미지
    val author:String,      // 작가
    val discount:String,    // 가격
    val publisher:String    // 출판사
)