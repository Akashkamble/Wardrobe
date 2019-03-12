package com.akash.wardrobe.ui

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.akash.wardrobe.R
import com.akash.wardrobe.adapters.BottomAdapter
import com.akash.wardrobe.adapters.TopAdapter
import com.akash.wardrobe.database.Bottom
import com.akash.wardrobe.database.Top
import com.akash.wardrobe.viewmodels.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    //Views
    private lateinit var topViewPager: ViewPager2
    private lateinit var bottomViewPager: ViewPager2
    private lateinit var addTop: ImageView
    private lateinit var addBottom: ImageView
    private lateinit var favoriteButton: FloatingActionButton
    private lateinit var shuffle: FloatingActionButton
    private lateinit var textViewAddTop: TextView
    private lateinit var textViewAddBottom: TextView

    //Adapters
    private lateinit var bottomAdapter: BottomAdapter
    private lateinit var topAdapter: TopAdapter

    //ViewModel
    private val mainViewModel: MainViewModel by viewModel()

    // Request codes
    private val REQUEST_IMAGE_FROM_GALLERY_TOP = 1
    private val REQUEST_IMAGE_FROM_GALLERY_BOTTOM = 2
    private val REQUEST_TAKE_PHOTO_TOP = 3
    private val REQUEST_TAKE_PHOTO_BOTTOM = 4


    private var topArrayList = emptyList<Top>()
    private var bottomArrayList = emptyList<Bottom>()
    private var currentTopPage = 0
    private var currentBottomPage = 0
    private var isAddedInFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        topAdapter = TopAdapter()
        bottomAdapter = BottomAdapter()
        topViewPager.adapter = topAdapter
        bottomViewPager.adapter = bottomAdapter


        addTop.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY_TOP)
            }
        }

        addBottom.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY_BOTTOM)
            }
        }

        /**
         * Below lines observes changes in Top table and set data in TopAdapter.
         */
        mainViewModel.getAllTops().observe(this, Observer {
            Log.e(TAG, "getAllTop invoked with ${it.size}")
            if (it.isNotEmpty()) {
                topArrayList = it
                topAdapter.setData(topArrayList)
                textViewAddTop.visibility = View.GONE
                favoriteButton.isVisible = bottomArrayList.isNotEmpty()
                shuffle.isVisible = bottomArrayList.isNotEmpty()
                if (bottomArrayList.isNotEmpty()) {
                    ifFavorite(topViewPager.currentItem, bottomViewPager.currentItem)
                }
            } else {
                favoriteButton.isVisible = false
                shuffle.isVisible = false
            }
        })

        /**
         * Below lines observes changes in Bottom table and set data in BottomAdapter.
         */
        mainViewModel.getAllBottoms().observe(this, Observer {
            Log.e(TAG, "getAllBottom invoked with ${it.size}")
            if (it.isNotEmpty()) {
                bottomArrayList = it
                bottomAdapter.setData(bottomArrayList)
                textViewAddBottom.visibility = View.GONE
                favoriteButton.isVisible = topArrayList.isNotEmpty()
                shuffle.isVisible = topArrayList.isNotEmpty()
                if (topArrayList.isNotEmpty()) {
                    ifFavorite(topViewPager.currentItem, bottomViewPager.currentItem)
                }
            } else {
                favoriteButton.isVisible = false
                shuffle.isVisible = false
            }
        })

        favoriteButton.setOnClickListener {
            val topPosition = topViewPager.currentItem
            val bottomPosition = bottomViewPager.currentItem
            isAddedInFavorite = if(isAddedInFavorite){
                mainViewModel.removeFromFavorite(topArrayList[topPosition], bottomArrayList[bottomPosition])
                favoriteButton.setImageResource(R.drawable.ic_heart_disabled)
                false
            }else{
                mainViewModel.addToFavorite(topArrayList[topPosition], bottomArrayList[bottomPosition])
                favoriteButton.setImageResource(R.drawable.ic_heart_enabled)
                true
            }
        }

        topViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentTopPage = position
                ifFavorite(currentTopPage, currentBottomPage)
            }
        })

        bottomViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentBottomPage = position
                ifFavorite(currentTopPage, currentBottomPage)
            }
        })

        shuffle.setOnClickListener {
            val randomTopPage = (0..topArrayList.size).random()
            val randomBottomPage = (0..bottomArrayList.size).random()
            topViewPager.setCurrentItem(randomTopPage,true)
            bottomViewPager.setCurrentItem(randomBottomPage,true)
        }
    }

    /**
     * This function checks Tops and Bottoms with particular Ids are present in favoriteList
     * maintained in MainViewModel and update image resource of FAB accordingly.
     */
    private fun ifFavorite(topId: Int, bottomId: Int) {
        try {
            isAddedInFavorite = if (mainViewModel.isFavorite(topArrayList[topId].id, bottomArrayList[bottomId].id)) {
                favoriteButton.setImageResource(R.drawable.ic_heart_enabled)
                true
            } else {
                favoriteButton.setImageResource(R.drawable.ic_heart_disabled)
                false
            }
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, "Exception")
        }
    }

    /**
     * This function is use to initialize all the views
     */
    private fun initViews() {
        topViewPager = findViewById(R.id.topViewPager)
        bottomViewPager = findViewById(R.id.bottomViewPager)
        addBottom = findViewById(R.id.addBottom)
        addTop = findViewById(R.id.addTop)
        favoriteButton = findViewById(R.id.favorite)
        shuffle = findViewById(R.id.shuffle)
        textViewAddTop = findViewById(R.id.textViewAddTop)
        textViewAddBottom = findViewById(R.id.textViewAddBottom)
    }

    /**
     * This function get result from gallery and store the data in Top or Bottom table
     * according to the result code.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_IMAGE_FROM_GALLERY_TOP) {
                    val uri = data?.data
                    Log.e(TAG, " ${uri.toString()}")
//                    showAlertDialog(uri!!)
                    mainViewModel.addTop(Top(0, uri.toString()))
                }
            }
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_IMAGE_FROM_GALLERY_BOTTOM) {
                    val uri = data?.data
                    Log.e(TAG, " ${uri.toString()}")
//                    showAlertDialog(uri!!)
                    mainViewModel.addBottom(Bottom(0, uri.toString()))
                }
            }
        } catch (exception: Exception) {

        }
    }


    /**
     * This function will display the image chosen from gallery and display the image in Dialog.
     * Then it retrieves the bitmap from ImageView and convert it to ByteArray.
     * We can use this function if in future we have to store image in BLOB format into Database.
     */
    private fun showAlertDialog(uri: Uri) {
        val builder = AlertDialog.Builder(this)
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(R.layout.image_alert, null)
        val imageView: ImageView = view.findViewById(R.id.imagePreview)
        imageView.setImageURI(uri)
        builder.setView(view)
        builder.setPositiveButton("Save") { dialog, _ ->
            val bitmap = imageView.drawable.toBitmap()
            // In future you can save the bitmap as bytearray in Database.
            val bitmapbyteArray = mainViewModel.convertBitmapToByteArray(bitmap)
            dialog!!.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog!!.dismiss() }
        builder.show()
    }
}
