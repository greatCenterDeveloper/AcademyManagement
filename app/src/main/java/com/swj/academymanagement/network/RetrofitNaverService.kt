package com.swj.academymanagement.network

import com.swj.academymanagement.model.NaverSearchApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

// 선생님 / 학생 권한 네이버 오픈 API 도서 검색 Retrofit
interface RetrofitNaverService {

    // 네이버 오픈 API 도서 검색
    @Headers("X-Naver-Client-Id: gwGTRDoelyNjhz1rbEVL", "X-Naver-Client-Secret: Ag6O4jfFyD")
    @GET("/v1/search/book.json?display=100")
    fun searchBookData(@Query("query") query:String): Call<NaverSearchApiResponse>
}