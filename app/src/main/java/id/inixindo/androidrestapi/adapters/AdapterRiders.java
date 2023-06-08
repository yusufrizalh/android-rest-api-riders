package id.inixindo.androidrestapi.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.inixindo.androidrestapi.R;
import id.inixindo.androidrestapi.activities.EditRiderActivity;
import id.inixindo.androidrestapi.activities.MainActivity;
import id.inixindo.androidrestapi.apis.ApiRequestData;
import id.inixindo.androidrestapi.apis.RetrofitServer;
import id.inixindo.androidrestapi.models.ModelResponse;
import id.inixindo.androidrestapi.models.ModelRiders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterRiders extends RecyclerView.Adapter<AdapterRiders.HolderRider> {
    private Context context;
    private List<ModelRiders> listRiders;

    public AdapterRiders(Context context, List<ModelRiders> listRiders) {
        this.context = context;
        this.listRiders = listRiders;
    }

    @NonNull
    @Override
    public HolderRider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_rider, parent, false);
        HolderRider holderRider = new HolderRider(view);
        return holderRider;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRider holder, int position) {
        ModelRiders modelRiders = listRiders.get(position);
        holder.textViewId.setText(String.valueOf(modelRiders.getId()));
        holder.textViewNama.setText(modelRiders.getNama());
        holder.textViewNomor.setText(modelRiders.getNomor());
        holder.textViewSponsor.setText(modelRiders.getSponsor());
        holder.textViewNegara.setText(modelRiders.getNegara());
        Glide.with(context).load(modelRiders.getFoto())
                .placeholder(R.drawable.icon_person_gray_32)
                .into(holder.imageViewFoto);
    }

    @Override
    public int getItemCount() {
        if (listRiders != null) {
            return listRiders.size();
        } else {
            Toast.makeText(context, "Data is not available!", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public class HolderRider extends RecyclerView.ViewHolder {
        ImageView imageViewFoto;
        TextView textViewId, textViewNama, textViewNomor, textViewSponsor, textViewNegara;

        public HolderRider(@NonNull View itemView) {
            super(itemView);

            imageViewFoto = itemView.findViewById(R.id.imageViewRider);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewNama = itemView.findViewById(R.id.textViewNama);
            textViewNomor = itemView.findViewById(R.id.textViewNomor);
            textViewSponsor = itemView.findViewById(R.id.textViewSponsor);
            textViewNegara = itemView.findViewById(R.id.textViewNegara);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Pilih Satu!");
                    dialog.setMessage("Operasional apa yang anda inginkan?");
                    dialog.setCancelable(true);
                    dialog.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirmDeleteRider();
                        }
                    });
                    dialog.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, EditRiderActivity.class);
                            intent.putExtra("xID", textViewId.getText().toString());
                            intent.putExtra("xNama", textViewNama.getText().toString());
                            intent.putExtra("xNomor", textViewNomor.getText().toString());
                            intent.putExtra("xSponsor", textViewSponsor.getText().toString());
                            intent.putExtra("xNegara", textViewNegara.getText().toString());
                            context.startActivity(intent);
                        }
                    });

                    dialog.show();
                    return false;
                }
            });
        }

        private void confirmDeleteRider() {
            AlertDialog.Builder confirmDelete = new AlertDialog.Builder(context);
            confirmDelete.setTitle("Warning!");
            confirmDelete.setMessage("Apakah anda yakin ingin menghapus data?");
            confirmDelete.setCancelable(true);
            confirmDelete.setNegativeButton("Batal", null);
            confirmDelete.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteRider();
                    dialog.dismiss();
                    ((MainActivity) context).getAllRiders();
                }
            });
            confirmDelete.show();
        }

        private void deleteRider() {
            int id = Integer.parseInt(textViewId.getText().toString());

            ApiRequestData apiRequestData = RetrofitServer.connectRetrofit().create(ApiRequestData.class);
            Call<ModelResponse> responseCall = apiRequestData.deleteData(id);

            responseCall.enqueue(new Callback<ModelResponse>() {
                @Override
                public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                    int status = response.body().getStatus();
                    String message = response.body().getMessage();

                    if (status == 1) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ModelResponse> call, Throwable t) {
                    Toast.makeText(context, "Failed connect to server!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
