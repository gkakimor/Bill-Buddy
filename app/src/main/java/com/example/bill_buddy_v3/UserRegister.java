package com.example.bill_buddy_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bill_buddy_v3.model.User;
import com.example.bill_buddy_v3.utilities.DbHandler;
import com.example.bill_buddy_v3.utilities.Security;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserRegister extends AppCompatActivity {



    EditText editTxtName, editTxtBirth, editTxtEmail, editTxtPassword, editTxtCheckPassword;
    Button btnAddUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);

        editTxtName = findViewById(R.id.editTxtName);
        editTxtBirth = findViewById(R.id.editTxtBirth);
        editTxtEmail = findViewById(R.id.editTxtEmail);
        editTxtPassword = findViewById(R.id.editTxtPassword);
        editTxtCheckPassword = findViewById(R.id.editTxtCheckPassword);
        btnAddUser = findViewById(R.id.btnAddUser);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registered();
            }
        });

        editTxtBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c  = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UserRegister.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                editTxtBirth.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });
    }

    private void registered() {
        if (areFieldsValid()) {

            //String userName = editTxtName.getText().toString();
            //String userEmail = editTxtEmail.getText().toString();
            //String userPassword = editTxtPassword.getText().toString();
           // String dateBirthString = editTxtBirth.getText().toString();


            String hashedPassword = Security.hashPassword(editTxtPassword.getText().toString());
            //Date birthDate = new Date();
            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            try {
                User newUser = new User();
                newUser.setName(editTxtName.getText().toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                newUser.setBirth(sdf.parse(editTxtBirth.getText().toString()));

                newUser.setEmail(editTxtEmail.getText().toString());
                newUser.setPassword(hashedPassword);

                DbHandler dbHandler = new DbHandler(this);
                dbHandler.addNewUser(newUser);

                dbHandler.close();

                openLogin();

            } catch (ParseException e) {
                Toast.makeText(UserRegister.this, "Cannot register now. Please try again later.", Toast.LENGTH_SHORT).show();
            }



        } else {
            Toast.makeText(UserRegister.this, "Please, fill the fields!", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean areFieldsValid() {
        String userName = editTxtName.getText().toString();
        String userEmail = editTxtEmail.getText().toString();
        String userPassword = editTxtPassword.getText().toString();
        String confirmPassword = editTxtCheckPassword.getText().toString();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userEmail)
                || TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(confirmPassword)) {

            Toast.makeText(this, "All the fields should be filled!", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!userPassword.equals(confirmPassword)) {
            Toast.makeText(this, "The password do not match!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void openLogin() {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }

    /*public void onButtonClick(View view) {
        boolean registrationSuccess = registered();

        if (registrationSuccess) {
            openLogin();
        }
    }*/

}

