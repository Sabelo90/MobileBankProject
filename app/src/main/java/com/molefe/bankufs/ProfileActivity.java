package com.molefe.bankufs;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.molefe.bankufs.Database.ConnectHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProfileActivity extends AppCompatActivity {


    EditText txtEmail,txtTeacher, txtVacation ,txtName, txtSurname,txtPhone,txtStudentNumber, txtStaffNumber,txtPassport;

    private Button update;
    Connection connection;
    String Email , Name  , PhoneNumber, userId;
    String IdNo;

    String Surname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtEmail = findViewById(R.id.profEmail);
        txtName = findViewById(R.id.profUserName);
        txtSurname = findViewById(R.id.profSurname);

        txtPassport = findViewById(R.id.profPassport);
        txtPhone = findViewById(R.id.profPhone);
        ConnectHelper c = new ConnectHelper(this);
        connection = c.getConnection();
        String sID ;
        userId = getIntent().getStringExtra("ID");


        if (connection!= null) {
            try {
                String sqlStatement = "SELECT * FROM AspNetUsers";
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(sqlStatement);
                while (resultSet.next()) {

                    sID = resultSet.getString("Id");


                    if (sID.equals(userId)) {

                        Name = resultSet.getString("FirstName");
                        Surname = resultSet.getString("LastName");
                        Email = resultSet.getString("Email");
                        IdNo =  resultSet.getString("IDnumber");
                        PhoneNumber = resultSet.getString("PhoneNumber");


                    }
                }
            }catch (Exception e){
                Log.e("Error:",e.getMessage());
            }
            txtName.setText(Name);
            txtPhone.setText(PhoneNumber);
            txtSurname.setText(Surname);
            txtPassport.setText(String.valueOf(IdNo));
            txtEmail.setText(Email);
        }

        update = findViewById(R.id.btnprofUpdate);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connection != null) {
                    try {
                        String updateSql = "UPDATE AspNetUsers SET FirstName = ?, LastName = ?, Email = ?, IDnumber = ?, PhoneNumber = ? WHERE Id = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(updateSql);

                        // Set parameters in sequence
                        preparedStatement.setString(1, txtName.getText().toString()); // FirstName
                        preparedStatement.setString(2, txtSurname.getText().toString()); // LastName
                        preparedStatement.setString(3, txtEmail.getText().toString()); // Email
                        preparedStatement.setString(4, txtPassport.getText().toString()); // IDnumber (converted to string)
                        preparedStatement.setString(5, txtPhone.getText().toString()); // PhoneNumber
                        preparedStatement.setString(6, userId); // Id (User ID)

                        // Execute the UPDATE statement
                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            System.out.println("Profile updated successfully.");
                        } else {
                            Toast.makeText(ProfileActivity.this, "Profile update failed", Toast.LENGTH_SHORT).show();
                            System.out.println("Profile update failed.");
                        }

                        // Close the PreparedStatement and Connection
                        preparedStatement.close();
                        connection.close();
                    } catch (Exception e) {
                        Log.e("Error:", e.getMessage());
                    }
                }
            }
        });







    }
}