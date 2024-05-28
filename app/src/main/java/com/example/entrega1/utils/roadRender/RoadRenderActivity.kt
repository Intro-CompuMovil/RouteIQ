package com.example.entrega1.utils.roadRender

import android.app.UiModeManager
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import com.example.entrega1.R
import com.example.entrega1.databinding.ActivityRoadRenderBinding
import com.example.entrega1.utils.data.PlacesRender
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.TilesOverlay

class RoadRenderActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRoadRenderBinding
    private lateinit var roadManager: RoadManager

    private var roadOverlay: Polyline? = null

    override fun onResume() {
        super.onResume()
        binding.mapOSM.onResume()

        val uiManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        if (uiManager.nightMode == UiModeManager.MODE_NIGHT_YES) {
            binding.mapOSM.overlayManager.tilesOverlay.setColorFilter(TilesOverlay.INVERT_COLORS)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.mapOSM.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoadRenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        binding.mapOSM.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapOSM.setMultiTouchControls(true)

        roadManager = OSRMRoadManager(this, "ANDROID")

        drawRoute()
    }

    // Toma lo que este en el companion object de PlacesRender
    private fun drawRoute(){
        val routePoints = PlacesRender.getGeopointsPlaces()
        val road = roadManager.getRoad(routePoints)

        Log.i("OSM_acticity", "Route length: ${road.mLength} klm")
        Log.i("OSM_acticity", "Duration: ${road.mDuration / 60} min")

        if (binding.mapOSM != null) {
            roadOverlay?.let { binding.mapOSM.overlays.remove(it) }
            roadOverlay = RoadManager.buildRoadOverlay(road)
            roadOverlay?.outlinePaint?.color = Color.RED
            roadOverlay?.outlinePaint?.strokeWidth = 10f
            binding.mapOSM.overlays.add(roadOverlay)

            binding.mapOSM.controller.setCenter(routePoints[0])
            binding.mapOSM.controller.setZoom(18.0)

            for (point in PlacesRender.getPlaces()) {
                createMarker(point.location.getGeoPoint(), point.title, point.description, 0)?.let {
                    binding.mapOSM.overlays.add(it)
                }
            }
        }
    }

    private fun createMarker(p: GeoPoint, title: String?, desc: String?, iconID: Int): Marker? {
        var marker : Marker? = null
        if (binding.mapOSM == null) return marker

        marker = Marker(binding.mapOSM)

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
}