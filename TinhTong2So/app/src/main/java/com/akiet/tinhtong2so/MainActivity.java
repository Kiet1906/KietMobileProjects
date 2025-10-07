package com.akiet.tinhtong2so;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    // (1). Khai báo các biến đối tượng
    //      nhằm liên kết các id trong flie xml layout

    EditText edSo1, edSo2;
    TextView Ketqua;
    Button btnTinhToan;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //(2). Link biến Java với id tương ứng
        edSo1 = findViewById(R.id.edtA);
        edSo2 = findViewById(R.id.edtB);
        Ketqua = findViewById(R.id.edtKetqua);
        btnTinhToan = findViewById(R.id.nutTinhTong);
    }
    public void HamTinhTong(View v) {
        //Code tính tổng
        // Form: (b1)lấy dữ liệu --> (b2)Xử lý --> (b3)Xuất kết quả
        // Bước 1:
        String strSo1 = edSo1.getText().toString();
        String strSo2 = edSo2.getText().toString();
        // Chuyển kiểu dữ liệu mới tính đc
        int soA = Integer.parseInt(strSo1);
        int soB = Integer.parseInt(strSo2);
        //Bước 2:
        int tong = soA + soB ;
        //Bước 3:
        // Chuyển sang string để setText lên view (TextView)
        String strTong = String.valueOf(tong);
        // Hiển thị lên view
        Ketqua.setText(strTong);
    }
}