package com.yenaly.weatherchan

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.yenaly.weatherchan.ui.place.PlaceSearchFragment

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 主Activity。
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (
            !isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) &&
            !isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
        )
            ActivityCompat.requestPermissions(this, permissions, 0)
        else
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.place_search_fragment, PlaceSearchFragment())
                .commit()

        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTPS)
        AMapLocationClient.updatePrivacyAgree(WeatherChanApplication.context, true)
        AMapLocationClient.updatePrivacyShow(WeatherChanApplication.context, true, true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.place_search_fragment, PlaceSearchFragment())
                    .commit()
            }
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}