package com.mallam.imagefilters.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.mallam.imagefilters.adapter.SavedImagesAdapter
import com.mallam.imagefilters.databinding.ActivitySavedImagesBinding
import com.mallam.imagefilters.utilities.displayToast
import com.mallam.imagefilters.viewmodel.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SavedImagesActivity : AppCompatActivity(), SavedImagesAdapter.SavedImageListener {
    //Binding
    private lateinit var binding: ActivitySavedImagesBinding

    //View Model
    private val viewModel: SavedImagesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setClickListeners()
        viewModel.loadSavedImages()

    }

    private fun setupObservers() {
        viewModel.savedImageUiState.observe(this, {
            val savedImagesDataState = it ?: return@observe
            binding.savedImagesProgressBar.visibility =
                if (savedImagesDataState.isLoading) View.VISIBLE else View.GONE
            savedImagesDataState.savedImages?.let { savedImages ->
                SavedImagesAdapter(savedImages, this).also { adapter ->
                    with(binding.savedImagesRecyclerView){
                        this.adapter = adapter
                        visibility = View.VISIBLE
                    }
                }
            } ?: run {
                savedImagesDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
    }

    private fun setClickListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onImageClicked(file: File) {
        val fileUri = FileProvider.getUriForFile(
            applicationContext,
            "${packageName}.provider",
            file
        )
        Intent(
            applicationContext,
            FilteredImageActivity::class.java
        ).also { filteredImageIntent ->
            filteredImageIntent.putExtra(EditImageActivity.KEY_FILTERED_IMAGE_URI, fileUri)
            startActivity(filteredImageIntent)

        }
    }


}