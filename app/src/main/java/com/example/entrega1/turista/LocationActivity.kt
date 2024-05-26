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
import com.example.entrega1.databinding.ActivityLocationBinding
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
    private lateinit var imageVieww: ImageView
    private lateinit var actualUser: User
    private lateinit var binding: ActivityLocationBinding
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
        binding.osmMapUser.onResume()
        setInitialPoint()

        val uiManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        if (uiManager.nightMode == UiModeManager.MODE_NIGHT_YES) {
            binding.osmMapUser.overlayManager.tilesOverlay.setColorFilter(TilesOverlay.INVERT_COLORS)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.osmMapUser.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = applicationContext.packageName

        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actualUser = intent.getParcelableExtra<User>("user")!!

        //toggle = NavInit().initNavigationBar(actualUser, R.id.navViewEnterprise, R.id.drawerLayoutEnterprise, this)


        binding.osmMapUser.setTileSource(TileSourceFactory.MAPNIK)
        binding.osmMapUser.setMultiTouchControls(true)
        binding.osmMapUser.overlays.add(createOverlaysEvents())
        mapController = binding.osmMapUser.controller
        binding.addPlaceButtonUser.setOnClickListener {
            if (longPressedMarker == null) {
                Toast.makeText(applicationContext, "No tienes un marcador", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.placeNameUser.text.toString() == "") {
                Toast.makeText(applicationContext, "Agrega un titulo del lugar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.placeDescriptionUser.text.toString() == "") {
                Toast.makeText(applicationContext, "Agrega una descripción del lugar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newPlace = Place(
                longPressedMarker?.position!!,
                binding.placeNameUser.text.toString(),
                longPressedMarker?.subDescription.toString(),
                binding.placeDescriptionUser.text.toString(),
                selectedImage
            )

            Places.addPlaceUser(actualUser, newPlace)
            Log.i("PLACES", Places.printPlaces())
            Toast.makeText(baseContext, "Lugar añadido con exito", Toast.LENGTH_SHORT).show()

            resetUI()
        }

        if (actualUser == null) {
            actualUser = User("anonymous@example.com", null, "Anonymous", "user")
            Log.i("USER HUELLA", "El usuario se ha loggeado con la huella, hay que hacer eso")
        }

        imageVieww = binding.imageView

        binding.btnGallery.setOnClickListener {
            openGallery()
        }

        binding.btnCamera.setOnClickListener {
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
        longPressedMarker?.let { binding.osmMapUser.overlays.remove(it) }

        val geocoder = Geocoder(baseContext)
        val address = geocoder.getFromLocation(p.latitude, p.longitude, 1)
        longPressedMarker = createMarker(p, address!![0].getAddressLine(0), address[0].countryName, 0)
        longPressedMarker?.let { binding.osmMapUser.overlays.add(it) }
    }
    private fun createMarker(p: GeoPoint, title: String?, desc: String?, iconID: Int): Marker? {
        var marker : Marker? = null
        if (binding.osmMapUser == null) return marker

        marker = Marker(binding.osmMapUser)

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
        binding.placeNameUser.setText("")
        binding.placeDescriptionUser.setText("")
        longPressedMarker?.let { binding.osmMapUser.overlays.remove(it) }
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
        imageVieww.setImageBitmap(selectedImage)
    }

    private fun handleGalleryImageResult(data: Intent?) {
        val imageUri = data?.data
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri!!)
            selectedImage = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            imageVieww.setImageBitmap(selectedImage)
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
