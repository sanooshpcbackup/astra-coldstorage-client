package com.astra.coldstoragedelivery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    lateinit var btn_product:CardView
    lateinit var btn_order:CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_product=findViewById(R.id.cv_product)
        btn_order=findViewById(R.id.cv_orders)

        btn_product.setOnClickListener {

            val intent = Intent(this, ProductsActivity::class.java)
// To pass any data to next activity
//            intent.putExtra("keyIdentifier", value)
// start your next activity
            startActivity(intent)


        }
    }
}