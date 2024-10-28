package com.molefe.bankufs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.molefe.bankufs.Database.ConnectHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etFirstName, etLastName, etStudentStaffNumber, etIdPassport, detPassword, confirm_password;
    private Button btnSubmit;
    public ConnectHelper connectHelper;
    private final String userType = "Student"; // Default to student

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.btnregLogIn).setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, MainActivity.class)));

        // Initialize views
        etEmail = findViewById(R.id.et_email);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etStudentStaffNumber = findViewById(R.id.et_student_staff_number);
        etIdPassport = findViewById(R.id.et_id_passport);
        detPassword = findViewById(R.id.det_password);
        confirm_password = findViewById(R.id.confirm_password);
        btnSubmit = findViewById(R.id.btnregRegister);

        connectHelper = new ConnectHelper(this);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String studentStaffNumber = etStudentStaffNumber.getText().toString().trim();
        String idPassport = etIdPassport.getText().toString().trim();
        String password = detPassword.getText().toString().trim();
        String confirmPassword = confirm_password.getText().toString().trim();

        // Validation checks
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(studentStaffNumber) ||
                TextUtils.isEmpty(idPassport) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idPassport.length() != 13) {
            Toast.makeText(RegisterActivity.this, "ID/Passport must be 13 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        if (studentStaffNumber.length() != 10) {
            Toast.makeText(RegisterActivity.this, "Student/Staff number must be 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique 18-digit account number
        String accountNumber = generateAccountNumber();

        // Insert into database
        UUID uuid = UUID.randomUUID();
        String Id = uuid.toString();

        String phoneNumber = "1234567890";  // Example phone number
        String dateOfBirth = "1990-01-01";
        String sqlQuery = "INSERT INTO AspNetUsers " +
                "(Id, DateOfBirth, FirstName, LastName, StudentStaffNumber, IDnumber, AccountNumber, confPassword, UserRole, " +
                "UserName, NormalizedUserName, Email, NormalizedEmail, EmailConfirmed, " +
                "PhoneNumber, PhoneNumberConfirmed, TwoFactorEnabled, LockoutEnd, LockoutEnabled, AccessFailedCount) " +
                "VALUES ('" + Id + "', '" + dateOfBirth + "', '" + firstName + "', '" + lastName + "', '" + studentStaffNumber + "', " +
                "'" + idPassport + "', '" + accountNumber + "', '" + confirmPassword + "', '" + userType + "', '" + lastName + "', '" + lastName.toUpperCase() + "', " +
                "'" + email + "', '" + email.toUpperCase() + "', 0, " +
                "'" + phoneNumber + "', 0, 0, '1900-01-01', 0, 0)";

        try {
            Statement statement = connectHelper.getConnection().createStatement();
            int result = statement.executeUpdate(sqlQuery);

            if (result > 0) {
                Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent( RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(RegisterActivity.this, "Error during registration", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            accountNumber.append(random.nextInt(10)); // Generate random number between 0-9
        }

        return accountNumber.toString();
    }
    // Function to generate a SHA-256 hash (password hash)

}
