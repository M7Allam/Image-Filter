package com.mallam.imagefilters.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mallam.imagefilters.R
import com.mallam.imagefilters.data.ImageFilter
import com.mallam.imagefilters.databinding.ItemContainerFilterBinding

class ImageFiltersAdapter(
    private val imageFilters: List<ImageFilter>,
    private val imageFilterListener: ImageFilterListener
) : RecyclerView.Adapter<ImageFiltersAdapter.ImageFilterViewHolder>() {

    private var selectedFilterPosition = 0
    private var previouslySelectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        val binding =
            ItemContainerFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, position: Int) {
        with(holder) {
            with(imageFilters[position]) {
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.textFilterName.text = name
                binding.root.setOnClickListener {
                    if(position != selectedFilterPosition){
                        imageFilterListener.onFilterSelected(this)
                        previouslySelectedPosition = selectedFilterPosition
                        selectedFilterPosition = position
                        with(this@ImageFiltersAdapter){
                            notifyItemChanged(previouslySelectedPosition, Unit)
                            notifyItemChanged(selectedFilterPosition, Unit)
                        }
                    }

                }
            }

            binding.textFilterName.setTextColor(
                ContextCompat.getColor(
                    binding.textFilterName.context,
                    if (selectedFilterPosition == position)
                        R.color.primaryDark
                    else
                        R.color.primaryText
                )
            )


        }
    }

    override fun getItemCount() = imageFilters.size

    //View Holder
    inner class ImageFilterViewHolder(val binding: ItemContainerFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    //Image Filter Listener
    interface ImageFilterListener {
        fun onFilterSelected(imageFilter: ImageFilter)
    }
}