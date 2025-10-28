package com.akiet.vidulistener;

import static android.widget.Toast.LENGTH_LONG;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    // Khai báo các biến tương ứng với các điều kiện bên XML layout cần tương tác
    Button bSayHi, bSayHello;
    // Để clean code, ta tạo một hàm tìm all đk ở đây và gọi trong OnCreate
    void TimDK(){
        bSayHi = findViewById(R.id.btnSayHi);
        bSayHello = findViewById(R.id.btnSayHello);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TimDK();
        bSayHi.setOnClickListener(ngheSayHi);
        bSayHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Nghe vô danh",LENGTH_LONG).show();
            }
        });
    }
    // Hữu danh
    View.OnClickListener ngheSayHi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Code ở đây
            Toast.makeText(MainActivity.this, "Nghe hữu danh",LENGTH_LONG).show();
        }
    };
}