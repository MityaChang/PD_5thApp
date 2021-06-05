package com.example.dailybudget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
     RecyclerView recyclerView;
     FloatingActionButton fab;

    //Firebase..
     FirebaseAuth mAuth;
     DatabaseReference mDatabase;

    //globar variable..

     String title;
     String description;
     String budget;
     String post_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("AllData").child(uid);


        //Recycler view..

        recyclerView = findViewById(R.id.recyclerId);

        //positioning item views within a RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //floating button

        fab = findViewById(R.id.btnAdd);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();

            }
        });
    }

    public void addData() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(this);
        //instantiate the contents of layout XML files into the alertDialog Builder
        LayoutInflater inflater = LayoutInflater.from(this);
        View myview = inflater.inflate(R.layout.inputbudget, null);
        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();

        final EditText mTitle = myview.findViewById(R.id.title);
        final EditText mDescription = myview.findViewById(R.id.description);
        final EditText mBudget = myview.findViewById(R.id.budget);
        Button btnSave = myview.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitle.getText().toString().trim();
                String description = mDescription.getText().toString().trim();
                String budget = mBudget.getText().toString().trim();

                if (TextUtils.isEmpty(title)) {
                    mTitle.setError("Required Field!");
                    return;
                }
                if (TextUtils.isEmpty(description)) {
                    mDescription.setError("Required Field!");
                    return;
                }
                if (TextUtils.isEmpty(budget)) {
                    mBudget.setError("Required Field!");
                    return;
                }

                String mDate = DateFormat.getDateInstance().format(new Date());
                String id = mDatabase.push().getKey();
                DataCenter data = new DataCenter(title, description, budget, id, mDate);
                mDatabase.child(id).setValue(data);

                Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

        dialog.show();


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<DataCenter> options = new FirebaseRecyclerOptions.Builder<DataCenter>().setQuery(mDatabase, DataCenter.class).setLifecycleOwner(this).build();
        FirebaseRecyclerAdapter<DataCenter, MyViewHolder> adapter = new FirebaseRecyclerAdapter<DataCenter, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull DataCenter model) {
                holder.setTitle(model.getTitle());
                holder.setDescription(model.getDescription());
                holder.setBudget(model.getBudget());
                holder.setDate(model.getDate());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key = getRef(position).getKey();
                        title = model.getTitle();
                        description = model.getDescription();
                        budget = model.getBudget();
                        upDateData();
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem, parent, false);
                return new HomeActivity.MyViewHolder(view);

            }
        };
        recyclerView.setAdapter(adapter);

    }

    // Use recycler view to display a large set amount of data
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView mTitle = mView.findViewById(R.id.cardTitle);
            mTitle.setText(title);
        }

        public void setDescription(String description) {
            TextView mDescription = mView.findViewById(R.id.cardDesc);
            mDescription.setText(description);
        }

        public void setBudget(String budget) {
            TextView mBudget = mView.findViewById(R.id.cardBudget);
            mBudget.setText("$" + budget);
        }

        public void setDate(String date) {
            TextView mDate = mView.findViewById(R.id.cardDate);
            mDate.setText(date);
        }
    }


    public void upDateData() {
//Creating alert dialog instead of going next page
        AlertDialog.Builder mydialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myview = inflater.inflate(R.layout.updatebudget, null);
        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();

        final EditText mTitle = myview.findViewById(R.id.titleUpdate);
        final EditText mDescription = myview.findViewById(R.id.descUpdate);
        final EditText mBudget = myview.findViewById(R.id.budgetUpdate);
        Button btnUpdate = myview.findViewById(R.id.btnUpdateUp);
        Button btnDelete = myview.findViewById(R.id.btnDeleteUpdate);

        //we need to set our server data inside edit text..

        mTitle.setText(title);
        mTitle.setSelection(title.length());

        mDescription.setText(description);
        mDescription.setSelection(description.length());

        mBudget.setText(budget);
        mBudget.setSelection(budget.length());


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = mTitle.getText().toString().trim();
                description = mDescription.getText().toString().trim();
                budget = mBudget.getText().toString().trim();

                String mDate = DateFormat.getDateInstance().format(new Date());

                DataCenter data = new DataCenter(title, description, budget, post_key, mDate);
                mDatabase.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });


        dialog.show();

    }


}