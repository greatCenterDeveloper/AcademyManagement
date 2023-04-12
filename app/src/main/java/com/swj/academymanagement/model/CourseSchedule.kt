package com.swj.academymanagement.model

// 학생 권한 수업 시간표 클래스
data class CourseSchedule(
    val date:String = "",       // 수업 날짜
    val day:String = "",        // 수업 요일
    val course:String = "",     // 수업 강좌 코드
    val period:String = "",     // 교시
    val room:String = ""        // 강의실 명
)