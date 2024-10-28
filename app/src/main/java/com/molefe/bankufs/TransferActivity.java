package com.molefe.bankufs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.molefe.bankufs.Database.ConnectHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.sql.Timestamp;
import java.util.Date;

public class TransferActivity extends AppCompatActivity {
    String UserName, AccNo, UserId, RecieverAccNo, _AccountNumber;
    float balance, amount, RecieverAmount;
    TextView txtAmount;
    Button btnTransfer;
    Connection connection;
    EditText txtAccNo, txtTransferAmount;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        // Initialize the connection
        ConnectHelper c = new ConnectHelper(this);
        connection = c.getConnection();

        // Initialize UI elements
        txtAmount = findViewById(R.id.txtTransferAvailableBalance);
        txtAccNo = findViewById(R.id.txtTranferAccountNumber);
        txtTransferAmount = findViewById(R.id.txtTransferAmount);
        btnTransfer = findViewById(R.id.btnTransferMoney);

        // Fetching intent data
        Intent intent = getIntent();
        UserId = intent.getStringExtra("UserID");
        UserName = intent.getStringExtra("UserName");
        AccNo = intent.getStringExtra("AccountNo");
        balance = intent.getFloatExtra("Balance", 0);
        txtAmount.setText("R " + balance);

        // Transfer button logic
        btnTransfer.setOnClickListener(view -> {
            // Parse transfer amount input
            String amountText = txtTransferAmount.getText().toString();
            RecieverAccNo = txtAccNo.getText().toString();

            // Validate input
            if (amountText.isEmpty() || RecieverAccNo.isEmpty()) {
                Toast.makeText(TransferActivity.this, "Please enter a valid amount and account number.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                amount = Float.parseFloat(amountText);
            } catch (NumberFormatException e) {
                Toast.makeText(TransferActivity.this, "Invalid amount format.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amount <= 0 || amount > balance) {
                Toast.makeText(TransferActivity.this, "Insufficient balance or invalid amount.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Database operation for checking receiver account and updating balances
            if (connection != null) {
                try {
                    String sqlCheckAccount = "SELECT * FROM BankAccounts WHERE AccountNumber = ?";
                    PreparedStatement stmtCheck = connection.prepareStatement(sqlCheckAccount);
                    stmtCheck.setString(1, RecieverAccNo);
                    ResultSet resultSet = stmtCheck.executeQuery();

                    boolean isReceiverFound = false;
                    while (resultSet.next()) {
                        _AccountNumber = resultSet.getString("AccountNumber");
                        RecieverAmount = resultSet.getFloat("Balance");

                        if (_AccountNumber.equals(RecieverAccNo)) {
                            isReceiverFound = true;
                            break;
                        }
                    }

                    if (!isReceiverFound) {
                        Toast.makeText(TransferActivity.this, "Invalid Account Number", Toast.LENGTH_SHORT).show();
                    } else {
                        // Update receiver's balance
                        RecieverAmount += amount;

                        String sqlUpdateReceiver = "UPDATE BankAccounts SET Balance = ? WHERE AccountNumber = ?";
                        PreparedStatement stmtUpdateReceiver = connection.prepareStatement(sqlUpdateReceiver);
                        stmtUpdateReceiver.setFloat(1, RecieverAmount);
                        stmtUpdateReceiver.setString(2, RecieverAccNo);
                        stmtUpdateReceiver.executeUpdate();

                        // Update sender's balance
                        balance -= amount;
                        String sqlUpdateSender = "UPDATE BankAccounts SET Balance = ? WHERE AccountNumber = ?";
                        PreparedStatement stmtUpdateSender = connection.prepareStatement(sqlUpdateSender);
                        stmtUpdateSender.setFloat(1, balance);
                        stmtUpdateSender.setString(2, AccNo);
                        stmtUpdateSender.executeUpdate();

                        try {
                            String sqlStatement = "SELECT * FROM AspNetUsers";
                            Statement stmt_ = connection.createStatement();
                            ResultSet resultSet_ = stmt_.executeQuery(sqlStatement);
                            while (resultSet_.next()) {


                                 id = resultSet_.getString("Id");
                                String accnoo = resultSet_.getString("AccountNumber");
                                if (accnoo.equals(RecieverAccNo)){
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            Log.e("Error:", e.getMessage());
                            Toast.makeText(TransferActivity.this, "Reciever User not found. Please try again.", Toast.LENGTH_SHORT).show();
                        }


                                    // Transaction is successful, now insert transaction record
                        insertTransactionRecord();

                        Toast.makeText(TransferActivity.this, "Transfer successful.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Error:", e.getMessage());
                    Toast.makeText(TransferActivity.this, "Transfer failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(TransferActivity.this, "Connection error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertTransactionRecord() {
        try {
            String sqlInsertTransaction = "INSERT INTO Transactions (BankAccountIdSender, BankAccountIdReceiver, Amount, TransactionDate, Reference, UserEmail, TrandactionType) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmtTransaction = connection.prepareStatement(sqlInsertTransaction);
            stmtTransaction.setInt(1, Integer.parseInt(AccNo)); // Assuming UserId is the sender's account ID
            stmtTransaction.setInt(2, Integer.parseInt(RecieverAccNo)); // Receiver account ID
            stmtTransaction.setFloat(3, amount);
            stmtTransaction.setTimestamp(4, new Timestamp(new Date().getTime())); // Current date and time
            stmtTransaction.setString(5, "Transfer"); // Set a custom reference if required
            stmtTransaction.setString(6, UserName); // Use UserName or other email data as needed
            stmtTransaction.setString(7, "Transfer");

            stmtTransaction.executeQuery();
        } catch (Exception e) {
            Log.e("Transaction Error:", e.getMessage());

        }
    }
}
