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
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // GPS 사용을 위한 멤버 변수 선언
    LocationManager locationManager;
    LocationListener locationListener;

    double latitude;
    double longitude;
    TextView tvLocation;  // TextView 선언
    TextView tvCounter;   // 카운터 값을 표시하는 TextView
    TextView tvSpeed;     // 속도를 표시할 TextView
    Button btnIncrease;   // 수치를 증가시킬 버튼
    int counter = 0;      // 카운터 값 (0부터 시작)

    Location previousLocation = null;  // 이전 GPS 위치
    float currentSpeed = 0;            // 현재 속도
    Handler speedHandler = new Handler();  // 1초마다 속도 업데이트를 위한 핸들러

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextView 초기화
        tvLocation = findViewById(R.id.tvLocation);
        tvCounter = findViewById(R.id.tvCounter);  // 카운터를 표시할 TextView
        tvSpeed = findViewById(R.id.tvSpeed);      // 속도를 표시할 TextView
        btnIncrease = findViewById(R.id.btnIncrease);  // 카운터를 증가시킬 버튼

        // 버튼 클릭 리스너 설정
        btnIncrease.setOnClickListener(v -> {
            if (counter < 100) {
                counter += 1;
                tvCounter.setText("Counter: " + counter);
            }
        });

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            // GPS의 정보를 얻어 올 수 있는 메소드
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                // 로그 출력
                Log.i("MyLocation", "위도 : " + latitude);
                Log.i("MyLocation", "경도 : " + longitude);

                // TextView에 위도 경도 표시
                tvLocation.setText("Latitude: " + latitude + "\nLongitude: " + longitude);

                // 속도 계산 및 업데이트
                updateSpeed(location);
            }
        };

        // 사용자가 앱에 GPS 사용 권한 부여하였는지 체크
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return;
        }

        // 3초마다 위치 업데이트 요청
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000, 1, locationListener);

        // 1초마다 속도 업데이트 실행
        speedHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvSpeed.setText("Speed: " + currentSpeed + " m/s");
                speedHandler.postDelayed(this, 1000);  // 1초마다 반복
            }
        }, 1000);
    }

    // 속도 업데이트 메소드
    private void updateSpeed(Location newLocation) {
        if (previousLocation != null) {
            // 이전 위치와 새로운 위치 사이의 시간차와 거리 계산
            float distance = newLocation.distanceTo(previousLocation);  // 두 위치 사이의 거리 (미터 단위)
            float timeElapsed = (newLocation.getTime() - previousLocation.getTime()) / 1000.0f;  // 시간차 (초 단위)

            if (timeElapsed > 0) {
                currentSpeed = distance / timeElapsed;  // 속도 계산 (m/s)
            }
        }
        previousLocation = newLocation;  // 현재 위치를 이전 위치로 업데이트
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
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        }
    }
}
