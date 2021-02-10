package com.example.organdonationapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

// post mean model class which has all data and view holder for view
public class UserAdaptor extends FirebaseRecyclerAdapter<User, UserAdaptor.UserViewHolder> {
    Context context;
    EditText etDialogName,etDialogEmail,etDialogPhone,etDialogType,etDialogPassword;
    Button editBtnDialog;

    public UserAdaptor(@NonNull FirebaseRecyclerOptions<User> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {

        setTextInHolder(holder,model);

        editUser(holder,position,model);

        deleteUser(holder,position);

        callUser(holder,model);

    }

    private void callUser(UserViewHolder holder, User model) {
        holder.ivCallUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = model.getPhone();
                Uri num = Uri.parse("tel:" + number);
                Intent dial = new Intent(Intent.ACTION_DIAL);
                dial.setData(num);
                context.startActivity(dial);
            }
        });
    }

    private void deleteUser(UserViewHolder holder, int position) {
        holder.ivDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(getRef(position).getKey())
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    private void editUser(UserViewHolder holder,int position, User model) {
        holder.ivEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.edit_user_dialog))
                        .setGravity(Gravity.CENTER)
                        //.setMargin(50,0,50,0)
                        //.setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();

                View view = dialog.getHolderView();

                intializeAllFields(view);

                setTextInDialog(model);

                editBtnDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String , Object> map = new HashMap<>();
                        map.put("Name",etDialogName.getText().toString().trim());
                        map.put("Email",etDialogEmail.getText().toString().trim());
                        map.put("Password",etDialogPassword.getText().toString().trim());
                        map.put("Phone",etDialogPhone.getText().toString().trim());
                        map.put("Type",etDialogType.getText().toString().trim());


                        FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(getRef(position).getKey())
                                .updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });

                    }
                });

                dialog.show();
            }
        });

    }

    private void setTextInDialog(User model) {
        etDialogName.setText(model.getName());
        etDialogEmail.setText(model.getEmail());
        etDialogPassword.setText(model.getPassword());
        etDialogPhone.setText(model.getPhone());
        etDialogType.setText(model.getType());
    }

    private void intializeAllFields(View view) {
        etDialogName = (EditText) view.findViewById(R.id.etDialogName);
        etDialogEmail= (EditText) view.findViewById(R.id.etDialogEmail);
        etDialogPassword = (EditText) view.findViewById(R.id.etDialogPassword);
        etDialogPhone = (EditText) view.findViewById(R.id.etDialogPhone);
        etDialogType = (EditText) view.findViewById(R.id.etDialogType);
        editBtnDialog = (Button) view.findViewById(R.id.editBtnDialog);
    }

    private void setTextInHolder(UserViewHolder holder,User model) {
        holder.tvUserName.setText("Name: " + model.getName());
        holder.tvUserPhone.setText("Phone: " + model.getPhone());
        holder.tvUserEmail.setText("Email: " + model.getEmail());
        holder.tvUserPassword.setText("Password: " + model.getPassword());
        holder.tvUserType.setText("User Type: " + model.getType());
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_view,parent,false);
        return new UserViewHolder(view);
        // post view has our own view on recycler view
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        TextView tvUserName,tvUserPhone,tvUserEmail,tvUserPassword,tvUserType;
        ImageView ivEditUser,ivDeleteUser,ivCallUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserPhone = itemView.findViewById(R.id.tvUserPhone);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserPassword = itemView.findViewById(R.id.tvUserPassword);
            tvUserType = itemView.findViewById(R.id.tvUserType);
            ivDeleteUser = itemView.findViewById(R.id.ivDeleteUser);
            ivEditUser = itemView.findViewById(R.id.ivEditUser);
            ivCallUser = itemView.findViewById(R.id.ivCallUser);

        }
    }

}
