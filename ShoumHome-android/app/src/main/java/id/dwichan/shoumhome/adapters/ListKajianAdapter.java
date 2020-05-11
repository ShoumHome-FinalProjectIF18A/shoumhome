package id.dwichan.shoumhome.adapters;

import android.content.Context;
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

import id.dwichan.shoumhome.R;

public class ListKajianAdapter extends RecyclerView.Adapter<ListKajianAdapter.MyHolder>{
    private Context context;
    ArrayList<String> mFoto = new ArrayList<>();
    ArrayList<String> mJudul = new ArrayList<>();
    ArrayList<String> mUstad = new ArrayList<>();
    ArrayList<String> mLink = new ArrayList<>();

    public ListKajianAdapter(Context context, ArrayList<String> mFoto, ArrayList<String> mJudul, ArrayList<String> mUstad, ArrayList<String> mLink) {
        this.context = context;
        this.mFoto = mFoto;
        this.mJudul = mJudul;
        this.mUstad = mUstad;
        this.mLink = mLink;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_kajian,parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        Glide.with(context).asBitmap().load(mFoto.get(position)).into(holder.imageViewFoto);
        holder.textViewJudul.setText(mJudul.get(position));
        holder.textViewUstad.setText(mUstad.get(position));
        holder.textViewLink.setText(mLink.get(position));

        holder.layout_data_kajian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,  mJudul.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mJudul.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageViewFoto;
        TextView textViewJudul,textViewUstad,textViewLink;
        RelativeLayout layout_data_kajian;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFoto=itemView.findViewById(R.id.imageViewFoto);
            textViewJudul=itemView.findViewById(R.id.textViewJudul);
            textViewUstad=itemView.findViewById(R.id.textViewUstad);
            textViewLink=itemView.findViewById(R.id.textViewLink);
            layout_data_kajian=itemView.findViewById(R.id.layout_data_kajian);
        }
    }
}