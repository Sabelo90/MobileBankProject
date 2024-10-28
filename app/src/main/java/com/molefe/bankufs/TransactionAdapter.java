package com.molefe.bankufs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<String[]> transactionsList;

    public TransactionAdapter(Context context, ArrayList<String[]> transactionsList) {
        this.context = context;
        this.transactionsList = transactionsList;
    }

    @Override
    public int getCount() {
        return transactionsList.size();
    }

    @Override
    public Object getItem(int position) {
        return transactionsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.transaction_layout, parent, false);
        }

        String[] transaction = transactionsList.get(position);


        TextView textFrom = convertView.findViewById(R.id.txtTFrom);
        TextView textAmount = convertView.findViewById(R.id.txtTAmount);
        TextView textDate = convertView.findViewById(R.id.txtTDate);


        textFrom.setText("Sender: " + transaction[1]);
        textAmount.setText(  transaction[3]);
        textDate.setText( transaction[6]);

        return convertView;
    }
}
