package com.example.bca;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class HighExpenseList extends Fragment {
    TextView listhe;
    EditText deleteid;
    Button delete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.high_expense_list, container, false);
        listhe = (TextView) view.findViewById(R.id.helist);
        deleteid = (EditText) view.findViewById(R.id.hedelid);
        delete = (Button) view.findViewById(R.id.hedelbutid);
        listhe.append("High Expense History\n\n\n");
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("HighExpense");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!listhe.getText().equals("")) {
                    listhe.setText("");
                    listhe.append("High Expense History\n\n\n");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (userID.equals(ds.child("userIDfk").getValue())) {
                            for (DataSnapshot innerds : ds.child("hespent").getChildren()) {
                                listhe.append("\nRs " + innerds.getValue() + " spent on " + innerds.getKey());
                            }
                            listhe.append("\nTotal Expense : " + ds.child("totalExpense").getValue()
                                    + "\nNumber of Expenditure : " + ds.child("numberOfExpense").getValue()
                                    + "\nDate : " + ds.child("date").getValue()
                                    + "\nTime : " + ds.child("time").getValue()
                                    + "\nID : " + ds.child("heid").getValue()
                                    + "\n\n");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteid.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Insert ID Please", Toast.LENGTH_LONG).show();
                } else {
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference("HighExpense");
                    dr.orderByChild("heid").equalTo(Integer.parseInt(deleteid.getText().toString())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                Toast.makeText(getActivity(), "No Records Found", Toast.LENGTH_LONG).show();
                            } else {
                                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                                    if (userID.equals(expenseSnapshot.child("userIDfk").getValue())) {
                                        Log.v("ID : ", "" + expenseSnapshot.toString());
                                        expenseSnapshot.getRef().removeValue();
                                        Toast.makeText(getActivity(), "Record Removed", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Record Not Found", Toast.LENGTH_LONG).show();
                                    }
                                }
                                deleteid.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            throw databaseError.toException();
                        }
                    });
                }
            }
        });
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}