package id.shoumhome.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

import id.shoumhome.android.R;
import id.shoumhome.android.activity.ShowKajianActivity;
import id.shoumhome.android.models.Kajian;

public class ListKajianAdapter extends RecyclerView.Adapter<ListKajianAdapter.MyHolder>{

    public ListKajianAdapter(Context context) {
        this.context = context;
        }
    ArrayList<Kajian> mKajian = new ArrayList<>();
    private Context context;

    public void setKajian(ArrayList<Kajian> mKajian) {
        if (mKajian != null) this.mKajian.clear();
        assert mKajian != null;
        this.mKajian.addAll(mKajian);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_kajian,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        if (Objects.equals(mKajian.get(position).getPlace(), "Di Tempat")) {
            if (mKajian.get(position).getImgResource()!= null)
                Glide.with(context).asBitmap().load(mKajian.get(position).getImgResource()).into(holder.imageViewFoto);
            else
                Glide.with(context).asBitmap().load(R.drawable.icon).into(holder.imageViewFoto);
        } else {
            Glide.with(context).asBitmap().load(mKajian.get(position).getImgResource()).into(holder.imageViewFoto);
        }

        holder.TextViewJudul.setText(mKajian.get(position).getTitle());
        holder.TextViewUstad.setText(mKajian.get(position).getUstadzName());
        holder.TextViewMosque.setText((Objects.equals(mKajian.get(position).getAddress(), "null")) ? mKajian.get(position).getMosque() : mKajian.get(position).getAddress());
        holder.TextViewKeterangan.setText(mKajian.get(position).getPlace());
        holder.TextViewTanggal.setText(mKajian.get(position).getDate());

        holder.layout_data_kajian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent KajianIntent = new Intent(holder.itemView.getContext(), ShowKajianActivity.class);
                KajianIntent.putExtra(ShowKajianActivity.EXTRA_KAJIAN_ID, mKajian.get(position).getId());
                ((Activity) holder.itemView.getContext()).startActivity(KajianIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mKajian == null? 0 : mKajian.size();
    }
    public void removeItem(int position) {
        this.mKajian.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mKajian.size());
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        View view;
        ImageView imageViewFoto;
        TextView TextViewJudul,TextViewUstad,TextViewMosque,TextViewKeterangan,TextViewTanggal;
        RelativeLayout layout_data_kajian;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageViewFoto=itemView.findViewById(R.id.Image_Judul);
            TextViewJudul=itemView.findViewById(R.id.TextView_Judul);
            TextViewUstad=itemView.findViewById(R.id.TextView_Ustadz);
            TextViewMosque=itemView.findViewById(R.id.TextView_Mosque);
            TextViewKeterangan=itemView.findViewById(R.id.TextView_Keterangan);
            TextViewTanggal=itemView.findViewById(R.id.TextView_Tanggal);
            layout_data_kajian=itemView.findViewById(R.id.layout_data_kajian);
        }


    }
}
