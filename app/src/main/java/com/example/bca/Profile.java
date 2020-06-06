package com.example.bca;

import android.app.LauncherActivity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import static java.lang.Float.parseFloat;

public class Profile extends Fragment {
    Button logout, showAllDetails, updateToggle, cancel, setdata;
    Integer flag;
    ScrollView sv;
    LinearLayout ll, oginfo;
    EditText fullName, emailID, number, budget, ufn, uln, ub, um;
    TextView profiledetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        ufn = (EditText) view.findViewById(R.id.updatefirstname);
        uln = (EditText) view.findViewById(R.id.updatelastname);
        ub = (EditText) view.findViewById(R.id.updatebudget);
        um = (EditText) view.findViewById(R.id.updatemobile);
        setdata = (Button) view.findViewById(R.id.set);

        fullName = (EditText) view.findViewById(R.id.fullnameet);
        emailID = (EditText) view.findViewById(R.id.emailet);
        number = (EditText) view.findViewById(R.id.contactet);
        budget = (EditText) view.findViewById(R.id.mbet);

        updateToggle = (Button) view.findViewById(R.id.updatehs);
        ll = (LinearLayout) view.findViewById(R.id.profileid);
        oginfo = (LinearLayout) view.findViewById(R.id.ogdata);
        cancel = (Button) view.findViewById(R.id.cancelb);

        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (userID.equals(ds.child("userIDOriginal").getValue().toString())) {
                        fullName.setText(ds.child("userFirstName").getValue().toString() + " " + ds.child("userLastName").getValue().toString());
                        emailID.setText(ds.child("userEmail").getValue().toString());
                        number.setText(ds.child("userContact").getValue().toString());
                        budget.setText(ds.child("userMonthlyBudget").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        sv = (ScrollView) view.findViewById(R.id.scrolldetails);
        profiledetails = (TextView) view.findViewById(R.id.alldeatils);
        showAllDetails = (Button) view.findViewById(R.id.allinfo);
        showAllDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showAllDetails.getText().equals("Show All Details")) {
                    showAllDetails.setText("Hide All Details");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (userID.equals(ds.child("userIDOriginal").getValue().toString())) {
                                    profiledetails.setText("Personal Data\n"
                                            + "\nFirst Name : " + ds.child("userFirstName").getValue().toString()
                                            + "\nLast Name : " + ds.child("userLastName").getValue().toString()
                                            + "\nContact : " + ds.child("userContact").getValue().toString()
                                            + "\nMonthly Budget : " + ds.child("userMonthlyBudget").getValue().toString()
                                            + "\nEmail ID : " + ds.child("userEmail").getValue().toString()
                                            + "\nPassword : " + ds.child("userPassword").getValue().toString()
                                            + "\nRegistered on : " + ds.child("userDate").getValue().toString()
                                            + "\nRegistered at : " + ds.child("userTime").getValue().toString());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    sv.setVisibility(View.VISIBLE);
                } else if (showAllDetails.getText().equals("Hide All Details")) {
                    showAllDetails.setText("Show All Details");
                    sv.setVisibility(View.GONE);
                }
            }
        });
        updateToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateToggle.getText().equals("Update")) {
                    updateToggle.setText("Update Profile");
                    oginfo.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                } else if (updateToggle.getText().equals("Update Profile")) {
                    updateToggle.setText("Update");
                    oginfo.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
            }
        });
        setdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ufn.getText().toString().equals("") || uln.getText().toString().equals("") ||
                        ub.getText().toString().equals("") || um.getText().toString().equals("")) {
                    flag = 0;
                    if (ufn.getText().toString().equals("")) {
                        ufn.setError("Required");
                    }
                    if (uln.getText().toString().equals("")) {
                        uln.setError("Required");
                    }
                    if (ub.getText().toString().equals("")) {
                        ub.setError("Required");
                    }
                    if (um.getText().toString().equals("")) {
                        um.setError("Required");
                    }
                } else if (!ub.getText().toString().equals("") || !um.getText().toString().equals("")) {
                    flag = 0;
                    if (Float.parseFloat(ub.getText().toString()) < 2000) {
                        ub.setError("Budget cannot be less than Rs 2000");
                    }
                    if (um.length() != 10) {
                        um.setError("Number Digits should be of 10 digits");
                    }
                }
                if (!ufn.getText().toString().equals("") && !uln.getText().toString().equals("") &&
                        !ub.getText().toString().equals("") && !um.getText().toString().equals("") &&
                        Float.parseFloat(ub.getText().toString()) >= 2000 && um.length() == 10) {
                    final String newfn = ufn.getText().toString();
                    final String newln = uln.getText().toString();
                    final String newb = ub.getText().toString();
                    final String newn = um.getText().toString();
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (userID.equals(ds.child("userIDOriginal").getValue().toString())) {
                                    ds.child("userFirstName").getRef().setValue(newfn);
                                    ds.child("userLastName").getRef().setValue(newln);
                                    ds.child("userContact").getRef().setValue(newn);
                                    ds.child("userMonthlyBudget").getRef().setValue(newb);
                                    Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_LONG).show();
                                    ufn.setText("");
                                    uln.setText("");
                                    ub.setText("");
                                    um.setText("");
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    oginfo.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
                //Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_LONG).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateToggle.setText("Update");
                oginfo.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
            }
        });
        logout = (Button) view.findViewById(R.id.logoutbutton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), BCALauncher.class);
                    startActivity(intent);
                }
                //getActivity().finish();
            }
        });
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}