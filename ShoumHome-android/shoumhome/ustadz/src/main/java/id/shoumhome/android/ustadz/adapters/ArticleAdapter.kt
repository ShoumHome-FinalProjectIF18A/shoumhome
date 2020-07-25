package id.shoumhome.android.ustadz.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.shoumhome.android.ustadz.R
import id.shoumhome.android.ustadz.ReadArticleActivity
import id.shoumhome.android.ustadz.items.Article
import kotlinx.android.synthetic.main.item_articles.view.*

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ArticleHolder>() {

    private var mData = ArrayList<Article>()

    fun setData(article: ArrayList<Article>) {
        mData.clear()
        mData.addAll(article)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleAdapter.ArticleHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_articles, parent, false
        )
        return ArticleHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ArticleAdapter.ArticleHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(article: Article) {
            with(itemView) {
                val title = article.title
                val content = article.content
                val postDate = article.post_date
                val id = article.id

                // tvArticleId.text = id
                tv_title.text = title
                tv_text_less.text = content
                tv_post_date.text = postDate

                if (!article.hasImg) {
                    imgArticle.visibility = View.GONE
                } else {
                    Glide.with(context)
                            .load(article.imgUrl)
                            .into(imgArticle)
                }

                setOnClickListener {
                    val i = Intent(context, ReadArticleActivity::class.java)
                    i.putExtra(ReadArticleActivity.EXTRA_ARTICLE_ID, id)
                    context.startActivity(i)
                }
            }
        }
    }
}