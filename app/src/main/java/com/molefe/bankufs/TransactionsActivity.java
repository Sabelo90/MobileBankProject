package com.molefe.bankufs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.molefe.bankufs.Database.ConnectHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.molefe.bankufs.Database.ConnectHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionsActivity extends AppCompatActivity {

    private ArrayList<String[]> transactionsList = new ArrayList<>();
    String UserName, AccNo, UserId, RecieverAccNo, _AccountNumber;
    float balance, amount, RecieverAmount;
    Connection connection;
    String id;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        // Initialize the connection
        ConnectHelper c = new ConnectHelper(this);
        connection = c.getConnection();

        // Fetching intent data
        Intent intent = getIntent();
        UserId = intent.getStringExtra("UserID");
        UserName = intent.getStringExtra("UserName");
        AccNo = intent.getStringExtra("AccountNo");
        balance = intent.getFloatExtra("Balance", 0);
        listView = findViewById(R.id.lstViewUserTransactions);

        // Fetch transactions as soon as the activity starts
        new FetchTransactionsTask().execute();
    }

    private class FetchTransactionsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (connection != null) {
                try {
                    // Clear the previous list
                    transactionsList.clear();

                    // SQL query to get transactions where the account is either sender or receiver
                    String sqlQuery = "SELECT * FROM Transactions WHERE BankAccountIdSender = ? OR BankAccountIdReceiver = ?";
                    PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                    stmt.setString(1, AccNo);
                    stmt.setString(2, AccNo);
                    ResultSet resultSet = stmt.executeQuery();

                    while (resultSet.next()) {
                        // Extract transaction details
                        String id = resultSet.getString("Id");
                        String senderId = resultSet.getString("BankAccountIdSender");
                        String receiverId = resultSet.getString("BankAccountIdReceiver");
                        String amount = resultSet.getString("Amount");
                        String date = resultSet.getString("TransactionDate");
                        String reference = resultSet.getString("Reference");
                        String type = resultSet.getString("TrandactionType");

                        // Add transaction details as an array
                        transactionsList.add(new String[]{id, senderId, receiverId, amount, date, reference, type});
                    }

                    connection.close();
                    return true;

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Create an instance of TransactionAdapter and set it to the ListView
                TransactionAdapter adapter = new TransactionAdapter(TransactionsActivity.this, transactionsList);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(TransactionsActivity.this, "Failed to retrieve data from database", Toast.LENGTH_LONG).show();
            }
        }
    }
}
