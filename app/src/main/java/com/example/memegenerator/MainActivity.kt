package com.example.memegenerator

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.database.Cursor
//import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import androidx.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.net.toUri
import androidx.core.view.drawToBitmap

import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.example.memegenerator.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

//Variables for constants
const val TEXT_O = "text-o"
const val TEXT_U = "text-u"
const val SIZE_O = "size-o"
const val SIZE_U = "size-u"
const val COLOR_O = "color-o"
const val COLOR_U = "color-u"
const val FONT_O = "font-o"
const val FONT_U = "font-u"
const val ALLING_O = "alling-o"
const val ALLING_U = "alling-u"
const val URI_VALUE = "uri"
const val BIAS_O = "bias-o"
const val BIAS_U = "bias-u"

//const val testFace:Typeface =Typeface.SERIF

//Initialize Listeners, added LIFECYCLE OBSERVER-------------------------------------------------------------
class MainActivity : AppCompatActivity(), View.OnTouchListener {
    //Initialize TAG-----------------------------------------------
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var random_number: Random
    //Initializing the Vies etc. for the Texteditor------------------------------------------------------------------------------->
    // private lateinit var editText1: EditText
    // HELPERS FOR Storing the State of Typeface and TextSize
    var typefacenr =  1
    var sizenr = 3
    var saved = 0

   var editText1_bias = 0.1f
    var editText2_bias = 1.0f
    //I make Variables, that can be used to save stuff
    /*
    var  content1 = editText1.getText().toString();
    var content2 = editText2.text.toString()
    var size1 = editText1.textSize.toString()
    var size2 = editText2.textSize.toString()
    var color1 = editText1.textColors.toString()
    var color2 = editText2.textColors.toString()
   // var alling1 = editText1.textAlignment.toString()
   // var alling2 = editText2.textAlignment.toString()
    var font1 = editText1.typeface.toString()
    var font2 = editText2.typeface.toString()*/
   // private lateinit var editText1: EditText
  //  private  var imageView: ImageView = binding.viewMeme

    val sampleDrawables = intArrayOf(
        R.drawable.sample_1,
        R.drawable.sample_2,
        R.drawable.sample_3,
        R.drawable.sample_4,
        R.drawable.sample_5,
        R.drawable.sample_6,
        R.drawable.sample_7,
        R.drawable.sample_8,
        R.drawable.sample_9,
        R.drawable.sample_10)







    @RequiresApi(Build.VERSION_CODES.N)
    // Bundle saved below will now be availiable \/ here
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
       // binding.invalidateAll()

        if(savedInstanceState == null) {
            change_sample_image()
        }
        // ------ image randomization?
       // imageView.setImageResource(R.drawable.sample_2)

        val view = binding.root
        setContentView(view)
        //Set Listeners for Touchscreen Text---------------------------------------
        setListeners()



        //When there is info stored in the Bundle, it should be called
        if(savedInstanceState != null){

        //    view_meme.setImageURI(toUri(savedInstanceState.getString(URI_VALUE)))
            val uri_name_as_String:String = savedInstanceState.getString(URI_VALUE).toString()
        val   stored_uri_name:Uri = uri_name_as_String.toUri()
            view_meme.setImageURI(stored_uri_name)
            //URI_VALUE.toUri()


            editText1.setText(  savedInstanceState.getString(TEXT_O))
            editText2.setText(  savedInstanceState.getString(TEXT_U))
            // BUGGY FOR UNKNOWN REASONS
          //  editText1.setTextSize(  savedInstanceState.getFloat(SIZE_O))
           // editText2.setTextSize(  savedInstanceState.getFloat(SIZE_U))


            sizenr = savedInstanceState.getInt(SIZE_U)

            when (sizenr) {
                1 -> changeSize1()
                2 -> changeSize2()
                3 -> changeSize3()
                4 -> changeSize4()
            }


          //  val SizeofText = savedInstanceState.getString(SIZE_O)
           // Toast.makeText(this, "TextSize: $SizeofText", Toast.LENGTH_LONG).show()

            editText1.setTextColor(  savedInstanceState.getInt(COLOR_O))
            editText2.setTextColor(  savedInstanceState.getInt(COLOR_U))

            typefacenr = savedInstanceState.getInt(FONT_O)

            when (typefacenr) {
                1 -> changeFont1()
                2 -> changeFont2()
                3 -> changeFont3()
                4 -> changeFont4()
            }

            editText1_bias = (savedInstanceState.getFloat(BIAS_O))
            editText2_bias = (savedInstanceState.getFloat(BIAS_U))



            /*alling1 = savedInstanceState.getString(ALLING_O).toString()
            editText1.textAlignment(alling1.)
             alling2 = savedInstanceState.getString(ALLING_U).toString()*/
/*
             font1 = savedInstanceState.getString(FONT_O).toString()
            editText1.setTypeface(font1.)
             font2 = savedInstanceState.getString(FONT_U).toString()
            editText2.setTypeface(font2.to)*/
        }

