package com.example.nbamir.sda1.Accounts;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nbamir.sda1.EventBoard.EventBoardActivity;
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


public class SignInActivity extends AppCompatActivity {

    TextView mEmailView,mSignUpView;
    TextView mPasswordView;

    String password;
    String email;

    private FirebaseAuth mAuth;
    DatabaseReference myRef;
    DatabaseReference studRef;
    DatabaseReference adminRef;
    DatabaseReference outRef;


    Intent i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mSignUpView=findViewById(R.id.link_signup);

        i = new Intent(this, EventBoardActivity.class);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        studRef = myRef.child("Student");
        adminRef= myRef.child("Admin");
        outRef = myRef.child("Outsider");

        mSignUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void buttonOnClick(View view) {

        switch (view.getId()) {
            case R.id.email_sign_in_button:

                email = mEmailView.getText().toString();
                password = mPasswordView.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(this,"Please enter your email.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(this,"Please enter your password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
//                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    if (user!=null) {
//                                        finish();
//                                        startActivity(i);
//                                    }
                                    startActivity(i);

//                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "signInWithEmail:failure", task.getException());

                                    Toast.makeText(SignInActivity.this, "Authentication failed." + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                }

                                // ...
                            }
                        });
                break;

        }
    }

}
