package com.swj.academymanagement.model

// 선생님 권한 학생 상세 화면에서 학생의 상담 현황 클래스
data class StudentManagementCounsel(
    val date:String,        // 상담일
    val name:String,        // 학생 이름
    val content:String      // 상담 내용
)