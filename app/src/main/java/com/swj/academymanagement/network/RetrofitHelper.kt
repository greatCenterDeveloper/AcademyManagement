package com.swj.academymanagement.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitHelper {
    companion object {
        fun getRetrofitInstance() :Retrofit {
            val builder:Retrofit.Builder = Retrofit.Builder()
            builder.baseUrl("http://academymrhi.dothome.co.kr/")
            builder.addConverterFactory(ScalarsConverterFactory.create())
            builder.addConverterFactory(GsonConverterFactory.create())
            return builder.build()
        }
    }
}