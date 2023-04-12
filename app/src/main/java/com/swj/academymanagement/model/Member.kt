package com.swj.academymanagement.model

// 회원 정보
class Member (
    val authority:String,       // 권한 ( 선생님, 학생 )
    val profile:String,         // 프로필 사진 주소 ( 파이어베이스 이미지 주소 )
    val id:String,              // 아이디 (PK)
    val password:String,        // 비밀번호
    val name:String,            // 이름
    var courseArr:MutableList<String> = mutableListOf(),    // 강좌 리스트
    val call_number:String      // 휴대폰 번호 (unique)
) {
    var course:String = ""
}