package com.anwera64.peruapp.presentation.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anwera64.peruapp.R
import com.anwera64.peruapp.data.model.Task
import kotlinx.android.synthetic.main.item_task.view.*

class AdapterMain(private val view: Delegate) : androidx.recyclerview.widget.RecyclerView.Adapter<AdapterMain.ViewHolder>() {

    var tasks: List<Task> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var selectedTasks: HashMap<String, Task> = HashMap()
    var isSelecting: Boolean = false

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdapterMain.ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_task, p0, false))
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val task = tasks[p1]
        val view = p0.itemView

        p0.isSelected = task.isSelected
        view.tvTitle.text = task.title
        view.tvDescription.text = task.detail
        view.tvDate.text = DateFormat.format("dd/MM/yy", task.expirationDate)

        view.setOnLongClickListener {
            selectTask(p0, task)
            isSelecting = true
            true
        }

        view.setOnClickListener {
            if (isSelecting) selectTask(p0, task)
        }
    }

    private fun selectTask(holder: ViewHolder, task: Task) {
        holder.isSelected = holder.isSelected.not()
        task.isSelected = task.isSelected.not()
        if (task.isSelected)
            selectedTasks[task.id] = task
        else {
            selectedTasks.remove(task.id)
            if (selectedTasks.isEmpty()) isSelecting = false
        }
        view.onItemSelected()
    }

    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var isSelected = false
            set(value) {
                field = value
                if (!value)
                    itemView.setBackgroundColor(itemView.context.getColor(R.color.white))
                else
                    itemView.setBackgroundColor(itemView.context.getColor(R.color.grey_300))
            }

    }

    interface Delegate {
        fun onItemSelected()
    }
}

