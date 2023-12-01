package com.example.bill_buddy_v3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bill_buddy_v3.model.User;
import com.example.bill_buddy_v3.utilities.DbHandler;
import com.example.bill_buddy_v3.utilities.Security;

public class LoginPage extends AppCompatActivity {

    DbHandler dbHandler = new DbHandler(LoginPage.this);
    TextView txtSignUp;
    EditText editTxtEmail, editTxtPassword;
    Button btnLogin;

    UserRegister userRegister = new UserRegister();
    User savedUser = new User();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);


        txtSignUp = findViewById(R.id.txtSignUp);
        editTxtEmail = findViewById(R.id.editTxtEmail);

        editTxtPassword = findViewById(R.id.editTxtPassword);
        btnLogin = findViewById(R.id.btnLogin);


        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUp();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginValidate();
            }
        });


    }

    //LOGIN VALIDATE
    private void loginValidate() {
        String userEmail = editTxtEmail.getText().toString();
        String userPassword = editTxtPassword.getText().toString();
        String hashedPassword = Security.hashPassword(userPassword);

        User user = new User(userEmail, hashedPassword);

        if (userValidate(user)) {
            openHome();
        } else {
            Toast.makeText(this, "Invalid user!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean userValidate(User user) {
        DbHandler dbHandler = new DbHandler(this);
        savedUser = dbHandler.checkUser(user.getEmail(), user.getPassword());
        dbHandler.close();

        if (savedUser.getId() == 0) {
            return false;
        }

        return true;
    }

    //SIGN UP CLICK--
    /*public void onTextViewClick(View view) {
        openSignUp();
    }*/

    private void openSignUp() {
        Intent intent = new Intent(this, UserRegister.class);
        startActivity(intent);
    }

    //LOGIN BUTTON CLICK--
    /*public void onButtonClick(View view) {
        loginValidate();
    }*/

    private void openHome() {
        Intent intent = new Intent(this, Home.class); //trocar por Home.class
        intent.putExtra("user_id", savedUser.getId());
        startActivity(intent);
    }



}

