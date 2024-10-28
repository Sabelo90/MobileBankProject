package com.molefe.bankufs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.molefe.bankufs.Database.ConnectHelper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class UserActivity extends AppCompatActivity {

    String UserName, AccNo, UserId , sLast , sFirst , sEmail , sPhone;
    TextView txtName, txtBalance, txtAccNo;
    MaterialCardView btnTransfer, btnTransactions , btnSendCash;
    Button requestAdvice;
    float balance;
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Initialize UI components
        txtName = findViewById(R.id.txtUserName);
        txtBalance = findViewById(R.id.txtUserBalance);
        txtAccNo = findViewById(R.id.txtUserAccountNumber);
        btnTransfer = findViewById(R.id.btnTransferMoney);
        btnTransactions = findViewById(R.id.btnTransactions);
        requestAdvice = findViewById(R.id.btnRequest);
        btnSendCash = findViewById(R.id.btnSendCash);

        // Initialize connection
        ConnectHelper c = new ConnectHelper(this);
        connection = c.getConnection();

        // Fetch intent data
        Intent intent = getIntent();
        UserId = intent.getStringExtra("UserID");
        UserName = intent.getStringExtra("UserName");
        sFirst = intent.getStringExtra("First");
        sLast = intent.getStringExtra("Last");
        sPhone = intent.getStringExtra("Phone");
        AccNo = intent.getStringExtra("AccountNo");
        balance = intent.getFloatExtra("Balance", 0);

        // Display user information
        txtName.setText(UserName);
        txtAccNo.setText(AccNo);
        txtBalance.setText("R " + balance);

        // Handle the advice request button click
        requestAdvice.setOnClickListener(view -> {
            new InsertAdviceTask().execute();
        });

        // Set onClick listeners for other buttons
        btnTransfer.setOnClickListener(view -> startNewActivity(TransferActivity.class));
        btnTransactions.setOnClickListener(view -> startNewActivity(TransactionsActivity.class));
        btnSendCash.setOnClickListener(view -> startNewActivity(SendCashActivity.class));
    }

    // AsyncTask to handle advice insertion
    private class InsertAdviceTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (connection != null) {
                try {
                    // SQL statement to insert new advice
                    String sql = "INSERT INTO Advices (ClientId, ClientName, CreatedDate, Amount) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt = connection.prepareStatement(sql);

                    // Set values for the advice record
                    stmt.setString(1, UserId);
                    stmt.setString(2, UserName);
                    stmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                    stmt.setFloat(4, balance);  // Assuming balance is the advice amount

                    // Execute insert statement
                    stmt.executeUpdate();
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
                Toast.makeText(UserActivity.this, "Advice request submitted successfully.", Toast.LENGTH_SHORT).show();
                showAdviceRequestPopup();
            } else {
                Toast.makeText(UserActivity.this, "Failed to submit advice request.", Toast.LENGTH_SHORT).show();
                showAdviceRequestPopup();
            }
        }
    }

    // Helper method to start new activities
    private void startNewActivity(Class<?> activityClass) {
        Intent intent = new Intent(UserActivity.this, activityClass);
        intent.putExtra("UserID", UserId);
        intent.putExtra("UserName", UserName);
        intent.putExtra("AccountNo", AccNo);
        intent.putExtra("Balance", balance);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      if (item.getItemId() == R.id.user_icon){

              Intent intent = new Intent(UserActivity.this, ProfileActivity.class);
              intent.putExtra("ID", UserId);
              startActivity(intent);
              return true;


      }
        return super.onOptionsItemSelected(item);
    }
    private void showAdviceRequestPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Success!")
                .setMessage("Your advice request has been sent successfully. A financial advisor will reach out to you soon.")
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)  // Using default Android icon
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // Optionally refresh or navigate away
                        // finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}