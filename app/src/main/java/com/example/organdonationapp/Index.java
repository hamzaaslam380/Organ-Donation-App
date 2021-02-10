package com.example.organdonationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

public class Index extends AppCompatActivity {
    LinearLayout llUser;
    TextView tvUser;
    ImageView ivUser,ivDonor,ivDoctor,ivContactUs,ivInfo;
    String getType,getName,getEmail,getPasword,getPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        getUserData();

        init();

        goToUserActivity();

        goToDonorActivity();

        goToDoctorActivity();

        goToRequestActivity();

        infoAboutApp();
    }

    private void goToRequestActivity() {
        ivContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index.this,ContactUsActivity.class);
                intent.putExtra("Type",getType);
                intent.putExtra("Email",getEmail);
                intent.putExtra("Phone",getPhone);
                startActivity(intent);
            }
        });
    }

    private void goToDoctorActivity() {
        ivDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index.this,DoctorActivity.class);
                intent.putExtra("Type",getType);
                startActivity(intent);
            }
        });
    }

    private void infoAboutApp() {
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.info_dialog))
                .setGravity(Gravity.CENTER)
                .create();

        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                View itemView = dialog.getHolderView();

                Button btnInfoClose;
                btnInfoClose = itemView.findViewById(R.id.btnInfoClose);

                btnInfoClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private void goToDonorActivity() {
        ivDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index.this,DonorActivity.class);
                intent.putExtra("Type",getType);
                startActivity(intent);
            }
        });
    }

    private void goToUserActivity() {
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index.this,UserActivity.class);
                intent.putExtra("Type",getType);
                intent.putExtra("Name",getName);
                intent.putExtra("Email",getEmail);
                intent.putExtra("Phone",getPhone);
                intent.putExtra("Password",getPasword);
                startActivity(intent);
            }
        });
    }

    private void getUserData() {
        Intent intent = getIntent();

        getType = intent.getStringExtra("Type");
        getName = intent.getStringExtra("Name");
        getEmail = intent.getStringExtra("Email");
        getPhone = intent.getStringExtra("Phone");
        getPasword = intent.getStringExtra("Password");
    }

    private void init() {
        tvUser = findViewById(R.id.tvUser);
        ivUser = findViewById(R.id.ivUser);
        ivDoctor = findViewById(R.id.ivDoctor);
        ivDonor = findViewById(R.id.ivDonor);
        llUser = findViewById(R.id.llUser);
        ivContactUs = findViewById(R.id.ivContactUs);
        ivInfo = findViewById(R.id.ivInfo);
    }
}