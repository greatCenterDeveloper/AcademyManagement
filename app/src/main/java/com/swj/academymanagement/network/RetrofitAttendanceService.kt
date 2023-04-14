package com.swj.academymanagement.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// 학생 권한 등,하원 Retrofit
interface RetrofitAttendanceService {

    // 등원 버튼 클릭
    @GET("/attendance/studentAttendance.php")
    fun studentAttendance(@Query("studentId") studentId:String,
                          @Query("attendanceTime") attendanceTime:String):Call<String>

    // 하원 버튼 클릭
    @FormUrlEncoded
    @POST("/attendance/studentTheLowerHouse.php")
    fun studentTheLowerHouse(@Field("attendanceCode") attendanceCode:String,
                             @Field("studentId") studentId:String,
                             @Field("theLowerHouseTime") theLowerHouseTime:String):Call<String>
}