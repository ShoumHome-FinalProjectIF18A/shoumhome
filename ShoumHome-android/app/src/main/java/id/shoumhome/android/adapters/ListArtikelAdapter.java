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
import id.shoumhome.android.activity.ReadArticleActivity;
import id.shoumhome.android.models.Article;

public class ListArtikelAdapter extends RecyclerView.Adapter<ListArtikelAdapter.Myartikel> {

    ArrayList<Article> mArticle = new ArrayList<>();

    public ListArtikelAdapter(Context context) {
        this.context = context;
    }

    private Context context;

    public void setArticle(ArrayList<Article> mArticle) {
        if (mArticle != null) this.mArticle.clear();
        assert mArticle != null;
        this.mArticle.addAll(mArticle);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Myartikel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_artikel,parent,false);
        return new Myartikel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myartikel holder, final int position) {
        if(Objects.equals(mArticle.get(position).getHasImg(), "1")) {
            //jika gambar ada tampilkan
            Glide.with(context).asBitmap().load(mArticle.get(position).getImgUrl()).into(holder.IVjudul);
        }else{
            //hilangkan tampilan gambar jika gambar tidak ada
            //untuk ngilangin gambar jika gambar tidak ada langsung tampil atribut dibawahnya
            holder.IVjudul.setVisibility(View.GONE);
        }
        holder.TVjudul.setText(mArticle.get(position).getTitle());
        holder.TVringkasan.setText(mArticle.get(position).getContent());
        holder.TVtanggal.setText(mArticle.get(position).getPost_date());
        holder.TVustad.setText(mArticle.get(position).getUstadzName());
        holder.layout_list_artikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent artikelIntent = new Intent(holder.itemView.getContext(), ReadArticleActivity.class);
                artikelIntent.putExtra(ReadArticleActivity.EXTRA_ARTICLE_ID, mArticle.get(position).getId());
                ((Activity) holder.itemView.getContext()).startActivity(artikelIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArticle.size();
    }


    static class Myartikel extends RecyclerView.ViewHolder {
        View view;
        ImageView IVjudul;
        TextView TVjudul, TVringkasan, TVustad, TVtanggal;
        RelativeLayout layout_list_artikel;

        public Myartikel(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            IVjudul = itemView.findViewById(R.id.I_Judul);
            TVjudul = itemView.findViewById(R.id.TV_Judul);
            TVringkasan = itemView.findViewById(R.id.TV_Ringkasan);
            TVustad = itemView.findViewById(R.id.TV_Ustadz);
            TVtanggal = itemView.findViewById(R.id.TV_Tanggal);
            layout_list_artikel = itemView.findViewById(R.id.layout_list_artikel);
        }

    }
}