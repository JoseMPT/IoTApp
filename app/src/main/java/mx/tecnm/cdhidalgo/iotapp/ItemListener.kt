package mx.tecnm.cdhidalgo.iotapp

import android.view.View

interface ItemListener {
    fun onClick(view: View, position: Int)
    fun onEdit(view: View, position: Int)
    fun onDelete(view: View, position: Int)
}