package mx.tecnm.cdhidalgo.iotapp

import ReqSingleton
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.toolbox.StringRequest
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private lateinit var textUsername : TextInputLayout
    private lateinit var textPassword : TextInputLayout
    private lateinit var btnLogin : Button
    private lateinit var session: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textUsername = findViewById(R.id.inputUsername)
        textPassword = findViewById(R.id.inputPassword)
        btnLogin = findViewById(R.id.btnLogin)
        session = getSharedPreferences("session", 0)

        btnLogin.setOnClickListener { login() }
    }

    private fun login() {
        val username = textUsername.editText?.text.toString()
        val pass = textPassword.editText?.text.toString()
        val url = Uri.parse(Config.URL + "login")
            .buildUpon()
            .build().toString()

        val post = object:StringRequest(Method.POST, url, {
           response -> with(session.edit()){
                putString("jwt", response)
                putString("username", username)
                apply()
           }
            startActivity(Intent(this, MainActivity2::class.java))
        }, {
            error -> Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["username"] = username
                params["password"] = pass
                return params
            }
        }

        ReqSingleton.getInstance(applicationContext).addToRequestQueue(post)
    }
}
