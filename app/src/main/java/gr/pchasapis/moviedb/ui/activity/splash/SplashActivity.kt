package gr.pchasapis.moviedb.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.ui.activity.navigation.NavigationActivity

class SplashActivity : AppCompatActivity() {

    companion object {
        const val SPLASH_DELAY: Long = 3000
    }

    private var mDelayHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initLayout()
    }
    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            startActivity(Intent(applicationContext, NavigationActivity::class.java))
            finish()
        }
    }

    public override fun onDestroy() {
        mDelayHandler?.removeCallbacks(mRunnable)
        super.onDestroy()
    }

    private fun initLayout() {
        mDelayHandler = Handler()
        mDelayHandler?.postDelayed(mRunnable, SPLASH_DELAY)
    }
}
