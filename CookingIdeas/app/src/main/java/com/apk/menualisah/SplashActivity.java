package com.apk.menualisah;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class SplashActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final long SPLASH_DELAY_MS = 2000;
    private static final long LOCATION_TIMEOUT_MS = 4000;

    private FusedLocationProviderClient fusedLocationClient;
    private TextView locationText, tvCountryFlag;
    private Handler handler;
    private final AtomicBoolean isTransitioned = new AtomicBoolean(false);

    // Variabel penampung hasil
    private String currentCountryCode = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Binding View (Sesuaikan dengan ID di XML lo)
        locationText = findViewById(R.id.tvCountryName);
        tvCountryFlag = findViewById(R.id.tvFlag);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        handler = new Handler(Looper.getMainLooper());

        // Timeout Guard: Jika GPS macet, setelah 4 detik paksa masuk MainActivity
        handler.postDelayed(() -> {
            if (!isTransitioned.get()) {
                startTransition();
            }
        }, LOCATION_TIMEOUT_MS);

        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getUserLocation();
        }
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            startTransition();
            return;
        }

        // Minta lokasi real-time paling akurat
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, location -> {
                    if (location != null) getCountryFromLocation(location);
                    else startTransition();
                }).addOnFailureListener(e -> startTransition());
    }

    private void getCountryFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            // Mengubah koordinat jadi Data Alamat
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                String countryName = addresses.get(0).getCountryName();
                currentCountryCode = addresses.get(0).getCountryCode();

                // Simpan Data Negara ke SharedPreferences
                SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                prefs.edit().putString("country_iso", currentCountryCode).apply();

                // Update UI Splash secara real-time
                runOnUiThread(() -> {
                    tvCountryFlag.setText(countryCodeToEmoji(currentCountryCode));
                    locationText.setText(countryName);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        startTransition();
    }

    private String countryCodeToEmoji(String code) {
        if (code == null || code.length() != 2) return "🌐";
        code = code.toUpperCase();
        int firstLetter = Character.codePointAt(code, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(code, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }

    private synchronized void startTransition() {
        if (isTransitioned.compareAndSet(false, true)) {
            handler.postDelayed(() -> {
                if (!isFinishing()) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    // Kirim kodenya ke MainActivity
                    intent.putExtra("COUNTRY_CODE", currentCountryCode);
                    startActivity(intent);
                    finish();
                    // Animasi perpindahan halus
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, SPLASH_DELAY_MS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) getUserLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) handler.removeCallbacksAndMessages(null);
    }
}