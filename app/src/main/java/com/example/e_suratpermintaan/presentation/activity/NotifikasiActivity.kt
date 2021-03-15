package com.example.e_suratpermintaan.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.DataNotifikasi
import com.e_suratpermintaan.core.domain.entities.responses.NotifItem
import com.e_suratpermintaan.core.domain.entities.responses.NotifikasiResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.ActivityNotifikasiBinding
import com.example.e_suratpermintaan.databinding.NotifikasiUnreadListBinding
import com.example.e_suratpermintaan.external.constants.IntentExtraConstants.ID_SP_EXTRA_KEY
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.NotifikasiViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.NotifikasiViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotifikasiActivity : BaseActivity<ActivityNotifikasiBinding>() {

    private val notifikasiViewModel: NotifikasiViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()

    private lateinit var notifikasiAdapter: BaseAdapter<NotifikasiViewHolder, NotifikasiUnreadListBinding>
    private var idUser: String? = null

    override fun getViewBinding(): ActivityNotifikasiBinding =
        ActivityNotifikasiBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idUser = profilePreference.getProfile()?.id

        init()

    }

    private fun init() {
        setupTollbar()
        notifikasiAdapter =
            BaseAdapter(NotifikasiUnreadListBinding::inflate, NotifikasiViewHolder::class.java)
        getNotifikasi(idUser)
        setupListeners()
        initRecyclerView()

    }

    private fun setupTollbar() {
        binding.toolbarNotifikasi.text = getString(R.string.toolbar_notifikasi)
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(this@NotifikasiActivity)
            adapter = notifikasiAdapter
        }
    }

    private fun setupListeners() {

        notifikasiAdapter.setOnItemClickListener { item, _ ->
            val notifItem = item as NotifItem

            val intent = Intent(this, DetailSuratPermintaanActivity::class.java)
            intent.putExtra(ID_SP_EXTRA_KEY, notifItem.idSp)
            startActivity(intent)
            finish()
        }
    }

    private fun getNotifikasi(idUser: String?) {
        disposable = notifikasiViewModel.getNotifikasiList(idUser.toString())
            .subscribe(this::notifikasiResponse, this::handleError)
    }

    private fun notifikasiResponse(response: NotifikasiResponse) {
        var dataNotif: DataNotifikasi? = DataNotifikasi()

        response.data?.forEach {
            dataNotif = it
        }

        if (dataNotif?.count == 0) {
            binding.constraintCountUnread.visibility = View.GONE
        } else {
            binding.constraintCountUnread.visibility = View.VISIBLE
            binding.tvCountNotifUnread.text = "${dataNotif?.count.toString()} notifikasi"
        }

        dataNotif?.notif?.forEach {
            if (it != null) {
                notifikasiAdapter.itemList.add(it)
            }
        }

        notifikasiAdapter.notifyDataSetChanged()
    }
}