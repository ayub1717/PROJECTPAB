package com.ayub.projectpab;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface ApiService {
    @POST("input_data.php")
    Call<Void> insertKomponen(@Body Data data);
    @GET("get_data.php")
    Call<List<Data>> getData();
    @PUT("update_data.php")
    Call<Void> updateData(@Body Data data);
    @DELETE("delete_data.php/{id}")
    Call<Void> deleteData(@Path("id") int id);

}
