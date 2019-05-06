package com.anwera64.peruapp.presentation.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anwera64.peruapp.R
import com.anwera64.peruapp.data.model.Task
import com.anwera64.peruapp.presentation.adapter.AdapterMain
import com.anwera64.peruapp.presentation.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NEW_TASK = 0
    }

    private val adapter = AdapterMain()
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.allTasks.observe(this, Observer { tasks ->
            tasks?.let { adapter.tasks = tasks }
        })

        rvMain.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rvMain.adapter = adapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_TASK && resultCode == Activity.RESULT_OK) {
            data?.let {
                val task = data.getSerializableExtra("task") as Task
                viewModel.insertTask(task)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_plus, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.addNew -> newTask()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun newTask() {
        val intent = Intent(this, NewTaskActivity::class.java)
        startActivityForResult(intent, NEW_TASK)
    }

}
