package com.csci448.carterjfowler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // New Kotlin Code to add an Event Listener
        val calculateButton: Button = findViewById( R.id.calculate_button )
        calculateButton.setOnClickListener {
            val numPeopleEditText: EditText = findViewById( R.id.num_people_edit_text )
            val numPeople: Int = numPeopleEditText.text.toString().toInt()
            val hungryRG: RadioGroup = findViewById( R.id.how_hungry_radio_group )
            val slicesPerPerson = when( hungryRG.checkedRadioButtonId ) {
                R.id.light_radio_button -> 1
                R.id.medium_radio_button -> 2
                R.id.heavy_radio_button -> 4
                else -> 0
            }
            val slicesPerPizza = 8.0 // a double literal
            val totalNumberPizzas = ceil(numPeople * slicesPerPerson / slicesPerPizza).toInt()
            val numPizzasTextView: TextView = findViewById( R.id.num_pizzas_text_view )
            numPizzasTextView.text = totalNumberPizzas.toString()
        }
    }
}
