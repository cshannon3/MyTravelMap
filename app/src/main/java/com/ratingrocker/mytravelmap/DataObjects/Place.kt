package com.ratingrocker.mytravelmap.DataObjects

import com.google.android.gms.maps.model.Marker

data class Place(
       // var id: Int,
        var City: String,
        var Country: String,
        var Lat: Double,
        var Long: Double,
        var Description: String,
        var Hostels: String,
        var Dates: String,
        var Links: String
) {
    constructor() : this("", "", 0.0, 0.0, "", "", "", "")
}