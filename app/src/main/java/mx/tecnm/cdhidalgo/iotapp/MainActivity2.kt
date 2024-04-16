package mx.tecnm.cdhidalgo.iotapp

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest

class MainActivity2 : AppCompatActivity(), ItemListener {
    private lateinit var itemsList: RecyclerView
    private lateinit var session: SharedPreferences
    private lateinit var list: Array<Array<String?>>
    private lateinit var btnRefresh: Button
    private lateinit var btnNew: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        itemsList = findViewById(R.id.itemsList)
        session = getSharedPreferences("session", 0)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnNew = findViewById(R.id.btnAddNew)

        itemsList.setHasFixedSize(true)
        itemsList.itemAnimator = DefaultItemAnimator()
        itemsList.layoutManager = LinearLayoutManager(this)
        fill()

        btnRefresh.setOnClickListener {
            fill()
        }

        btnNew.setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java))
        }
    }

    private fun fill() {
        val url = Uri.parse(Config.URL + "sensors").buildUpon().build().toString()
        val request = object:JsonObjectRequest(Method.GET, url, null,
            { response->
                Log.d("RESPONSE", "fill: $response")
                val data = response.getJSONArray("data")
                list = Array(data.length()){ arrayOfNulls(5) }
                for (i in 0 until data.length()){
                    val sensor = data.getJSONObject(i)
                    list[i][0] = sensor.getString("id")
                    list[i][1] = sensor.getString("name")
                    list[i][2] = sensor.getString("type")
                    list[i][3] = sensor.getString("value")
                    list[i][4] = sensor.getString("date")
                }
                itemsList.adapter = ItemsAdapter(list, this)
            },
            { error ->
                Log.e("ERROR", "error: ${error.message ?: "Error!!"}")
            }
        ){
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = session.getString("jwt","") ?: ""
                return params
            }
        }
        ReqSingleton.getInstance(applicationContext).addToRequestQueue(request)
    }

    override fun onClick(view: View, position: Int) {
        Toast.makeText(
            this,
            "Posición seleccionada: $position", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onEdit(view: View, position: Int) {
        val intent = Intent(this, MainActivity3::class.java)
        intent.putExtra("id", list[position][0])
        intent.putExtra("name", list[position][1])
        intent.putExtra("type", list[position][2])
        intent.putExtra("value", list[position][3])
        startActivity(intent)
    }

    override fun onDelete(view: View, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("ELIMINACIÓN")
            .setMessage("¿Eliminar el item ${list[position][1]}?")
            .setPositiveButton("Sí") { dialog, which ->
                val url = Uri.parse(Config.URL + "sensors/${list[position][0]}")
                    .buildUpon()
                    .build().toString()
                val post = object : StringRequest(Method.DELETE, url, { _ ->
                    fill()
                }, { error ->
                    Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                    fill()
                })
                {
                    override fun getHeaders(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["Authorization"] = session.getString("jwt","") ?: ""
                        return params
                    }
                }
                ReqSingleton.getInstance(applicationContext).addToRequestQueue(post)
            }
            .setOnCancelListener {  }
            .show()
    }
}