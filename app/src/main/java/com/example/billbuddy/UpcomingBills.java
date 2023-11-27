package com.example.billbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.billbuddy.model.Bill;
import com.example.billbuddy.utilities.DbHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class UpcomingBills extends AppCompatActivity {

    private final Class HOME = UpdateBill.class;
    private final UpcomingBills CURRENT = UpcomingBills.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_bills);

        Bundle extras = getIntent().getExtras();
        int user_id = extras.getInt("user_id");

        ListView lvBills = findViewById(R.id.lvUpcomingBills);
        ArrayList<HashMap<String, Object>> payedBillsMap = new ArrayList<>();

        DbHandler dbHandler = new DbHandler(UpcomingBills.this);

        ArrayList<Bill> payedBillList = dbHandler.getAllNotPayedBills(user_id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < payedBillList.size(); i++){
            HashMap<String, Object> map = new HashMap<>();

            map.put("id", payedBillList.get(i).getId());
            map.put("type", payedBillList.get(i).getType());
            map.put("amount", payedBillList.get(i).getAmount());
            map.put("payee", payedBillList.get(i).getPayee());
            map.put("dueDate", sdf.format(payedBillList.get(i).getDue_date()));
            map.put("frequency", payedBillList.get(i).getFrequency().getFrequency());

            payedBillsMap.add(map);
        }

        String[] from = {"payee", "dueDate", "frequency", "amount"};
        int to[] = {R.id.txtName, R.id.txtDueDate, R.id.txtFrequency, R.id.txtAmount};

        SimpleAdapter simpleAdapter = new SimpleAdapter(UpcomingBills.this,
                payedBillsMap, R.layout.upcoming_bill_list, from, to);

        lvBills.setAdapter(simpleAdapter);
    }

    public void home (View view) {
        Intent intent = new Intent(CURRENT, HOME);
        startActivity(intent);
    }
}