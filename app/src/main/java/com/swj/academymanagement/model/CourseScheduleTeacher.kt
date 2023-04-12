package com.swj.academymanagement.model

import com.google.gson.annotations.SerializedName

// 선생님 권한 수업 시간표 클래스
data class CourseScheduleTeacher(
    val date:String,        // 수업 날짜
    val day:String,         // 수업 요일
    val course:String,      // 수업 강좌 코드
    val period:String,      // 교시
    val room:String,        // 강의실 명
    //@SerializedName("studentArr")
    var studentArr:MutableList<Member> = mutableListOf()    // 해당 강좌에 수강 중인 학생 목록
)