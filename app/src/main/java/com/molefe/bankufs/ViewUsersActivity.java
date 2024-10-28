package com.molefe.bankufs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.molefe.bankufs.Database.ConnectHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ViewUsersActivity extends AppCompatActivity {
Connection connection;
ListView lstUsers;
List<User> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);
        lstUsers = findViewById(R.id.lstViewUser);


        ConnectHelper c = new ConnectHelper(this);
        connection = c.getConnection();

        if (c!= null){
            try {


                String sqlStatement = "SELECT * FROM AspNetUsers";
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(sqlStatement);

                items = new ArrayList<>();

                while (resultSet.next()) {
                    User user = new User();

                    user.setUserName(resultSet.getString("UserName"));
                    user.setRole(resultSet.getString("UserRole"));
                    user.setEmail(resultSet.getString("Email"));
                    user.setId(resultSet.getString("Id"));

                    items.add(user);

                }
                connection.close();
            }catch (Exception e){
                Log.e("Error:",e.getMessage());
            }

        }

        List<User> Items = items;



        // Create and set the adapter
        ViewUsersAdapter adapter = new ViewUsersAdapter(this, R.layout.users_layout, Items);
        lstUsers.setAdapter(adapter);


    }


}