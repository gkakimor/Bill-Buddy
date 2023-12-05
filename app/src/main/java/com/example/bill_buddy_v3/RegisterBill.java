package com.example.bill_buddy_v3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bill_buddy_v3.model.Bill;
import com.example.bill_buddy_v3.model.Frequency;
import com.example.bill_buddy_v3.model.Type;
import com.example.bill_buddy_v3.model.User;
import com.example.bill_buddy_v3.utilities.DbHandler;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RegisterBill extends AppCompatActivity {
    Bill bill = new Bill();
    ArrayList<Frequency> frequencyList = new ArrayList<>();
    ArrayList<Type> typeList = new ArrayList<>();

    Type type;
    Frequency frequency;
    EditText editTxtPayee, editTxtAmount;
    Spinner spnTypeBill, spnFrequency;
    ArrayAdapter<Type> adapter;
    ArrayAdapter<Frequency> adapterFrequency;
    EditText editTxtDate;
    Calendar calendar;
    Button btnAdd;
    ImageView imgBack;
    Frequency selectedFrequency;
    Type selectedType;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_bill);

        Bundle extras = getIntent().getExtras();
        user_id = extras.getInt("user_id");

        editTxtDate = findViewById(R.id.editTxtDate);
        calendar = Calendar.getInstance();
        editTxtAmount = findViewById(R.id.editTxtAmount);
        editTxtPayee = findViewById(R.id.editTxtPayee);
        spnTypeBill = findViewById(R.id.spnTypeBill);
        spnFrequency = findViewById(R.id.spnFrequency);
        btnAdd = findViewById(R.id.btnAdd);
                btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areFieldsValid()) {
                    addBill();
                }
            }
        });

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterBill.this, Home.class);
                startActivity(intent);
            }
        });

        DbHandler dbHandler = new DbHandler(RegisterBill.this);

        typeList.add(new Type(998, "Type"));
        ArrayList<Type> savedTypes = dbHandler.getAllTypes();
        if (!savedTypes.isEmpty()) {
            typeList.addAll(dbHandler.getAllTypes());
        }
        typeList.add(new Type(999, "Add a new type"));

        adapter = new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_item, typeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTypeBill.setAdapter(adapter);
        spnTypeBill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = (Type) parent.getItemAtPosition(position);
                if (selectedType.getId() == 999) {
                    showAddTypeDialog();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        frequencyList.add(new Frequency(999, "How Often"));
        frequencyList.addAll(dbHandler.getAllFrequency());

        adapterFrequency = new ArrayAdapter<Frequency>(this, android.R.layout.simple_spinner_item, frequencyList);
        adapterFrequency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFrequency.setAdapter(adapterFrequency);
        spnFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFrequency = (Frequency) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editTxtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });
    }

    private void showAddTypeDialog() {
        final EditText editText = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Add New Type")
                .setView(editText)
                .setPositiveButton("Add", (dialog, which) -> {
                    String newType = editText.getText().toString().trim();
                    if (!newType.isEmpty() && !typeList.contains(newType)) {

                        try {
                            DbHandler dbHandler = new DbHandler(RegisterBill.this);
                            Type type = new Type();
                            type.setType(newType);
                            Long addCode = dbHandler.addNewType(type);
                            selectedType.setType(newType);
                            selectedType.setId(addCode.intValue());

                            adapter.notifyDataSetChanged();
                            typeList.add(new Type(999, "Add a new type"));
                        } catch (Exception e) {

                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editTxtDate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private boolean areFieldsValid() {

        if (selectedType.getId() == 998) {
            Toast.makeText(this, "Select Bill Type", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (editTxtAmount.getText().toString().isEmpty()) {
            Toast.makeText(this, "Inform the amount.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (editTxtPayee.getText().toString().isEmpty()) {
            Toast.makeText(this, "Inform the Payee.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (editTxtDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Inform the due Date.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedFrequency.getId() == 999) {
            Toast.makeText(this, "Select the Frequency", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void addBill(){

        String payee = editTxtPayee.getText().toString();

        double amount = 0.0;
        try {
            amount = Double.parseDouble(editTxtAmount.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            bill.setDue_date(sdf.parse(editTxtDate.getText().toString()));
            bill.setPayee(payee);
            bill.setAmount(amount);
            bill.setType(selectedType);
            bill.setUser_id(user_id);
            bill.setFrequency(selectedFrequency);

            DbHandler dbHandler = new DbHandler(RegisterBill.this);
            dbHandler.addNewBill(bill);
            dbHandler.close();

            Toast.makeText(this, "Bill saved.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterBill.this, UpcomingBills.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);


        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Bill not saved. Try again later.", Toast.LENGTH_SHORT).show();
        }
    }

}
