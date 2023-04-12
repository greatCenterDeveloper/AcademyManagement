package com.swj.academymanagement.model

// 선생님 / 학생 권한 노트 클래스
data class Note(
    val num:Int = 0,        // num ( auto_increment -> primary key )
    val kind:String,        // 노트 종류 ( 할일, 노트 )
    val title:String,       // 노트 제목
    val date:String,        // 작성일
    val content:String,     // 노트 내용
    val authority:String    // 권한 ( 선생님, 학생 )
)
