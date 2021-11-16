package com.mallam.imagefilters.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mallam.imagefilters.databinding.ActivityFilteredImageBinding

class FilteredImageActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityFilteredImageBinding

    //File Uri
    private lateinit var fileUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilteredImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayFilteredImage()
        setClickListeners()
    }

    private fun displayFilteredImage() {
        intent.getParcelableExtra<Uri>(EditImageActivity.KEY_FILTERED_IMAGE_URI)?.let { imageUri ->
            fileUri = imageUri
            binding.imageFiltered.setImageURI(imageUri)
        }
    }

    private fun setClickListeners() {
        binding.fabShare.setOnClickListener {
            with(Intent(Intent.ACTION_SEND)) {
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                startActivity(this)
            }
        }

        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }

}