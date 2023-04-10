package com.swj.academymanagement.network

import com.swj.academymanagement.model.CounselRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitCounselStudentService {

    // 학생 권한 상담 신청 리스트 조회
    @GET("/counselRequest/counselRequestList.php")
    fun counselRequestList(@Query("studentId") studentId:String):Call<MutableList<CounselRequest>>

    // 학생 권한 상담 신청 DB Insert
    @POST("/counselRequest/counselRequestInsert.php")
    fun counselRequestInsert(@Body counselRequest: CounselRequest): Call<String>

    // 학생 권한 상담 신청 DB Update
    @POST("/counselRequest/counselRequestUpdate.php")
    fun counselRequestUpdate(@Body counselRequest: CounselRequest): Call<String>

    // 학생 권한 상담 신청 DB Delete
    @GET("/counselRequest/counselRequestDelete.php")
    fun counselRequestDelete(@Query("studentId") studentId:String,
           @Query("counselRequestCode") counselRequestCode:String): Call<String>
}