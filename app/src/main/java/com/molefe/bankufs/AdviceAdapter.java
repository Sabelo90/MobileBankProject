package com.molefe.bankufs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdviceAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<String[]> AdviceList;
    private final Connection connection;
    private final String AdvisorName;
    private final String AdvisorId;

    public AdviceAdapter(Context context, ArrayList<String[]> AdviceList, Connection connection, String AdvisorName, String AdvisorId) {
        this.context = context;
        this.AdviceList = AdviceList;
        this.connection = connection;
        this.AdvisorName = AdvisorName;
        this.AdvisorId = AdvisorId;
    }

    @Override
    public int getCount() {
        return AdviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return AdviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.advice_layout, parent, false);
        }

        String[] advice = AdviceList.get(position);

        TextView textFrom = convertView.findViewById(R.id.txtAdviceFrom);
        TextView textAmount = convertView.findViewById(R.id.txtTAdviceAmount);
        Button btnGiveAdvice = convertView.findViewById(R.id.btnGiveAdvice);

        textFrom.setText(advice[4]);  // clientName
        textAmount.setText(advice[7]);  // Amount

        btnGiveAdvice.setOnClickListener(view -> showMessageDialog(advice[0]));

        return convertView;
    }

    private void showMessageDialog(String adviceId) {
        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Advice Message");

        // Set up the input
        final EditText input = new EditText(context);
        input.setHint("Type your advice message here");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Submit", (dialog, which) -> {
            String message = input.getText().toString();
            if (!message.isEmpty()) {
                // Update the advice with the message, advisorName, and advisorId
                new UpdateAdviceTask(adviceId, message, AdvisorName, AdvisorId).execute();
            } else {
                Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Show the dialog
        builder.show();
    }

    private class UpdateAdviceTask extends AsyncTask<Void, Void, Boolean> {

        private final String adviceId;
        private final String message;
        private final String advisorName;
        private final String advisorId;

        public UpdateAdviceTask(String adviceId, String message, String advisorName, String advisorId) {
            this.adviceId = adviceId;
            this.message = message;
            this.advisorName = advisorName;
            this.advisorId = advisorId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (connection != null) {
                try {
                    String updateQuery = "UPDATE Advices SET Message = ?, advisorName = ?, advisorId = ? WHERE Id = ?";
                    PreparedStatement statement = connection.prepareStatement(updateQuery);

                    statement.setString(1, message);        // Set the entered message
                    statement.setString(2, advisorName);    // Set the advisor's name
                    statement.setString(3, advisorId);      // Set the advisor's ID
                    statement.setString(4, adviceId);       // Use adviceId to identify the record

                    int rowsAffected = statement.executeUpdate();
                    statement.close();

                    return rowsAffected > 0;

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(context, "Advice updated successfully", Toast.LENGTH_SHORT).show();
                if (context instanceof AdvisorActivity) {
                    ((AdvisorActivity) context).refreshAdviceList();
                }
            } else {
                Toast.makeText(context, "Failed to update advice", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
