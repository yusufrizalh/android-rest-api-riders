package id.inixindo.androidrestapi.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.inixindo.androidrestapi.R;
import id.inixindo.androidrestapi.adapters.AdapterRiders;
import id.inixindo.androidrestapi.apis.ApiRequestData;
import id.inixindo.androidrestapi.apis.RetrofitServer;
import id.inixindo.androidrestapi.models.ModelResponse;
import id.inixindo.androidrestapi.models.ModelRiders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewRiders;
    private RecyclerView.Adapter adapterRider;
    private RecyclerView.LayoutManager layoutManagerRider;
    private Toolbar toolbarMainActivity;
    private ProgressBar progressBarRiders;
    private SwipeRefreshLayout swipeRefreshRiders;
    private List<ModelRiders> listRiders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewRiders = findViewById(R.id.recyclerViewRiders);
        toolbarMainActivity = findViewById(R.id.toolbarMainActivity);
        progressBarRiders = findViewById(R.id.progressBarRiders);
        swipeRefreshRiders = findViewById(R.id.swipeRefreshRider);

        // Toolbar
        toolbarMainActivity.setTitle("Android REST API");
        toolbarMainActivity.setTitleTextColor(Color.WHITE);
        toolbarMainActivity.setBackgroundColor(Color.parseColor("#7bde21"));
        setSupportActionBar(toolbarMainActivity);

        // RecyclerView
        layoutManagerRider = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewRiders.setLayoutManager(layoutManagerRider);

        // swipe refresh
        swipeRefreshRiders.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshRiders.setRefreshing(false);
                        getAllRiders();
                    }
                }, 5000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllRiders();
    }

    private void getAllRiders() {
        progressBarRiders.setVisibility(View.VISIBLE);

        ApiRequestData apiRequestData = RetrofitServer.connectRetrofit().create(ApiRequestData.class);

        Call<ModelResponse> responseCall = apiRequestData.getAllData();
        responseCall.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                listRiders = response.body().getData();
                adapterRider = new AdapterRiders(MainActivity.this, listRiders);
                recyclerViewRiders.setAdapter(adapterRider);
                adapterRider.notifyDataSetChanged();
                progressBarRiders.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed connect to server!", Toast.LENGTH_SHORT).show();
                progressBarRiders.setVisibility(View.GONE);
            }
        });
    }
}