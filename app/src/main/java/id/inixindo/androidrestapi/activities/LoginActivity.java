package id.inixindo.androidrestapi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.inixindo.androidrestapi.R;
import id.inixindo.androidrestapi.apis.ApiRequestData;
import id.inixindo.androidrestapi.apis.RetrofitServer;
import id.inixindo.androidrestapi.models.ModelResponseUsers;
import id.inixindo.androidrestapi.models.ModelUsers;
import id.inixindo.androidrestapi.utils.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;
    private int id;
    private String JSON_STRING;
    private List<ModelUsers> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        usersList = new ArrayList<>();

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            if (editTextEmail.getText().toString().length() == 0) {
                editTextEmail.setError("Email address cannot be blanK!");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches()) {
                editTextEmail.setError("Invalid email address!");
            } else if (editTextPassword.getText().toString().length() == 0) {
                editTextPassword.setError("Password cannot be blank!");
            } else {
                showUser();
            }
        }
    }

    private void showUser() {
        ApiRequestData apiRequestData = RetrofitServer.connectRetrofit().create(ApiRequestData.class);
        Call<ModelResponseUsers> responseUsersCall = apiRequestData.getAllUsers();

        responseUsersCall.enqueue(new Callback<ModelResponseUsers>() {
            @Override
            public void onResponse(Call<ModelResponseUsers> call, Response<ModelResponseUsers> response) {
                if (response.isSuccessful()) {
                    usersList = response.body().getData();
                    Boolean loginSuccess = false;

                    for (int i = 0; i < usersList.size(); i++) {
                        if (editTextEmail.getText().toString().equals(usersList.get(i).getEmail()) &&
                                editTextPassword.getText().toString().equals(usersList.get(i).getPassword())) {
                            id = usersList.get(i).getId();
                            loginSuccess = true;
                        }
                    }

                    if (loginSuccess) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra(Config.TAG_ID, id);
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, "Success to Login!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelResponseUsers> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failed connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}