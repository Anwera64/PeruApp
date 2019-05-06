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
import com.anwera64.peruapp.data.model.Task
import com.anwera64.peruapp.presentation.adapter.AdapterMain
import com.anwera64.peruapp.presentation.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.text.Normalizer

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

        val manager = this.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val search = menu?.findItem(R.id.action_search)?.actionView as SearchView
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

    private fun filterTasks(query: String) {
        val filteredTasks = viewModel.allTasks.value
        val queryNormalized = Normalizer.normalize(query.toLowerCase(), Normalizer.Form.NFD)

        filteredTasks?.let {
            adapter.tasks = filteredTasks.filter { task ->
                val titleNormalized = Normalizer.normalize(task.title.toLowerCase(), Normalizer.Form.NFD)
                titleNormalized.contains(queryNormalized)
            }
        }
    }

    private fun filterTasks() {
        filterTasks("")
    }

}
