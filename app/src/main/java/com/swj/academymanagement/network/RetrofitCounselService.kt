package com.swj.academymanagement.network

import com.swj.academymanagement.model.CounselRequestTeacher
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitCounselService {

    @GET("/counsel/counselRequestStudentList.php")
    fun counselRequestStudentList(@Query("teacherId") teacherId:String):Call<MutableList<CounselRequestTeacher>>
}