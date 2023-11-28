package com.example.bill_buddy_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
   // DbHandler dbHandler = new DbHandler(RegisterBill.this);
    Bill bill = new Bill();
    ArrayList<Frequency> frequencyList = new ArrayList<>();
    ArrayList<Type> typeList = new ArrayList<>();

    Type type;
    Frequency frequency;

    EditText editTxtPayee, editTxtAmount;
    Spinner spnTypeBill, spnFrequency;
    //ArrayList<String> typeBillOptions;
    ArrayAdapter<Type> adapter;
    ArrayAdapter<Frequency> adapterFrequency;
    EditText editTxtDate;
    Calendar calendar;
    Button btnAdd;

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
        //String amount = editTxtAmount.getText().toString();

        editTxtPayee = findViewById(R.id.editTxtPayee);


        spnTypeBill = findViewById(R.id.spnTypeBill);

        spnFrequency = findViewById(R.id.spnFrequency);

        btnAdd = findViewById(R.id.btnAdd);
                btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               addBill();
            }
        });

//add types spinner Type
        DbHandler dbHandler = new DbHandler(RegisterBill.this);

        /*typeBillOptions = new ArrayList<>();
        typeBillOptions.add("Type");
        typeBillOptions.add("Electricity");
        typeBillOptions.add("Rental");
        typeBillOptions.add("Insurance");
        typeBillOptions.add("Credit Card");
        typeBillOptions.add("Add a new type");*/

        typeList.add(new Type(998, "Type"));
        ArrayList<Type> savedTypes = dbHandler.getAllTypes();
        if (!savedTypes.isEmpty()) {
            typeList.addAll(dbHandler.getAllTypes());
        }
        typeList.add(new Type(999, "Add a new type"));

        adapter = new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_item, typeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTypeBill.setAdapter(adapter);
//user add new type
        //typeBillOptions.get(spnTypeBill.getSelectedItemPosition());
        spnTypeBill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String type = typeBillOptions.get(position);
                selectedType = (Type) parent.getItemAtPosition(position);
                if (selectedType.getId() == 999) {
                    showAddTypeDialog();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

//spinner frequency
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


        /*adapterFrequency.add("How Often");
        adapterFrequency.add("Once");
        adapterFrequency.add("Weekly");
        adapterFrequency.add("Bi-Weekly");
        adapterFrequency.add("Monthly");
        adapterFrequency.add("Yearly");*/

//Date - datePicker
        editTxtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });







    }

    //FUNCTIONS--
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

                            //typeList.add(typeList.size() - 1, new Type( addCode.intValue(), newType));
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
                        //calendar.set(year, monthOfYear, dayOfMonth);
                        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        editTxtDate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    }
                },
                year, month, day
        );

        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private void addBill(){
        //Payee values
        //editTxtPayee = findViewById(R.id.editTxtPayee); //??????????preciso disso aqui?
        String payee = editTxtPayee.getText().toString();

        //Amount to double
        double amount = 0.0;
        try {
           // editTxtAmount = findViewById(R.id.editTxtAmount);   //??????????preciso disso aqui?
            amount = Double.parseDouble(editTxtAmount.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return; // mensagem para inserir numero valido
        }

        //Date
//        editTxtDate = findViewById(R.id.editTxtDate);//??????????preciso disso aqui?
//        calendar = Calendar.getInstance();//??????????preciso disso aqui?
//        String dateString = editTxtDate.getText().toString();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//        Date due_date;
//        try {
//            due_date = dateFormat.parse(dateString);
//        } catch (ParseException e) {
//
//            e.printStackTrace();
//            return; // mensagem para inserir date valida
//        }

        //Spinners
        //Type selectedType = (Type) spnTypeBill.getSelectedItem();
        //Frequency selectedFrequency = (Frequency) spnFrequency.getSelectedItem();

        //Type, Frequency e Bill
        //Type type = new Type();
        //type.setType(selectedType.getType());

        //Frequency frequency = new Frequency();
        //frequency.setFrequency(selectedFrequency.getFrequency());

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
            Toast.makeText(this, "Bill not saved. Try again later.", Toast.LENGTH_SHORT).show();
        }
/*

        Log.d("AddBillTest", "Payee: " + bill.getPayee());
        Log.d("AddBillTest", "Amount: " + bill.getAmount());
       // Log.d("AddBillTest", "Date: " + due_date);
        Log.d("AddBillTest", "Type: " + bill.getType().getType());
        Log.d("AddBillTest", "Frequency: " + bill.getFrequency().getFrequency());

        Toast.makeText(this, "Dados coletados corretamente", Toast.LENGTH_SHORT).show();

*/




    }




}





//    private void addBill() {
//        type = new Type();
//        frequency = new Frequency();
//
//        String payee = editTxtPayee.getText().toString();
//        //String amount = editTxtAmount.getText().toString();
//
//        String frequencyValue = spnFrequency.getSelectedItem().toString();
//        frequency.setFrequency(frequencyValue);
//
//        String typeValue = spnTypeBill.getSelectedItem().toString();
//        type.setType(typeValue);
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//        String dateString = editTxtDate.getText().toString();
//
//        Date date = null;
//        try {
//            date = dateFormat.parse(dateString);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Bill bill = new Bill();
//        currentBill = bill;
//
//
////        bill.setFrequency(frequency);
////        bill.setPayee(payee);
////        bill.setDue_date(date);
//
//
////        Bill bill = new Bill(payee, type.getType(), date, amount, frequency.getFrequency());
////        currentBill = bill;
//
//        String message = "Payee: " + bill.getPayee() + "\nType: " + bill.getType() +
//                "\nDate: " + (date != null ? new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date.getTime()) : "");
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//
////        String message = "Payee: " + bill.getPayee() + "\nType: " + bill.getType() +
////                "\nDate: " + (dateString != null ? new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(dateFormat) : "");
////        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//
////        String message = "Payee: " + bill.getPayee() +
////                "\nType: " + type.getType() +
////                "\nDate: " + (date != null ? new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date.getTime()) : "") +
////              //  "\nAmount: " + bill.getAmount() +
////                "\nFrequency: " + bill.getFrequency();
////        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
////    }
