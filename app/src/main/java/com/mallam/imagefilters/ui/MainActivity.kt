package com.mallam.imagefilters.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.animation.AnimationUtils
import com.mallam.imagefilters.R
import com.mallam.imagefilters.databinding.ActivityMainBinding
import com.mallam.imagefilters.utilities.BackgroundImages
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    //Companion Object
    companion object{
        private var BACKGROUND_IMAGE_POSITION = 1
        private var BREAK_LOOP = false
        private const val REQUEST_CODE_PICK_IMAGE = 1
        const val KEY_IMAGE_URi = "imageUri"
    }

    //Binding
    private lateinit var binding: ActivityMainBinding


    /** onCreate **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Buttons Click Listeners
        setClickListeners()

    }

    override fun onStart() {
        super.onStart()
        BREAK_LOOP = false
        changeBackgroundImage()
    }

    private fun setClickListeners(){
        binding.buttonEditNewImage.setOnClickListener {
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ).also { pickerIntent ->
                pickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivityForResult(pickerIntent, REQUEST_CODE_PICK_IMAGE)
            }
        }

        binding.buttonSaveImages.setOnClickListener {
            Intent(
                applicationContext,
                SavedImagesActivity::class.java
            ).also { intent ->
                BREAK_LOOP = true
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK){
            data?.data?.let { imageUri ->
                Intent(
                    applicationContext,
                    EditImageActivity::class.java
                ).also { editImageIntent ->
                    editImageIntent.putExtra(KEY_IMAGE_URi, imageUri)
                    BREAK_LOOP = true
                    startActivity(editImageIntent)
                }
            }
        }
    }

    private fun changeBackgroundImage(){
        GlobalScope.launch {
            while (!BREAK_LOOP){
                delay(8000L)
                Log.d("Meeee", "changeBackgroundImage: ")
                withContext(Dispatchers.Main){
                    binding.imageBackground.animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
                    binding.imageBackground.animate()
                    binding.imageBackground.setImageResource(BackgroundImages.images[BACKGROUND_IMAGE_POSITION++])
                    if(BACKGROUND_IMAGE_POSITION == BackgroundImages.images.size){
                        BACKGROUND_IMAGE_POSITION = 0
                    }
                }
            }
        }
    }


}