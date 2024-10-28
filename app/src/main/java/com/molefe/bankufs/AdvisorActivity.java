package com.molefe.bankufs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.molefe.bankufs.Database.ConnectHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdvisorActivity extends AppCompatActivity {

    String UserName, AccNo, UserId , sLast , sFirst , sEmail , sPhone;
    TextView txtName, txtBalance, txtAccNo;
    MaterialCardView btnTransfer, btnTransactions , btnSendCash;
    Button requestAdvice;
    float balance;
    Connection connection;
    ListView listView;
    private ArrayList<String[]> AdviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor);

        listView = findViewById(R.id.lstAdvice);


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

        new FetchAdvicesTask().execute();
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

            Intent intent = new Intent(AdvisorActivity.this, ProfileActivity.class);
            intent.putExtra("ID", UserId);
            startActivity(intent);
            return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private class FetchAdvicesTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (connection != null) {
                try {
                    // Clear the previous list
                    AdviceList.clear();

                    // SQL query to fetch advices where advisorId and advisorName are null
                    String query = "SELECT * FROM Advices WHERE advisorId IS NULL AND advisorName IS NULL";
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery();

                    // Process the result set
                    while (resultSet.next()) {
                        // Assuming you want to store the fields from the result set
                        String[] advice = new String[]{
                                resultSet.getString("Id"),
                                resultSet.getString("CreatedDate"),
                                resultSet.getString("Message"),
                                resultSet.getString("clientId"),
                                resultSet.getString("clientName"),
                                resultSet.getString("advisorId"),
                                resultSet.getString("advisorName"),
                                String.valueOf(resultSet.getDouble("Amount")),
                                String.valueOf(resultSet.getDouble("Income")),
                                String.valueOf(resultSet.getDouble("Expenses"))
                        };
                        AdviceList.add(advice);
                    }


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
                AdviceAdapter adapter = new AdviceAdapter(AdvisorActivity.this, AdviceList , connection , UserName ,UserId);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(AdvisorActivity.this, "Failed to retrieve data from database", Toast.LENGTH_LONG).show();
            }
        }


    }

    public void refreshAdviceList() {
        new FetchAdvicesTask().execute();
    }
}