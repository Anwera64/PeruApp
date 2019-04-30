package com.anwera64.peruapp.presentation.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.anwera64.peruapp.presentation.adapter.AdapterMain
import com.anwera64.peruapp.presentation.presenter.MainPresenter
import com.anwera64.peruapp.presentation.presenter.MainPresenterDelegate
import com.anwera64.peruapp.R
import com.anwera64.peruapp.data.model.Task
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_task.*

class MainActivity : AppCompatActivity(), MainPresenterDelegate {

    private val NEW_TASK = 0

    private val adapter = AdapterMain(ArrayList())
    private val mPresenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.adapter = adapter

        mPresenter.getTasks()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_plus, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.addNew -> newTask()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun newTask() {
        val intent = Intent(this, NewTaskActivity::class.java)
        startActivityForResult(intent, NEW_TASK)
    }

    override fun onTasksReady(tasks: ArrayList<Task>) {
        adapter.tasks = tasks
        adapter.notifyDataSetChanged()
    }
}
