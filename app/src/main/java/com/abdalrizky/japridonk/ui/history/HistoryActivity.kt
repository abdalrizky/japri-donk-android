package com.abdalrizky.japridonk.ui.history

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.abdalrizky.japridonk.R
import com.abdalrizky.japridonk.adapter.HistoryAdapter
import com.abdalrizky.japridonk.database.entity.Recipient
import com.abdalrizky.japridonk.databinding.ActivityHistoryBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel

    private var isHistoryNotEmpty = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]

        viewModel.getAllHistory().observe(this) { historyList ->
            Log.d("tes", historyList.toString())
            if (historyList.isNotEmpty()) {
                val adapter = HistoryAdapter(historyList)
                adapter.setOnClickCallback(object : HistoryAdapter.OnItemClickCallback {
                    override fun onItemClicked(recipient: Recipient) {
                        Intent().also {
                            it.putExtra(EXTRA_NUMBER, recipient.number)
                            it.putExtra(EXTRA_MESSAGE, recipient.message)
                            setResult(Activity.RESULT_OK, it)
                            finish()
                        }
                    }

                    override fun onClearButtonClicked(recipient: Recipient) {
                        viewModel.delete(recipient)
                    }
                })
                Log.d("tes", historyList.toString())
                binding.apply {
                    rvHistory.apply {
                        this.adapter = adapter
                        visibility = View.VISIBLE
                        if (itemDecorationCount == 0) {
                            addItemDecoration(
                                DividerItemDecoration(
                                    this@HistoryActivity, DividerItemDecoration.VERTICAL
                                )
                            )
                        }
                    }
                    layoutEmpty.visibility = View.GONE
                }
                isHistoryNotEmpty = true
                invalidateOptionsMenu()
            } else {
                binding.apply {
                    layoutEmpty.visibility = View.VISIBLE
                    rvHistory.visibility = View.GONE
                }
                isHistoryNotEmpty = false
                invalidateOptionsMenu()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_history, menu)
        menu.findItem(R.id.btn_clear).isVisible = isHistoryNotEmpty
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_clear -> {
                MaterialAlertDialogBuilder(this)
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
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_NUMBER = "extra_number"
        const val EXTRA_MESSAGE = "extra_message"
    }
}