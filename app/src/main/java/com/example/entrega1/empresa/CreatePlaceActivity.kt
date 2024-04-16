package com.example.entrega1.empresa

import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.entrega1.R
import com.example.entrega1.databinding.ActivityCreatePlaceBinding
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

class CreatePlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePlaceBinding
    private var longPressedMarker: Marker? = null
    private lateinit var mapController : IMapController

    override fun onResume() {
        super.onResume()
        binding.osmMapPlace.onResume()
        setInitialPoint()
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

        binding.osmMapPlace.setTileSource(TileSourceFactory.MAPNIK)
        binding.osmMapPlace.setMultiTouchControls(true)
        binding.osmMapPlace.overlays.add(createOverlaysEvents())
        mapController = binding.osmMapPlace.controller

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
}