package br.com.heiderlopes.dispenseriotlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var btScanQRCode: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btScanQRCode = findViewById(R.id.btScanQRCode)

        btScanQRCode.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }
    }
}