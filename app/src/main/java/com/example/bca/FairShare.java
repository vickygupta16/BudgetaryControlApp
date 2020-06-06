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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class FairShare extends Fragment {
    StringBuffer stringBuffer;
    EditText cname, camt;
    Button addContro, calculateFair, clearContro;
    Map<String, Float> contribution = new HashMap<>();
    Float totalFair;
    Float perHead;
    int totalContributors;
    int fsid = 0;
    int flag = 0;
    Integer idFlag = 0;
    ArrayList<Integer> fscount = new ArrayList<>();
    String currentDate, currentTime;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("FairShareData");
    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("FairShareData");
    FairShareData fairShareData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fair_share, container, false);
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("FairShareData");
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fairShareData = new FairShareData();
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("fsid")) {
                    idFlag = 0;
                    Log.d("Inside ifds", "idFlag 0");
                } else {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        fscount.add(Integer.parseInt(ds.child("fsid").getValue().toString()));
                        idFlag = 1;
                        Log.d("Inside ifds", "idFlag 1");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        cname = (EditText) view.findViewById(R.id.contributorsName);
        camt = (EditText) view.findViewById(R.id.controAmount);
        addContro = (Button) view.findViewById(R.id.addControB);
        calculateFair = (Button) view.findViewById(R.id.calcFair);
        clearContro = (Button) view.findViewById(R.id.clearControB);
        addContro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cname.getText().toString().equals("") || camt.getText().toString().equals("")) {
                    if (cname.getText().toString().equals("")) {
                        cname.setError("Required");
                        cname.requestFocus();
                    }
                    if (camt.getText().toString().equals("")) {
                        camt.setError("Required");
                        camt.requestFocus();
                    }
                } else {
                    String cn = cname.getText().toString();
                    Float ca = Float.parseFloat(camt.getText().toString());
                    contribution.put(cn, ca);
                    Toast.makeText(getActivity(), "Contribution Added", Toast.LENGTH_SHORT).show();
                    cname.setText("");
                    camt.setText("");
                    cname.requestFocus();
                    flag = 1;
                }
            }
        });
        clearContro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contribution.clear();
                flag = 0;
                Toast.makeText(getActivity(), "Contribution Cleared", Toast.LENGTH_LONG).show();
            }
        });
        calculateFair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Calc Fair", Toast.LENGTH_LONG).show();
                System.out.println("Calc Fair");
                Log.d("Tag", "Calc Fair");
                if (flag == 1) {
                    if (idFlag == 0) {
                        fsid = 0;
                        Log.v("Inside if idFlag : ", "ID : " + fsid);
                    } else if (idFlag == 1) {
                        fsid = Collections.max(fscount);
                        Log.v("Inside else idFlag : ", "ID : " + fsid);
                    }
                    stringBuffer = new StringBuffer();
                    totalFair = 0.f;
                    perHead = 0.f;
                    totalContributors = 0;
                    //Toast.makeText(getActivity(),"MAX : "+fsid,Toast.LENGTH_SHORT).show();
                    for (Map.Entry<String, Float> entry : contribution.entrySet()) {
                        String key = entry.getKey();
                        Float value = entry.getValue();
                        totalFair = totalFair + value;
                        ++totalContributors;
                        perHead = totalFair / totalContributors;
                        stringBuffer.append(key + " " + value + "\n");
                    }
                    stringBuffer.append("Total Contributors : " + totalContributors);
                    stringBuffer.append("\nPer Head : " + perHead);
                    stringBuffer.append("\nTotal Spent : " + totalFair);
                    showMessage("FairShare", stringBuffer.toString());
                    currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    fairShareData.setFSID(++fsid);
                    fairShareData.setUserIDfk(userID);
                    fairShareData.setPerHead(perHead);
                    fairShareData.setTotalExpense(totalFair);
                    fairShareData.setTotalContributors(totalContributors);
                    fairShareData.setControInfo(contribution);
                    fairShareData.setTime(currentTime);
                    fairShareData.setDate(currentDate);
                    databaseReference.push().setValue(fairShareData);
                    Toast.makeText(getActivity(), "Fair Calculated", Toast.LENGTH_SHORT).show();
                    contribution.clear();
                    flag = 0;
                } else {
                    Toast.makeText(getActivity(), "No Contribution Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();
    }
}