package gk1.nguyenanhkiet.thigk;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BaiThuocActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_thuoc);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewBaiThuoc);

        // Dữ liệu bài thuốc
        List<BaiThuoc> baiThuocList = new ArrayList<>();
        baiThuocList.add(new BaiThuoc("Lá trầu không", "Kháng khuẩn, trị hôi miệng"));
        baiThuocList.add(new BaiThuoc("Gừng tươi", "Làm ấm, giảm ho, chống buồn nôn"));
        baiThuocList.add(new BaiThuoc("Nghệ vàng", "Chống viêm, làm đẹp da"));
        baiThuocList.add(new BaiThuoc("Tỏi trắng", "Hạ huyết áp, tăng đề kháng"));
        baiThuocList.add(new BaiThuoc("Mật ong", "Làm dịu họng, kháng khuẩn"));

        BaiThuocAdapter adapter = new BaiThuocAdapter(baiThuocList, baiThuoc -> {
            Intent intent = new Intent(this, ChiTietBaiActivity.class);
            intent.putExtra("ten", baiThuoc.getTen());
            intent.putExtra("congdung", baiThuoc.getCongDung());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}