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

public class FairShareList extends Fragment {
    TextView listfs;
    EditText deleteid;
    Button delete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fair_share_list, container, false);
        listfs = (TextView) view.findViewById(R.id.fslist);
        listfs.append("Fair Share History\n\n\n");
        deleteid = (EditText) view.findViewById(R.id.fsdelid);
        delete = (Button) view.findViewById(R.id.fsdelbutid);
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dr1 = FirebaseDatabase.getInstance().getReference().child("FairShareData");
        dr1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!listfs.getText().equals("")) {
                    listfs.setText("");
                    listfs.append("Fair Share History\n\n\n");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (userID.equals(ds.child("userIDfk").getValue())) {
                            for (DataSnapshot innerds : ds.child("controInfo").getChildren()) {
                                listfs.append("\nRs " + innerds.getValue() + " spent by " + innerds.getKey());
                                //listfs.append("\n"+innerds.getKey()+"\t"+innerds.getValue());
                                //stringBuffer.append("\nName : "+innerds.getKey()+" Contro : "+innerds.getValue());
                            }
                            listfs.append("\nTotal Expense : " + ds.child("totalExpense").getValue()
                                    + "\nPer Head : " + ds.child("perHead").getValue()
                                    + "\nContributors : " + ds.child("totalContributors").getValue()
                                    + "\nDate : " + ds.child("date").getValue()
                                    + "\nTime : " + ds.child("time").getValue()
                                    + "\nID : " + ds.child("fsid").getValue()
                                    + "\n\n");
                        /*stringBuffer.append(ds.child("totalExpense").getValue()+"\n"
                        +ds.child("perHead").getValue()+"\n"
                        +ds.child("totalContributors"));*/
                        }
                        //stringBuffer.append(ds.child("userIDfk").getValue()+"\n\n");
                        //stringBuffer.append(ds.getKey()+" "+ds.getValue()+"\n");
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
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("FairShareData");
                    dr.orderByChild("fsid").equalTo(Integer.parseInt(deleteid.getText().toString())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                Toast.makeText(getActivity(), "No Records Found", Toast.LENGTH_LONG).show();
                            } else {
                                for (DataSnapshot fairsharesnapshot : dataSnapshot.getChildren()) {
                                    if (userID.equals(fairsharesnapshot.child("user̥IDfk").getValue())) {
                                        Log.v("ID : ", "" + fairsharesnapshot.toString());
                                        fairsharesnapshot.getRef().removeValue();
                                        Toast.makeText(getActivity(), "Record Removed", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Record Not Found", Toast.LENGTH_LONG).show();
                                    }
                                    deleteid.setText("");
                                }
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