package com.swj.academymanagement.model

// 선생님 권한 학생의 등원, 하원 클래스
data class StudentAttendance(
    val attendanceDate:String,      // 출석 날짜
    val student:String,             // 학생 아이디 (FK)
    val attendanceTime:String,      // 등원 시간
    val gohomeTime:String           // 하원 시간
)