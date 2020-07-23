package id.shoumhome.android.ustadz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.shoumhome.android.ustadz.R
import id.shoumhome.android.ustadz.items.Kajian
import kotlinx.android.synthetic.main.item_kajian.view.*

class KajianAdapter : RecyclerView.Adapter<KajianAdapter.KajianHolder>() {

    private val mData = ArrayList<Kajian>()

    fun setData(items: ArrayList<Kajian>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KajianAdapter.KajianHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_kajian, parent, false)
        return KajianHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: KajianAdapter.KajianHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class KajianHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(kajian: Kajian) {
            with(itemView) {
                val mosqueOrAddress = if (kajian.place.equals("Di Tempat")) kajian.address else kajian.mosque
                tv_title.text = kajian.title
                tv_ket.text = kajian.place
                tv_due.text = kajian.date
                tv_mosque_address.text = mosqueOrAddress

                if (kajian.imgResource != "null") {
                    Glide.with(context)
                            .load(kajian.imgResource)
                            .apply(RequestOptions().override(100, 100))
                            .into(img_kajian)
                } else {
                    Glide.with(context)
                            .load(R.drawable.icon)
                            .apply(RequestOptions().override(100, 100))
                            .into(img_kajian)
                }

                setOnClickListener {
                    //val i = Intent(context, SplashActivity::class.java)
                    Toast.makeText(context, kajian.title, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}