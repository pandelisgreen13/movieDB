package gr.pchasapis.moviedb

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RobolectricUnitTest {


    private var context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `Validate with Robolectric`() {
        val result = context.getString(R.string.app_name)

        Assert.assertEquals("MovieDB", result)
    }
}
