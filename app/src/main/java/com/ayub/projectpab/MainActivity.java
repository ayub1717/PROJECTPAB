package com.ayub.projectpab;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(userList, this);
        recyclerView.setAdapter(userAdapter);
        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUserDialog();
            }
        });
        fetchUsers();
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add User");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_user, null);
        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        final EditText editTextNik = view.findViewById(R.id.editTextNik);
        final EditText editTextNim = view.findViewById(R.id.editTextNim);
        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String NIK = editTextNik.getText().toString();
                String NIM = editTextNim.getText().toString();
                addUser(name, email, NIK, NIM);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
    private void addUser(String name, String email, String NIK, String NIM) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        User user = new User(name, email, NIK, NIM);
        Call<Void> call = apiService.insertUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                    fetchUsers();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add user: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to add user",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchUsers() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<User>> call = apiService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response)
            {
                if (response.isSuccessful()) {
                    userList.clear();
                    userList.addAll(response.body());
                    userAdapter.notifyDataSetChanged();
                } else {
                    Log.e("MainActivity", "Response error: " + response.errorBody().toString());
                    Toast.makeText(MainActivity.this, "Failed to fetch users: "
                            + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch users" + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser(int id, String name, String email, String NIK, String NIM) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        User user = new User(id, name, email, NIK, NIM);
        Call<Void> call = apiService.updateUser(user);

        Log.d("MainActivity", "Updating user: " + id + ", " + name + ", " + email +", " + NIK + ", " + NIM);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("MainActivity", "User updated successfully");
                    Toast.makeText(MainActivity.this, "User updated successfully",
                            Toast.LENGTH_SHORT).show();
                    fetchUsers();  // Refresh user list
                } else {
                    Log.e("MainActivity", "Response error: " + response.errorBody().toString());
                    Toast.makeText(MainActivity.this, "Failed to update user"+ response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MainActivity", "Fetch error: ", t);
                Toast.makeText(MainActivity.this, "Failed to update user" + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showUpdateDialog(final User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update User");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_update_user,
                (ViewGroup) findViewById(android.R.id.content), false);
        final EditText inputName = viewInflated.findViewById(R.id.editTextName);
        final EditText inputEmail = viewInflated.findViewById(R.id.editTextEmail);
        final EditText inputNik = viewInflated.findViewById(R.id.editTextNik);
        final EditText inputNim = viewInflated.findViewById(R.id.editTextNim);

        inputName.setText(user.getName());
        inputEmail.setText(user.getEmail());
        inputNik.setText(user.getNik());
        inputNim.setText(user.getNim());

        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String name = inputName.getText().toString();
                String email = inputEmail.getText().toString();
                String Nik = inputNik.getText().toString();
                String Nim = inputNim.getText().toString();
                updateUser(user.getId(), name, email, Nik, Nim);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void refreshData() {
        fetchUsers();
    }
}