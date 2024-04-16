package mx.tecnm.cdhidalgo.iotapp

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class MainActivity3 : AppCompatActivity() {
    private lateinit var editId: TextView
    private lateinit var editName: EditText
    private lateinit var editType: EditText
    private lateinit var editValue: EditText
    private lateinit var btnSaveEdit: Button
    private lateinit var btnCancelEdit: Button

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editId = findViewById(R.id.editId)
        editName = findViewById(R.id.editName)
        editType = findViewById(R.id.editType)
        editValue = findViewById(R.id.editValue)
        btnSaveEdit = findViewById(R.id.btnSaveEdit)
        btnCancelEdit = findViewById(R.id.btnCancelEdit)
        sharedPreferences = getSharedPreferences("session", 0)

        btnCancelEdit.setOnClickListener { finish() }


        if (intent.extras != null){
            editId.text = intent.extras!!.getString("id")
            editName.setText(intent.extras!!.getString("name"))
            editType.setText(intent.extras!!.getString("type"))
            editValue.setText(intent.extras!!.getString("value"))

            btnSaveEdit.setOnClickListener { saveChanges() }
        } else {
            btnSaveEdit.setOnClickListener { addNew() }
        }
    }

    private fun saveChanges() {
        val data = JSONObject()
        data.put("name", editName.text.toString())
        data.put("type", editType.text.toString())
        data.put("value", editValue.text.toString())

        val url = Uri.parse(Config.URL + "sensors/" + editId.text.toString()).buildUpon().build().toString()
        val request = object: JsonObjectRequest(
            Method.PUT, url, data,
            { response->
                Log.d("RESPONSE", "fill: $response")
                Toast.makeText(this, "Cambios guardados", Toast.LENGTH_LONG).show()
                finish()
            },
            { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            }
        ){
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = sharedPreferences.getString("jwt","") ?: ""
                return params
            }
        }
        ReqSingleton.getInstance(applicationContext).addToRequestQueue(request)
    }

    private fun addNew() {
        val data = JSONObject()
        data.put("name", editName.text.toString())
        data.put("type", editType.text.toString())
        data.put("value", editValue.text.toString())

        val url = Uri.parse(Config.URL + "sensors").buildUpon().build().toString()
        val request = object: JsonObjectRequest(
            Method.POST, url, data,
            { response->
                Log.d("RESPONSE", "fill: $response")
                Toast.makeText(this, "Se agregó la información", Toast.LENGTH_LONG).show()
                finish()
            },
            { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            }
        ){
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = sharedPreferences.getString("jwt","") ?: ""
                return params
            }
        }
        ReqSingleton.getInstance(applicationContext).addToRequestQueue(request)
    }
}