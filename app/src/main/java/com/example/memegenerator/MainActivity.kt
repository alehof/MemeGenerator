package com.example.memegenerator

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.database.Cursor
//import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.Color
import androidx.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
//import android.support.annotation.RequiresApi
//import android.support.v4.content.ContextCompat
//import android.support.v7.app.AppCompatActivity
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.memegenerator.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

//Initialize Listeners-------------------------------------------------------------
class MainActivity : AppCompatActivity(), View.OnTouchListener, View.OnDragListener, View.OnLongClickListener {
    //Initialize TAG-----------------------------------------------
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    //Initializing the Vies etc. for the Texteditor------------------------------------------------------------------------------->
   // private lateinit var editText1: EditText
    private  lateinit var view_meme: ImageView




    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val view = binding.root
        setContentView(view)
        //Set Listeners for Touchscreen Text---------------------------------------
        setListeners()

        //Data binding for nav drawer
        //drawerLayout = binding.drawerLayout

        //val navController = this.findNavController(R.id.myNavHostFragment)
        //NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        //NavigationUI.setupWithNavController(binding.navView, navController)

        button_share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            val shareSub: String
            val shareBody: String
            shareSub = "Hello Test"
            shareBody = "Hello Body"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(shareIntent, "Share using"))

        }

        button_camera.setOnClickListener {
            prepTakePhoto()
        }

// CODE FROM https://devofandroid.blogspot.com/2018/09/pick-image-from-gallery-android-studio_15.html
        // For Retrieving Image from Gallery with Permission Checks.
        button_open.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    //permission already granted
                    pickImageFromGallery()
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

       //--------------------------------------------------------------------------------------------------------------------------------------------------------------
        //the optionsmenu For Color and Text
        button_settings.setOnClickListener {
            val popup1 = PopupMenu(this, button_settings)
            val popup2 = PopupMenu(this, button_settings)

            //Inflating the Popup using xml file
            popup1.menuInflater.inflate(R.menu.button_size, popup1.menu)
            popup2.menuInflater.inflate(R.menu.button_color, popup2.menu)

            popup1.setOnMenuItemClickListener {
                if (it.itemId == R.id.size1) {
                    changeSize1()
                } else if (it.itemId == R.id.size2) {
                    changeSize2()
                } else if (it.itemId == R.id.size3) {
                    changeSize3()
                } else if (it.itemId == R.id.size4) {
                    changeSize4()
                }
                true
            }
            popup2.setOnMenuItemClickListener {
                if (it.itemId == R.id.color1) {
                    changeColor1()
                } else if (it.itemId == R.id.color2) {
                    changeColor2()
                } else if (it.itemId == R.id.color3) {
                    changeColor3()
                } else if (it.itemId == R.id.color4) {
                    changeColor4()
                }
                true
            }
            popup1.show()
            popup2.show()
        }

    }

    //Function to change color of Text----------------------------------------------------!!!!!!!!!!!!!!------------------------------------------
    private fun changeColor1(){
        //val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextColor(Color.WHITE)
    }
    private fun changeColor2(){
       // val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextColor(Color.BLACK)

    }
    private fun changeColor3(){
      //  val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextColor(Color.GREEN)

    }
    private fun changeColor4(){
      //  val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextColor(Color.BLUE)

    }
    //Now for the Sizes:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private fun changeSize1(){
       // val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextSize(10F)
    }
    private fun changeSize2(){
       // val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextSize(20F)

    }
    private fun changeSize3(){
        //val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextSize(30F)

    }
    private fun changeSize4(){
       // val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextSize(40F)

    }

   //Override with specific Listeners for Touchscreen Text-----------------------------------------
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setListeners() {

        ll_pinklayout.setOnLongClickListener {
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(ll_pinklayout)
           ll_pinklayout.startDragAndDrop(data, shadowBuilder, ll_pinklayout, 0)
            true
        }

        editText1.setOnLongClickListener {
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(editText1)
            editText1.startDragAndDrop(data, shadowBuilder, editText1, 0)
            true
        }

        ll_pinklayout.setOnDragListener(dragListener)

    }

    override fun onDrag(view:View, dragEvent: DragEvent):Boolean {
        Log.d(TAG, "onDrag: view->$view\n DragEvent$dragEvent")
        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_ENDED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_ENDED ")
                return true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_EXITED")
                return true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_ENTERED")
                return true
            }
            DragEvent.ACTION_DRAG_STARTED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_STARTED")
                return true
            }
            DragEvent.ACTION_DROP -> {
                Log.d(TAG, "onDrag: ACTION_DROP")
                val tvState = dragEvent.localState as View
                Log.d(TAG, "onDrag:viewX" + dragEvent.x + "viewY" + dragEvent.y)
                Log.d(TAG, "onDrag: Owner->" + tvState.parent)
                val tvParent = tvState.parent as ViewGroup
                tvParent.removeView(tvState)
                val container = view as LinearLayout
                container.addView(tvState)
                tvParent.removeView(tvState)
                tvState.x = dragEvent.x
                tvState.y = dragEvent.y
                view.addView(tvState)
                view.setVisibility(View.VISIBLE)
                return true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_LOCATION")
                return true
            }
            else -> return false
        }
    }
    override fun onTouch(view:View, motionEvent: MotionEvent):Boolean {

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        return true

    }

    override fun onLongClick(v: View?): Boolean {
        val dragShadowBuilder = View.DragShadowBuilder(v)
        v?.startDrag(null, dragShadowBuilder, v, 0)
        true
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val dragListener = View.OnDragListener {
            view, event ->
        val tag = "Drag and drop"
        event?.let {
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    Log.d(tag, "ACTION_DRAG_STARTED")
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    Log.d(tag, "ACTION_DRAG_ENDED")
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    Log.d(tag, "ACTION_DRAG_ENDED")
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    Log.d(tag, "ACTION_DRAG_ENDED")
                }
                DragEvent.ACTION_DROP -> {
                    Log.d(tag, "ACTION_DROP")
                    val tvState = event.localState as View
                    Log.d(TAG, "onDrag:viewX" + event.x + "viewY" + event.y)
                    Log.d(TAG, "onDrag: Owner->" + tvState.parent)
                    val tvParent = tvState.parent as ViewGroup
                    tvParent.removeView(tvState)
                    val container = view as LinearLayout
                    container.addView(tvState)
                    tvParent.removeView(tvState)
                    tvState.x = event.x
                    tvState.y = event.y
                    view.addView(tvState)
                    view.setVisibility(View.VISIBLE)
                }
                else -> {
                    Log.d(tag, "ACTION_DRAG_ ELSE ...")
                }
            }
        }
        true
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
