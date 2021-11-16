package com.mallam.imagefilters.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.mallam.imagefilters.adapter.ImageFiltersAdapter
import com.mallam.imagefilters.data.ImageFilter
import com.mallam.imagefilters.databinding.ActivityEditImageBinding
import com.mallam.imagefilters.utilities.displayToast
import com.mallam.imagefilters.utilities.show
import com.mallam.imagefilters.viewmodel.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity(), ImageFiltersAdapter.ImageFilterListener {
    //Companion Object
    companion object{
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }
    //Binding
    private lateinit var binding: ActivityEditImageBinding
    //View Model
    private val viewModel: EditImageViewModel by viewModel()
    //GPU Image
    private lateinit var gpuImage: GPUImage
    //Image Bitmaps
    private lateinit var originalBitmap : Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Buttons Click Listeners
        setClickListeners()

        //Observers
        setupObservers()

        //Prepare Image Preview
        prepareImagePreview()
    }

    private fun setupObservers() {
        viewModel.imagePreviewUiState.observe(this, {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                //For the first time 'filtered image = original image'
                originalBitmap = bitmap
                filteredBitmap.value = bitmap
                with(originalBitmap){
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImageFilter(this)
                }

            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })

        viewModel.imageFiltersUIState.observe(this, {
            val imageFiltersDataState = it ?: return@observe
            binding.imageFiltersProgressBar.visibility = if(imageFiltersDataState.isLoading) View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilter?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters, this).also { adapter ->
                    binding.filtersRecyclerView.adapter = adapter
                }
            } ?: kotlin.run {
                imageFiltersDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })

        filteredBitmap.observe(this, { bitmap ->
            binding.imagePreview.setImageBitmap(bitmap)
        })

        viewModel.saveFilteredImageUIState.observe(this, {
            val saveFilteredImageDataState = it ?: return@observe
            if(saveFilteredImageDataState.isLoading){
                binding.imageSave.visibility = View.GONE
                binding.saveFilteredImageProgressBar.visibility = View.VISIBLE
            }else{
                binding.saveFilteredImageProgressBar.visibility = View.GONE
                binding.imageSave.visibility = View.VISIBLE
            }
            saveFilteredImageDataState.uri?.let { savedImageUri ->
                Intent(
                    applicationContext,
                    FilteredImageActivity::class.java
                ).also { filteredImageIntent ->
                    filteredImageIntent.putExtra(KEY_FILTERED_IMAGE_URI, savedImageUri)
                    startActivity(filteredImageIntent)
                }
            } ?: kotlin.run {
                saveFilteredImageDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
    }

    private fun prepareImagePreview() {
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URi)?.let { imageUri ->
            viewModel.prepareImageView(imageUri)
        }
    }

    private fun setClickListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

        binding.imageSave.setOnClickListener {
            filteredBitmap.value?.let{ bitmap ->
                viewModel.saveFilteredImage(bitmap)
            }
        }

        /*
        This will show original image when long click the ImageView until we release click,
        So that we can see difference between original and filtered image
        */
        binding.imagePreview.setOnLongClickListener {
            binding.imagePreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }

        binding.imagePreview.setOnClickListener {
            binding.imagePreview.setImageBitmap(filteredBitmap.value)
        }
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter){
            with(gpuImage){
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }
}