package com.luvris2.gpstestapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    // GPS 사용을 위한 멤버 변수 선언
    LocationManager locationManager;
    LocationListener locationListener;

    double latitude;
    double longitude;
    TextView tvLocation;  // TextView 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextView 초기화
        tvLocation = findViewById(R.id.tvLocation);

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
                3000, -1, locationListener);
    }

    public void onToggleClicked(View view) {
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            Toast.makeText(this, "Press on", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unchecked", Toast.LENGTH_SHORT).show();
        }
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
