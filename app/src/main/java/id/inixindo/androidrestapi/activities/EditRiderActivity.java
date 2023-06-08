package id.inixindo.androidrestapi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import id.inixindo.androidrestapi.R;
import id.inixindo.androidrestapi.apis.ApiRequestData;
import id.inixindo.androidrestapi.apis.RetrofitServer;
import id.inixindo.androidrestapi.models.ModelResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRiderActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextNama, editTextNomor, editTextSponsor, editTextNegara;
    private Button btnUpdate;
    private Toolbar toolbarEditRiderActivity;
    private String xID, xNama, xNomor, xSponsor, xNegara;
    private String nama, nomor, sponsor, negara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rider);

        editTextNama = findViewById(R.id.editTextNama);
        editTextNomor = findViewById(R.id.editTextNomor);
        editTextSponsor = findViewById(R.id.editTextSponsor);
        editTextNegara = findViewById(R.id.editTextNegara);
        btnUpdate = findViewById(R.id.btnUpdate);
        toolbarEditRiderActivity = findViewById(R.id.toolbarEditRiderActivity);

        toolbarEditRiderActivity.setTitle("Edit Rider");
        toolbarEditRiderActivity.setTitleTextColor(Color.WHITE);
        toolbarEditRiderActivity.setBackgroundColor(Color.parseColor("#303F9F"));
        setSupportActionBar(toolbarEditRiderActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent retrieveIntent = getIntent();
        xID = retrieveIntent.getStringExtra("xID");
        xNama = retrieveIntent.getStringExtra("xNama");
        xNomor = retrieveIntent.getStringExtra("xNomor");
        xSponsor = retrieveIntent.getStringExtra("xSponsor");
        xNegara = retrieveIntent.getStringExtra("xNegara");

        editTextNama.setText(xNama);
        editTextNomor.setText(xNomor);
        editTextSponsor.setText(xSponsor);
        editTextNegara.setText(xNegara);

        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnUpdate) {
            nama = editTextNama.getText().toString();
            nomor = editTextNomor.getText().toString();
            sponsor = editTextSponsor.getText().toString();
            negara = editTextNegara.getText().toString();

            // validasi
            if (nama.trim().equals("")) {
                editTextNama.setError("Nama Rider tidak boleh kosong!");
            } else if (nomor.trim().equals("")) {
                editTextNomor.setError("Nomor Rider tidak boleh kosong!");
            } else if (sponsor.trim().equals("")) {
                editTextSponsor.setError("Sponsor Rider tidak boleh kosong!");
            } else if (negara.trim().equals("")) {
                editTextNegara.setError("Negara Rider tidak boleh kosong!");
            } else {
                updateRider();
            }
        }
    }

    private void updateRider() {
        ApiRequestData apiRequestData = RetrofitServer.connectRetrofit().create(ApiRequestData.class);
        Call<ModelResponse> responseCall = apiRequestData.updateData(xID, nama, nomor, sponsor, negara);

        responseCall.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                int status = response.body().getStatus();
                String message = response.body().getMessage();

                if (status == 1) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed connect to server!", Toast.LENGTH_SHORT).show();
            }
        });

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}