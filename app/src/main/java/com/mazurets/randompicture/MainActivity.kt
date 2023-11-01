package com.mazurets.randompicture

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mazurets.randompicture.databinding.ActivityMainBinding
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var useKeyword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainBtn.setOnClickListener {
            onGetRandomImagePressed()
        }

        binding.mainCheckBox.setOnClickListener {
            this.useKeyword = binding.mainCheckBox.isChecked
            updateUi()
        }

        binding.mainTextEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
                return@setOnEditorActionListener onGetRandomImagePressed()

            return@setOnEditorActionListener false
        }

        binding.mainCheckBox.setOnCheckedChangeListener { _, isChecked ->

        }
        updateUi()
    }

    private fun updateUi() {
        binding.mainCheckBox.isChecked = useKeyword
        if (useKeyword)
            binding.mainTextEdit.visibility = View.VISIBLE
        else
            binding.mainTextEdit.visibility = View.GONE
    }

    private fun onGetRandomImagePressed(): Boolean {
        val keyword = binding.mainTextEdit.text.toString().trim()
        val encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.name())

        if (useKeyword && keyword.isBlank()) {
            binding.mainTextEdit.error = "Keyword is empty"
            return true
        }

        binding.mainProgressBar.visibility = View.VISIBLE
        Glide.with(this)
            .load("https://source.unsplash.com/random/800x600?${if (useKeyword) "?$encodedKeyword" else ""}")
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.mainProgressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.mainProgressBar.visibility = View.GONE
                    return false
                }

            })
            .into(binding.mainImage)

        return false
    }
}