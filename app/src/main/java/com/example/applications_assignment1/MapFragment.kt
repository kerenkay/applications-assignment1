package com.example.applications_assignment1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import com.example.applications_assignment1.utilities.ScoreStorage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Date

//class MapFragment : Fragment(), OnMapReadyCallback {
//
//    private val vm: TopTenViewModel by activityViewModels()
//
//    private var map: GoogleMap? = null
//    private var scores: List<ScoreEntry> = emptyList()
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // 1) מקבלים את המפה (חשוב: childFragmentManager)
//        val mapFragment = childFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//        // 2) מאזינים לשיאים (מה־ViewModel)
//        vm.scores.observe(viewLifecycleOwner) { list ->
//            scores = list
//            drawMarkersIfReady()
//        }
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//        drawMarkersIfReady()
//    }
//
//    private fun drawMarkersIfReady() {
//        val gMap = map ?: return
//        if (scores.isEmpty()) return
//
//        // 3) ניקוי + הוספת markers
//        gMap.clear()
//
//        val valid = scores.filter { it.lat != 0.0 && it.lon != 0.0 }
//        if (valid.isEmpty()) return
//
//        valid.forEach { entry ->
//            val pos = LatLng(entry.lat, entry.lon)
//            gMap.addMarker(
//                MarkerOptions()
//                    .position(pos)
//                    .title("Score: ${entry.score}")
//                    .snippet(Date(entry.timestamp).toString())
//            )
//        }
//
//        // 4) Zoom לנקודה הכי גבוהה (או אחרונה)
//        val focus = valid.maxBy { it.score }
//        gMap.moveCamera(
//            CameraUpdateFactory.newLatLngZoom(LatLng(focus.lat, focus.lon), 10f)
//        )
//    }
//}


//class MapFragment : Fragment(), OnMapReadyCallback {
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val mapFragment = childFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//
//        mapFragment.getMapAsync(this)
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            googleMap.isMyLocationEnabled = true
//        }
//    }
//}

//class MapFragment : Fragment(R.layout.fragment_map) {
//
//    private lateinit var map: MapView
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        map = view.findViewById(R.id.map)
//        map.setMultiTouchControls(true)
//
//        val controller = map.controller
//        controller.setZoom(12.0)
//        controller.setCenter(GeoPoint(32.0853, 34.7818))
//
//        val list = ScoreStorage.loadAll(requireContext())
//        list.filter { it.lat != 0.0 && it.lon != 0.0 }.forEach { e ->
//            val marker = Marker(map)
//            marker.position = GeoPoint(e.lat, e.lon)
//            marker.title = "Score: ${e.score}"
//            map.overlays.add(marker)
//        }
//
//        map.invalidate()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        map.onResume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        map.onPause()
//    }
//}


//class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val mapFrag = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFrag.getMapAsync(this)
//    }
//
//    override fun onMapReady(map: GoogleMap) {
//        val list = ScoreStorage.loadAll(requireContext())
//
//        list.filter { it.lat != 0.0 && it.lon != 0.0 }.forEach { e ->
//            map.addMarker(
//                MarkerOptions()
//                    .position(LatLng(e.lat, e.lon))
//                    .title("Score: ${e.score}")
//            )
//        }
//
//        list.firstOrNull { it.lat != 0.0 && it.lon != 0.0 }?.let {
//            map.moveCamera(
//                CameraUpdateFactory.newLatLngZoom(LatLng(it.lat, it.lon), 11f)
//            )
//        }
//    }
//}



//package com.example.applications_assignment1
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [MapFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class MapFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_map, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment MapFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            MapFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}