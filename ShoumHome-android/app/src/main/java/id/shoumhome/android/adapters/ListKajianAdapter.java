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
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import id.shoumhome.android.R;
import id.shoumhome.android.activity.KajianActivity;
import id.shoumhome.android.activity.LoginActivity;
import id.shoumhome.android.activity.ShowKajianActivity;

public class ListKajianAdapter extends RecyclerView.Adapter<ListKajianAdapter.MyHolder>{
    private Context context;
    ArrayList<String> mId = new ArrayList<>();
    ArrayList<String> mFoto = new ArrayList<>();
    ArrayList<String> mJudul = new ArrayList<>();
    ArrayList<String> mUstad = new ArrayList<>();
    ArrayList<String> mKeterangan = new ArrayList<>();
    ArrayList<String> mTanggal = new ArrayList<>();

    public ListKajianAdapter(Context context,ArrayList<String> mId, ArrayList<String> mFoto, ArrayList<String> mJudul, ArrayList<String> mUstad, ArrayList<String> mKeterangan, ArrayList<String> mTanggal) {
        this.context = context;
        this.mId = mId;
        this.mFoto = mFoto;
        this.mJudul = mJudul;
        this.mUstad = mUstad;
        this.mKeterangan = mKeterangan;
        this.mTanggal = mTanggal;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_kajian,parent,false);
        MyHolder holder= new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        Glide.with(context).asBitmap().load(mFoto.get(position)).into(holder.imageViewFoto);
        holder.TextViewJudul.setText(mJudul.get(position));
        holder.TextViewUstad.setText(mUstad.get(position));
        holder.TextViewKeterangan.setText(mKeterangan.get(position));
        holder.TextViewTanggal.setText(mTanggal.get(position));


        holder.layout_data_kajian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent KajianIntent = new Intent(holder.itemView.getContext(), ShowKajianActivity.class);
                KajianIntent.putExtra(ShowKajianActivity.EXTRA_KAJIAN_ID, mId.get(position));
                ((Activity) holder.itemView.getContext()).startActivity(KajianIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mJudul.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        View view;
        ImageView imageViewFoto;
        TextView TextViewJudul,TextViewUstad,TextViewKeterangan,TextViewTanggal;
        RelativeLayout layout_data_kajian;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageViewFoto=itemView.findViewById(R.id.Image_Judul);
            TextViewJudul=itemView.findViewById(R.id.TextView_Judul);
            TextViewUstad=itemView.findViewById(R.id.TextView_Ustadz);
            TextViewKeterangan=itemView.findViewById(R.id.TextView_Keterangan);
            TextViewTanggal=itemView.findViewById(R.id.TextView_Tanggal);
            layout_data_kajian=itemView.findViewById(R.id.layout_data_kajian);
        }
    }
}
