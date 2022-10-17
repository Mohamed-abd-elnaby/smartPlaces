package fahmy.smartplaces.features.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.recyclerview.widget.RecyclerView
import fahmy.smartplaces.databinding.SmartplacesAddressItemBinding
import fahmy.smartplaces.enitities.Result

//
// Created by fahmy on 3/11/20.
//

@Keep
internal class AddressAdapter(var callBack: (Result) -> Unit) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

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

    var results: MutableList<Result> = mutableListOf()
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