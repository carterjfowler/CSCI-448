package com.carterjfowler.carterjfowler_a3.History

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carterjfowler.carterjfowler_a3.Data.LocationData
import com.carterjfowler.carterjfowler_a3.R
import org.w3c.dom.Text
import java.text.SimpleDateFormat

class HistoryHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private lateinit var location: LocationData

    val formatter = SimpleDateFormat("EEE, MMM dd, yyyy hh:mm aaa")
    val checkInTextView: TextView = itemView.findViewById(R.id.check_in_text)
    val latitudeTextView: TextView = itemView.findViewById(R.id.latitude_text)
    val longitudeTextView: TextView = itemView.findViewById(R.id.longitude_text)
    val tempTextView: TextView = itemView.findViewById(R.id.temp_text)
    val weatherTextView: TextView = itemView.findViewById(R.id.weather_text)

//    , clickListener: (LocationData) -> Unit
    fun bind(location: LocationData) {
        this.location = location
//        itemView.setOnClickListener { clickListener(this.location) }

        this.location.run {
            checkInTextView.text = formatter.format(location.time)
            latitudeTextView.text = String.format("%.6f", location.latitude) + "°"
            longitudeTextView.text = String.format("%.6f", location.longitude) + "°"
            tempTextView.text = String.format("%.2f", location.temp) + "°F"
            weatherTextView.text = location.weatherDescription
        }
    }

    fun clear() {
        checkInTextView.text = ""
        latitudeTextView.text = ""
        longitudeTextView.text = ""
        tempTextView.text = ""
        weatherTextView.text = ""
    }
}