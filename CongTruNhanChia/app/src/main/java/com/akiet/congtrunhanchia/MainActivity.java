package com.akiet.congtrunhanchia;

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

    EditText edtSoA, edtSoB;

    TextView tvKetQua;

    Button btnTong, btnHieu, btnTich, btnThuong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtSoA = findViewById(R.id.edtA);
        edtSoB = findViewById(R.id.edtB);
        tvKetQua = findViewById(R.id.edtKetqua);
        btnTong = findViewById(R.id.nutTinhTong);
        btnHieu = findViewById(R.id.nutTinhHieu);
        btnTich = findViewById(R.id.nutTinhTich);
        btnThuong = findViewById(R.id.nutTinhThuong);
    }

    public void HamTinhTong (View v) {
        try{
            String strSoA = edtSoA.getText().toString();
            String strSoB = edtSoB.getText().toString();

            if (strSoA.isEmpty() || strSoB.isEmpty()){
                tvKetQua.setText("Vui lòng nhập đủ số A và B");
                return;
            }

            int soA = Integer.parseInt(strSoA);
            int soB = Integer.parseInt(strSoB);

            int Tong = soA + soB;

            String strTong = String.valueOf(Tong);

            tvKetQua.setText(strTong);
        } catch (NumberFormatException e){
            tvKetQua.setText("Vui lòng nhập số hợp lệ");
        }
    }
    public void HamTinhHieu (View v) {
        try{
            String strSoA = edtSoA.getText().toString();
            String strSoB = edtSoB.getText().toString();

            if (strSoA.isEmpty() || strSoB.isEmpty()){
                tvKetQua.setText("Vui lòng nhập đủ số A và B");
                return;
            }

            int soA = Integer.parseInt(strSoA);
            int soB = Integer.parseInt(strSoB);

            int Hieu = soA - soB;

            String strHieu = String.valueOf(Hieu);

            tvKetQua.setText(strHieu);
        } catch (NumberFormatException e){
            tvKetQua.setText("Vui lòng nhập số hợp lệ");
        }
    }
    public void HamTinhTich (View v) {
        try{
            String strSoA = edtSoA.getText().toString();
            String strSoB = edtSoB.getText().toString();

            if (strSoA.isEmpty() || strSoB.isEmpty()){
                tvKetQua.setText("Vui lòng nhập đủ số A và B");
                return;
            }

            int soA = Integer.parseInt(strSoA);
            int soB = Integer.parseInt(strSoB);

            int Tich = soA * soB;

            String strTich = String.valueOf(Tich);

            tvKetQua.setText(strTich);
        } catch (NumberFormatException e){
            tvKetQua.setText("Vui lòng nhập số hợp lệ");
        }
    }
    public void HamTinhThuong (View v) {
        try{

        } catch (NumberFormatException e){
            tvKetQua.setText("Vui lòng nhập số hợp lệ");
        }

        String strSoA = edtSoA.getText().toString();
        String strSoB = edtSoB.getText().toString();

        if (strSoA.isEmpty() || strSoB.isEmpty()) {
            tvKetQua.setText("Vui lòng nhập đủ số A và B");
            return;
        }

        int soA = Integer.parseInt(strSoA);
        int soB = Integer.parseInt(strSoB);

        if (soB == 0){
            tvKetQua.setText("Không thể chia cho 0");
            return;
        }
        float Thuong = (float) soA / soB;

        String strThuong = String.valueOf(Thuong);

        tvKetQua.setText(strThuong);
    }
}