package com.swj.academymanagement.network

import com.swj.academymanagement.model.CounselRequest
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitCounselStudentService {

    // 학생 상담 신청 리스트 조회
    @GET("/counselRequest/counselRequestList.php")
    fun counselRequestList(@Query("studentId") studentId:String):Call<MutableList<CounselRequest>>
}