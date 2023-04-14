package com.swj.academymanagement.network

import com.swj.academymanagement.model.CounselCurrent
import com.swj.academymanagement.model.CounselRequestTeacher
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// 선생님 권한 상담 관련 Retrofit
interface RetrofitCounselService {

    // 선생님 강좌에 수강 중인 학생이 신청한 상담 신청 리스트
    @GET("/counsel/counselRequestStudentList.php")
    fun counselRequestStudentList(@Query("teacherId") teacherId:String):Call<MutableList<CounselRequestTeacher>>

    // 상담 신청에서 상담 내용 입력후 상담 테이블에 Insert
    @POST("/counsel/counselInsert.php")
    fun counselInsert(@Body counselCurrent: CounselCurrent):Call<String>

    // 상담 내용 입력한 상담 현황 리스트
    @GET("/counsel/counselList.php")
    fun counselList(@Query("teacherId") teacherId:String):Call<MutableList<CounselCurrent>>

    // 상담 현황 리스트 학생 이름 검색
    @GET("/counsel/counselNameSearch.php")
    fun counselNameSearch(@Query("teacherId") teacherId:String,
                          @Query("name") name:String):Call<MutableList<CounselCurrent>>

    // 상담 현황 수정
    @POST("/counsel/counselUpdate.php")
    fun counselUpdate(@Body counselCurrent: CounselCurrent):Call<String>

    // 상담 현황 삭제
    @GET("/counsel/counselDelete.php")
    fun counselDelete(@Query("counselCode") counselCode:String):Call<String>
}