        //Data binding for nav drawer
        //drawerLayout = binding.drawerLayout

        //val navController = this.findNavController(R.id.myNavHostFragment)
        //NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        //NavigationUI.setupWithNavController(binding.navView, navController)

    button_save.setOnClickListener{
        val bitmap:Bitmap = viewToBitmap(frameLayout,frameLayout.width, frameLayout.height)


        view_meme.setImageBitmap(bitmap)
       val uri:Uri = saveImage(bitmap)
        saved = 1
     // For sharing purposes.
        view_meme.setTag(uri.toString());
      // Toast.makeText(this, "saved: $uri",Toast.LENGTH_LONG).show()

       // to retrieve it from the tag use this.
        val path = view_meme.getTag().toString();
        Toast.makeText(this, "saved: $path",Toast.LENGTH_LONG).show()
    }

        button_move_text_in.setOnClickListener {
     val set = ConstraintSet()
        set.clone(inside_constraint)
            editText1_bias = editText1_bias+0.1f
            editText2_bias = editText2_bias-0.1f
            set.setVerticalBias(editText1.id, editText1_bias)
           // set.setVerticalBias(editText1.id, 0.3f)
            set.setVerticalBias(editText2.id, editText2_bias)

            set.applyTo(inside_constraint)
        }

        button_move_text_out.setOnClickListener {
            val set = ConstraintSet()
            editText1_bias = editText1_bias-0.1f
            editText2_bias = editText2_bias+0.1f
            set.clone(inside_constraint)
            set.setVerticalBias(editText1.id, editText1_bias)
            set.setVerticalBias(editText2.id, editText2_bias)

            set.applyTo(inside_constraint)
        }






        button_share.setOnClickListener {
            if (saved != 0) {

            val path = view_meme.getTag().toString()

            val text = "Look at my meme dude";
            val  pictureUri = Uri.parse(path);
            val shareIntent = Intent()
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            shareIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
            shareIntent.setType("image/*");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share images..."));


        }else {
                Toast.makeText(this, "Pls save before sharing", Toast.LENGTH_SHORT).show()

        }}

        button_camera.setOnClickListener {
            prepTakePhoto()
        }

