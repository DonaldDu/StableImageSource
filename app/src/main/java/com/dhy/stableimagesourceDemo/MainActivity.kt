package com.dhy.stableimagesourceDemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dhy.stableimagesource.ImageData
import com.dhy.stableimagesource.putSerializable
import com.dhy.stableimagesource.readSerializable
import com.dhy.stableimagesourceDemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val picUtil = MyPickUtil(this)
    private var data = ImageData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        data = savedInstanceState.readSerializable(data)
        picUtil.onCreate(savedInstanceState)
        binding.apply {
            val holder = MyImageHolder(image, btReselect, picUtil.takePhotoOnClickListener)
            holder.init(data)

            btDelete.setOnClickListener {
                cacheDir.listFiles()?.forEach { f -> f.deleteRecursively() }
            }
            btMultipleImagesActivity.setOnClickListener {
                startActivity(Intent(this@MainActivity, MultipleImagesActivity::class.java))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        picUtil.onSaveInstanceState(outState)
        outState.putSerializable(data)
    }
}

