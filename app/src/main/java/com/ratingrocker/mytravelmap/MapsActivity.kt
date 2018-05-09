package com.ratingrocker.mytravelmap

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.maps.model.Marker
import com.ratingrocker.mytravelmap.DataObjects.Place


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private var myplaces: List<Place> ?=null
    private var mymarkers: MutableList<Marker> ?= null
    private var links: List<String> ?=null
    private var mCountryName: TextView? = null
    private var mWebview: WebView? = null
    private var mLast: Button? = null
    private var mNext: Button? = null
    private var currentloc = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mCountryName = findViewById(R.id.CountryName)
        mLast = findViewById<Button>(R.id.LastPlace) as Button
        mNext = findViewById<Button>(R.id.NextPlace) as Button
        mWebview = findViewById<WebView>(R.id.webview) as WebView
        //myWebView.loadUrl("http://www.example.com")
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        myplaces = arrayListOf( Place("Barcelona", "spain",41.38506389999999,2.1734034999999494,"","","","https://www.lonelyplanet.com/spain/barcelona "),
                Place("Madrid", "spain", 40.4167754,-3.7037901999999576, "","","","https://www.lonelyplanet.com/spain/madrid "),
                Place("Munich", "germany", 48.1351253,11.58198049999998681779037, "","","","https://www.lonelyplanet.com/germany/munich "),
                Place("Zagreb", "croatia", 45.835283093660415,15.981640981779037, "","","","https://www.lonelyplanet.com/croatia/zagreb "),
                Place("Zadar", "croatia", 44.119371,15.231364799999938, "","","","https://www.lonelyplanet.com/croatia/dalmatia/zadar "),
                Place("Split", "croatia",43.5081323, 16.440193499999964, "","","","https://www.lonelyplanet.com/croatia/dalmatia/split " ),
                Place("Dubrovnik", "croatia",42.650661, 18.094424, "","","","https://www.lonelyplanet.com/croatia/dubrovnik " ),
                Place("Kotor", "montenegro",42.424662,18.77123400000005, "","","","https://www.lonelyplanet.com/montenegro/coastal-montenegro/kotor "),
                Place("Budva", "montenegro", 42.2911489,18.84029499999997, "","","","https://www.lonelyplanet.com/montenegro/coastal-montenegro/budva "),
                Place("Cluj-Napoca", "romania", 46.771210, 23.623635, "","","Untold Jul 18 – Jul 22, Electric Castle Aug 2-5","https://www.lonelyplanet.com/romania/transylvania/cluj-napoca https://untold.com/en https://electriccastle.ro/"),
                Place("Novi-Sad", "serbia", 45.236097, 19.886172, "","","Jul 12 – Jul 15","https://www.lonelyplanet.com/serbia/vojvodina/novi-sad https://exitfest.org/en "),
                Place("Budapest", "hungary",47.497912,19.04023499999994,"","","Sziget Aug 8-15","https://www.lonelyplanet.com/hungary/budapest https://en.szigetfestival.com/")

               )
        Log.e("Test", myplaces!![0].City)


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        var b=0;
        mymarkers = arrayListOf()
        for (place in myplaces!!){
            try{
                var mymarker: Marker = mMap.addMarker(MarkerOptions()
                        .title(place.City + ", " + place.Country)
                        .snippet(place.Description)
                        .position(LatLng(place.Lat, place.Long)).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(place.Country, 75, 75))))
                mymarkers!!.add(mymarker)
                Log.e(mymarker.title, mymarker.id)
                b+=1;
            }catch (E: Exception){

            }

        }

        mMap.setOnMarkerClickListener(this);

        mCountryName!!.text = myplaces!![currentloc].City + " - " + myplaces!![currentloc].Country
        mWebview!!.loadUrl(myplaces!![currentloc].Links)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(myplaces!![currentloc].Lat, myplaces!![currentloc].Long)))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5.0f))
        mLast!!.setOnClickListener(View.OnClickListener {
            if ( 0 < currentloc ){ currentloc -= 1} else{ currentloc = myplaces!!.size-1}
            mCountryName!!.text = myplaces!![currentloc].City + " - " + myplaces!![currentloc].Country
            links = myplaces!![currentloc].Links.split(" ")
            mWebview!!.loadUrl(links!![0])
            var nLL = LatLng(myplaces!![currentloc].Lat, myplaces!![currentloc].Long)
            mMap.animateCamera(CameraUpdateFactory.zoomTo(5.0f), 2000, null)
            Handler().postDelayed({
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nLL, 5.0f))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f), 2000, null)
            }, 2000)

        })
        mNext!!.setOnClickListener(View.OnClickListener {
            if ( currentloc <= myplaces!!.size-2){ currentloc += 1} else{ currentloc = 0}
            mCountryName!!.text = myplaces!![currentloc].City + " - " + myplaces!![currentloc].Country
            links = myplaces!![currentloc].Links.split(" ")
            mWebview!!.loadUrl(links!![0])
            var nLL = LatLng(myplaces!![currentloc].Lat, myplaces!![currentloc].Long)
            mMap.animateCamera(CameraUpdateFactory.zoomTo(5.0f), 2000, null)
            Handler().postDelayed({
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nLL, 5.0f))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f), 2000, null)
            }, 2000)
        })
        mNext!!.setOnLongClickListener(View.OnLongClickListener {
            try{
                mWebview!!.loadUrl(links!![1])
                true
            }catch (E:Exception){
                false
            }
        })



    }

    fun resizeMapIcons(iconName: String, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(resources, resources.getIdentifier(iconName, "drawable", packageName))
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

   override fun onMarkerClick(marker: Marker?): Boolean {

        for (ind in mymarkers!!.indices) {
           // Log.e("msg", mymarker.title)
            if (marker!!.equals(mymarkers!![ind])) {
                Log.e("msg", marker.title)
                mCountryName!!.text = myplaces!![ind].City + " - " + myplaces!![ind].Country
                mWebview!!.loadUrl(myplaces!![ind].Links)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(myplaces!![ind].Lat, myplaces!![ind].Long)))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f))
                currentloc = ind;
                return true
            }
        }
        return false
    }

}
