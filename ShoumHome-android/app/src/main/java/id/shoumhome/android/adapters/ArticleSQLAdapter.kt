package id.shoumhome.android.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.shoumhome.android.R
import id.shoumhome.android.activity.ReadArticleSQLActivity
import id.shoumhome.android.models.Article
import kotlinx.android.synthetic.main.list_artikel.view.*

class ArticleSQLAdapter : RecyclerView.Adapter<ArticleSQLAdapter.ArticleSQLViewHolder>(){
    private var mArticle = ArrayList<Article>()
    private lateinit var mContext: Context

    fun setArticle(article: ArrayList<Article>){
        mArticle.clear()
        mArticle.addAll(article)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleSQLAdapter.ArticleSQLViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.list_artikel, parent, false
        )
        return ArticleSQLViewHolder(view)
    }

    override fun getItemCount(): Int = mArticle.size

    override fun onBindViewHolder(holder: ArticleSQLAdapter.ArticleSQLViewHolder, position: Int) {
        holder.bind(mArticle[position])
    }

    fun removeItem(position: Int) {
        mArticle.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mArticle.size)
    }

    inner class ArticleSQLViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(article: Article){
            with(itemView) {
                val title = article.title
                val content = article.content
                val postDate = article.post_date
                val id = article.id
                val namaUstadz = article.ustadzName

                //tvArticleId.text = id
                TV_Judul.text = title
                TV_Ringkasan.text = content
                TV_Tanggal.text = postDate
                TV_Ustadz.text = namaUstadz

                if (article.hasImg == "false"){
                    I_Judul.visibility = View.GONE
                } else {
                    Glide.with(context)
                            .load(article.imgUrl)//model
                            .into(I_Judul)//layout list adapter
                }

                setOnClickListener{
                    val i = Intent(context, ReadArticleSQLActivity::class.java)
                    i.putExtra(ReadArticleSQLActivity.EXTRA_PARCEL_ARTICLES, article)

                    (mContext as Activity).startActivity(i)
                }
            }
        }
    }
}