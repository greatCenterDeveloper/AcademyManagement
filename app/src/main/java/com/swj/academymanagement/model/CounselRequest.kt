package com.swj.academymanagement.model

// 학생 권한 상담 신청 클래스
data class CounselRequest(
    val date:String,            // 상담 신청일
    val startTime:String,       // 상담 시작 시간
    val endTime:String,         // 상담 마지막 시간
    val content:String,         // 상담 신청 내용
    val studentId:String,       // 상담 신청 학생 아이디 (FK) -> 학생 테이블의 학생 아이디 (PK)
    var counselRequestCode:String = ""  // 상담 신청 코드 (PK) -> 상담 신청 수정, 삭제에 필요
)
