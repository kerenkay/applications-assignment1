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
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import java.util.Date

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private val vm: TopTenViewModel by activityViewModels()
    private var gMap: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFrag = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFrag.getMapAsync(this)

        vm.top10.observe(viewLifecycleOwner) { list ->
            drawIfReady(list)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        gMap = map

        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(32.0853, 34.7818), 10f)
        )

        android.util.Log.d("MAP", "onMapReady called")

        vm.top10.value?.let { drawIfReady(it) }
    }

    private fun drawIfReady(list: List<ScoreEntry>) {
        val googleMap = gMap ?: return

        val top10 = list
            .sortedByDescending { it.score }
            .take(10)

        googleMap.clear()

        val valid = top10.filter { it.lat != 0.0 && it.lon != 0.0 }
        if (valid.isEmpty()) {
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(32.0853, 34.7818), 10f)
            )
            return
        }

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
                val bounds = boundsBuilder.build()
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 120))
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
        val pos = if (lat != 0.0 && lon != 0.0) LatLng(lat, lon) else LatLng(32.0853, 34.7818)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
    }
}

//class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {
//
//    private val vm: TopTenViewModel by activityViewModels()
//
//    private var gMap: GoogleMap? = null
//    private var scores: List<ScoreEntry> = emptyList()
//    private var map: GoogleMap? = null
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val mapFrag = childFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFrag.getMapAsync(this)
//
//        vm.top10.observe(viewLifecycleOwner) { list ->
//            scores = list
//            drawIfReady()
//        }
//    }
//
//    override fun onMapReady(map: GoogleMap) {
//        gMap = map
//        drawIfReady()
//    }
//
//    private fun drawIfReady() {
//        val map = gMap ?: return
//        val list = scores.filter { it.lat != 0.0 && it.lon != 0.0 }
//        if (list.isEmpty()) return
//
//        map.clear()
//
//        list.forEach { e ->
//            map.addMarker(
//                MarkerOptions()
//                    .position(LatLng(e.lat, e.lon))
//                    .title("Score: ${e.score}")
//            )
//        }
//
//        val focus = list.maxBy { it.score }
//        map.moveCamera(
//            CameraUpdateFactory.newLatLngZoom(LatLng(focus.lat, focus.lon), 10f)
//        )
//    }
//        fun zoomTo(lat: Double, lon: Double) {
//        val googleMap = map ?: return
//
//        // Use default location if coordinates are missing (same logic as onMapReady)
//        val pos = if (lat != 0.0 && lon != 0.0) {
//            LatLng(lat, lon)
//        } else {
//             LatLng(32.0853, 34.7818)
//        }
//
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
//    }
//}

//
//class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
//        mapFragment?.getMapAsync(this)
//    }
//
//    fun zoomTo(lat: Double, lon: Double) {
//        val googleMap = map ?: return
//
//        // Use default location if coordinates are missing (same logic as onMapReady)
//        val pos = if (lat != 0.0 && lon != 0.0) {
//            LatLng(lat, lon)
//        } else {
//             LatLng(32.0853, 34.7818)
//        }
//
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
//    }
//
//    // Store map reference
//    private var map: GoogleMap? = null
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//        val context = context ?: return
//        val scores = ScoreStorage.loadAll(context)
//
//        // Default location: Tel Aviv
//        val defaultLoc = LatLng(32.0853, 34.7818)
//
//        scores.forEach { entry ->
//            val position = if (entry.lat != 0.0 && entry.lon != 0.0) {
//                LatLng(entry.lat, entry.lon)
//            } else {
//                defaultLoc
//            }
//
//            googleMap.addMarker(
//                MarkerOptions()
//                    .position(position)
//                    .title("Score: ${entry.score}")
//                    .snippet(Date(entry.timestamp).toString())
//            )
//        }
//
//        // Center map
//        if (scores.isNotEmpty()) {
//            val validOrFirst = scores.firstOrNull { it.lat != 0.0 && it.lon != 0.0 }
//            val target = if (validOrFirst != null) {
//                LatLng(validOrFirst.lat, validOrFirst.lon)
//            } else {
//                defaultLoc
//            }
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 12f))
//        } else {
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 12f))
//        }
//    }
//}