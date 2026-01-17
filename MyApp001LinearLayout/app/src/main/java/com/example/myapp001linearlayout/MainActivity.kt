package com.example.myapp001linearlayout

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etJmeno = findViewById<EditText>(R.id.etJmeno)
        val etPrijmeni = findViewById<EditText>(R.id.etPrijmeni)
        val etObec = findViewById<EditText>(R.id.etObec)
        val etVek = findViewById<EditText>(R.id.etVek)

        val btnOdeslat = findViewById<Button>(R.id.btnOdeslat)
        val btnVymazat = findViewById<Button>(R.id.btnVymazat)

        val tvVystup = findViewById<TextView>(R.id.tvVystup)

        //Tlačítko Odeslat
        btnOdeslat.setOnClickListener {
            tvVystup.text = "Jméno: ${etJmeno.text}, " +
                    "Přijmení: ${etPrijmeni.text}, " +
                    "Obec: ${etObec.text}, " +
                    "Věk: ${etVek.text}"
        }

        //Tlačítko Vymazat
        btnVymazat.setOnClickListener {
            etJmeno.text.clear()
            etPrijmeni.text.clear()
            etObec.text.clear()
            etVek.text.clear()
            tvVystup.text = ""
        }
    }
}
