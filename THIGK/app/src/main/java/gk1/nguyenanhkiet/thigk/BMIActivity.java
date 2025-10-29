package gk1.nguyenanhkiet.thigk;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BMIActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        EditText edtCanNang = findViewById(R.id.edtCanNang);
        EditText edtChieuCao = findViewById(R.id.edtChieuCao);
        Button btnTinhBMI = findViewById(R.id.btnTinhBMI);
        TextView tvKetQua = findViewById(R.id.tvKetQua);

        btnTinhBMI.setOnClickListener(v -> {
            String cnStr = edtCanNang.getText().toString().trim();
            String ccStr = edtChieuCao.getText().toString().trim();

            if (cnStr.isEmpty() || ccStr.isEmpty()) {
                tvKetQua.setText("Vui lòng nhập đủ thông tin!");
                return;
            }

            double canNang = Double.parseDouble(cnStr);
            double chieuCao = Double.parseDouble(ccStr);

            if (chieuCao <= 0) {
                tvKetQua.setText("Chiều cao phải > 0!");
                return;
            }

            double bmi = canNang / (chieuCao * chieuCao);
            String phanLoai;

            if (bmi < 18.5) {
                phanLoai = "Gầy";
            } else if (bmi < 25) {
                phanLoai = "Bình thường";
            } else if (bmi < 30) {
                phanLoai = "Thừa cân";
            } else {
                phanLoai = "Béo phì";
            }

            tvKetQua.setText(String.format("BMI: %.2f\n%s", bmi, phanLoai));
        });
    }
}