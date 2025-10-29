package gk1.nguyenanhkiet.thigk;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ChiTietBaiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_bai);

        TextView tvTenBai = findViewById(R.id.tvTenBai);
        TextView tvCongDung = findViewById(R.id.tvCongDung);

        tvTenBai.setText(getIntent().getStringExtra("ten"));
        tvCongDung.setText(getIntent().getStringExtra("congdung"));
    }
}