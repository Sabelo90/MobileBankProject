package com.molefe.bankufs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.molefe.bankufs.Database.ConnectHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Random;

public class SendCashActivity extends AppCompatActivity {
    String UserName, AccNo, UserId, RecieverAccNo, _AccountNumber;
    float balance, amount, RecieverAmount;
    TextView txtAmount;
    Button btnSend;
    Connection connection;
    EditText txtSendAmount , txtPin;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_cash);

        // Initialize the connection
        ConnectHelper c = new ConnectHelper(this);
        connection = c.getConnection();
        txtAmount = findViewById(R.id.txtSendCashAvailableBalance);
        txtSendAmount = findViewById(R.id.txtCashSendAmount);
        txtPin = findViewById(R.id.txtCashSendPin);
        btnSend = findViewById(R.id.btnSend);
        // Fetching intent data
        Intent intent = getIntent();
        UserId = intent.getStringExtra("UserID");
        UserName = intent.getStringExtra("UserName");
        AccNo = intent.getStringExtra("AccountNo");
        balance = intent.getFloatExtra("Balance", 0);
        txtAmount.setText("R " + balance);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve entered amount and PIN
                String amountText = txtSendAmount.getText().toString();
                String pinText = txtPin.getText().toString();

                // Input validation
                if (amountText.isEmpty() || pinText.isEmpty()) {
                    Toast.makeText(SendCashActivity.this, "Please enter a valid amount and PIN.", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    amount = Float.parseFloat(amountText);
                } catch (NumberFormatException e) {
                    Toast.makeText(SendCashActivity.this, "Invalid amount format.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check for sufficient balance
                if (amount <= 0 || amount > balance) {
                    Toast.makeText(SendCashActivity.this, "Insufficient balance or invalid amount.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verify PIN
                if (!pinText.equals("1234")) { // Replace "1234" with actual PIN retrieval logic
                    Toast.makeText(SendCashActivity.this, "Invalid PIN.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Execute cash send operation
                if (connection != null) {
                    try {
                        // Deduct amount from user's balance
                        balance -= amount;
                        String sqlUpdateSender = "UPDATE BankAccounts SET Balance = ? WHERE AccountNumber = ?";
                        PreparedStatement stmtUpdateSender = connection.prepareStatement(sqlUpdateSender);
                        stmtUpdateSender.setFloat(1, balance);
                        stmtUpdateSender.setString(2, AccNo);
                        stmtUpdateSender.executeUpdate();

                        // Insert a record into the transactions table
                        String sqlInsertTransaction = "INSERT INTO Transactions (BankAccountIdSender, BankAccountIdReceiver, Amount, TransactionDate, Reference, TrandactionType) VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement stmtInsertTransaction = connection.prepareStatement(sqlInsertTransaction);
                        stmtInsertTransaction.setString(1, AccNo);
                        stmtInsertTransaction.setInt(2, 0); // Receiver Account ID as 0
                        stmtInsertTransaction.setFloat(3, amount);
                        stmtInsertTransaction.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                        stmtInsertTransaction.setString(5, "Cash Send");
                        stmtInsertTransaction.setString(6, "Cash Send");
                        stmtInsertTransaction.executeUpdate();

                        // Generate a 10-digit code
                        String withdrawCode = generateWithdrawCode();

                        // Show the code, PIN, and sent amount in a modal dialog
                        showWithdrawCodeDialog(withdrawCode, pinText, amount);

                        Toast.makeText(SendCashActivity.this, "Cash sent successfully.", Toast.LENGTH_SHORT).show();
                        txtAmount.setText("R " + balance); // Update displayed balance

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SendCashActivity.this, "Failed to send cash. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SendCashActivity.this, "Connection error.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String generateWithdrawCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            code.append(random.nextInt(10)); // Appends a random number between 0-9
        }
        return code.toString();
    }

    // Method to show a modal dialog with the generated code, PIN, and sent amount
    private void showWithdrawCodeDialog(String code, String pin, float amount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SendCashActivity.this);
        builder.setTitle("Withdrawal Code")
                .setMessage("Use this code, PIN, and amount for withdrawal:\n\nCode: " + code + "\nPIN: " + pin + "\nAmount: R " + amount)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

}