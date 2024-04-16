package com.example.entrega1.turista

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.entrega1.R
import com.example.entrega1.databinding.ActivityMapTouristBinding
import com.example.entrega1.utils.data.Places
import com.example.entrega1.utils.schemas.Place
import com.example.entrega1.utils.schemas.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.TilesOverlay
import java.security.Permissions

class MapTouristActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMapTouristBinding
    private lateinit var user: User
    private var userPlaces: HashSet<Place>? = null
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    override fun onResume() {
        super.onResume()
        binding.osmUser.onResume()

        val controller: IMapController = binding.osmUser.controller

        controller.setZoom(12.5)
        controller.setCenter(GeoPoint(4.658516673824757, -74.1064970215919))

        val uiManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        if (uiManager.nightMode == UiModeManager.MODE_NIGHT_YES) {
            binding.osmUser.overlayManager.tilesOverlay.setColorFilter(TilesOverlay.INVERT_COLORS)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.osmUser.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapTouristBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        user = intent.getParcelableExtra("user")!!

        Configuration.getInstance().userAgentValue = applicationContext.packageName

        binding.osmUser.setTileSource(TileSourceFactory.MAPNIK)
        binding.osmUser.setMultiTouchControls(true)

        userPlaces = Places.getPlacesFromUser(user)

        if (userPlaces == null) {
            Toast.makeText(this, "No tienes sitios guardados", Toast.LENGTH_SHORT).show()
        } else {
            for (place in userPlaces!!) {
                createMarkerPlace(place)
            }
        }

        requestPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            "Para acceder a la localizacion",
            1
        )
    }

    private fun createMarkerPlace(place: Place) {
        val marker = Marker(binding.osmUser)
        marker.title = place.title
        val myIcon = BitmapDrawable(resources, place.bitmap)
        marker.icon = myIcon
        marker.position = place.location
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.subDescription = place.description
        binding.osmUser.overlays.add(marker)
    }

    private fun createMarkerUser(lat: Double, lng: Double) {
        val marker = Marker(binding.osmUser)
        marker.title = "Usted esta aquí"
        marker.position = GeoPoint(lat, lng)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.subDescription = "Usted se encuentra aqui"
        marker.textLabelForegroundColor = androidx.appcompat.R.color.material_blue_grey_800
        binding.osmUser.overlays.add(marker)
    }

    private fun requestPermission(context: Activity, permission: String, justify: String, idCode: Int) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permiso dado
                mFusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    Log.i("LATITUDE", it.latitude.toString())
                    Log.i("LONGITUDE", it.longitude.toString())
                    createMarkerUser(it.latitude, it.longitude)
                }

            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                context,
                permission
            ) -> {
                // Explicar porque mierda necesitamos el permiso
                requestPermissions(
                    arrayOf(permission),
                    idCode
                )
            }

            else -> {
                requestPermissions(
                    arrayOf(permission),
                    idCode
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Gracias", Toast.LENGTH_SHORT).show()
                    //contactsText.text = "PERMISSION GRANTED"
                    //contactsText.setTextColor(Color.GREEN)
                } else {
                    Toast.makeText(this, "Hijo de fruta", Toast.LENGTH_SHORT).show()
                    //contactsText.text = "PERMISSION DENIED"
                    //contactsText.setTextColor(Color.RED)
                }

                return
            }

            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permiso de localizacion", Toast.LENGTH_SHORT).show()
                    getLocation()
                } else {
                    Toast.makeText(this, "Funcionalidades reducidas", Toast.LENGTH_SHORT).show()
                }
            }

            else -> {
                // Ignorar todos los demas permisos
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        mFusedLocationProviderClient.lastLocation.addOnSuccessListener {
            Log.i("LATITUDE", it.latitude.toString())
            Log.i("LONGITUDE", it.longitude.toString())
        }
    }
}