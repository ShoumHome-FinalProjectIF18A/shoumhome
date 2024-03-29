package id.shoumhome.android.ustadz

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import id.shoumhome.android.ustadz.models.Kajian
import id.shoumhome.android.ustadz.models.Mosque
import id.shoumhome.android.ustadz.preferences.CredentialPreference
import kotlinx.android.synthetic.main.activity_add_update_kajian.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddUpdateKajianActivity : AppCompatActivity(), View.OnClickListener {

    private val calendar = Calendar.getInstance()
    private lateinit var kajian: Kajian
    private var isEditMode: Boolean = false

    companion object {
        const val EXTRA_PARCEL_KAJIAN = "extra_parcel_kajian"

        // Return Value setelah keluar dari activity ini
        const val RESULT_SAVE = 130
        const val RESULT_UPDATE = 260
        private const val RESULT_LOAD_IMAGE = 111
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_kajian)
        progressMessage.visibility = View.GONE
        errorMessage.visibility = View.GONE

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.add_kajian)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        kajian = Kajian()
        val bundle = intent.extras
        if (bundle != null) {
            supportActionBar?.title = resources.getString(R.string.update_kajian)
            // tangkap datanya gan!
            val kajianParams = bundle.getParcelable<Kajian>(EXTRA_PARCEL_KAJIAN)!!
            isEditMode = true

            // saatnya inisialisasi data ke model!
            kajian.id = kajianParams.id
            kajian.title = kajianParams.title
            kajian.date = kajianParams.date
            kajian.address = kajianParams.address
            kajian.place = kajianParams.place
            kajian.description = kajianParams.description
            kajian.ytLink = kajianParams.ytLink
            kajian.imgResource = kajianParams.imgResource
            kajian.mosqueId = kajianParams.mosqueId
            kajian.mosqueName = kajianParams.mosqueName

            // let's define a value to a form!
            when (kajian.place) {
                "Di Tempat" -> {
                    spKajianCategory.setSelection(0)
                    rlKajianImage.visibility = View.VISIBLE
                    tilKajianAddress.visibility = View.VISIBLE
                    tilKajianYtLink.visibility = View.GONE
                    if (kajian.imgResource != null) {
                        Glide.with(this)
                                .load(kajian.imgResource)
                                .into(imgKajian)
                    }
                    edtKajianAddress.setText(kajian.address)
                }
                "Video" -> {
                    spKajianCategory.setSelection(1)
                    rlKajianImage.visibility = View.GONE
                    tilKajianAddress.visibility = View.GONE
                    tilKajianYtLink.visibility = View.VISIBLE
                    edtKajianYtLink.setText(kajian.ytLink)
                }
                "Live Streaming" -> {
                    spKajianCategory.setSelection(2)
                    rlKajianImage.visibility = View.GONE
                    tilKajianAddress.visibility = View.GONE
                    tilKajianYtLink.visibility = View.VISIBLE
                    edtKajianYtLink.setText(kajian.ytLink)
                }
            }
            edtKajianTitle.setText(kajian.title)
            tvMosqueId.text = kajian.mosqueId
            tvMosqueName.text = kajian.mosqueName
            edtKajianDescription.setText(kajian.description)

            // Date Conversion
            val dateParse = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(kajian.date!!)
            val timeParse = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(kajian.date!!)
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dateParse!!)
            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(timeParse!!)

            edtKajianDateDue.setText(date)
            edtKajianTimeDue.setText(time)

            // jurus sementara, soalnya kalo upload gambar pasti keluhannya malah gak ada parameter yg masuk di servernya
            spKajianCategory.isEnabled = false
            btnChooseKajianImage.isEnabled = false
        }

        // Listeners
        btnChooseMosque.setOnClickListener(this)
        btnChooseKajianImage.setOnClickListener(this)
        btnRetryError.setOnClickListener(this)
        btnCancelError.setOnClickListener(this)
        spKajianCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                /* no-op */
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        rlKajianImage.visibility = View.VISIBLE
                        tilKajianAddress.visibility = View.VISIBLE
                        tilKajianYtLink.visibility = View.GONE
                    }
                    1, 2 -> {
                        rlKajianImage.visibility = View.GONE
                        tilKajianAddress.visibility = View.GONE
                        tilKajianYtLink.visibility = View.VISIBLE
                    }
                }
            }
        }

        val date = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dateFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
            edtKajianDateDue.setText(sdf.format(calendar.time))

        }
        edtKajianDateDue.setOnClickListener {
            val dpd = DatePickerDialog(
                    this,
                    date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            )
            dpd.setTitle(resources.getString(R.string.choose_date_due))
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000 // minimal hari ini
            dpd.show()
        }
        edtKajianTimeDue.setOnClickListener {
            val mCurrentTime = Calendar.getInstance()
            val hourNow = mCurrentTime.get(Calendar.HOUR_OF_DAY)
            val minuteNow = mCurrentTime.get(Calendar.MINUTE)

            val timePickerListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val timeCalendarSet = Calendar.getInstance()
                timeCalendarSet.set(Calendar.HOUR_OF_DAY, hourOfDay)
                timeCalendarSet.set(Calendar.MINUTE, minute)
                val timeSet = SimpleDateFormat("HH:mm", Locale.getDefault()).format(timeCalendarSet.time)
                edtKajianTimeDue.setText(timeSet)
            }

            val tpd = TimePickerDialog(
                    this,
                    timePickerListener,
                    hourNow,
                    minuteNow,
                    true
            )
            tpd.setTitle(resources.getString(R.string.choose_time_due))
            tpd.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_update, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) super.onBackPressed()
        else if (item.itemId == R.id.menuSave) {
            postData()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnChooseMosque -> {
                val i = Intent(this, MosqueChooserActivity::class.java)
                i.putExtra(MosqueChooserActivity.EXTRA_IS_USTADZ_ONLY, true)
                startActivityForResult(i, MosqueChooserActivity.REQUEST_MOSQUE)
            }
            R.id.btnChooseKajianImage -> {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                        val i = Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(i, RESULT_LOAD_IMAGE)
                    }
                    else -> {
                        Toast.makeText(
                                this,
                                resources.getString(R.string.file_permission_denied),
                                Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }
            R.id.btnRetryError -> postData()
            R.id.btnCancelError -> {
                progressMessage.visibility = View.GONE
                errorMessage.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            MosqueChooserActivity.REQUEST_MOSQUE -> {
                val mosque = data?.getParcelableExtra<Mosque>(MosqueChooserActivity.EXTRA_MOSQUE_RESULT)!!
                tvMosqueId.text = mosque.id.toString()
                tvMosqueName.text = mosque.mosqueName
            }
            RESULT_OK -> {
                if (requestCode == RESULT_LOAD_IMAGE && data != null) {
                    val selectedImage = data.data
                    Glide.with(this)
                            .load(selectedImage)
                            .into(imgKajian)
                    val path = selectedImage!!.path!!.substring(5)
                    tvKajianImagePath.text = path
                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun postData() {
        if (!isEditMode) {
            // SAVE DATA
            val selectedItem = spKajianCategory.selectedItem.toString()
            val categories = resources.getStringArray(R.array.kajian_category_entries)

            val kajianTitle: String?
            var kajianAddress: String? = null
            var kajianYtLink: String? = null
            val kajianDescription: String?
            val kajianDateDue: String?
            val kajianTimeDue: String?

            // verify forms
            when (selectedItem) {
                categories[0] -> {
                    if (edtKajianAddress.text.toString().isEmpty()) {
                        Toast.makeText(this, resources.getString(R.string.kajian_address_empty), Toast.LENGTH_SHORT).show()
                        edtKajianAddress.requestFocus()
                        return
                    }

                }
                categories[1], categories[2] -> {
                    if (edtKajianYtLink.text.toString().isEmpty()) {
                        Toast.makeText(this, resources.getString(R.string.kajian_yt_link_empty), Toast.LENGTH_SHORT).show()
                        edtKajianYtLink.requestFocus()
                        return
                    }
                }
            }
            if (edtKajianTitle.text.toString().isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.kajian_title_empty), Toast.LENGTH_SHORT).show()
                edtKajianTitle.requestFocus()
                return
            }
            if (tvMosqueId.text.toString() == resources.getString(R.string.example_id)) {
                Toast.makeText(this, resources.getString(R.string.kajian_mosque_empty), Toast.LENGTH_SHORT).show()
                btnChooseMosque.requestFocus()
                return
            }
            if (edtKajianDescription.text.toString().isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.kajian_desc_empty), Toast.LENGTH_SHORT).show()
                edtKajianTitle.requestFocus()
                return
            }
            if (edtKajianDateDue.text.toString().isEmpty() || edtKajianTimeDue.text.toString().isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.kajian_due_empty), Toast.LENGTH_SHORT).show()
                svAddUpdateKajian.fullScroll(ScrollView.FOCUS_DOWN)
                return
            }
            // verify complete

            progressMessage.visibility = View.VISIBLE
            errorMessage.visibility = View.GONE
            // catch all values
            val kajianCategory = when (selectedItem) {
                categories[0] -> "Di Tempat"
                categories[1] -> "Video"
                categories[2] -> "Live Streaming"
                else -> throw IllegalArgumentException("Invalid Kajian Category!")
            } // Indonesian Language is needed for this variable!
            kajianTitle = edtKajianTitle.text.toString()
            when (selectedItem) {
                categories[0] -> {
                    kajianAddress = edtKajianAddress.text.toString()
                }
                else -> kajianYtLink = edtKajianYtLink.text.toString()
            }
            val mosqueId: Int = tvMosqueId.text.toString().toInt()
            kajianDescription = edtKajianDescription.text.toString()

            kajianDateDue = edtKajianDateDue.text.toString()
            kajianTimeDue = edtKajianTimeDue.text.toString()
            val kajianDateTimeDue = "$kajianDateDue $kajianTimeDue"

            // send data
            val api = resources.getString(R.string.server) + "api/kajian"
            val client = AsyncHttpClient()
            val credential = CredentialPreference(this)

            val params = RequestParams()
            params.put("title", kajianTitle)
            params.put("ustadz_id", credential.getCredential().username)
            params.put("mosque_id", mosqueId)
            if (selectedItem == categories[0]) {
                params.put("address", kajianAddress)
                if (tvKajianImagePath.text.toString() != resources.getString(R.string.example_path)) {
                    // FIXME: 11/08/2020 error statuscode 0 (null)
                    val file = File(tvKajianImagePath.text.toString())
                    if (file.isFile)
                        params.put("file", file)
                    else
                        Toast.makeText(this, "Not found: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                }
            } else params.put("yt_url", kajianYtLink)
            params.put("place", kajianCategory)
            params.put("desc", kajianDescription)
            params.put("due", kajianDateTimeDue)
            client.setTimeout(60000)
            client.post(api, params, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                    progressMessage.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                    Toast.makeText(this@AddUpdateKajianActivity, resources.getString(R.string.add_kajian_success), Toast.LENGTH_SHORT).show()

                    this@AddUpdateKajianActivity.setResult(RESULT_SAVE)
                    finish()
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                    progressMessage.visibility = View.GONE
                    errorMessage.visibility = View.VISIBLE
                    val errorMsg = "Oops! An error occurred.\n[$statusCode]\n${responseBody?.let { String(it) }}"
                    tvErrorMessage.text = errorMsg
                }
            })
        } else {
            // UPDATE DATA
            val selectedItem = spKajianCategory.selectedItem.toString()
            val categories = resources.getStringArray(R.array.kajian_category_entries)

            val kajianTitle: String?
            var kajianAddress: String? = null
            var kajianYtLink: String? = null
            val kajianDescription: String?
            val kajianDateDue: String?
            val kajianTimeDue: String?

            // verify forms
            when (selectedItem) {
                categories[0] -> {
                    if (edtKajianAddress.text.toString().isEmpty()) {
                        Toast.makeText(this, resources.getString(R.string.kajian_address_empty), Toast.LENGTH_SHORT).show()
                        edtKajianAddress.requestFocus()
                        return
                    }

                }
                categories[1], categories[2] -> {
                    if (edtKajianYtLink.text.toString().isEmpty()) {
                        Toast.makeText(this, resources.getString(R.string.kajian_yt_link_empty), Toast.LENGTH_SHORT).show()
                        edtKajianYtLink.requestFocus()
                        return
                    }
                }
            }
            if (edtKajianTitle.text.toString().isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.kajian_title_empty), Toast.LENGTH_SHORT).show()
                edtKajianTitle.requestFocus()
                return
            }
            if (tvMosqueId.text.toString() == resources.getString(R.string.example_id)) {
                Toast.makeText(this, resources.getString(R.string.kajian_mosque_empty), Toast.LENGTH_SHORT).show()
                btnChooseMosque.requestFocus()
                return
            }
            if (edtKajianDescription.text.toString().isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.kajian_desc_empty), Toast.LENGTH_SHORT).show()
                edtKajianTitle.requestFocus()
                return
            }
            if (edtKajianDateDue.text.toString().isEmpty() || edtKajianTimeDue.text.toString().isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.kajian_due_empty), Toast.LENGTH_SHORT).show()
                svAddUpdateKajian.fullScroll(ScrollView.FOCUS_DOWN)
                return
            }
            // verify complete

            progressMessage.visibility = View.VISIBLE
            errorMessage.visibility = View.GONE
            // catch all values
            val kajianCategory = when (selectedItem) {
                categories[0] -> "Di Tempat"
                categories[1] -> "Video"
                categories[2] -> "Live Streaming"
                else -> throw IllegalArgumentException("Invalid Kajian Category!")
            } // Indonesian Language is needed for this variable!
            kajianTitle = edtKajianTitle.text.toString()
            when (selectedItem) {
                categories[0] -> {
                    kajianAddress = edtKajianAddress.text.toString()
                }
                else -> kajianYtLink = edtKajianYtLink.text.toString()
            }
            val mosqueId: Int = tvMosqueId.text.toString().toInt()
            kajianDescription = edtKajianDescription.text.toString()

            kajianDateDue = edtKajianDateDue.text.toString()
            kajianTimeDue = edtKajianTimeDue.text.toString()
            val kajianDateTimeDue = "$kajianDateDue $kajianTimeDue"

            // send data
            val api = resources.getString(R.string.server) + "api/kajian/${kajian.id}"
            val client = AsyncHttpClient()
            val credential = CredentialPreference(this)

            val params = RequestParams()
            params.put("title", kajianTitle)
            params.put("ustadz_id", credential.getCredential().username)
            params.put("mosque_id", mosqueId)
            if (selectedItem == categories[0]) {
                params.put("address", kajianAddress)
                if (tvKajianImagePath.text.toString() != resources.getString(R.string.example_path)) {
                    // FIXME: 11/08/2020 error statuscode 0 (null)
                    val file = File(tvKajianImagePath.text.toString())
                    if (file.isFile)
                        params.put("file", file)
                    else
                        Toast.makeText(this, "Not found: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                }
            } else params.put("yt_url", kajianYtLink)
            params.put("place", kajianCategory)
            params.put("desc", kajianDescription)
            params.put("due", kajianDateTimeDue)
            client.setTimeout(60000)
            client.put(api, params, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                    progressMessage.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                    Toast.makeText(this@AddUpdateKajianActivity, resources.getString(R.string.update_kajian_success), Toast.LENGTH_SHORT).show()

                    this@AddUpdateKajianActivity.setResult(RESULT_UPDATE)
                    finish()
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                    progressMessage.visibility = View.GONE
                    errorMessage.visibility = View.VISIBLE
                    val errorMsg = "Oops! An error occurred.\n[$statusCode]\n${responseBody?.let { String(it) }}"
                    tvErrorMessage.text = errorMsg
                }
            })
        }
    }
}