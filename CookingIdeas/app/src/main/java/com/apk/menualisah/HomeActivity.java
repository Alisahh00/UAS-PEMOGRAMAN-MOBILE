package com.apk.menualisah;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    // Deklarasi View untuk Say Hello
    private TextView tvGreeting, tvMainFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pastikan layout ini yang lu pakai
        setContentView(R.layout.activity_main);

        // --- 1. INISIALISASI LOGIKA SAY HELLO ---
        tvGreeting = findViewById(R.id.tvGreeting);
        tvMainFlag = findViewById(R.id.tvMainFlag);

        // Ambil data negara dari Splash (jika ada)
        String countryCode = getIntent().getStringExtra("COUNTRY_CODE");
        if (countryCode == null) {
            countryCode = Locale.getDefault().getCountry(); // Default ambil dari setingan HP
        }

        // Jalankan fungsi sapaan
        setGreetingByCountry(countryCode);
        tvMainFlag.setText(getFlagEmoji(countryCode));


        // --- 2. INISIALISASI TOMBOL MENU RESEP ---
        Button btnCekMenuNasiGoreng = findViewById(R.id.btn_cek_nasi_goreng);
        Button btnCekMenuAyamGoreng = findViewById(R.id.btn_cek_ayam_goreng);

        // Aksi Klik Nasi Goreng
        btnCekMenuNasiGoreng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ResepNasiGorengActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        // Aksi Klik Ayam Goreng
        btnCekMenuAyamGoreng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ResepAyamGorengActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    // --- HELPER LOGIKA SAY HELLO ---

    private void setGreetingByCountry(String code) {
        String greeting;
        switch (code.toUpperCase()) {
            case "ID": greeting = "Selamat Datang, Alisah!"; break;
            case "JP": greeting = "Konnichiwa, Alisah!"; break;
            case "KR": greeting = "Annyeong, Alisah!"; break;
            case "FR": greeting = "Bonjour, Alisah!"; break;
            case "SA": greeting = "Ahlan, Alisah!"; break;
            case "US": case "GB": case "AU": greeting = "Welcome Alisah!"; break;
            case "MY": greeting = "Selamat Datang, Alisah!"; break;
            case "TH": greeting = "Sawatdee, Alisah!"; break;
            default: greeting = "Hello, Alisah!"; break;
        }
        tvGreeting.setText(greeting);
    }

    public String getFlagEmoji(String countryCode) {
        if (countryCode == null || countryCode.length() != 2) return "👋";
        // Logika konversi kode negara (ID, US, dll) ke Emoji Bendera
        int firstChar = Character.codePointAt(countryCode.toUpperCase(), 0) - 0x41 + 0x1F1E6;
        int secondChar = Character.codePointAt(countryCode.toUpperCase(), 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstChar)) + new String(Character.toChars(secondChar));
    }
}