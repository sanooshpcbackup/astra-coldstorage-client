package com.astra.coldstoragedelivery

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ProductsActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val collectionReference:CollectionReference=db.collection("Products")
    var prodadapter:productListAdapter?=null
    lateinit var rv:RecyclerView
    lateinit var img_back:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        rv=findViewById(R.id.rv)
        img_back=findViewById(R.id.img_back)


        img_back.setOnClickListener{
            finish()
        }

        val query:Query=collectionReference.orderBy("name",Query.Direction.ASCENDING)
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<ProductDetailsModel> = FirestoreRecyclerOptions.Builder<ProductDetailsModel>()
            .setQuery(query, ProductDetailsModel ::class.java)
            .build()

        prodadapter= productListAdapter(this,firestoreRecyclerOptions)
        rv.layoutManager= LinearLayoutManager(this)
        rv.adapter=prodadapter

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            val intent = Intent(this, AddProductsActivity::class.java)
            intent.putExtra("to","addproduct")
            intent.putExtra("snapshotid","")
            intent.putExtra("id", "")
            intent.putExtra("name","")
            intent.putExtra("desc","")
            intent.putExtra("price","")
            startActivity(intent)
        }



    }

    override fun onStart() {
        super.onStart()
        prodadapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        prodadapter!!.stopListening()

    }
}