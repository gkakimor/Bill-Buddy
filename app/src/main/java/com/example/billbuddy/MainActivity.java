package com.example.billbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billbuddy.model.Bill;
import com.example.billbuddy.model.Frequency;
import com.example.billbuddy.model.Type;
import com.example.billbuddy.model.User;
import com.example.billbuddy.utilities.DbHandler;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void save (View view){
        DbHandler dbHandler = new DbHandler(MainActivity.this);
        dbHandler.populateInitialData();

        // Save new Type Example
        /*Type type = new Type();
        type.setType(editTextText2.getText().toString());
        dbHandler.addNewType(type);*/

        // Save new Frequency Example
        /*Frequency frequency = new Frequency();
        frequency.setFrequency(editTextText2.getText().toString());
        dbHandler.addNewFrequency(frequency);*/

        // Save new Bill Example
        /*Bill bill = new Bill();
        bill.setType(1);
        bill.setPayee("douglas");
        bill.setDue_date(new Date());
        bill.setFrequency(1);
        dbHandler.addNewBill(bill);*/

        // Update Bill Payment Example
        /*Bill bill = new Bill();
        bill.setPayment_date(new Date());
        bill.setId(1);
        dbHandler.updateBillPaymentDate(bill);*/

        // Save New User Example
        /*User user = new User();
        user.setPassword("12344");
        user.setEmail("teste@gmail.com");
        user.setName("Teste teste");
        user.setBirth(new Date());
        dbHandler.addNewUser(user);*/

        // Get One User Example
        /*User user = dbHandler.getUserByMail("teste@gmail.com");
        textView.setText(user.getName());
        textView2.setText(user.getBirth().toString());*/

        // Get Types Example
        /*ArrayList<Type> typeList = dbHandler.getAllTypes();
        textView.setText(typeList.get(0).getType());
        textView2.setText(typeList.get(1).getType());*/

        // Get Frequency Example
        /*ArrayList<Frequency> freqList = dbHandler.getAllFrequency();
        textView.setText(freqList.get(0).getFrequency());
        textView2.setText(freqList.get(1).getFrequency());*/

        // Get Not Payed Bills Example
        /*ArrayList<Bill> billList = dbHandler.getAllNotPayedBills();
        textView.setText(Integer.toString(billList.get(0).getId()));
        textView2.setText(billList.get(0).getDue_date().toString());*/

        // GEt Payed Bills Example
        /*ArrayList<Bill> billList = dbHandler.getAllPayedBills();
        textView.setText(Integer.toString(billList.get(0).getId()));
        textView2.setText(billList.get(0).getPayment_date().toString());*/


        Toast.makeText(getApplicationContext(), "Success",Toast.LENGTH_LONG).show();
    }

}