package com.example.calendarapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.annotation.NonNull;
import android.widget.CalendarView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<String[]> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // 日付が選択されたときの処理
                // year, month, dayOfMonth を使用して処理を行う
                // 予定のTodoリストとかにしてもいいんじゃない？
            }
        });

        ImageView imageView = findViewById(R.id.BackGroundImage);
        Button btnSelectImage = (Button)findViewById(R.id.buttonSelectImage);
        launcher = registerForActivityResult(new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        imageView.setImageURI(uri);
                    }
                }
        );
        btnSelectImage.setOnClickListener(v -> launcher.launch(new String[]{"image/*"}));
    }
}