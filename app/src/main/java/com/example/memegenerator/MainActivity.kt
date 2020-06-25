package com.example.memegenerator

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.database.Cursor
//import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import androidx.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
//import android.support.annotation.RequiresApi
//import android.support.v4.content.ContextCompat
//import android.support.v7.app.AppCompatActivity
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.memegenerator.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    //Initializing the Vies etc. for the Texteditor------------------------------------------------------------------------------->
    lateinit var TextView1: TextView
    lateinit var TextView2: TextView
    lateinit var EditText1: EditText
    lateinit var EditText2: EditText
    lateinit var view_meme: ImageView
    lateinit var textup: ClipData.Item
    lateinit var textdown: ClipData.Item



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val view = binding.root
        setContentView(view)

        //Data binding for nav drawer
        //drawerLayout = binding.drawerLayout

        //val navController = this.findNavController(R.id.myNavHostFragment)
        //NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        //NavigationUI.setupWithNavController(binding.navView, navController)

        button_share.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            val shareSub :String
            val shareBody :String
            shareSub = "Hello Test"
            shareBody = "Hello Body"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
            shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody)
            startActivity(Intent.createChooser(shareIntent, "Share using"))

        }

        button_camera.setOnClickListener{
            prepTakePhoto()
        }

// CODE FROM https://devofandroid.blogspot.com/2018/09/pick-image-from-gallery-android-studio_15.html
        // For Retrieving Image from Gallery with Permission Checks.
        button_open.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

        // For the Text on the meme-----------------------------------------------------------------------Tutorial mihn 15:00 Ich will den Button mit den Textviews verbinden, also quasi untermenues für größe, Farbe, ober und untertext-------------------->
      //  TextView1 = findViewById(R.id.TextView1) as TextView
      //  TextView2 = findViewById(R.id.TextView2) as TextView

       // EditText1 = findViewById(R.id.EditText1) as EditText
       // EditText2 = findViewById(R.id.EditText2) as EditText

        //textup = findViewById(R.id.textup) as Button
       // textdown = findViewById(R.id.textdown) as Button
        //-- ------------------------------------------------------------------------------------- -->
        //the optionsmenu wip
       //button_settings.setOnClickListener(){
           // var input = EditText1.text
        //    Toast.makeText(this, EditText1.text, Toast.LENGTH_LONG).show()
      // }

       // textdown.setOnClickListener(){
            //EditText2.setText(textdown.getText().toString());

          //  textdown.setText("");
      //  }
           // -----------------------------------------------------------------------------------------
        // Just to know how to set Text.
        // binding.titleText.text = "Edit textivew Text"

        //the optionsmenu wip
     //   button_settings.setOnClickListener{
         //   val popup = PopupMenu(this, button_settings)
         //   popup.inflate(R.menu.popup_menu)
           // popup.setOnMenuItemClickListener{
                //Toast.makeText(this, "Item:" +it.title,Toast.LENGTH_SHORT).show()
               // true

                // }
           // popup.show()

       // }

    }

    // See if we have permission or not
    @RequiresApi(Build.VERSION_CODES.M)
    private fun prepTakePhoto() {

        if (ContextCompat.checkSelfPermission(this!!,Manifest.permission.CAMERA) == PERMISSION_GRANTED) {
            takePhoto()
        } else {
            val permissionRequest = arrayOf(Manifest.permission.CAMERA) // asking for camera permission, it can accept more than 1 permission at once that's why its in an array
            requestPermissions(permissionRequest,CAMERA_PERMISSION_REQUEST_CODE)
        }
    }



    private fun takePhoto() {
        // on this new intent, also apply the things I stated
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{
                takePictureIntent -> takePictureIntent.resolveActivity(this!!.packageManager)?.also {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
        }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001

        private val CAMERA_PERMISSION_REQUEST_CODE = 2000

        private val CAMERA_REQUEST_CODE = 2001
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    // permission granted, let's take a photo then
                    takePhoto()
                } else {
                    Toast.makeText(this, "Unable to take photo without permission",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image  - so the URI is loaded into the view_meme placeholder
    // THIS IS the callback function from external of the app
    // resultCode represents what the user selected,
    // the Data parameter is the one we use
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            view_meme.setImageURI(data?.data)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE){
            val imageBitmap = data!!.extras!!.get("data") as Bitmap // typecasting in  kotlin "as"
            view_meme.setImageBitmap(imageBitmap)
        }
    }

}