        button_undo.setOnClickListener {
            change_sample_image()
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
            val popup3 = PopupMenu(this, button_settings)

            //Inflating the Popup using xml file
            popup1.menuInflater.inflate(R.menu.button_size, popup1.menu)
            popup2.menuInflater.inflate(R.menu.button_color, popup2.menu)
            popup3.menuInflater.inflate(R.menu.button_text, popup3.menu)

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
            popup3.setOnMenuItemClickListener {
                if (it.itemId == R.id.text1) {
                    changeFont1()
                } else if (it.itemId == R.id.text2) {
                    changeFont2()
                } else if (it.itemId == R.id.text3) {
                    changeFont3()
                } else if (it.itemId == R.id.text4) {
                    changeFont4()
                }
                true
            }
            popup1.show()
            popup2.show()
            popup3.show()
        }

    }

    private fun change_sample_image() {

        var zeresulto:Int = (1..10).random()
        view_meme.setImageResource(sampleDrawables[zeresulto])
        Toast.makeText(this, "Drawable value $zeresulto", Toast.LENGTH_LONG).show()

    }

    private fun saveImage(bitmap: Bitmap):Uri {
         val root_of_directory = getApplicationInfo().dataDir
        val file = File(root_of_directory, "${UUID.randomUUID()}.jpg")
        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    private fun saveImageForRestoring(bitmap: Bitmap):Uri {
        val root_of_directory = getApplicationInfo().dataDir
        val file = File(root_of_directory, "${UUID.randomUUID()}.jpg")
        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }







    // Store inside app done.
    private fun viewToBitmap(view: View, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveImageToInternalStorage(): Uri {
        // Get the image from drawable resource as drawable object
        val drawable = ContextCompat.getDrawable(applicationContext, view_meme.id)

        // Get the bitmap from drawable object
        val bitmap = (drawable as BitmapDrawable).bitmap

        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)


        // Create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image uri
        return Uri.parse(file.absolutePath)
    }
    //Function to change color of Text----------------------------------------------------!!!!!!!!!!!!!!------------------------------------------
    private fun changeColor1(){
        //val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextColor(Color.WHITE)
        editText2.setTextColor(Color.WHITE)

    }
    private fun changeColor2(){
        // val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextColor(Color.BLACK)
        editText2.setTextColor(Color.BLACK)

    }
    private fun changeColor3(){
        //  val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextColor(Color.GREEN)
        editText2.setTextColor(Color.GREEN)

    }
    private fun changeColor4(){
        //  val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextColor(Color.BLUE)
        editText2.setTextColor(Color.BLUE)

    }
    //Now for the Sizes:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private fun changeSize1(){
        // val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextSize(10F)
        editText2.setTextSize(10F)
        sizenr = 1

    }

    private fun changeSize2() {
        // val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextSize(20F)
        editText2.setTextSize(20F)
        sizenr = 2

    }

    private fun changeSize3() {
        //val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextSize(30F)
        editText2.setTextSize(30F)
        sizenr = 3

    }

    private fun changeSize4() {
        // val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTextSize(40F)
        editText2.setTextSize(40F)
        sizenr = 4

    }

    //Now the fonts:---------------------------------------------------------------------------------------------------
    private fun changeFont1() {
        // val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTypeface(Typeface.DEFAULT)
        editText2.setTypeface(Typeface.DEFAULT)
        typefacenr = 1
        // nr 1

    }
    private fun changeFont2(){
        // val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTypeface(Typeface.SERIF)
        editText2.setTypeface(Typeface.SERIF)
        typefacenr = 2

    }

    private fun changeFont3() {
        //val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTypeface(Typeface.DEFAULT_BOLD)
        editText2.setTypeface(Typeface.DEFAULT_BOLD)
        typefacenr = 3
// n3
    }

    private fun changeFont4() {
        // val editText1: EditText = findViewById(R.id.editText1)
        editText1.setTypeface(Typeface.MONOSPACE)
        editText2.setTypeface(Typeface.MONOSPACE)
        typefacenr = 4
//n4
    }


   //Override with specific Listeners for Touchscreen Text-----------------------------------------
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setListeners() {



    }

    override fun onTouch(view:View, motionEvent: MotionEvent):Boolean {

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        return true

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

    //Lifecycle OS already does part of that, we want other information safed, e.g. when the Phone is flipped, the TextView settings should be safed
    //should work, bc. I think we are weeellll below the 100kb that would exeed the bundles limit.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //I use Constants, to ensure, that the exact same key is passed to the reconstruction method.
        //CONTENT
        outState.putString(TEXT_O, editText1.text.toString())
        outState.putString(TEXT_U, editText2.text.toString())

        //SIZE
        outState.putInt(SIZE_O, sizenr)
        outState.putInt(SIZE_U, sizenr)
  //      outState.putFloat(SIZE_O,editText1.textSize)
       // outState.putString(SIZE_O, editText1.textSize.toString())
   //     outState.putFloat(SIZE_U, editText2.textSize)

        //COLOR
        outState.putInt(COLOR_O, editText1.currentTextColor)
        outState.putInt(COLOR_U, editText2.currentTextColor)

        //allingment--probably worthless
//        outState.putString(ALLING_O, editText1.textAlignment.toString())
//        outState.putString(ALLING_U, editText2.textAlignment.toString())

        //FONT
        outState.putInt(FONT_O, typefacenr)
        outState.putInt(FONT_U, typefacenr)

        //STORING THE PIC
        val inbetween_storage_uri: Uri = saveImageForRestoring(view_meme.drawToBitmap())
        outState.putString(URI_VALUE, inbetween_storage_uri.toString())

        // STORING BIAS OF TEXT ALIGNS

        outState.putFloat(BIAS_O, editText1_bias)
        outState.putFloat(BIAS_U, editText2_bias)


    }
}
