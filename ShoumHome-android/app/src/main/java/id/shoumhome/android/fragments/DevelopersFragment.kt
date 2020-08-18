package id.shoumhome.android.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import id.shoumhome.android.R
import id.shoumhome.android.data.DeveloperData
import kotlinx.android.synthetic.main.fragment_developers.*

class DevelopersFragment : Fragment() {

    companion object {
        private const val ARG_DEVELOPER_POSITION = "arg_developer_position"

        fun newInstance(position: Int): DevelopersFragment {
            val fragment = DevelopersFragment()

            val bundle = Bundle()
            bundle.putInt(ARG_DEVELOPER_POSITION, position)

            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_developers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var position = 1
        if (arguments != null) {
            position = requireArguments().getInt(ARG_DEVELOPER_POSITION, 1)
        }

        // Let's write some data!
        Glide.with(this)
                .load(DeveloperData.drawableId[position])
                .override(150, 150)
                .centerCrop()
                .into(civPhoto)
        tvName.text = DeveloperData.name[position]
        tvNim.text = DeveloperData.nim[position]
        tvGrade.text = DeveloperData.grade[position]
        tvYearRegistered.text = DeveloperData.yearRegistered[position]
        tvGitHub.text = DeveloperData.githubUsername[position]
        tvEmail.text = DeveloperData.email[position]
        tvAddress.text = DeveloperData.address[position]

        val githubUrl = "https://github.com/${DeveloperData.githubUsername[position]}"
        btnVisit.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(githubUrl)
            startActivity(i)
        }
    }

}