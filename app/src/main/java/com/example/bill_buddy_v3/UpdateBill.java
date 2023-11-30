package com.example.bill_buddy_v3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bill_buddy_v3.adapter.BillAdapter;
import com.example.bill_buddy_v3.model.Bill;
import com.example.bill_buddy_v3.utilities.DbHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UpdateBill extends AppCompatActivity {

    EditText editTxtPaymentDate;
    Button btnUpdate;
    ArrayList<Bill> billList = new ArrayList<>();
    BillAdapter billAdapter;
    Bill selectedBill;
    TextView txtSelectedPayee, txtSelectedFrequency, txtSelectedDueDate, txtSelectedAmount;
    private int user_id;

    private final Class HOME = Home.class;
    private final UpdateBill CURRENT = UpdateBill.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_bill);

        Bundle extras = getIntent().getExtras();
        user_id = extras.getInt("user_id");

        // Initialize Spinner
        initList();
        Spinner spinner = findViewById(R.id.spinBills);
        billAdapter = new BillAdapter(this, billList);
        //billAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(billAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedBill = (Bill) parent.getItemAtPosition(position);
                LinearLayout linearLayoutBillDetails = findViewById(R.id.linearLayoutBillDetails);
                txtSelectedPayee = findViewById(R.id.txtSelectedPayee);
                txtSelectedFrequency = findViewById(R.id.txtSelectedFrequency);
                txtSelectedDueDate = findViewById(R.id.txtSelectedDueDate);
                txtSelectedAmount = findViewById(R.id.txtSelectedAmount);

                if (selectedBill.getId() != 999){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

                    txtSelectedPayee.setText(selectedBill.getPayee());
                    txtSelectedFrequency.setText(selectedBill.getFrequency().getFrequency());
                    txtSelectedDueDate.setText("Due: " + sdf.format(selectedBill.getDue_date()));
                    txtSelectedAmount.setText("Amount: " + selectedBill.getFormattedAmount());
                    linearLayoutBillDetails.setVisibility(View.VISIBLE);
                    linearLayoutBillDetails.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutBillDetails.setVisibility(View.INVISIBLE);
                }

                //Toast.makeText(UpdateBill.this, name + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // date picker
        editTxtPaymentDate = findViewById(R.id.editTxtPaymentDate);
        editTxtPaymentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c  = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateBill.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                editTxtPaymentDate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });
    }

    private void initList(){
        Bill selectBill = new Bill();
        selectBill.setId(999);
        selectBill.setPayee("Select Bill");
        billList.add(selectBill);

        DbHandler dbHandler = new DbHandler(UpdateBill.this);

        billList.addAll(dbHandler.getAllNotPayedBills(user_id));
        dbHandler.close();
    }

    public void updateBill (View view) throws ParseException {

        if (!editTxtPaymentDate.getText().toString().matches("")){
            DbHandler dbHandler = new DbHandler(UpdateBill.this);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            selectedBill.setPayment_date(sdf.parse(editTxtPaymentDate.getText().toString()));

            dbHandler.updateBillPaymentDate(selectedBill);
            dbHandler.close();

            Intent intent = new Intent(UpdateBill.this, PaymentHistory.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        } else {
            Toast.makeText(UpdateBill.this, "Select Payment Date", Toast.LENGTH_SHORT).show();
        }

    }

    public void home (View view) {
        Intent intent = new Intent(CURRENT, HOME);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }
}