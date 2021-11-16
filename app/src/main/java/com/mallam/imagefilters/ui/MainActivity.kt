package com.mallam.imagefilters.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.mallam.imagefilters.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //Companion Object
    companion object{
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
                    startActivity(editImageIntent)
                }
            }
        }
    }



}