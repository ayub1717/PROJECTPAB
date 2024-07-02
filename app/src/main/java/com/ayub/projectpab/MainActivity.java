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
                showAddKomponenDialog();
            }
        });
        fetchData();
    }

    private void showAddKomponenDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Data Komponen");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_data, null);
        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextJenis = view.findViewById(R.id.editTextJenis);
        final EditText editTextMerk = view.findViewById(R.id.editTextMerk);
        final EditText editTextDetail = view.findViewById(R.id.editTextDetail);
        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString();
                String jenis = editTextJenis.getText().toString();
                String merk = editTextMerk.getText().toString();
                String detail = editTextDetail.getText().toString();
                addData(name, jenis, merk, detail);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
    private void addData(String name, String jenis, String merk, String detail) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Data data = new Data(name, jenis, merk, detail);
        Call<Void> call = apiService.insertData(data);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Data Komponen Berhasil Di Tambahkan", Toast.LENGTH_SHORT).show();
                    fetchData();
                } else {
                    Toast.makeText(MainActivity.this, "Gagal Menambahkan Data Komponen: " + response.message(), Toast.LENGTH_SHORT).show();
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

    private void updateUser(int id, String nama_komponen, String jenis, String merk, String detail) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Data data = new Data(id, nama_komponen, jenis, merk, detail);
        Call<Void> call = apiService.updateData(data);

        Log.d("MainActivity", "Update Data Komponen: " + id + ", " + nama_komponen + ", " + jenis +", " + merk + ", " + detail);
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
        findViewById(R.id.buttonUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void showUpdateDialog(final Data data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update User");
        View view = getLayoutInflater().inflate(R.layout.dialog_update_data, null);
        final EditText inputKomponen = view.findViewById(R.id.editTextName);
        final EditText inputJenis = view.findViewById(R.id.editTextJenis);
        final EditText inputMerk = view.findViewById(R.id.editTextMerk);
        final EditText inputDetail = view.findViewById(R.id.editTextDetail);

        inputKomponen.setText(data.getNama_komponen());
        inputJenis.setText(data.getJenis());
        inputMerk.setText(data.getMerk());
        inputDetail.setText(data.getDetail());

        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String komponen = inputKomponen.getText().toString();
                String jenis = inputJenis.getText().toString();
                String merk = inputMerk.getText().toString();
                String detail = inputDetail.getText().toString();
                updateUser(data.getId(), komponen, jenis, merk, detail);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();

    }
   public void showDetail(final Data data){

       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle("Detail Data");
       View view = getLayoutInflater().inflate(R.layout.detail_komponen, null);
       final EditText inputKomponen = view.findViewById(R.id.editTextNamaKomponen);
       final EditText inputDetail = view.findViewById(R.id.editTextDetail);
       inputDetail.setText(data.getDetail());
       inputKomponen.setText(data.getNama_komponen());
       builder.setView(view);

       builder.setNegativeButton("Cancel", null);
       builder.create().show();

   }

    public void refreshData() {
        fetchData();
    }
}