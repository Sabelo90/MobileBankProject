package com.molefe.bankufs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {
Button _btnViewUsers;
Button btnManageFacility , ManageManagers;

Button _btnAddFacility , btnViewOrders, btnInCharge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        btnViewOrders = findViewById(R.id.btnViewOrdersAdmin);
        _btnViewUsers = findViewById(R.id.btnViewUsers);
        _btnAddFacility = findViewById(R.id.btnAdminAddFacility);
        btnInCharge = findViewById(R.id.btnViewInChargeAdmin);
        ManageManagers = findViewById(R.id.btnAdminManageManagers);
        btnManageFacility = findViewById(R.id.btnAdminManageFacility);
//
//        ManageManagers.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AdminActivity.this, AdminManagers.class);
//                startActivity(intent);
//
//            }
//        });
//
//        btnManageFacility.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(AdminActivity.this, ManageFacilityActivity.class);
//
//
//                startActivity(intent);
//
//            }
//        });
//
//
//        btnInCharge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AdminActivity.this, AdminInChargeActivity.class);
//                startActivity(intent);
//
//            }
//        });


        _btnViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ViewUsersActivity.class);
                startActivity(intent);

            }
        });

//        btnViewOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AdminActivity.this, ViewBookings.class);
//                startActivity(intent);
//
//
//            }
//        });

//        _btnAddFacility.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AdminActivity.this, AddFacility.class);
//                startActivity(intent);
//
//
//            }
//        });
    }
}