package com.swj.academymanagement.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// 한 주의 요일 및 날짜를 저장하는 클래스
data class Week(
    val monday:Monday,
    val tuesday:Tuesday,
    val wednesday:Wednesday,
    val thursday:Thursday,
    val friday:Friday
)

// 각 요일별 날짜 및 요일을 저장하는 클래스
class Monday(val date:String, val day:String)
class Tuesday(val date:String, val day:String)
class Wednesday(val date:String, val day:String)
class Thursday(val date:String, val day:String)
class Friday(val date:String, val day:String)

// 한 주의 요일 및 날짜 가져오기
class WeekDay {
    companion object {
        // 날짜 형식 포맷
        fun dateDay(date : Date) : String {
            val dayFormat = SimpleDateFormat("YYYY-MM-dd", Locale.getDefault())
            val day = dayFormat.format(date)
            return day
        }

        // 각 요일별 요일 및 날짜 가져오기 ( ex. 2023-04-09 )
        fun getWeekDate() : Week{
            val calendar = Calendar.getInstance()

            // 월요일
            calendar.add(Calendar.DAY_OF_MONTH, (2-calendar.get(Calendar.DAY_OF_WEEK)))
            val mondayDate = calendar.time
            val monday = Monday(dateDay(mondayDate), "월")

            // 화요일
            calendar.add(Calendar.DAY_OF_MONTH, (3-calendar.get(Calendar.DAY_OF_WEEK)))
            val tuesdayDate = calendar.time
            val tuesday = Tuesday(dateDay(tuesdayDate), "화")

            // 수요일
            calendar.add(Calendar.DAY_OF_MONTH, (4-calendar.get(Calendar.DAY_OF_WEEK)))
            val wednesdayDate = calendar.time
            val wednesday = Wednesday(dateDay(wednesdayDate),"수")

            // 목요일
            calendar.add(Calendar.DAY_OF_MONTH, (5-calendar.get(Calendar.DAY_OF_WEEK)))
            val thursdayDate = calendar.time
            val thursday = Thursday(dateDay(thursdayDate), "목")

            // 금요일
            calendar.add(Calendar.DAY_OF_MONTH, (6-calendar.get(Calendar.DAY_OF_WEEK)))
            val fridayDate = calendar.time
            val friday = Friday(dateDay(fridayDate), "금")
            return Week(monday, tuesday, wednesday, thursday, friday)
        }
    }
}