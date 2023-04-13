package com.swj.academymanagement.network

import com.swj.academymanagement.model.CourseSchedule
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// 학생 권한 수업 시간표 Retrofit
interface RetrofitCourseScheduleStudentService {

    // 학생 권한 수업 시간표 리스트
    @GET("/courseSchedule/courseScheduleTotalList.php")
    fun courseScheduleList(@Query("authority") authority:String,
                           @Query("memberId") memberId:String): Call<MutableList<CourseSchedule>>

    // 학생 권한 수업 시간에 적은 내용 입력
    @FormUrlEncoded
    @POST("/courseSchedule/courseScheduleStudentContentInsert.php")
    fun courseScheduleStudentContentInsert(@Field("courseScheduleCode") courseScheduleCode:String,
                                           @Field("studentId") studentId:String,
                                           @Field("courseScheduleContent") courseScheduleContent:String): Call<String>

    // 학생 권한 수업 시간에 적은 내용 읽기
    @FormUrlEncoded
    @POST("/courseSchedule/courseScheduleStudentContentRead.php")
    fun courseScheduleStudentContentRead(@Field("courseScheduleStudentCode") courseScheduleStudentCode:String,
                                         @Field("studentId") studentId:String): Call<String>

    // 학생 권한 수업 시간에 적었던 내용 삭제
    @GET("/courseSchedule/courseScheduleStudentContentDelete.php")
    fun courseScheduleStudentContentDelete(@Query("courseScheduleStudentCode") courseScheduleStudentCode:String,
                                           @Query("studentId") studentId:String): Call<String>
}