package com.molefe.bankufs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.molefe.bankufs.Database.ConnectHelper;
import com.molefe.bankufs.MainActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LogIn extends AppCompatActivity {
    Connection connection;
    EditText txtEmail, txtPassword;
    Button btnLogIn;
    Button btnRegister;
    String password ,role, Email;
    String spass , sEmail , sFirst , sLast , sPhone;
    String UserId,UserName;
    String AccountNumber , _AccountNumber ;
    float Balance;
    int  UserTypeID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        txtPassword = findViewById(R.id.LogPassword);
        txtEmail = findViewById(R.id.logEmail);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnRegister = findViewById(R.id.btnRegister);
        ConnectHelper c = new ConnectHelper(this);
        connection = c.getConnection();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, RegisterActivity.class);

                startActivity(intent);

            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = txtPassword.getText().toString();
                Email = txtEmail.getText().toString();
                if (Email.equals("") || password.equals("")){
                    Toast.makeText(LogIn.this, "Please Enter Password and Email", Toast.LENGTH_SHORT).show();
                }else {
                    if (connection != null) {
                        try {
                            String sqlStatement = "SELECT * FROM AspNetUsers";
                            Statement stmt = connection.createStatement();
                            ResultSet resultSet = stmt.executeQuery(sqlStatement);
                            while (resultSet.next()) {

                                spass = resultSet.getString("Password");
                                sEmail = resultSet.getString("Email");
                                role = resultSet.getString("UserRole");
                                UserId = resultSet.getString("Id");

                                if (password.equals(spass) &&
                                        Email.equals(sEmail)  ){


//                                    if(role.equals("Admin")){
//
//
//
//
////                                        Intent intent = new Intent(LogIn.this, AdminActivity.class);
////                                        intent.putExtra("UserName",UserName);
////                                        intent.putExtra("UserID",UserId);
////                                        intent.putExtra("UserType",UserTypeID);
////                                        intent.putExtra("role","");
////                                        startActivity(intent);
//
//                                    }else if (role.equals("Discussion-Room-Manager")|| role.equals("Parking-Manager")||role.equals("Gym-Manager")||role.equals("Laundry-Room-Manager")){
////                                        Intent intent = new Intent(LogIn.this, ManagerActivity.class);
////                                        UserTypeID = resultSet.getInt("UserTypeID");
////                                        UserName = resultSet.getString("UserName");
////                                        UserId = resultSet.getString("Id");
////                                        intent.putExtra("UserName",UserName);
////                                        intent.putExtra("UserID",UserId);
////                                        intent.putExtra("UserType",UserTypeID);
////                                        intent.putExtra("role",role);
////
////                                        startActivity(intent);
//
//
//                                    } else if(role.equals("Laundry-FacilityInCharge") || role.equals("Discussion-FacilityInCharge") ||
//                                            role.equals("Parking-FacilityInCharge") || role.equals("Gym-FacilityInCharge") ){
//                                        Intent intent = new Intent(LogIn.this, FacilityinChacrgeActivity.class);
//                                        UserTypeID = resultSet.getInt("UserTypeID");
//                                        UserName = resultSet.getString("UserName");
//                                        UserId = resultSet.getString("Id");
//                                        intent.putExtra("UserName",UserName);
//                                        intent.putExtra("UserID",UserId);
//                                        intent.putExtra("UserType",UserTypeID);
//                                        intent.putExtra("role",role);
//                                        startActivity(intent);
//
//                                    }

//                                    else{
                                    Intent intent;

                                    if (role.equals("student") || role.equals("staff")){
                                        intent = new Intent(LogIn.this, UserActivity.class);
                                    }else{
                                        intent = new Intent(LogIn.this, AdvisorActivity.class);
                                    }



                                    UserName = resultSet.getString("UserName");
                                    sPhone = resultSet.getString("PhoneNumber");
                                    sFirst = resultSet.getString("FirstName");
                                    sLast = resultSet.getString("LastName");
                                    UserId = resultSet.getString("Id");
                                    AccountNumber = resultSet.getString("AccountNumber");
                                    String sqlStatement_ = "SELECT * FROM BankAccounts";
                                    Statement stmt_ = connection.createStatement();
                                    ResultSet resultSet_ = stmt_.executeQuery(sqlStatement_);
                                    while (resultSet_.next()) {
                                        _AccountNumber = resultSet_.getString("AccountNumber");
                                        Balance = resultSet_.getFloat("Balance");
                                        if (_AccountNumber.equals(AccountNumber)){
                                            break;
                                        }
                                    }

                                    intent.putExtra("UserName",UserName);
                                    intent.putExtra("UserID",UserId);
                                    intent.putExtra("AccountNo",AccountNumber);
                                    intent.putExtra("Balance" , Balance);
                                    intent.putExtra("Email" , sEmail);
                                    intent.putExtra("Phone" ,sPhone);
                                    intent.putExtra("Last" , sLast);
                                    intent.putExtra("First" , sFirst);

                                    startActivity(intent);

                                    //    }
                                    connection.close();

                                }
                            }
                            Toast.makeText(LogIn.this, "Incorrect Email/Password", Toast.LENGTH_SHORT).show();

                        }catch (Exception e){
                            Log.e("Error:",e.getMessage());
                        }
                    }else{
                        Toast.makeText(LogIn.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
}