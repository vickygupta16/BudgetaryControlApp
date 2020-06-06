package com.example.bca;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DailyExpense extends Fragment {
    int flag = 0;
    StringBuffer stringBuffer;
    Float totalExpenditure = 0.f;
    Integer numberOfExpenditure = 0;
    String currentDate, currentTime;
    DailyExpenseData dailyExpenseData;
    Map<String, Float> demap = new HashMap<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DailyExpense");
    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("DailyExpense");
    Integer deID = 0;
    Integer idFlag = 0;
    EditText category, amount;
    Button add, clear, generatelist;
    ArrayList<Integer> decount = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_expense, container, false);
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dailyExpenseData = new DailyExpenseData();
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("dailyExpenseID")) {
                    idFlag = 0;
                    Log.d("Inside ifds", "idFlag 0");
                } else {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        decount.add(Integer.parseInt(ds.child("dailyExpenseID").getValue().toString()));
                        idFlag = 1;
                        Log.d("Inside ifds", "idFlag 1");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //Toast.makeText(getActivity(),"Max ID : "+ Collections.max(decount),Toast.LENGTH_SHORT).show();
        category = (EditText) view.findViewById(R.id.categoryidde);
        amount = (EditText) view.findViewById(R.id.moneyspentde);
        add = (Button) view.findViewById(R.id.addexpensede);
        clear = (Button) view.findViewById(R.id.clearexpensede);
        generatelist = (Button) view.findViewById(R.id.expenselistde);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (category.getText().toString().equals("") || amount.getText().toString().equals("")) {
                    if (category.getText().toString().equals("")) {
                        category.setError("Required");
                        category.requestFocus();
                    }
                    if (amount.getText().toString().equals("")) {
                        amount.setError("Required");
                        amount.requestFocus();
                    }
                } else {
                    final String cate = category.getText().toString();
                    final Float expen = Float.parseFloat(amount.getText().toString());
                    demap.put(cate, expen);
                    category.setText("");
                    amount.setText("");
                    category.requestFocus();
                    Toast.makeText(getActivity(), "Expense Added", Toast.LENGTH_SHORT).show();
                    flag = 1;
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setText("");
                amount.setText("");
                category.requestFocus();
                demap.clear();
                flag = 0;
                Toast.makeText(getActivity(), "Expense List Cleared", Toast.LENGTH_SHORT).show();
            }
        });
        generatelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1) {
                    if (idFlag == 0) {
                        deID = 0;
                        Log.v("Inside if idFlag : ", "ID : " + deID);
                    } else if (idFlag == 1) {
                        deID = Collections.max(decount);
                        Log.v("Inside else idFlag : ", "ID : " + deID);
                    }
                    category.requestFocus();
                    stringBuffer = new StringBuffer();
                    for (Map.Entry<String, Float> entry : demap.entrySet()) {
                        String cat = entry.getKey();
                        Float exp = entry.getValue();
                        totalExpenditure = totalExpenditure + exp;
                        ++numberOfExpenditure;
                        stringBuffer.append("\n" + cat + " : " + exp);
                    }
                    stringBuffer.append("\nTotal Amount Spent : " + totalExpenditure
                            + "\nNumber of Expenditure : " + numberOfExpenditure);
                    currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    currentTime = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());
                    dailyExpenseData.setDailyExpenseID(++deID);
                    dailyExpenseData.setDate(currentDate);
                    dailyExpenseData.setDespent(demap);
                    dailyExpenseData.setNumberOfExpense(numberOfExpenditure);
                    dailyExpenseData.setTime(currentTime);
                    dailyExpenseData.setTotalExpense(totalExpenditure);
                    dailyExpenseData.setUserIDfk(userID);
                    databaseReference.push().setValue(dailyExpenseData);
                    demap.clear();
                    flag = 0;
                    totalExpenditure = 0.f;
                    numberOfExpenditure = 0;
                    idFlag = 0;
                    Toast.makeText(getActivity(), "List Generated", Toast.LENGTH_LONG).show();
                    showMessage("Todays Expense", stringBuffer.toString());
                } else {
                    Toast.makeText(getActivity(), "No Expenses Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}