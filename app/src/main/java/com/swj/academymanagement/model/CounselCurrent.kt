package com.swj.academymanagement.model

// 선생님 권한 상담 현황 클래스
data class CounselCurrent(
    val counselRequestCode:String,      // 상담 신청 코드 (FK)
    val studentId:String,               // 상담 학생 아이디(FK)
    val studentName:String = "",        // 상담 받은 학생 이름
    val teacherId:String,               // 상담한 선생님 아이디(FK)
    val teacherName:String = "",        // 상담한 선생님 이름
    val date:String,                    // 상담 일자
    val counselContent:String,          // 상담 내용
    val counselCode:String = ""         // 상담 테이블 상담 코드(PK) -> 상담 내용 수정, 삭제 시 필요
)
