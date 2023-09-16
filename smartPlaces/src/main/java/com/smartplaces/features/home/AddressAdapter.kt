package com.smartplaces.features.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.recyclerview.widget.RecyclerView
import com.smartplaces.databinding.SmartplacesAddressItemBinding
import com.smartplaces.enitities.Result

//
// Created by fahmy on 3/11/20.
//

@Keep
internal class AddressAdapter(var callBack: (Result) -> Unit) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(mResults: List<Result>) {
        results=mResults
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged") fun clearAll(){
        results= listOf()
        notifyDataSetChanged()
    }

    class ViewHolder(var view: SmartplacesAddressItemBinding, var callBack: (Result) -> Unit) :
        RecyclerView.ViewHolder(view.root) {
        fun display(result: Result) {
            view.apply {
                tvAddress.text = result.name
                tvAddress.setOnClickListener {
                    callBack(result)
                }
            }
        }

    }

    private var results: List<Result> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SmartplacesAddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)) {
            callBack(it)
        }

    }

    override fun getItemCount(): Int {
        return results.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.display(results[position])
    }
}