package com.luvris2.gpstestapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // GPS 사용을 위한 멤버 변수 선언
    LocationManager locationManager;
    LocationListener locationListener;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            // GPS의 정보를 얻어 올 수 있는 메소드
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitude = Log.i("MyLocation", "위도 : " + location.getLatitude());
                longitude = Log.i("MyLocation", "경도 : " + location.getLongitude());
            }
        };

        // 사용자가 앱에 GPS 사용 권한 부여하였는지 체크
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return;
        }
        // requestLocationUpdates(String provider, long minTimeMs, float minDistanceM, LocationListener listener, Looper looper)
        // 첫번째 파라미터 : Provider 지정 -> LocationManager.GPS_PROVIDER (GPS 사용 선언)
        // 두번째 파라미터 : GPS 호출 시간 간격
        // 세번째 파라미터 : GPS 호출 최소 이동거리
        // 네번재 파라미터 : 로캐이션 리스너 이름
        // 예시) 3초마다 위치 정보 업데이트
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000, -1, locationListener);
        
        TextView printLog = new TextView(this);
        printLog.setText("위도 : " + latitude + " 경도 : " + longitude);
        setContentView(printLog);
    }

    // 앱 권한 요청 설정 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            // 권한 요청
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        }
    }
}