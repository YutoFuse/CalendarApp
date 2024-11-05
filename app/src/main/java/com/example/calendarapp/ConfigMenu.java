package com.example.calendarapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ConfigMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_config_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 仮に日にち情報を表示
        TextView dateTextView = findViewById(R.id.dateTextView);
        String selectedDate = getIntent().getStringExtra("selectedDate");
        dateTextView.setText(selectedDate);

        SwitchMaterial switchMaterial = findViewById(R.id.material_switch);
        ImageView alertIcon = findViewById(R.id.alertIcon);

        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                alertIcon.setImageResource(R.drawable.ic_alert);
                alertIcon.setVisibility(View.VISIBLE);
            } else {
                alertIcon.setImageResource(R.drawable.ic_alert_close);
                alertIcon.setVisibility(View.VISIBLE);
            }
        });

        // 戻るボタン
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
}