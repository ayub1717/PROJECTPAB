package com.ayub.projectpab;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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
    private DataAdapter dataAdapter;
    private List<Data> dataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataAdapter = new DataAdapter(dataList, this);
        recyclerView.setAdapter(dataAdapter);
        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUserDialog();
            }
        });
        fetchData();
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add User");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_data, null);
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
        Data data = new Data(name, email, NIK, NIM);
        Call<Void> call = apiService.insertData(data);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                    fetchData();
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
    private void fetchData() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Data>> call = apiService.getData();
        call.enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response)
            {
                if (response.isSuccessful()) {
                    dataList.clear();
                    dataList.addAll(response.body());
                    dataAdapter.notifyDataSetChanged();
                } else {
                    Log.e("MainActivity", "Response error: " + response.errorBody().toString());
                    Toast.makeText(MainActivity.this, "Failed to fetch Data Komponen: "
                            + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Data>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch Data Komponen" + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser(int id, String name, String email, String NIK, String NIM) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Data data = new Data(id, name, email, NIK, NIM);
        Call<Void> call = apiService.updateData(data);

        Log.d("MainActivity", "Updating user: " + id + ", " + name + ", " + email +", " + NIK + ", " + NIM);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("MainActivity", "User updated successfully");
                    Toast.makeText(MainActivity.this, "User updated successfully",
                            Toast.LENGTH_SHORT).show();
                    fetchData();  // Refresh user list
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
    public void showUpdateDialog(final Data data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update User");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_update_data,
                (ViewGroup) findViewById(android.R.id.content), false);
        final EditText inputKomponen = viewInflated.findViewById(R.id.editTextName);
        final EditText inputJenis = viewInflated.findViewById(R.id.editTextEmail);
        final EditText inputMerek = viewInflated.findViewById(R.id.editTextNik);
        final EditText inputDetail = viewInflated.findViewById(R.id.editTextNim);

        inputKomponen.setText(data.getNama_komponen());
        inputJenis.setText(data.getJenis());
        inputMerek.setText(data.getMerek());
        inputDetail.setText(data.getDetail());

        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String komponen = inputKomponen.getText().toString();
                String jenis = inputJenis.getText().toString();
                String merek = inputMerek.getText().toString();
                String detail = inputDetail.getText().toString();
                updateUser(data.getId(), komponen, jenis, merek, detail);
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
        fetchData();
    }
}