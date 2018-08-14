package io.incepted.ultrafittimer.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.incepted.ultrafittimer.R
import io.incepted.ultrafittimer.databinding.PresetListItemBinding
import io.incepted.ultrafittimer.db.model.Preset
import io.incepted.ultrafittimer.viewmodel.PresetListViewModel
import kotlinx.android.synthetic.main.preset_list_item.view.*

class PresetAdapter(var data: MutableList<Preset>, val viewModel: PresetListViewModel)
    : RecyclerView.Adapter<PresetAdapter.ViewHolder>() {


    class ViewHolder(val itemBinding: PresetListItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.preset_list_item, parent, false)
        return ViewHolder(binding as PresetListItemBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemBinding = holder.itemBinding
        itemBinding.root.preset_item_container.setOnLongClickListener {
            viewModel.onLongClick(data[position].id ?: -1L)
            true
        }
        itemBinding.preset = data[position]
        itemBinding.executePendingBindings()
    }


    override fun getItemCount(): Int {
        return data.size
    }


    private fun setList(newData: MutableList<Preset>) {
        this.data = newData
        notifyDataSetChanged()
    }


    fun replaceData(newData: MutableList<Preset>) {
        setList(newData)
    }
}