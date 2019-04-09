package com.example.nbamir.sda1.Accounts;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nbamir.sda1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    // UI references.
    private TextInputEditText mEmailView;
    private EditText mPasswordView;
    private EditText mIdView;
    private TextInputEditText mNameView;
    private TextInputEditText mSchoolView;
    private EditText mBatchView;
    private FirebaseAuth mAuth;
    RadioButton adm;
    RadioButton stu;
    RadioButton ots;
    private TextInputLayout mIdViewa;
    private TextInputLayout mNameViewa;
    private TextInputLayout mSchoolViewa;
    private TextInputLayout mBatchViewa;
    private TextView signIn;
    private Button signUp;
    private Button confirm;
    private Button change;

    String pas;
    String em;
    DatabaseReference myRef;
    DatabaseReference studRef;
    DatabaseReference adminRef;
    DatabaseReference outRef;
    DatabaseReference preStud;
    DatabaseReference preAdm;
    AccountMaker acc;
    String returnedEmail;
    String adminID = "";
    String studID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);

        mIdView = findViewById(R.id.id);
        mNameView = findViewById(R.id.name);
        mSchoolView = findViewById(R.id.school);
        mBatchView = findViewById(R.id.batch);
        signIn = findViewById(R.id.link_signin);
        signUp = findViewById(R.id.email_sign_up_button);
        confirm = findViewById(R.id.confirm);
        change = findViewById(R.id.change);


        mIdViewa = findViewById(R.id.id1);
        mNameViewa = findViewById(R.id.name1);
        mSchoolViewa = findViewById(R.id.school1);
        mBatchViewa = findViewById(R.id.batch1);


        mPasswordView.setVisibility(View.GONE);
        mNameViewa.setVisibility(View.GONE);
        mSchoolViewa.setVisibility(View.GONE);
        mBatchViewa.setVisibility(View.GONE);
        signUp.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
        change.setVisibility(View.GONE);


        stu = findViewById(R.id.student);
        adm = findViewById(R.id.admin);
        ots = findViewById(R.id.outs);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        studRef = myRef.child("Student");
        adminRef = myRef.child("Admin");
        outRef = myRef.child("Outsider");

        preStud = myRef.child("preStudents");
        preAdm = myRef.child("preAdmins");

        acc = new AccountMaker();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
    }


    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI

            if(dataSnapshot.exists()){
                returnedEmail = dataSnapshot.getValue(String.class).toString();
            }
            else {
                returnedEmail = "";
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.d("User Fetch Error", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    public void buttonOnClick(View view) {
        //Getting Email And Password from Text Fields
        em = mEmailView.getText().toString();
        pas = mPasswordView.getText().toString();

        switch (view.getId()) {


            case R.id.email_sign_up_button:
                mSchoolView.setEnabled(false);
                mEmailView.setEnabled(false);
                mIdView.setEnabled(false);
                adm.setEnabled(false);
                stu.setEnabled(false);
                ots.setEnabled(false);
                mNameView.setEnabled(false);
                mBatchView.setEnabled(false);
                mPasswordView.setEnabled(false);
                signUp.setVisibility(View.GONE);
                change.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);


                if(mPasswordView.getText().toString().trim().equals("")){
                    Toast.makeText(SignUpActivity.this, "Password Field Cannot Be Empty" ,
                            Toast.LENGTH_LONG).show();
                    break;
                }

                if (stu.isChecked()){
                    studID = mIdView.getText().toString();
                    preStud.child(studID).addValueEventListener(userListener);
                    if (returnedEmail != null && !(returnedEmail.trim().equals(em.trim()))){
                        Toast.makeText(SignUpActivity.this, "RegNo did not match Email" ,
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                else if (adm.isChecked()){
                    adminID = mIdView.getText().toString();
                    preAdm.child(adminID).addValueEventListener(userListener);
                    if ( returnedEmail != null && !(returnedEmail.trim().equals(em.trim()))){
                        Toast.makeText(SignUpActivity.this, "RegNo did not match Email" ,
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                }

                // Create User if Everything Everything passes
                mAuth.createUserWithEmailAndPassword(em, pas)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (stu.isChecked()) {
                                        acc.makeStudent(user.getUid(), mIdView.getText().toString(), mNameView.getText().toString(), Integer.parseInt(mBatchView.getText().toString()), mSchoolView.getText().toString(),em,pas,studRef);
                                    } else if (adm.isChecked()) {
                                        acc.makeAdmin(user.getUid(), mIdView.getText().toString(), mNameView.getText().toString(),em,pas,adminRef);
                                    } else {
                                        acc.makeOutsider(user.getUid(), mNameView.getText().toString(),em,pas,outRef);
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed. " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });
                break;

            case R.id.change:
                mSchoolView.setEnabled(true);
                mEmailView.setEnabled(true);
                mIdView.setEnabled(true);
                adm.setEnabled(true);
                stu.setEnabled(true);
                ots.setEnabled(true);
                mNameView.setEnabled(true);
                mBatchView.setEnabled(true);
                mPasswordView.setEnabled(true);
                signUp.setVisibility(View.VISIBLE);
                change.setVisibility(View.GONE);
                confirm.setVisibility(View.GONE);
                break;

            case R.id.confirm:

                if(mPasswordView.getText().toString().trim().equals("")){
                    Toast.makeText(SignUpActivity.this, "Password Field Cannot Be Empty" ,
                            Toast.LENGTH_LONG).show();
                    break;
                }
                if (stu.isChecked()){
                    studID = mIdView.getText().toString();
                    preStud.child(studID).addValueEventListener(userListener);
                    if (returnedEmail != null && !(returnedEmail.trim().equals(em.trim()))){
                        Toast.makeText(SignUpActivity.this, "RegNo did not match Email" ,
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                else if (adm.isChecked()){
                    adminID = mIdView.getText().toString();
                    preAdm.child(adminID).addValueEventListener(userListener);
                    if ( returnedEmail != null && !(returnedEmail.trim().equals(em.trim()))){
                        Toast.makeText(SignUpActivity.this, "RegNo did not match Email" ,
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                }

                // Create User if Everything Everything passes
                mAuth.createUserWithEmailAndPassword(em, pas)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (stu.isChecked()) {
                                        acc.makeStudent(user.getUid(), mIdView.getText().toString(), mNameView.getText().toString(), Integer.parseInt(mBatchView.getText().toString()), mSchoolView.getText().toString(),em,pas,studRef);
                                    } else if (adm.isChecked()) {
                                        acc.makeAdmin(user.getUid(), mIdView.getText().toString(), mNameView.getText().toString(),em,pas,adminRef);
                                    } else {
                                        acc.makeOutsider(user.getUid(), mNameView.getText().toString(),em,pas,outRef);
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUpActivity.this, "Authentication failed. " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });
                // Go to sign in if successful
                Intent successIntent = new Intent(this, SignInActivity.class);
                startActivity(successIntent);
                break;


            case R.id.student:
                mNameViewa.setVisibility(View.VISIBLE);
                mIdViewa.setVisibility(View.VISIBLE);
                mBatchViewa.setVisibility(View.VISIBLE);
                mSchoolViewa.setVisibility(View.VISIBLE);
                studID = mIdView.getText().toString();
                mPasswordView.setVisibility(View.VISIBLE);
                signUp.setVisibility(View.VISIBLE);
                break;

            case R.id.admin:
                mSchoolViewa.setVisibility(View.GONE);
                mBatchViewa.setVisibility(View.GONE);
                mNameViewa.setVisibility(View.VISIBLE);
                mIdViewa.setVisibility(View.VISIBLE);
                signUp.setVisibility(View.VISIBLE);
                mPasswordView.setVisibility(View.VISIBLE);
                adminID = mIdView.getText().toString();
                break;

            case R.id.outs:
                mSchoolViewa.setVisibility(View.GONE);
                mBatchViewa.setVisibility(View.GONE);
                mNameViewa.setVisibility(View.VISIBLE);
                mIdViewa.setVisibility(View.GONE);
                signUp.setVisibility(View.VISIBLE);
                break;
        }
    }
}
