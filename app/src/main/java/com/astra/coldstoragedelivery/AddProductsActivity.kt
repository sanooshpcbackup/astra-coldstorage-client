package com.astra.coldstoragedelivery

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap


class AddProductsActivity : AppCompatActivity() {

    lateinit var saveBtn:Button
    lateinit var updateBtn:Button
    lateinit var deleteBtn:Button

    lateinit var imgBtn1:Button
    lateinit var imgBtn2:Button

    lateinit var img1:ImageView


    //firebase image storage

    private var filePath: Uri?=null

    internal var storage:FirebaseStorage?=null
    internal var storageReference:StorageReference?=null

    private val PICK_IMAGE_REQUEST=1234


    var imageUrl:String=""




    val db = FirebaseFirestore.getInstance()
    lateinit var id:TextInputEditText
    lateinit var name:TextInputEditText
    lateinit var desc:TextInputEditText
    lateinit var price:TextInputEditText
    var snap_id:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_products)

        //initfirebase
        storage= FirebaseStorage.getInstance()
        storageReference=storage!!.reference


        img1=findViewById(R.id.img1)




        saveBtn=findViewById(R.id.btn_save)
        updateBtn=findViewById(R.id.btn_update)
        deleteBtn=findViewById(R.id.btn_delete)
        id=findViewById(R.id.product_id)
        name=findViewById(R.id.product_name)
        desc=findViewById(R.id.product_desc)
        price=findViewById(R.id.product_price)

        imgBtn1=findViewById(R.id.btn_img1)
        imgBtn2=findViewById(R.id.btn_img2)


        imgBtn1.setOnClickListener {

            showFileChooser()


        }

        imgBtn2.setOnClickListener {

            uploadFile()
        }


        val from:String = intent.getStringExtra("to").toString()
        snap_id=intent.getStringExtra("snapshotid").toString()
        val prod_id:String = intent.getStringExtra("id").toString()
        val prod_name:String = intent.getStringExtra("name").toString()
        val prod_desc:String = intent.getStringExtra("desc").toString()
        val prod_price:String = intent.getStringExtra("price").toString()

