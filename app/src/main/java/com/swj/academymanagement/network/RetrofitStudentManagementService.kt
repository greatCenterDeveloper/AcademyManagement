package com.swj.academymanagement.network

import com.swj.academymanagement.model.Member
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitStudentManagementService {

    @GET("/studentManagement/studentList.php")
    fun studentManagementList(@Query("id") id:String):Call<List<Member>>
}