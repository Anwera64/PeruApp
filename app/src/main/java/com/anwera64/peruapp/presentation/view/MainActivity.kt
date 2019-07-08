package com.anwera64.peruapp.presentation.view

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anwera64.peruapp.R
import com.anwera64.peruapp.data.local.Preferences
import com.anwera64.peruapp.data.model.Task
import com.anwera64.peruapp.presentation.adapter.AdapterMain
import com.anwera64.peruapp.presentation.adapter.PageableAdapter
import com.anwera64.peruapp.presentation.viewmodel.MainViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.text.Normalizer

class MainActivity : AppCompatActivity(), AdapterMain.Delegate, PageableAdapter.Delegate {

    companion object {
        private const val NEW_TASK = 0
    }

    private enum class MenuType { Normal, OneSelected }

    private val adapter = AdapterMain(this, this)
    private lateinit var viewModel: MainViewModel
    private var menuType = MenuType.Normal
    private lateinit var prefs: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = Preferences.getInstance(this, Gson())

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.taskPageable.observe(this, Observer { taskPageable ->
            taskPageable?.run {
                val items = content.takeLast(20 - content.size%20)
                adapter.update(page, items, isLast)
            }
        })

        rvMain.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rvMain.adapter = adapter

        viewModel.getTasks(prefs.getToken().accessToken)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_TASK && resultCode == Activity.RESULT_OK) {
            data?.let {
                val task = data.getSerializableExtra("task") as Task
                viewModel.insertTask(task, prefs.getToken().accessToken)
            }
        }
    }

    override fun requestNewPage(page: Int) {
        viewModel.getTasks(prefs.getToken().accessToken, page)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_normal, menu)
        menu?.let { createNormalMenu(menu) }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            when (menuType) {
                MenuType.Normal -> {
                    menu.findItem(R.id.delete)?.isVisible = false
                    menu.findItem(R.id.action_search)?.isVisible = true
                    menu.findItem(R.id.addNew)?.isVisible = true
                }
                MenuType.OneSelected -> {
                    menu.findItem(R.id.delete)?.isVisible = true
                    menu.findItem(R.id.action_search)?.isVisible = false
                    menu.findItem(R.id.addNew)?.isVisible = false
                }
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun createNormalMenu(menu: Menu) {

        val manager = this.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val search = menu.findItem(R.id.action_search)?.actionView as SearchView
        search.setSearchableInfo(manager.getSearchableInfo(this.componentName))

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    filterTasks(query)
                }
                search.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterTasks(newText)
                }
                return true
            }
        })

        search.setOnCloseListener {
            filterTasks()
            false
        }

        search.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val mgr = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                mgr.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.addNew -> newTask()
            R.id.delete -> deleteTasks()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteTasks() {
        adapter.selectedTasks.values.forEach { v -> viewModel.deleteTask(v, prefs.getToken().accessToken) }
        adapter.selectedTasks.clear()
        menuType = MenuType.Normal
        invalidateOptionsMenu()
    }

    private fun newTask() {
        val intent = Intent(this, NewTaskActivity::class.java)
        startActivityForResult(intent, NEW_TASK)
    }

    private fun filterTasks(query: String = "") {
        viewModel.taskPageable.value?.run {
            val queryNormalized = Normalizer.normalize(query.toLowerCase(), Normalizer.Form.NFD)
            val filteredTasks = content.filter { task ->
                val titleNormalized = Normalizer.normalize(task.title.toLowerCase(), Normalizer.Form.NFD)
                titleNormalized.contains(queryNormalized)
            }
            adapter.update(0, filteredTasks, true)
        }
    }

    override fun onItemSelected() {
        menuType = if (adapter.selectedTasks.isEmpty()) {
            MenuType.Normal
        } else {
            MenuType.OneSelected
        }
        invalidateOptionsMenu()
    }

}


