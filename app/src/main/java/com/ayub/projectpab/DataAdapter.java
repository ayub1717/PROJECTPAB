package com.ayub.projectpab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    private List<Data> dataList;
    private Context context;
    private MainActivity mainActivity;
    public DataAdapter(List<Data> userList, Context context) {
        this.dataList = userList;
        this.context = context;
        // Check if context is an instance of MainActivity and set mainActivity
        if (context instanceof MainActivity) {
            this.mainActivity = (MainActivity) context;
        }
    }
    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_component, parent,
                false);
        return new DataViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Data data = dataList.get(position);
        holder.nama_komponen.setText(data.getNama_komponen());
        holder.jenis.setText(data.getJenis());
        holder.merk.setText(data.getMerk());
        holder.detail.setText(data.getDetail());

        // Mengatur OnClickListener pada itemView untuk menangani tap pada item
        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mainActivity.showUpdateDialog(data);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity != null) {
                    mainActivity.showDetail(data);
                }
            }
        });


        // Mengatur OnClickListener pada tombol delete untuk menangani tap pada tombol delete
       holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(data.getId());
            }
        });

    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void showDeleteConfirmationDialog(final int data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete User");
        builder.setMessage("Are you sure you want to delete this user?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {deleteData(data);
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    // Metode untuk menghapus pengguna dari daftar dan server
    private void deleteData(int komponenId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.deleteData(komponenId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.get(i).getId() == komponenId) {
                            dataList.remove(i);
                            notifyItemRemoved(i);
                            break;
                        }}
                    Toast.makeText(context, "Data Komponen Berhasil Di Hapus", Toast.LENGTH_SHORT).show();
                    // Refresh user list after deletion
                    mainActivity.refreshData();
                } else {
                    Toast.makeText(context, "Gagal Menghapus Data Komponen: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Gagal Menghapus Data Komponen: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public class DataViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_komponen, jenis,merk, detail;
        public ImageView buttonDelete, buttonUpdate;
        public DataViewHolder(View itemView) {
            super(itemView);
            nama_komponen = itemView.findViewById(R.id.textViewKomponen);
            jenis = itemView.findViewById(R.id.textViewJenis);
            merk = itemView.findViewById(R.id.textViewMerk);
            detail = itemView.findViewById(R.id.textViewDetail);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
        }
    }
    // Metode untuk mengatur MainActivity yang terkait
    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}