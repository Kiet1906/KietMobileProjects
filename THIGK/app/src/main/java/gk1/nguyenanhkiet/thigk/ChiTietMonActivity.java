package gk1.nguyenanhkiet.thigk;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ChiTietMonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_mon);

        TextView tvTenMon = findViewById(R.id.tvTenMon);
        TextView tvMoTa = findViewById(R.id.tvMoTa);

        tvTenMon.setText(getIntent().getStringExtra("ten"));
        tvMoTa.setText(getIntent().getStringExtra("mota"));
    }
}