package ua.dmitrij.randomimages

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ua.dmitrij.randomimages.databinding.ActivityMainBinding
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var useKeyWord = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGlide.setOnClickListener {
            getRandomPhotoWithGlide()
        }
        binding.buttonGlide.setOnLongClickListener {
            longClicker()
        }
        binding.inputSearchText.setOnEditorActionListener { _, actionID, _ ->
            if (actionID == EditorInfo.IME_ACTION_SEARCH) {
                return@setOnEditorActionListener getRandomPhotoWithGlide()
            }
            return@setOnEditorActionListener false
        }

        binding.switchKeyWord.setOnClickListener {
            this.useKeyWord = binding.switchKeyWord.isChecked
            updateUI()
        }
        updateUI()
    }


    private fun getRandomPhotoWithGlide(): Boolean {
        val keyWord = binding.inputSearchText.text.toString()
        if (useKeyWord && keyWord.isBlank()) {
            binding.inputSearchText.error = "Keyword is Empty"
            return true
        }
        val encodedKeyword = URLEncoder.encode(keyWord, StandardCharsets.UTF_8.name())
        binding.progressCircular.visibility = View.VISIBLE
        Glide.with(this)
            .load("https://source.unsplash.com/random/800x600${if (useKeyWord) "?$encodedKeyword" else ""}")
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressCircular.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressCircular.visibility = View.GONE
                    return false
                }
            }
            )
            .error(R.drawable.ic_baseline_cloud_download_24)
            .into(binding.loadingWithGlide)
        return false
    }

    private fun longClicker(): Boolean {
        val text = "Too long"
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        return true
    }
    private fun error() = Toast.makeText(this, "Disconnect", Toast.LENGTH_SHORT)

    private fun updateUI() = with(binding) {
        if (binding.switchKeyWord.isChecked) {
            inputSearchText.visibility = View.VISIBLE
        } else {
            inputSearchText.visibility = View.GONE
        }

    }
}

