package com.example.entrega1

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.entrega1.utils.data.LoginStub
import com.example.entrega1.utils.misc.NavInit
import com.example.entrega1.utils.schemas.User
import kotlin.math.roundToInt

class LocationActivity : AppCompatActivity() {

    private lateinit var btnAgregarFoto: Button
    private lateinit var imagenLugar: ImageView
    private lateinit var toggleButton: ActionBarDrawerToggle

    private val takePicture: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                handleImageResult(data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)


        var user = intent.getParcelableExtra<User>("user")

        if (user == null) {
            user = LoginStub.anonymousUser
            Log.i("USER HUELLA", "El usuario se ha loggeado con la huella, hay que hacer eso")
        }


        toggleButton = NavInit().initNavigationBar(user, R.id.navViewLocation, R.id.drawerLayoutLocation, this)


        btnAgregarFoto = findViewById(R.id.btnAgregarFoto)
        imagenLugar = findViewById(R.id.imagenLugar)

        btnAgregarFoto.setOnClickListener {
            if (checkCameraPermission()) {
                dispatchTakePictureIntent()
            } else {
                requestCameraPermission()
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            takePicture.launch(takePictureIntent)
        }
    }

    private fun handleImageResult(data: Intent?) {
        val extras = data?.extras
        val imageBitmap = extras?.get("data") as Bitmap

        imagenLugar.setImageBitmap( Bitmap.createScaledBitmap(imageBitmap, 1020, 1020, false) )
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggleButton.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
