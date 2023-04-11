package com.swj.academymanagement.model

// 선생님 권한 학생 상세 화면에서 학생의 특정 강좌 출석부 다이얼로그 출석 현황 클래스
data class StudentManagementDialogAttendance(
    val day:String,                 // 요일
    val period:String,              // 교시
    val attendanceTime:String,      // 출석 시간 (년-월-일 시:분:초)
    val attendanceState:String      // 출석 상태 ( 출석, 결석, 지각 )
)
