package com.example.thankyoutree

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thankyoutree.extensions.add
import com.example.thankyoutree.extensions.replace
import kotlinx.android.synthetic.main.loader_layout.*


class MainActivity : AppCompatActivity(), TreeBaseContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        add(LandingFragment(), addToBackStack = false)
    }

    override fun hideLoadingView() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showLoadingView() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
            super.onBackPressed()
    }
}