//        Log.e("Data",from+ "  " +prod_id+ " " + prod_name+ " "+ prod_desc + " " + prod_price)
        Log.e("SnapId",snap_id.toString())

        if (from.equals("addproduct"))
        {

            updateBtn.visibility= View.INVISIBLE
            saveBtn.visibility=View.VISIBLE
            deleteBtn.visibility=View.INVISIBLE


        }
        else if (from.equals("editproduct"))
        {

            updateBtn.visibility= View.VISIBLE
            deleteBtn.visibility=View.VISIBLE
            saveBtn.visibility=View.INVISIBLE
            id.setText(prod_id)
            id.isEnabled=false
            name.setText(prod_name)
            desc.setText(prod_desc)
            price.setText(prod_price)


        }
        else
        {
            Snackbar.make(getWindow().getDecorView().getRootView(), " Something went Wrong!! ", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()        }


deleteBtn.setOnClickListener {


    deleteData()
}

saveBtn.setOnClickListener {
    saveData()
}



        updateBtn.setOnClickListener {

            updateData()



        }

    }



    fun saveData()
    {
            val idtxt=id.text.toString()
            val nametxt=name.text.toString()
            val desctxt=desc.text.toString()
            val pricetxt=price.text.toString()

        val capitalCities = db.collection("Products")

        db.collection("Products").whereEqualTo("id", idtxt)
            .limit(1).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val isEmpty = task.result!!.isEmpty

                    if (isEmpty)
                    {
                        Toast.makeText(this, "True!", Toast.LENGTH_SHORT).show()

                        addData(idtxt,nametxt,desctxt,pricetxt)

                    }
                    else{

                        Toast.makeText(this, "False!", Toast.LENGTH_SHORT).show()

                        alertmsg("simplealert","The ProductId you have entered already exists. Please try with a different ID")


                    }

                    Log.e("IsEmptyValue",isEmpty.toString())
                }
            }



    }

    fun updateData()
    {

        val idtxt=id.text.toString()
        val nametxt=name.text.toString()
        val desctxt=desc.text.toString()
        val pricetxt=price.text.toString()


        db.collection("Products").document(snap_id.toString())
            .update(mapOf(
                "id" to idtxt,
                "name" to nametxt,
                "desc" to desctxt,
                "price" to pricetxt
            )).addOnCompleteListener {task ->
                if (task.isSuccessful) {

                    Toast.makeText(this, "Updation Successfull!", Toast.LENGTH_SHORT).show()

                }
                else
                {
                    Toast.makeText(this, "Updation Failed!", Toast.LENGTH_SHORT).show()
                }
            }

    }


        fun alertmsg(s: String, s1: String)
        {

            val builder = AlertDialog.Builder(this)


            builder.setTitle("ALERT")
            builder.setMessage(s1)
    //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            if (s.equals("productadded"))
            {
                builder.setCancelable(false)
                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    finish()
    //            Toast.makeText(applicationContext,
    //                android.R.string.yes, Toast.LENGTH_SHORT).show()
                }
            }
            else
            {

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
    //            Toast.makeText(applicationContext,
    //                android.R.string.yes, Toast.LENGTH_SHORT).show()
                }

            }




            builder.show()
        }

    fun addData(idtxt: String, nametxt: String, desctxt: String, pricetxt: String
    )
    {

        val user: MutableMap<String, Any> = HashMap()
        user["id"] = idtxt
        user["name"] = nametxt
        user["desc"] = desctxt
        user["price"] = pricetxt

// Add a new document with a generated ID
            db.collection("Products")
                .add(user)
                .addOnSuccessListener { documentReference ->


                    alertmsg("productadded","The Product has been Successfully added!!!")


                    Log.e("Logs" , documentReference.id.toString())
                }
                .addOnFailureListener { e ->
                    Log.e("Error adding document", e.toString()
                    )
                }





    }


    fun deleteData()
    {

        db.collection("Products").document(snap_id.toString())
            .delete().addOnCompleteListener {task ->
                if (task.isSuccessful) {

                    Toast.makeText(this, "Deletion Successfull!", Toast.LENGTH_SHORT).show()

                }
                else
                {
                    Toast.makeText(this, "Updation Failed!", Toast.LENGTH_SHORT).show()
                }
            }

    }


    fun uploadFile()
    {

        if(filePath!=null)
        {
            val progressDialog= ProgressDialog(this)
            progressDialog.setTitle("Uploading....")
            val idtxt=id.text.toString()


            progressDialog.show()


            val imageRef=storageReference!!.child(idtxt+"/"+"mainimage")
            imageRef.putFile(filePath!!)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,"FIle Uploaded",Toast.LENGTH_SHORT).show()
                    Log.e("ImagePath : ","ImagePath")

                    Log.e("ImagePath : ",storageReference.toString() +it.storage.path.toString())
                }
                .addOnFailureListener{

                    progressDialog.dismiss()

                    Toast.makeText(applicationContext,"FIle Uploading Failed",Toast.LENGTH_SHORT).show()

                }
                .addOnProgressListener { taskSnapShot ->
                    val progress = 100.0 * taskSnapShot.bytesTransferred/taskSnapShot.totalByteCount
                    progressDialog.setMessage("Uploaded"+progress.toInt() + "%....")




                }

        }


    }


    fun showFileChooser()
    {
        val intent=Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"SELECT PICTURE"),PICK_IMAGE_REQUEST)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode== Activity.RESULT_OK && data!!.data != null    )
        {
            filePath = data.data
            try {
                val bitmap=MediaStore.Images.Media.getBitmap(contentResolver,filePath)
                img1!!.setImageBitmap(bitmap)
            }
            catch (e:IOException)
            {
                Log.e("ActivityResultIO",e.toString())
            }
        }
    }
}