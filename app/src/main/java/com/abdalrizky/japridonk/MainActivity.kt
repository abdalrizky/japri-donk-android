package com.abdalrizky.japridonk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.abdalrizky.japridonk.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.apply {
            btnSubmit.isEnabled = false
            setEditTextListener()
            btnClear.setOnClickListener { clearField() }
            btnSubmit.setOnClickListener { submitForm() }
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
        binding.apply {
            var numberInput = edtNumber.text.toString().trim()
            var message: String? = null
            val regex = Regex("^0")
            if (regex.containsMatchIn(numberInput)) {
                numberInput = regex.replace(numberInput, "62")
            }
            if (edtMessage.text != null) {
                message = edtMessage.text.toString()
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/$numberInput/?text=$message")
                ).also { startActivity(it) }
            } else {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/$numberInput")
                ).also { startActivity(it) }
            }
        }
    }

    private fun clearField() {
        binding.apply {
            edtMessage.setText("")
            edtNumber.setText("")
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
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.dialog_message_info)
                    .setPositiveButton(R.string.dialog_positive_btn_info, null)
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}