package id.inixindo.androidrestapi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.inixindo.androidrestapi.R;
import id.inixindo.androidrestapi.models.ModelRiders;

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
        }
    }
}
