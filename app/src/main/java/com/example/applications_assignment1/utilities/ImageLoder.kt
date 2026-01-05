package com.example.applications_assignment1.utilities


import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.applications_assignment1.R
import java.lang.ref.WeakReference

class ImageLoader private constructor(context: Context) {
    private val contextRef = WeakReference(context)

    companion object {
        @Volatile
        private var instance: ImageLoader? = null
        fun init(context: Context): ImageLoader {
            return instance ?: synchronized(this) {
                instance
                    ?: ImageLoader(context).also { instance = it }
            }
        }

        fun getInstance(): ImageLoader {
            return instance ?: throw IllegalStateException(
                "ImageLoader must be initialized by calling init(context) before use."
            )

        }
    }

//    fun loadImage(
//        source: Int,
//        imageView: ImageView,
//        placeHolder: Int = R.drawable.unavailable_photo
//    ) {
//        contextRef.get()?.let{
//                context ->
//            Glide
//                .with(context)
//                .load(source)
//                .centerCrop()
//                .placeholder(placeHolder)
//                .into(imageView)
//        }
//    }

    fun loadImage(
        source: Int,
        imageView: ImageView,
        placeHolder: Int = R.drawable.img_unavailable_photo
    ) {
        Glide.with(imageView)
            .load(source)
            .override(512, 512)
            .centerCrop()
            .placeholder(placeHolder)
            .into(imageView)
    }

    fun loadImage(
        source: String,
        imageView: ImageView,
        placeHolder: Int = R.drawable.img_unavailable_photo
    ) {
        contextRef.get()?.let{
                context ->
            Glide
                .with(context)
                .load(source)
                .override(256, 256)
                .centerCrop()
                .placeholder(placeHolder)
                .into(imageView)
        }
    }
}