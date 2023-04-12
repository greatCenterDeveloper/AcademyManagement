package com.swj.academymanagement.model

// 선생님 권한 학생이 상담 신청한 상담 신청 클래스
data class CounselRequestTeacher(
    val counselRequestCode:String,      // 상담 신청 코드 (FK)
    val studentId:String,               // 상담 학생 아이디 (FK)
    val name:String,                    // 상담 학생 이름
    val date:String,                    // 상담 신청일
    val counselDate:String,             // 상담 희망일
    val counselStartTime:String,        // 상담 시작 시간
    val counselEndTime:String,          // 상담 마지막 시간
    val counselContent:String           // 상담 신청 내용
)
