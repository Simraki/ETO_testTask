package yeapcool.testtask.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {

    fun isNetworkConnected(context: Context): Boolean {

        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (manager.activeNetworkInfo != null)
            manager.activeNetworkInfo.isAvailable && manager.activeNetworkInfo.isConnected
        else
            false

    }
}