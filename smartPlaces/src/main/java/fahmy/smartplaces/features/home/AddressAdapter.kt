package fahmy.smartplaces.features.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fahmy.smartplaces.R
import fahmy.smartplaces.enitities.Result
import kotlinx.android.synthetic.main.smartplaces_address_item.view.*

//
// Created by fahmy on 3/11/20.
//

class AddressAdapter(var results: List<Result>, var callBack: (Result) -> Unit) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    class ViewHolder(var view: View, var callBack: (Result) -> Unit) :
        RecyclerView.ViewHolder(view) {
        fun display(result: Result) {
            view.apply {
                tv_address.text = result.name
                tv_address.setOnClickListener {
                    callBack(result)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val item =
            LayoutInflater.from(SmartPlaces.mContext)
                .inflate(R.layout.smartplaces_address_item, parent, false)

        return ViewHolder(item) {
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