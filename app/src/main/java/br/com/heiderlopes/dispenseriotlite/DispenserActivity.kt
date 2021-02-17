package br.com.heiderlopes.dispenseriotlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DispenserActivity : AppCompatActivity() {

    private lateinit var tvLevel: TextView
    private lateinit var btRecharge: Button

    private var dispenserId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispenser)

        tvLevel = findViewById(R.id.tvLevel)
        btRecharge = findViewById(R.id.btRecharge)

        btRecharge.setOnClickListener {
            recharge(dispenserId)
        }

        dispenserId = intent.getStringExtra(ScanActivity.DISPENSER_ID) ?: ""

        loadLevel(dispenserId)
    }

    private fun getDispenserService(): DispenserService {
        return Retrofit.Builder()
            .baseUrl("http://192.168.0.9:1880")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(DispenserService::class.java)
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun loadLevel(dispenserID: String) {
        getDispenserService().getDispenser(dispenserID)
            .enqueue(object : Callback<DispenserLevel> {
                override fun onFailure(call: Call<DispenserLevel>, t: Throwable) {
                    showMessage(t.message?: "Não foi possível realizar a requisição")
                }

                override fun onResponse(
                    call: Call<DispenserLevel>,
                    response: Response<DispenserLevel>
                ) {
                    if (response.isSuccessful) {
                        val dispenserLevel = response.body()
                        tvLevel.text = dispenserLevel?.percentageValue.toString() + "%"
                    } else {
                        showMessage("Não foi possível recuperar o nível do dispositivo")
                        tvLevel.text = "-%"
                    }
                }
            })
    }

    private fun recharge(dispenserID: String) {
        getDispenserService().recharge(dispenserID, DispenserRecharge(400))
            .enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    showMessage(t.message?: "Não foi possível realizar a requisição")
                }


                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        showMessage("Recarga realizada com sucesso!")
                    } else {
                        showMessage("Não foi possível realizar a recarga")
                    }
                }
            })
    }
}