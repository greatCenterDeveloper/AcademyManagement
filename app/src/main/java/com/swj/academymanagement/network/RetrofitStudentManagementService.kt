package com.swj.academymanagement.network

import com.swj.academymanagement.model.Member
import com.swj.academymanagement.model.StudentAttendance
import com.swj.academymanagement.model.StudentManagementCourse
import com.swj.academymanagement.model.StudentManagementDialogAttendance
import com.swj.academymanagement.model.StudentManagementMessage
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitStudentManagementService {

    // 내 강좌 학생 목록 보여주기
    @GET("/studentManagement/studentList.php")
    fun studentList(@Query("id") id:String):Call<MutableList<Member>>

    // 학생 이름 검색
    @GET("/studentManagement/studentNameSearch.php")
    fun studentNameSearch(@Query("teacherId") teacherId:String,
                          @Query("name") name:String):Call<MutableList<Member>>

    // 특정 강좌에 수강하는 학생 목록 검색
    @GET("/studentManagement/studentCourseSearch.php")
    fun studentCourseSearch(@Query("teacherId") teacherId:String,
                            @Query("course") course:String):Call<MutableList<Member>>

    // 학생 상세 정보 -> 한 학생의 강좌 보여주기
    @GET("/studentManagement/studentCourseList.php")
    fun studentCourseList(@Query("studentId") studentId:String,
                          @Query("teacherId") teacherId:String):Call<MutableList<StudentManagementCourse>>

    // 학생 상세 정보 -> 문자 보낸 내역
    @FormUrlEncoded
    @POST("/studentManagement/studentMessageList.php")
    fun studentMessageList(@Field("studentId") studentId:String,
                           @Field("teacherId") teacherId:String):Call<MutableList<StudentManagementMessage>>

    // 학생 상세 정보 -> 특정 강좌 출석부
    @GET("/studentManagement/studentCourseAttendanceList.php")
    fun studentCourseAttendanceList(@Query("courseCode") courseCode:String,
                                    @Query("studentId") studentId:String,
                                    @Query("teacherId") teacherId:String):Call<MutableList<StudentManagementDialogAttendance>>

    // 출결 현황
    @GET("/studentManagement/studentAttendanceList.php")
    fun studentAttendanceList(@Query("teacherId") teacherId:String):Call<MutableList<StudentAttendance>>

    // 출결 현황 시작 날짜 선택
    @GET("/studentManagement/studentAttendanceStartDateList.php")
    fun studentAttendanceStartDateList(@Query("teacherId") teacherId:String,
                                       @Query("startDate") startDate:String):Call<MutableList<StudentAttendance>>

    // 출결 현황 마지막 날짜 선택
    @GET("/studentManagement/studentAttendanceEndDateList.php")
    fun studentAttendanceEndDateList(@Query("teacherId") teacherId:String,
                                     @Query("startDate") startDate:String,
                                     @Query("endDate") endDate:String):Call<MutableList<StudentAttendance>>

    // 출결 현황 이름 검색
    @GET("/studentManagement/studentAttendanceNameSearch.php")
    fun studentAttendanceNameSearch(@Query("teacherId") teacherId:String,
                                     @Query("name") name:String):Call<MutableList<StudentAttendance>>
}