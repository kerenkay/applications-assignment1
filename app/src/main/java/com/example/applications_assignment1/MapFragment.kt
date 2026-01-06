package com.example.applications_assignment1

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.applications_assignment1.utilities.ScoreStorage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLngBounds
import java.util.Date

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private var gMap: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFrag = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFrag.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        gMap = map

        val top10 = ScoreStorage.loadTop10()
        drawMarkers(top10)
    }

    private fun drawMarkers(list: List<ScoreEntry>) {
        val googleMap = gMap ?: return

        val top10 = list
            .sortedByDescending { it.score }
            .take(10)

        val valid = top10.filter { it.lat != 0.0 && it.lon != 0.0 }

        if (valid.isEmpty()) {
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(32.0853, 34.7818), 10f)
            )
            return
        }

        googleMap.clear()

        val boundsBuilder = LatLngBounds.Builder()

        valid.forEachIndexed { index, e ->
            val rank = index + 1
            val pos = LatLng(e.lat, e.lon)

            googleMap.addMarker(
                MarkerOptions()
                    .position(pos)
                    .title("#$rank  Score: ${e.score}")
                    .snippet(Date(e.timestamp).toString())
            )

            boundsBuilder.include(pos)
        }

        view?.post {
            try {
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 120)
                )
            } catch (_: Exception) {
                val first = valid.first()
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(LatLng(first.lat, first.lon), 12f)
                )
            }
        }
    }

    fun zoomTo(lat: Double, lon: Double) {
        val googleMap = gMap ?: return
        val pos =
            if (lat != 0.0 && lon != 0.0) LatLng(lat, lon)
            else LatLng(32.0853, 34.7818)

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
    }

    fun refresh() {
        drawMarkers(ScoreStorage.loadTop10())
    }
}
