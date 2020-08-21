package id.shoumhome.android.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.shoumhome.android.R
import id.shoumhome.android.activity.ShowKajianActivity
import id.shoumhome.android.activity.ShowKajianSQLActivity
import id.shoumhome.android.models.Kajian
import kotlinx.android.synthetic.main.list_data_kajian.view.*

class KajianSQLAdapter : RecyclerView.Adapter<KajianSQLAdapter.KajianSQLViewHolder>() {
    private val mKajian = ArrayList<Kajian>()
    private lateinit var mcontext: Context

    fun setData(items: ArrayList<Kajian>) {
        mKajian.clear()
        mKajian.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KajianSQLAdapter.KajianSQLViewHolder {
        mcontext = parent.context
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_data_kajian, parent, false)
        return KajianSQLViewHolder(view)
    }

    override fun getItemCount(): Int = mKajian.size

    override fun onBindViewHolder(holder: KajianSQLAdapter.KajianSQLViewHolder, position: Int) {
        holder.bind(mKajian[position], position)
    }

    fun removeItem(position: Int) {
        mKajian.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mKajian.size)
    }

    inner class KajianSQLViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(kajian: Kajian, position: Int) {
            with(itemView) {
                val mosqueorAddress = if (kajian.place.equals("Di Tempat")) kajian.address else kajian.mosque
                TextView_Judul.text = kajian.title
                TextView_Ustadz.text = kajian.ustadzName
                TextView_Mosque.text = mosqueorAddress
                TextView_Keterangan.text = kajian.place
                TextView_Tanggal.text = kajian.date

                if (kajian.imgResource != "null") {
                    Glide.with(context)
                            .load(kajian.imgResource)
                            .apply(RequestOptions().override(100, 100))
                            .into(Image_Judul)
                } else {
                    Glide.with(context)
                            .load(R.drawable.icon)
                            .apply(RequestOptions().override(100, 100))
                            .into(Image_Judul)
                }

                setOnClickListener {
                    val i = Intent(context, ShowKajianSQLActivity::class.java)
                    i.putExtra(ShowKajianSQLActivity.EXTRA_PARCEL_KAJIAN, kajian)
                    (mcontext as Activity).startActivity(i)
                }
            }
        }
    }
}