package com.example.bca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Register extends AppCompatActivity {
    EditText firstName, lastName, email, password, contact, monthlyBudget;
    Button registerButton;
    FirebaseAuth fAuth;
    //FirebaseFirestore fStore;
    Integer flag = 0;
    String userID, currentTime, currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.emailIDr);
        password = (EditText) findViewById(R.id.passwordr);
        contact = (EditText) findViewById(R.id.contact);
        monthlyBudget = (EditText) findViewById(R.id.monthlybudget);
        registerButton = (Button) findViewById(R.id.registerbutton);
        final UserData userData = new UserData();
        fAuth = FirebaseAuth.getInstance();
        //fStore = FirebaseFirestore.getInstance();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Button Clicked",Toast.LENGTH_LONG).show();
                final String emailid = email.getText().toString();
                final String pw = password.getText().toString();
                final String fn = firstName.getText().toString();
                final String ln = lastName.getText().toString();
                final String number = contact.getText().toString();
                final String mb = monthlyBudget.getText().toString();
                if (emailid.equals("") || pw.equals("") || fn.equals("") || ln.equals("") || number.equals("") || mb.equals("")) {
                    if (TextUtils.isEmpty(fn)) {
                        firstName.setError("Required");
                    }
                    if (TextUtils.isEmpty(ln)) {
                        lastName.setError("Required");
                    }
                    if (TextUtils.isEmpty(number)) {
                        contact.setError("Required");
                    }
                    if (TextUtils.isEmpty(emailid)) {
                        email.setError("Required");
                    }
                    if (TextUtils.isEmpty(pw)) {
                        password.setError("Required");
                    }
                    if (TextUtils.isEmpty(mb)) {
                        monthlyBudget.setError("Required");
                    }
                    flag = 0;
                } else if (!emailid.equals("") && !pw.equals("") && !fn.equals("") && !ln.equals("") && !number.equals("") && !mb.equals("")) {
                    if (Float.parseFloat(mb) < 2000) {
                        monthlyBudget.setError("Budget cannot be less than Rs 2000");
                        flag = 0;
                    }
                    if (pw.length() < 7) {
                        password.setError("Password must be >=7 characters");
                        flag = 0;
                    }
                    if (number.length() != 10) {
                        contact.setError("Number Digits should be of 10 digits");
                        flag = 0;
                    }
                }
                if (!emailid.equals("") && !pw.equals("") && !fn.equals("") && !ln.equals("") && !number.equals("") && !mb.equals("")
                        && Float.parseFloat(mb) >= 2000 && pw.length() >= 7 && number.length() == 10) {
                    flag = 1;
                }
                if (flag == 1) {
                    Toast.makeText(getApplicationContext(), "Registration in Process", Toast.LENGTH_LONG).show();
                    fAuth.createUserWithEmailAndPassword(emailid, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                userID = fAuth.getCurrentUser().getUid();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                                userData.setUserFirstName(fn);
                                userData.setUserLastName(ln);
                                userData.setUserEmail(emailid);
                                userData.setUserPassword(pw);
                                userData.setUserContact(number);
                                userData.setUserMonthlyBudget(mb);
                                userData.setUserDate(currentDate);
                                userData.setUserTime(currentTime);
                                userData.setUserIDOriginal(userID);
                                databaseReference.push().setValue(userData);
                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                                email.setText("");
                                firstName.setText("");
                                lastName.setText("");
                                contact.setText("");
                                password.setText("");
                                monthlyBudget.setText("");
                                firstName.requestFocus();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Registration Unsuccessful", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    flag = 0;
                }
            }
        });
    }
}