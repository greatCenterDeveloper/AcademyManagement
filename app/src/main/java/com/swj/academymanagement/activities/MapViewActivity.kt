package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import com.kakao.util.maps.helper.Utility
import com.swj.academymanagement.databinding.ActivityMapViewBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapViewActivity : AppCompatActivity() {

    val binding:ActivityMapViewBinding by lazy {
        ActivityMapViewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 화면 전체 다 먹기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // 뒤로 가기
        binding.ivBackspace.setOnClickListener { finish() }

        // 키 해시값 얻어오기
        //Log.i("keyHash", Utility.getKeyHash(this))

        val mapView:MapView = MapView(this)
        val containerMapView:ViewGroup = binding.containerMapview
        containerMapView.addView(mapView)

        // 중심점 변경
        //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5617, 127.0344), true)

        // 줌 레벨 변경
        //mapView.setZoomLevel(7, true)

        // 중심점 변경 + 줌 레벨 변경
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.5617, 127.0343), 1, true)

        // 줌 인할 때 애니메이션 효과
        mapView.zoomIn(true)

        // 줌 아웃할 때 애니메이션 효과
        mapView.zoomOut(true)

        // 마커 표시
        val marker = MapPOIItem()

        // 마커에 표시될 이름
        marker.itemName = "학원 위치"

        // 이 마커를 가리킬 태그값
        marker.tag = 0

        // 마커 표시할 위치
        marker.mapPoint = MapPoint.mapPointWithGeoCoord(37.5617, 127.0343)

        // 화면에 표시되는 마커 색상 (파란색)
        marker.markerType = MapPOIItem.MarkerType.BluePin

        // 선택했을 경우 바뀌는 마커 색상 (빨강색)
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin

        // 맵뷰에 마커 추가
        mapView.addPOIItem(marker)
    }
}