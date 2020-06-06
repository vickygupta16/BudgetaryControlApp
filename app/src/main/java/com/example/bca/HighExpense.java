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

public class HighExpense extends Fragment {
    int flag = 0;
    StringBuffer stringBuffer;
    Float totalExpenditure = 0.f;
    Integer numberOfExpenditure = 0;
    String currentDate, currentTime;
    HighExpenseData highExpenseData;
    Map<String, Float> hemap = new HashMap<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("HighExpense");
    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("HighExpense");
    Integer heID = 0;
    Integer idFlag = 0;
    ArrayList<Integer> hecount = new ArrayList<>();
    EditText item, amt;
    Button add, clear, generateList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.high_expense, container, false);
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        highExpenseData = new HighExpenseData();
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("heid"))
                {
                    //heID = 0;
                    idFlag = 0;
                    Log.d("Inside ifds", "idFlag 0");
                } else {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        hecount.add(Integer.parseInt(ds.child("heid").getValue().toString()));
                        idFlag = 1;
                        Log.d("Inside ifds", "idFlag 1");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        item = (EditText) view.findViewById(R.id.itemhe);
        amt = (EditText) view.findViewById(R.id.moneyhe);
        add = (Button) view.findViewById(R.id.addexpensehe);
        clear = (Button) view.findViewById(R.id.clearexpensehe);
        generateList = (Button) view.findViewById(R.id.listhe);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getText().toString().equals("") || amt.getText().toString().equals("")) {
                    if (item.getText().toString().equals("")) {
                        item.setError("Required");
                        item.requestFocus();
                    }
                    if (amt.getText().toString().equals("")) {
                        amt.setError("Required");
                        amt.requestFocus();
                    }
                } else {
                    final String itemname = item.getText().toString();
                    final Float amount = Float.parseFloat(amt.getText().toString());
                    hemap.put(itemname, amount);
                    item.setText("");
                    amt.setText("");
                    item.requestFocus();
                    flag = 1;
                    Toast.makeText(getActivity(), "Item with Expense Added", Toast.LENGTH_LONG).show();
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1) {
                    item.setText("");
                    amt.setText("");
                    item.requestFocus();
                    hemap.clear();
                    flag = 0;
                    Toast.makeText(getActivity(), "Expense List Cleared", Toast.LENGTH_LONG).show();
                }
            }
        });
        generateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1) {
                    if (idFlag == 0) {
                        heID = 0;
                        Log.v("Inside if idFlag : ", "ID : " + heID);
                    } else if (idFlag == 1) {
                        heID = Collections.max(hecount);
                        Log.v("Inside else idFlag : ", "ID : " + heID);
                    }
                    item.requestFocus();
                    stringBuffer = new StringBuffer();
                    for (Map.Entry<String, Float> entry : hemap.entrySet()) {
                        String in = entry.getKey();
                        Float ma = entry.getValue();
                        totalExpenditure = totalExpenditure + ma;
                        ++numberOfExpenditure;
                        stringBuffer.append("\n" + in + " : " + ma);
                    }
                    stringBuffer.append("\nTotal Expense : " + totalExpenditure
                            + "\nNumber of Expenditure : " + numberOfExpenditure);
                    currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    currentTime = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());
                    highExpenseData.setHEID(++heID);
                    highExpenseData.setDate(currentDate);
                    highExpenseData.setTime(currentTime);
                    highExpenseData.setHespent(hemap);
                    highExpenseData.setNumberOfExpense(numberOfExpenditure);
                    highExpenseData.setUserIDfk(userID);
                    highExpenseData.setTotalExpense(totalExpenditure);
                    databaseReference.push().setValue(highExpenseData);
                    hemap.clear();
                    numberOfExpenditure = 0;
                    flag = 0;
                    totalExpenditure = 0.f;
                    Toast.makeText(getActivity(), "List Generated", Toast.LENGTH_LONG).show();
                    showMessage("High Expense", stringBuffer.toString());
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
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.show();
    }
}