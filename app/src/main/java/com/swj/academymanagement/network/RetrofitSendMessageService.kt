package com.swj.academymanagement.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

// 선생님 권한 메세지 Retrofit
interface RetrofitSendMessageService {

    // 메세지 전송
    @FormUrlEncoded
    @POST("/sendMessage/sendMessage.php")
    fun sendMessage(@Field("studentId") studentId:String,
                    @Field("teacherId") teacherId:String,
                    @Field("message") message:String,
                    @Field("image") image:String,
                    @Field("index") index:Int,
                    @Field("size") size:Int):Call<String>
}