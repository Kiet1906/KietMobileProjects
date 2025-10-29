package gk1.nguyenanhkiet.thigk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btnChucNang2 = findViewById(R.id.btnChucNang2);
        Button btnChucNang3 = findViewById(R.id.btnChucNang3);
        Button btnChucNang4 = findViewById(R.id.btnChucNang4);
        Button btnAboutMe = findViewById(R.id.btnAboutMe);

        btnChucNang2.setOnClickListener(v -> startActivity(new Intent(this, BMIActivity.class)));
        btnChucNang3.setOnClickListener(v -> startActivity(new Intent(this, MonAnActivity.class)));
        btnChucNang4.setOnClickListener(v -> startActivity(new Intent(this, BaiThuocActivity.class)));
        btnAboutMe.setOnClickListener(v -> startActivity(new Intent(this, AboutMeActivity.class)));
    }
}