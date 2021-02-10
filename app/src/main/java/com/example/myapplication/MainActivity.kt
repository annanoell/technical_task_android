package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.example.myapplication.common.networkStatus.NetworkStatus
import com.example.myapplication.common.networkStatus.NetworkStatusInterface
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(R.layout.activity_main), NetworkStatusInterface,
    View.OnTouchListener {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setupNetworkStatus()
    }

    private fun setupNetworkStatus() {
        loaderView.setOnTouchListener(this)
    }

    override fun setNetWorkStatus(ns: NetworkStatus) {
        when (ns) {
            is NetworkStatus.Loading -> {
                loaderView.visibility = if (ns.loading) View.VISIBLE else View.GONE
            }
            is NetworkStatus.Error -> {
                loaderView.visibility = View.GONE
                showSnackBar(ns)
            }
            is NetworkStatus.Success -> {
                loaderView.visibility = View.GONE
                showSnackBar(ns)
            }
        }
    }

    private fun showSnackBar(ns: NetworkStatus) {
        val parentLayout =
            findViewById<View>(android.R.id.content)
        val snackBar = Snackbar.make(parentLayout, "", Snackbar.LENGTH_LONG)
        when (ns) {
            is NetworkStatus.Error -> {
                snackBar.setText(ns.errorMsg)
                snackBar.setBackgroundTint(Color.RED)
            }
            is NetworkStatus.Success -> {
                snackBar.setText(ns.successMessage)
                snackBar.setBackgroundTint(ContextCompat.getColor(this, R.color.teal_700))
            }
            else -> snackBar.setText("")
        }
        val view: View = snackBar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snackBar.show()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return true
    }

}