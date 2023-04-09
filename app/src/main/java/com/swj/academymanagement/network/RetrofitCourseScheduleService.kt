package com.swj.academymanagement.network

import com.swj.academymanagement.model.CourseScheduleTeacher
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitCourseScheduleService {

    @GET("/courseSchedule/courseScheduleTotalList.php")
    fun courseScheduleList():Call<MutableList<CourseScheduleTeacher>>
}