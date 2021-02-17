package br.com.heiderlopes.dispenseriotlite

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanActivity : BaseScanActivity(), ZXingScannerView.ResultHandler {

    companion object {
        const val DISPENSER_ID = "DISPENSER_ID"
    }

    private lateinit var mScannerView: ZXingScannerView
    private lateinit var btPermission: Button
    private lateinit var containerPermission: View

    override val baseScannerView: ZXingScannerView?
        get() = mScannerView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scan)

        mScannerView = findViewById(R.id.mScannerView)
        btPermission = findViewById(R.id.btPermission)
        containerPermission = findViewById(R.id.containerPermission)

        btPermission.setOnClickListener {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:$packageName")
            )
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            startActivity(intent)
        }

        super.requestPermission()
    }

    public override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            containerPermission.visibility = View.GONE
            mScannerView.setResultHandler(this)
            mScannerView.startCamera()
        } else {
            containerPermission.visibility = View.VISIBLE
        }
    }

    override fun onPermissionDenied() {
        containerPermission.visibility = View.VISIBLE
    }

    override fun onPermissionGranted() {
        containerPermission.visibility = View.GONE
    }

    override fun handleResult(rawResult: Result?) {
        val dispenserId = rawResult?.text
        val intent = Intent(this, DispenserActivity::class.java)
        intent.putExtra(DISPENSER_ID, dispenserId)
        startActivity(intent)
        finish()
    }
}

