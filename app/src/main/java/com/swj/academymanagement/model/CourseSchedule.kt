package com.swj.academymanagement.model

// 학생 권한 수업 시간표 클래스
data class CourseSchedule(
    val date:String = "",                       // 수업 날짜
    val day:String = "",                        // 수업 요일
    val course:String = "",                     // 수업 강좌 코드
    val period:String = "",                     // 교시
    val room:String = "",                       // 강의실 명
    val isMyCourse:Boolean = false,             // 현재 수강 중인 강좌인지 아닌지..
    val courseScheduleCode:String = "",         // 수업 시간표 코드 (FK)
    val courseScheduleStudentCode:String = "",  // 학생 수업 시간표 내용 입력 코드 (PK)
    val courseScheduleContent:String = ""       // 수업 시간에 적은 메모 내용
)