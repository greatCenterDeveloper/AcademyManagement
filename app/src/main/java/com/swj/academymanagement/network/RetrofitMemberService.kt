package com.swj.academymanagement.network

import com.swj.academymanagement.model.Member
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// 선생님 / 학생 회원 정보 Retrofit
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

    // 회원 로그인
    @FormUrlEncoded
    @POST("/member/memberLogin.php")
    fun memberLogin(@Field("id") id:String,
                    @Field("password") password:String):Call<Member>

    // 아이디 찾기
    @FormUrlEncoded
    @POST("/member/findMemberId.php")
    fun findMemberId(@Field("call") call:String):Call<String>

    // 비밀번호 찾기
    @FormUrlEncoded
    @POST("/member/findMemberPassword.php")
    fun findMemberPassword(@Field("id") id:String,
                           @Field("call") call:String):Call<String>

    // 비밀번호 수정
    @FormUrlEncoded
    @POST("/member/updateMemberPassword.php")
    fun updateMemberPassword(@Field("prevPassword") prevPassword:String,
                             @Field("password") password:String,
                             @Field("id") id:String):Call<String>

    // 회원 탈퇴
    @FormUrlEncoded
    @POST("/member/memberUnregister.php")
    fun memberUnregister(@Field("id") id:String):Call<String>

    // 선생님 / 학생 메인 화면 NavigationView 프로필 이미지 이름 가져오기
    @FormUrlEncoded
    @POST("/member/getMemberProfile.php")
    fun getMemberProfile(@Field("id") id:String):Call<String>

    // 선생님 / 학생 메인 화면 NavigationView 프로필 이미지 변경
    @FormUrlEncoded
    @POST("/member/updateMemberProfile.php")
    fun updateMemberProfile(@Field("id") id:String,
                            @Field("profile") profile:String):Call<String>
}