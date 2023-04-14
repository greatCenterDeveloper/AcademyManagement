package com.swj.academymanagement.network

import com.swj.academymanagement.model.CourseScheduleTeacher
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// 선생님 권한 수업 시간표 Retrofit
interface RetrofitCourseScheduleService {

    // 선생님 권한 수업 시간표 리스트
    @GET("/courseSchedule/courseScheduleTotalList.php")
    fun courseScheduleList(@Query("authority") authority:String,
                           @Query("memberId") memberId:String):Call<MutableList<CourseScheduleTeacher>>

    // 선생님 권한 학생 출결 체크
    @FormUrlEncoded
    @POST("/courseSchedule/courseScheduleAttendanceCheck.php")
    fun courseScheduleAttendanceCheck(@Field("courseScheduleCode") courseScheduleCode:String,
                                      @Field("studentId") studentId:String,
                                      @Field("attendanceState") attendanceState:String,
                                      @Field("attendanceTime") attendanceTime:String): Call<String>

    // 선생님 권한 학생 체크된 출결 체크 읽어오기
    @FormUrlEncoded
    @POST("/courseSchedule/courseScheduleAttendanceRead.php")
    fun courseScheduleAttendanceRead(@Field("courseScheduleCode") courseScheduleCode:String,
                                     @Field("studentId") studentId:String): Call<String>
}