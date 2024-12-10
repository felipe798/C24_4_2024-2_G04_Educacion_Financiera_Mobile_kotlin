package com.principe.felipe.finango_d1.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.principe.felipe.finango_d1.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        // Ubicación del destino
        val destination = intent.getStringExtra("address")
        val destinationCoordinates = if (destination == "Av. Carretera Central 111 Nivel 1 Int. LF-3A Lima Map, Santa Anita 15008") {
            LatLng(-12.062106, -76.970611)
        } else {
            LatLng(-12.059502, -76.971841)
        }

        // Mostrar marcador
        googleMap.addMarker(MarkerOptions().position(destinationCoordinates).title(destination))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationCoordinates, 15f))

        // Obtener la ubicación del usuario y trazar la ruta
        getUserLocation { userCoordinates ->
            drawRoute(userCoordinates, destinationCoordinates)
        }
    }

    private fun getUserLocation(callback: (LatLng) -> Unit) {
        // Simulación de ubicación del usuario (esto se debe reemplazar con GPS real)
        val userLocation = LatLng(-12.063373, -76.970425) // Ejemplo: Cerca del destino
        callback(userLocation)
    }

    private fun drawRoute(origin: LatLng, destination: LatLng) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = buildDirectionsUrl(origin, destination)
            val request = Request.Builder().url(url).build()

            try {
                val response = client.newCall(request).execute()
                val json = JSONObject(response.body?.string().orEmpty())
                val routes = json.getJSONArray("routes")
                if (routes.length() > 0) {
                    val points = decodePolyline(
                        routes.getJSONObject(0)
                            .getJSONObject("overview_polyline")
                            .getString("points")
                    )

                    // Dibujar la ruta en el mapa
                    runOnUiThread {
                        googleMap.addPolyline(
                            PolylineOptions().addAll(points).color(resources.getColor(R.color.teal_200))
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun buildDirectionsUrl(origin: LatLng, destination: LatLng): String {
        val apiKey = "YOUR_API_KEY"
        return "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=${origin.latitude},${origin.longitude}" +
                "&destination=${destination.latitude},${destination.longitude}" +
                "&key=$apiKey"
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = mutableListOf<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dLat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dLat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dLng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dLng

            poly.add(LatLng(lat / 1E5, lng / 1E5))
        }

        return poly
    }
}
