package com.anwera64.peruapp.presentation.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.anwera64.peruapp.R
import com.anwera64.peruapp.data.model.Task
import com.anwera64.peruapp.extensions.checkEditText
import kotlinx.android.synthetic.main.activity_new_task.*
import java.util.*

class NewTaskActivity : AppCompatActivity() {

    private var dueDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        cvDueDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            dueDate = calendar.time
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.date_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spNotificationDate.adapter = adapter
        }

        swRecordatorio.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                false -> {
                    spNotificationDate.visibility = View.GONE
                }
                true -> {
                    spNotificationDate.visibility = View.VISIBLE
                }
            }
        }

        btnAdd.setOnClickListener { checkForCompletion() }
    }

    private fun onTaskCreated(task: Task) {
        val intent = Intent()
        intent.putExtra("task", task)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun checkForCompletion() {
        if (tilTitle.checkEditText(getString(R.string.obligatory_error))) return
        if (tilDetail.checkEditText(getString(R.string.obligatory_error))) return
        if (dueDate == null) {
            dateAlert()
            return
        }

        val title = tilTitle.editText?.text.toString()
        val detail = tilDetail.editText?.text.toString()
        val creationDate = Calendar.getInstance().time
        val notificationDate: Date? = null

        if (swRecordatorio.isChecked) {

        }

        val task =
            Task(UUID.randomUUID().toString(), title, detail, creationDate.time, dueDate!!.time, notificationDate?.time)
        onTaskCreated(task)
    }

    private fun dateAlert() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.date_error_msg))
            .setNeutralButton(getString(R.string._ok)) { dialog, _ -> dialog.dismiss() }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.home -> {
                AlertDialog.Builder(this)
                    .setMessage(getString(R.string.cancel_warning))
                    .setPositiveButton(getString(R.string._continue)) { dialog, _ ->
                        dialog.dismiss()
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                    .setNegativeButton(getString(R.string._cancel)) { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}