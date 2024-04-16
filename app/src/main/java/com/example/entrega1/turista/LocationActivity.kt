package com.example.entrega1.turista

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.entrega1.R
import com.example.entrega1.utils.data.LoginStub
import com.example.entrega1.utils.misc.NavInit
import com.example.entrega1.utils.schemas.User
import android.widget.EditText
import com.example.entrega1.utils.data.Places
import com.example.entrega1.utils.schemas.Place
import java.io.IOException
import java.io.InputStream
import android.app.UiModeManager
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.widget.Toast
import com.example.entrega1.databinding.ActivityCreatePlaceBinding
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.TilesOverlay


class LocationActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var imageView: ImageView
    private lateinit var actualUser: User
    private lateinit var binding: ActivityCreatePlaceBinding
    private var longPressedMarker: Marker? = null
    private lateinit var mapController : IMapController

    private var selectedImage: Bitmap? = null

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                handleImageResult(data)
            }
        }

    private val pickGalleryImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                handleGalleryImageResult(data)
            }
        }

    override fun onResume() {
        super.onResume()
        binding.osmMapPlace.onResume()
        setInitialPoint()

        val uiManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        if (uiManager.nightMode == UiModeManager.MODE_NIGHT_YES) {
            binding.osmMapPlace.overlayManager.tilesOverlay.setColorFilter(TilesOverlay.INVERT_COLORS)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.osmMapPlace.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = applicationContext.packageName

        binding = ActivityCreatePlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actualUser = intent.getParcelableExtra<User>("user")!!

        toggle = NavInit().initNavigationBar(actualUser, R.id.navViewEnterprise, R.id.drawerLayoutEnterprise, this)


        binding.osmMapPlace.setTileSource(TileSourceFactory.MAPNIK)
        binding.osmMapPlace.setMultiTouchControls(true)
        binding.osmMapPlace.overlays.add(createOverlaysEvents())
        mapController = binding.osmMapPlace.controller
        binding.addPlaceButton.setOnClickListener {
            if (longPressedMarker == null) {
                Toast.makeText(applicationContext, "No tienes un marcador", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.placeName.text.toString() == "") {
                Toast.makeText(applicationContext, "Agrega un titulo del lugar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.placeDescription.text.toString() == "") {
                Toast.makeText(applicationContext, "Agrega una descripción del lugar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newPlace = Place(
                longPressedMarker?.position!!,
                binding.placeName.text.toString(),
                longPressedMarker?.subDescription.toString(),
                binding.placeDescription.text.toString(),
                selectedImage
            )

            Places.addPlaceUser(actualUser, newPlace)
            Toast.makeText(baseContext, "Lugar añadido con exito", Toast.LENGTH_SHORT).show()

            resetUI()
        }

        if (actualUser == null) {
            actualUser = LoginStub.anonymousUser
            Log.i("USER HUELLA", "El usuario se ha loggeado con la huella, hay que hacer eso")
        }

        toggle = NavInit().initNavigationBar(
            actualUser,
            R.id.navViewLocation,
            R.id.drawerLayoutLocation, this
        )

        imageView = findViewById(R.id.imageView)

        findViewById<View>(R.id.btnGallery).setOnClickListener {
            openGallery()
        }

        findViewById<View>(R.id.btnCamera).setOnClickListener {
            openCamera()
        }
    }

    private fun createOverlaysEvents() : MapEventsOverlay {
        val overlayEvents = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                return false
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                longPressOnMap(p!!)
                return true
            }

        })

        return overlayEvents
    }

    private fun longPressOnMap(p: GeoPoint) {
        longPressedMarker?.let { binding.osmMapPlace.overlays.remove(it) }

        val geocoder = Geocoder(baseContext)
        val address = geocoder.getFromLocation(p.latitude, p.longitude, 1)
        longPressedMarker = createMarker(p, address!![0].getAddressLine(0), address[0].countryName, 0)
        longPressedMarker?.let { binding.osmMapPlace.overlays.add(it) }
    }
    private fun createMarker(p: GeoPoint, title: String?, desc: String?, iconID: Int): Marker? {
        var marker : Marker? = null
        if (binding.osmMapPlace == null) return marker

        marker = Marker(binding.osmMapPlace)

        title?.let { marker.title = it }
        desc?.let { marker.subDescription = it }
        if (iconID != 0) {
            val myIcon = resources.getDrawable(iconID, this.theme)
            marker.icon = myIcon
        }
        marker.position = p
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        return marker
    }

    private fun setInitialPoint() {
        // Javeriana
        val startPoint = GeoPoint(4.628208456021053, -74.06430006640453)
        mapController.setZoom(18.0)
        mapController.setCenter(startPoint)
    }

    private fun resetUI() {
        binding.placeName.setText("")
        binding.placeDescription.setText("")
        longPressedMarker?.let { binding.osmMapPlace.overlays.remove(it) }
        setInitialPoint()
    }

    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_EXTERNAL_STORAGE_PERMISSION
            )
        } else {
            pickGalleryImage.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            takePicture.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
    }

    private fun handleImageResult(data: Intent?) {
        val extras = data?.extras
        selectedImage = data?.extras?.get("data") as? Bitmap?
        imageView.setImageBitmap(selectedImage)
    }

    private fun handleGalleryImageResult(data: Intent?) {
        val imageUri = data?.data
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri!!)
            selectedImage = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            imageView.setImageBitmap(selectedImage)
        } catch (e: IOException) {
            e.printStackTrace()
            // Manejar cualquier error que pueda ocurrir al decodificar la imagen
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    companion object {
        private const val REQUEST_EXTERNAL_STORAGE_PERMISSION = 101
        private const val REQUEST_CAMERA_PERMISSION = 102
    }
}
