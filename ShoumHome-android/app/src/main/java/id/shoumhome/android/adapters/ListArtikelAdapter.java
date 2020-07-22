package id.shoumhome.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import id.shoumhome.android.R;

public class ListArtikelAdapter extends RecyclerView.Adapter<ListArtikelAdapter.Myartikel> {

    public ListArtikelAdapter(Context context, ArrayList<String> mImageJudul, ArrayList<String> mJudul, ArrayList<String> mRingkasan, ArrayList<String> mlike, ArrayList<String> mtanggal, ArrayList<String> mustad) {
        this.context = context;
        this.mImageJudul = mImageJudul;
        this.mJudul = mJudul;
        this.mRingkasan = mRingkasan;
        this.mlike = mlike;
        this.mtanggal = mtanggal;
        this.mustad = mustad;
    }

    private Context context;
    ArrayList<String> mImageJudul =new ArrayList<>();
    ArrayList<String> mJudul =new ArrayList<>();
    ArrayList<String> mRingkasan =new ArrayList<>();
    ArrayList<String> mlike =new ArrayList<>();
    ArrayList<String> mtanggal =new ArrayList<>();
    ArrayList<String> mustad =new ArrayList<>();


    @NonNull
    @Override
    public Myartikel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_artikel,parent,false);
        Myartikel holder=new Myartikel(view);
       return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myartikel holder, int position) {
        Glide.with(context).asBitmap().load(mImageJudul.get(position)).into(holder.IVjudul);
        holder.TVjudul.setText(mJudul.get(position));
        holder.TVringkasan.setText(mRingkasan.get(position));
        holder.like.setText(mlike.get(position));
        holder.TVtanggal.setText(mtanggal.get(position));
        holder.TVustad.setText(mustad.get(position));


    }

    @Override
    public int getItemCount() {
        return mJudul.size();
    }

    class Myartikel extends RecyclerView.ViewHolder{
        ImageView IVjudul;
        TextView TVjudul,TVringkasan, TVustad, TVtanggal, like;
        ConstraintLayout layout_list_artikel;

        public Myartikel(@NonNull View itemView) {
            super(itemView);
            IVjudul=itemView.findViewById(R.id.I_Judul);
            TVjudul=itemView.findViewById(R.id.TV_Judul);
            TVringkasan=itemView.findViewById(R.id.TV_Ringkasan);
            TVustad=itemView.findViewById(R.id.TV_Ustad);
            TVtanggal=itemView.findViewById(R.id.TV_Tanggal);
            like=itemView.findViewById(R.id.TV_Like);
            layout_list_artikel=itemView.findViewById(R.id.layout_list_artikel);
        }
    }
}
