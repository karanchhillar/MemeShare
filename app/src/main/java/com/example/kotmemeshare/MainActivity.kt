package com.example.kotmemeshare

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.ResponseDelivery
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    var currentImageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }
    private fun loadMeme(){
        val progressBar: ProgressBar = findViewById(R.id.progressBar1)
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

//// Request a string response from the provided URL.
//        val stringRequest = JsonObjectRequest(
//            Request.Method.GET, url,
//            { response ->
//                Log.d("Success" , response.substring(0,500))
//            },
//            {
//                Log.d("Error", it.localizedMessage)
//            })

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET , url ,null,
            { response ->
                currentImageUrl = response.getString("url")
                val memeImageView: ImageView = findViewById(R.id.memeImageView)
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(memeImageView)
            },
            {Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show() }
        )


// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }


    
//    private fun shareImage() {
//        val contentUri = getContentUri()
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.type = "image/png"
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here") //for sharing with email apps
//        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        startActivity(Intent.createChooser(shareIntent, "Share Via"))
//    }
//
//    private  fun getContentUri(): Uri? {
//        var bitmap: Bitmap? = null
//        //get bitmap from uri
//        try {
//            bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                val source = ImageDecoder.createSource(contentResolver, imageUri!!)
//                ImageDecoder.decodeBitmap(source)
//            } else {
//                MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
//            }
//        } catch (e: Exception) {
//            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
//        }
//
//        //if you want to get bitmap from imageview instead of uri then
//        /*BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.imageIv.getDrawable();
//        bitmap = bitmapDrawable.getBitmap();*/
//        val imagesFolder = File(cacheDir, "images")
//        var contentUri: Uri? = null
//        try {
//            imagesFolder.mkdirs() //create if not exists
//            val file = File(imagesFolder, "shared_image.png")
//            val stream = FileOutputStream(file)
//            bitmap!!.compress(Bitmap.CompressFormat.PNG, 50, stream)
//            stream.flush()
//            stream.close()
//            contentUri = FileProvider.getUriForFile(this, "com.technifysoft.shareimagetext.fileprovider", file)
//        } catch (e: Exception) {
//            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
//        }
//        return contentUri
//    }




//    private fun showShareIntent() {
//        // Step 1: Create Share itent
//        val intent = Intent(Intent.ACTION_SEND).setType("image/*")
//
//        // Step 2: Get Bitmap from your imageView
//        val imageView: ImageView = findViewById(R.id.memeImageView)
//        val bitmap = imageView.drawable.toBitmap() // your imageView here.
//
//        // Step 3: Compress image
//        val bytes = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//
//        // Step 4: Save image & get path of it
////        val path = MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, "tempimage", null)
//
//        // Step 5: Get URI of saved image
//        val uri = currentImageUrl
//
//        // Step 6: Put Uri as extra to share intent
//        intent.putExtra(Intent.EXTRA_STREAM, uri)
//
//        // Step 7: Start/Launch Share intent
//        startActivity(intent)
//    }




    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT , "URL = $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share This Meme")
        startActivity(chooser)

//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "image/*"
////        intent.putExtra(Intent.EXTRA_STREAM)
//        val chooser = Intent.createChooser(intent, "Share This Meme")
//        startActivity(chooser)

//        showShareIntent()
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}

