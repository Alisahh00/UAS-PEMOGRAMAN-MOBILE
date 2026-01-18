package com.apk.menualisah; // SESUAIKAN DENGAN PACKAGE ANDA

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResepAyamGorengActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Menggunakan layout resep yang sama
        setContentView(R.layout.activity_resep_detail);

        // 1. Inisialisasi TextView
        TextView title = findViewById(R.id.resep_title);
        TextView contentBahan = findViewById(R.id.resep_content_bahan);
        TextView contentCara = findViewById(R.id.resep_content_cara);

        // 2. Mengisi Data Resep AYAM GORENG dari strings.xml
        // Pastikan strings.xml Anda sudah berisi string ini!
        title.setText(getString(R.string.title_ayam_goreng));
        contentBahan.setText(getString(R.string.bahan_ayam_goreng));
        contentCara.setText(getString(R.string.cara_masak_ayam_goreng));

        // 3. Logika Tombol Kembali (Panah Kiri)
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kembali ke Activity sebelumnya (Home)
                // Animasi saat kembali
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    // Opsional: Untuk memastikan animasi juga berjalan saat tombol back fisik ditekan
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}