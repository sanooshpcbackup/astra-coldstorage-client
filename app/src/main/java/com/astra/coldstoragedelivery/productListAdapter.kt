package com.astra.coldstoragedelivery

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class productListAdapter (private val mcontext: Context,options: FirestoreRecyclerOptions<ProductDetailsModel>) :
    FirestoreRecyclerAdapter<ProductDetailsModel, productListAdapter.productViewHolder>(options) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): productViewHolder {
        return productViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.singleitem,parent,false))
    }

    override fun onBindViewHolder(holder: productViewHolder, position: Int, model: ProductDetailsModel)
    {

        holder.productName.text=model.name
        holder.productDesc.text=model.desc
        holder.productPrice.text=model.price

       Log.e("IDXYZ",getSnapshots().getSnapshot(position).getId())


        holder.productCardView.setOnClickListener {



            Log.e("id",model.id.toString())
            Log.e("name",model.name.toString())
            Log.e("desc",model.desc.toString())
            Log.e("price",model.price.toString())



            val intent = Intent(mcontext, AddProductsActivity::class.java)
            intent.putExtra("to","editproduct")
            intent.putExtra("snapshotid",getSnapshots().getSnapshot(position).getId().toString())
            intent.putExtra("id", model.id.toString())
            intent.putExtra("name",model.name.toString())
            intent.putExtra("desc",model.desc.toString())
            intent.putExtra("price",model.price.toString())


            mcontext?.startActivity(intent)
        }

    }


    class productViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var productName=itemView.findViewById<TextView>(R.id.tvname)
        var productDesc=itemView.findViewById<TextView>(R.id.tvdesc)
        var productPrice=itemView.findViewById<TextView>(R.id.tvprice)
        var productCardView=itemView.findViewById<CardView>(R.id.itemCv)


    }
}