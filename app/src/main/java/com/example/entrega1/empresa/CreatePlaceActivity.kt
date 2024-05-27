package com.example.entrega1.empresa

import android.app.UiModeManager
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.entrega1.R
import com.example.entrega1.databinding.ActivityCreatePlaceBinding
import com.example.entrega1.utils.data.Places
import com.example.entrega1.utils.data.UserProvider
import com.example.entrega1.utils.misc.NavInit
import com.example.entrega1.utils.schemas.Place
import com.example.entrega1.utils.schemas.User
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.TilesOverlay

class CreatePlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePlaceBinding
    private var longPressedMarker: Marker? = null
    private lateinit var mapController : IMapController
    private lateinit var actualUser: User
    private lateinit var toggle: ActionBarDrawerToggle

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

        actualUser = UserProvider.actualUser!!

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
                null
            )

            Places.addPlaceUser(actualUser, newPlace)
            Toast.makeText(baseContext, "Lugar añadido con exito", Toast.LENGTH_SHORT).show()

            resetUI()
        }
    }

    private fun createOverlaysEvents() : MapEventsOverlay {
        val overlayEvents = MapEventsOverlay(object : MapEventsReceiver{
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}