package com.swj.academymanagement.network

import com.swj.academymanagement.model.Member
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface RetrofitMemberService {
    // 아이디 중복 검사.. 결과 문자열 : ( "사용", "중복" )
    @GET("/member/memberIdCheck.php")
    fun memberSignUpIdCheck(@Query("id") memberId:String):Call<String>

    // 휴대폰 번호 중복 검사.. 결과 문자열 : ( "사용", "중복" )
    @FormUrlEncoded
    @POST("/member/memberCallCheck.php")
    fun memberSignUpCallNumberCheck(@Field("call") call:String):Call<String>

    // 가입 승인 처리.. 결과 문자열 : ( "가입 완료", "가입 불가" )
    @POST("/member/memberSignup.php")
    fun memberSignUp(@Body member:Member):Call<String>

    // 프로필 사진을 올렸을 경우 파이어베이스 서버에 업로드하기.. 결과 문자열 : ( "성공", "살패" )

    @FormUrlEncoded
    @POST("/member/memberLogin.php")
    fun memberLogin(@Field("id") id:String, @Field("password") password:String):Call<Member>
}