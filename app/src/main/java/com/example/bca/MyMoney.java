package com.example.bca;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.lang.Float.parseFloat;

public class MyMoney extends Fragment {
    Float monthlyBudget, result, de, deCopy = 0.f;
    String userID, date, time;
    TextView mmtv, mbtv, tytv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_money, container, false);
        mmtv = (TextView) view.findViewById(R.id.trackmm);
        mbtv = (TextView) view.findViewById(R.id.mbmm);
        tytv = (TextView) view.findViewById(R.id.stop);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Toast.makeText(getActivity(),"User ID : "+userID,Toast.LENGTH_LONG).show();
        DatabaseReference dr1 = FirebaseDatabase.getInstance().getReference().child("users");
        dr1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (userID.equals(ds.child("userIDOriginal").getValue().toString())) {
                        mbtv.setText("Your Monthly Budget");
                        monthlyBudget = Float.parseFloat(ds.child("userMonthlyBudget").getValue().toString());
                        mbtv.append(" : Rs " + monthlyBudget);
                        calcMoneyTrack();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //Toast.makeText(getActivity(),""+monthlyBudget,Toast.LENGTH_LONG).show();
        return view;
    }

    private void calcMoneyTrack() {
        //Toast.makeText(getActivity(),"Func : "+MB,Toast.LENGTH_LONG).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DailyExpense");
        databaseReference.orderByChild("dailyExpenseID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!mmtv.getText().equals("")) {
                    mmtv.setText("");
                    tytv.setText("");
                    mmtv.setText("My Money Track\n\n\n");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (userID.equals(ds.child("userIDfk").getValue().toString())) {
                            result = 0.f;
                            date = ds.child("date").getValue().toString();
                            time = ds.child("time").getValue().toString();
                            de = Float.parseFloat(ds.child("totalExpense").getValue().toString());
                            deCopy += de;
                            result = monthlyBudget - deCopy;
                            mmtv.append("\n\nDate\t\t\t\t\t\t\t\t : " + date
                                    + "\nTime\t\t\t\t\t\t\t\t : " + time
                                    + "\nMoney Spent  : " + de);
                            if (result > 0.f) {
                                mmtv.append("\nRemaining \t\t\t : " + result);
                            }
                            if (result <= 0.f) {
                                tytv.setText("\n\nYou're out of Budget now.\nPlease start saving Money,\nThank You!\n");
                            } else {
                                tytv.setText("");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        deCopy = 0.f;
    }
}