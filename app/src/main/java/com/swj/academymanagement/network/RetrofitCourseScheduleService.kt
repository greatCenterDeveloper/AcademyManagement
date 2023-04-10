package com.swj.academymanagement.network

import com.swj.academymanagement.model.CourseScheduleTeacher
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitCourseScheduleService {

    @GET("/courseSchedule/courseScheduleTotalList.php")
    fun courseScheduleList(@Query("authority") authority:String,
                           @Query("memberId") memberId:String):Call<MutableList<CourseScheduleTeacher>>
}