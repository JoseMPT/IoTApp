package mx.tecnm.cdhidalgo.iotapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(private val dataset: Array<Array<String?>>?, private val listener: ItemListener) :
    RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, listener: ItemListener) :
        RecyclerView.ViewHolder(itemView) {
        var txtItemId: TextView
        var txtItemType: TextView
        var txtItemName: TextView
        var txtItemValue: TextView
        var txtItemDate: TextView
        private var btnItemEdit: Button
        private var btnItemDelete: Button
        init {
            txtItemId = itemView.findViewById(R.id.txtItemId)
            txtItemType = itemView.findViewById(R.id.txtItemType)
            txtItemName = itemView.findViewById(R.id.txtItemName)
            txtItemValue = itemView.findViewById(R.id.txtItemValue)
            txtItemDate = itemView.findViewById(R.id.txtItemDate)
            btnItemEdit = itemView.findViewById(R.id.btnEdit)
            btnItemDelete = itemView.findViewById(R.id.btnDelete)

            btnItemEdit.setOnClickListener { view -> listener.onEdit(view, adapterPosition) }
            btnItemDelete.setOnClickListener { view -> listener.onDelete(view, adapterPosition) }
            itemView.setOnClickListener { view -> listener.onClick(view, adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sensor, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtItemId.text = dataset!![position][0]
        holder.txtItemName.text = dataset[position][1]
        holder.txtItemType.text = dataset[position][2]
        holder.txtItemValue.text = dataset[position][3]
        holder.txtItemDate.text = dataset[position][4]
    }

    override fun getItemCount(): Int {
        return dataset!!.size
    }
}