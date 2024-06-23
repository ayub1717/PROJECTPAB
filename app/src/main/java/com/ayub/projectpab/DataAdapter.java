package com.ayub.projectpab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.UserViewHolder> {
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
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_component, parent,
                false);
        return new UserViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Data data = dataList.get(position);
        holder.nama_komponen.setText(data.getNama_komponen());
        holder.jenis.setText(data.getJenis());
        holder.merek.setText(data.getMerek());
        holder.detail.setText(data.getDetail());

        // Mengatur OnClickListener pada itemView untuk menangani tap pada item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity != null) {
                    mainActivity.showUpdateDialog(data);
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

    private void showDeleteConfirmationDialog(final int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete User");
        builder.setMessage("Are you sure you want to delete this user?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {deleteUser(userId);
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    // Metode untuk menghapus pengguna dari daftar dan server
    private void deleteUser(int userId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.deleteData(userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.get(i).getId() == userId) {
                            dataList.remove(i);
                            notifyItemRemoved(i);
                            break;
                        }}
                    Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
                    // Refresh user list after deletion
                    mainActivity.refreshData();
                } else {
                    Toast.makeText(context, "Failed to delete user: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failed to delete user: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    // ViewHolder untuk UserAdapter
    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_komponen, jenis,merek, detail;
        public Button buttonDelete;
        public UserViewHolder(View itemView) {
            super(itemView);
            nama_komponen = itemView.findViewById(R.id.textViewKomponen);
            jenis = itemView.findViewById(R.id.textViewJenis);
            merek = itemView.findViewById(R.id.textViewMerek);
            detail = itemView.findViewById(R.id.textViewDetail);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
    // Metode untuk mengatur MainActivity yang terkait
    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}