package com.akiet.vidulistview;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvhocphan;
    // (1) Chuẩn bị nguồn dữ liệu hiển thị
    // -- Khai báo --
    // (2) Tạo Adapter
    ArrayAdapter<String> HPAdapter;
    void TimDK () {
        lvhocphan = findViewById(R.id.dshocphan);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TimDK();
        // Đọc nội dung trong file JSON
        String jsonString = Utils.getJsonFromAsset(this, "listHP.json");
        // Dùng Gson để chuyển JSON thành String
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<String>>(){}.getType();
        List<String> subjects = gson.fromJson(jsonString, listUserType);

        // -- Lấy dữ liệu đưa vào listHocPhan
        // -- LẤY Ở ĐÂU ??? = file, database, internet(Cloud)...

        HPAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,subjects
        );
        // (3) Gắn Adapter
        lvhocphan.setAdapter(HPAdapter);
        // (4) Thiết lập sự kiện
        lvhocphan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Xử lý việc chon Item ở đây
                // Biến position chứa vị trí của Item được chọn
                // (4.1) Lấy giá trị của phần tử được chọn
                // Cách 1: Lấy gián tiếp qua Adapter
                String HPduocChon =  HPAdapter.getItem(position).toString();
                // Cách 2: Lấy trực tiếp từ biến chứa danh sách
                // String HPduocChon1 = listHocPhan.get(position);
                // (4.2) Làm gì với nó thì làm
                String thongbao = " Bạn đã chọn học lại học phần " + HPduocChon;
                Toast.makeText(MainActivity.this, thongbao, Toast.LENGTH_LONG).show();
            }
        });
    }
}