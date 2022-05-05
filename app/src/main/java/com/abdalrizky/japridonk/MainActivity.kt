package com.abdalrizky.japridonk

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.abdalrizky.japridonk.adapter.HistoryAdapter
import com.abdalrizky.japridonk.database.entity.Recipient
import com.abdalrizky.japridonk.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        checkWhatsAppIsInstalled(packageManager)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.getHistory().observe(this) { history ->
            if (history.isNotEmpty()) {
                val adapter = HistoryAdapter(history)
                binding.apply {
                    rvHistory.apply {
                        this.adapter = adapter
                        visibility = View.VISIBLE
                        Log.d("tes", itemDecorationCount.toString())
                        if (itemDecorationCount == 0) {
                            addItemDecoration(
                                DividerItemDecoration(
                                    this@MainActivity, DividerItemDecoration.VERTICAL
                                )
                            )
                        }
                    }
                    adapter.setOnClickCallback(object : HistoryAdapter.OnItemClickCallback {
                        override fun onItemClicked(recipient: Recipient) {
                            edtNumber.setText(recipient.number)
                            edtMessage.setText(recipient.message)
                            edtNumber.clearFocus()
                            edtMessage.clearFocus()
                            nestedScrollView.scrollTo(0,0)
                        }

                        override fun onClearButtonClicked(recipient: Recipient) {
                            viewModel.delete(recipient)
                        }
                    })
                    lottieEmptyHistory.visibility = View.GONE
                    tvHistoryEmpty.visibility = View.GONE
                    ivDeleteAllHistory.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    rvHistory.visibility = View.GONE
                    lottieEmptyHistory.visibility = View.VISIBLE
                    tvHistoryEmpty.visibility = View.VISIBLE
                    ivDeleteAllHistory.visibility = View.GONE
                }
            }
        }

        binding.apply {
            btnSubmit.isEnabled = false
            setEditTextListener()
            btnClear.setOnClickListener { clearField() }
            btnSubmit.setOnClickListener { submitForm() }
            ivDeleteAllHistory.setOnClickListener {
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("Hapus Riwayat")
                    .setMessage("Anda yakin ingin menghapus semua riwayat aktivitas?")
                    .setIcon(R.drawable.ic_delete_all_history)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        viewModel.deleteAllHistory()
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_information -> {
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle(R.string.dialog_title_info)
                    .setMessage(R.string.dialog_message_info)
                    .setPositiveButton(R.string.ok, null)
                    .setNeutralButton(R.string.view_source_code) { _, _ ->
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.source_code_url))
                        ).also { startActivity(it) }
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkWhatsAppIsInstalled(packageManager: PackageManager) {
        val packageName = getString(R.string.whatsapp_package_name)
        try {
            packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            MaterialAlertDialogBuilder(this)
                .setIcon(R.drawable.ic_warning)
                .setTitle(R.string.not_installed)
                .setMessage(R.string.make_sure_you_install_whatsapp)
                .setPositiveButton(R.string.leave_from_app) { _, _ ->
                    finishAndRemoveTask()
                }
                .setCancelable(false)
                .show()
        }
    }

    private fun setEditTextListener() {
        binding.apply {
            edtNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnSubmit.isEnabled = s.toString().trim().isNotEmpty()
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun submitForm() {
        var number = binding.edtNumber.text.toString()
        val message = binding.edtMessage.text.toString().ifEmpty { null }

        val recipient = Recipient().apply {
            this.number = number
            this.message = message
        }

        val regex = Regex("^0")
        if (regex.containsMatchIn(number)) {
            number = regex.replace(number, "62")
        }

        var url = "https://wa.me/$number"
        message?.let {
            Uri.encode(it).also { messageDecoded ->
                url += "/?text=$messageDecoded"
            }
        }

        Intent(Intent.ACTION_VIEW, Uri.parse(url)).also { startActivity(it) }
        Log.d("tes", url)
        viewModel.insert(recipient)
    }

    private fun clearField() {
        binding.apply {
            edtMessage.setText("")
            edtNumber.setText("")
            edtNumber.requestFocus()
        }
    }
}