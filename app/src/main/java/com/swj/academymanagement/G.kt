package com.swj.academymanagement

import com.swj.academymanagement.model.Member

class G {
    companion object {
        // 전체 화면에서 사용할 멤버 객체 ( 선생님, 학생 )
        var member:Member = Member("", "", "", "", "", call_number = "")

        // 학생 권한 등원 완료 시 attendance 테이블의 attendanceCode (PK) 가져오기
        var attendanceCode:String = ""
    }
}