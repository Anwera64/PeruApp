package com.anwera64.peruapp.Presentation.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.anwera64.peruapp.Presentation.Adapter.AdapterMain
import com.anwera64.peruapp.Presentation.Presenter.MainPresenter
import com.anwera64.peruapp.Presentation.Presenter.MainPresenterDelegate
import com.anwera64.peruapp.R
import com.anwera64.peruapp.data.model.Task
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainPresenterDelegate {

    private val adapter = AdapterMain(ArrayList())
    private val mPresenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.adapter = adapter

        mPresenter.getTasks()
    }

    override fun onTasksReady(tasks: ArrayList<Task>) {
        adapter.tasks = tasks
        adapter.notifyDataSetChanged()
    }
}
