package id.inixindo.androidrestapi.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewRiders;
    private RecyclerView.Adapter adapterRider;
    private RecyclerView.LayoutManager layoutManagerRider;
    private Toolbar toolbarMainActivity;
    private ProgressBar progressBarRiders;
    private SwipeRefreshLayout swipeRefreshRiders;
    private List<ModelRiders> listRiders = new ArrayList<>();

    private FloatingActionButton fabAddRider;

    // Replace the below with your own ONESIGNAL_APP_ID
    private static final String ONESIGNAL_APP_ID = "#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewRiders = findViewById(R.id.recyclerViewRiders);
        toolbarMainActivity = findViewById(R.id.toolbarMainActivity);
        progressBarRiders = findViewById(R.id.progressBarRiders);
        swipeRefreshRiders = findViewById(R.id.swipeRefreshRider);
        fabAddRider = findViewById(R.id.fabAddRider);

        // Toolbar
        toolbarMainActivity.setTitle("Android REST API");
        toolbarMainActivity.setTitleTextColor(Color.WHITE);
        toolbarMainActivity.setBackgroundColor(Color.parseColor("#303F9F"));
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

        // floating action button
        fabAddRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddRiderActivity.class));
            }
        });

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllRiders();
    }

    public void getAllRiders() {
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