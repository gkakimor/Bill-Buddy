package com.example.bill_buddy_v3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bill_buddy_v3.model.Bill;
import com.example.bill_buddy_v3.utilities.DbHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class PaymentHistory extends AppCompatActivity {
    private final Class HOME = Home.class;
    private final PaymentHistory CURRENT = PaymentHistory.this;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_history);

        Bundle extras = getIntent().getExtras();
        user_id = extras.getInt("user_id");

        ListView lvBills = findViewById(R.id.lvPayedBills);
        ArrayList<HashMap<String, Object>> payedBillsMap = new ArrayList<>();

        DbHandler dbHandler = new DbHandler(PaymentHistory.this);

        ArrayList<Bill> payedBillList = dbHandler.getAllPayedBills(user_id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < payedBillList.size(); i++){
            HashMap<String, Object> map = new HashMap<>();

            map.put("id", payedBillList.get(i).getId());
            map.put("type", payedBillList.get(i).getType());
            map.put("amount", payedBillList.get(i).getFormattedAmount());
            map.put("payee", payedBillList.get(i).getPayee());
            map.put("dueDate", sdf.format(payedBillList.get(i).getDue_date()));
            map.put("frequency", payedBillList.get(i).getFrequency().getFrequency());
            map.put("paymentDate", sdf.format(payedBillList.get(i).getPayment_date()));

            payedBillsMap.add(map);
        }

        String[] from = {"payee", "dueDate", "frequency", "amount", "paymentDate"};
        int to[] = {R.id.txtName, R.id.txtDueDate, R.id.txtFrequency, R.id.txtAmount, R.id.txtPayedDate};

        SimpleAdapter simpleAdapter = new SimpleAdapter(PaymentHistory.this,
                payedBillsMap, R.layout.payed_bill_list, from, to);

        lvBills.setAdapter(simpleAdapter);
    }

    public void home (View view) {
        Intent intent = new Intent(CURRENT, HOME);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }
}