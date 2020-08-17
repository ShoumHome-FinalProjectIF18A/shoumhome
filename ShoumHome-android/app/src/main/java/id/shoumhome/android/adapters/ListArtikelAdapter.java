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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import id.shoumhome.android.R;
import id.shoumhome.android.activity.ArtikelActivity;
import id.shoumhome.android.activity.LoginActivity;
import id.shoumhome.android.activity.ReadArticleActivity;

public class ListArtikelAdapter extends RecyclerView.Adapter<ListArtikelAdapter.Myartikel> {

    public ListArtikelAdapter(Context context,ArrayList<String> mId, ArrayList<String> mImageJudul, ArrayList<String> mJudul, ArrayList<String> mRingkasan, ArrayList<String> mlike, ArrayList<String> mtanggal, ArrayList<String> mustad) {
        this.context = context;
        this.mId = mId ;
        this.mImageJudul = mImageJudul;
        this.mJudul = mJudul;
        this.mRingkasan = mRingkasan;
        this.mlike = mlike;
        this.mtanggal = mtanggal;
        this.mustad = mustad;
    }

    private Context context;
    ArrayList<String>mId =new ArrayList<>();
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
    public void onBindViewHolder(@NonNull final Myartikel holder, final int position) {
        Glide.with(context).asBitmap().load(mImageJudul.get(position)).into(holder.IVjudul);
        holder.TVjudul.setText(mJudul.get(position));
        holder.TVringkasan.setText(mRingkasan.get(position));
        holder.like.setText(mlike.get(position));
        holder.TVtanggal.setText(mtanggal.get(position));
        holder.TVustad.setText(mustad.get(position));

        holder.layout_list_artikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent artikelIntent = new Intent(holder.itemView.getContext(), ReadArticleActivity.class);
                artikelIntent.putExtra(ReadArticleActivity.EXTRA_ARTICLE_ID, mId.get(position));
                ((Activity) holder.itemView.getContext()).startActivity(artikelIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mJudul.size();
    }

    class Myartikel extends RecyclerView.ViewHolder{
        View view;
        ImageView IVjudul;
        TextView TVjudul,TVringkasan, TVustad, TVtanggal, like;
        RelativeLayout layout_list_artikel;

        public Myartikel(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            IVjudul=itemView.findViewById(R.id.I_Judul);
            TVjudul=itemView.findViewById(R.id.TV_Judul);
            TVringkasan=itemView.findViewById(R.id.TV_Ringkasan);
            TVustad=itemView.findViewById(R.id.TV_Ustadz);
            TVtanggal=itemView.findViewById(R.id.TV_Tanggal);
            like=itemView.findViewById(R.id.TV_Like);
            layout_list_artikel=itemView.findViewById(R.id.layout_list_artikel);
        }


    }
}
