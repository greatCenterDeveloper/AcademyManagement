package com.swj.academymanagement.model

// 선생님 권한 학생 상세 화면에서 학생에게 보낸 문자 메세지 클래스
data class StudentManagementMessage(
    val date:String,        // 문자 보낸 날짜
    val message:String      // 문자 메세지 내용
)
