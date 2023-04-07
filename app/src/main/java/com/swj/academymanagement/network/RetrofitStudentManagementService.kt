package com.swj.academymanagement.network

import com.swj.academymanagement.model.Member
import com.swj.academymanagement.model.StudentManagementCourse
import com.swj.academymanagement.model.StudentManagementMessage
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitStudentManagementService {

    @GET("/studentManagement/studentList.php")
    fun studentManagementList(@Query("id") id:String):Call<MutableList<Member>>

    @GET("/studentManagement/studentCourseList.php")
    fun studentManagementCourseList(@Query("studentId") studentId:String,
                                    @Query("teacherId") teacherId:String):Call<MutableList<StudentManagementCourse>>

    @FormUrlEncoded
    @POST("/studentManagement/studentMessageList.php")
    fun studentManagementMessageList(@Field("studentId") studentId:String,
                                     @Field("teacherId") teacherId:String):Call<MutableList<StudentManagementMessage>>


}