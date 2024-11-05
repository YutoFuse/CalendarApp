package com.example.calendarapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.EditText;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

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

        // 日付情報を表示
        TextView dateTextView = findViewById(R.id.dateTextView);
        String selectedDate = getIntent().getStringExtra("selectedDate");
        dateTextView.setText(selectedDate);

        SwitchMaterial switchMaterial = findViewById(R.id.material_switch);
        ImageView alertIcon = findViewById(R.id.alertIcon);

        // アラートスイッチの切り替え
        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                alertIcon.setImageResource(R.drawable.ic_alert);
                alertIcon.setVisibility(View.VISIBLE);
            } else {
                alertIcon.setImageResource(R.drawable.ic_alert_close);
                alertIcon.setVisibility(View.VISIBLE);
            }
        });

        // EditText の初期化
        TextInputEditText startHour = findViewById(R.id.startHour);
        TextInputEditText startMinute = findViewById(R.id.startMinute);
        TextInputEditText endHour = findViewById(R.id.endHour);
        TextInputEditText endMinute = findViewById(R.id.endMinute);

        // 現在時刻を初期値に設定
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        startHour.setText(String.format("%02d", currentHour));
        startMinute.setText(String.format("%02d", currentMinute));
        endHour.setText(String.format("%02d", (currentHour + 1) % 24)); // 終了時間は一時間後と仮に設定
        endMinute.setText(String.format("%02d", currentMinute));

        // 時刻入力に対する範囲チェックを適用
        applyTimeValidation(startHour, 0, 23);
        applyTimeValidation(startMinute, 0, 59);
        applyTimeValidation(endHour, 0, 23);
        applyTimeValidation(endMinute, 0, 59);

        // 戻るボタン
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void applyTimeValidation(TextInputEditText editText, int min, int max) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!input.isEmpty()) {
                    try {
                        int value = Integer.parseInt(input);
                        if (value < min || value > max) {
                            editText.setError(min + " から " + max + " の範囲で入力してください");
                        }
                    } catch (NumberFormatException e) {
                        editText.setError("数値を入力してください");
                    }
                }
            }
        });
    }
}
