package gk1.nguyenanhkiet.thigk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MonAnActivity extends AppCompatActivity {
    private final ArrayList<String> tenMonList = new ArrayList<>();
    private final ArrayList<String> moTaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_an);

        ListView listView = findViewById(R.id.listViewMonAn);

        // Đọc JSON
        try {
            InputStream is = getResources().openRawResource(R.raw.monan);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                tenMonList.add(obj.getString("ten"));
                moTaList.add(obj.getString("mota"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, tenMonList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ChiTietMonActivity.class);
            intent.putExtra("ten", tenMonList.get(position));
            intent.putExtra("mota", moTaList.get(position));
            startActivity(intent);
        });
    }
}