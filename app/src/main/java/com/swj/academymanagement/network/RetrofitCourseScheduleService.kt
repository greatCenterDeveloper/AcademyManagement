package com.swj.academymanagement.network

import com.swj.academymanagement.model.CourseScheduleTeacher
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// 선생님 권한 수업 시간표 Retrofit
interface RetrofitCourseScheduleService {

    // 선생님 권한 수업 시간표 리스트
    @GET("/courseSchedule/courseScheduleTotalList.php")
    fun courseScheduleList(@Query("authority") authority:String,
                           @Query("memberId") memberId:String):Call<MutableList<CourseScheduleTeacher>>
}