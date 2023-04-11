package com.swj.academymanagement.model

// 선생님 권한 학생 상세 화면에서 강좌 정보
data class StudentManagementCourse(
    val course:String,              // 강좌 코드
    val profile:String,             // 선생님 프로필 사진
    val teacher:String,             // 선생 아이디
    val attendance:String = "0",    // 출석 일수
    val absence:String = "0",       // 결석 일수
    val studentId:String,           // 학생 아이디
    val studentName:String          // 학생 이름
)
