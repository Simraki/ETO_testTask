package yeapcool.testtask

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKAccessTokenTracker
import com.vk.sdk.VKSdk
import yeapcool.testtask.utils.NetworkUtil
import yeapcool.testtask.mvp.main.MainActivity
import yeapcool.testtask.mvp.model.ImageLoaderInst
import yeapcool.testtask.mvp.model.preferences.InstPreferences


class Application : Application() {

    private val vkAccessTokenTracker: VKAccessTokenTracker = object : VKAccessTokenTracker() {

        override fun onVKAccessTokenChanged(oldToken: VKAccessToken?, newToken: VKAccessToken?) {

            if (newToken == null) {
                val intent = Intent(applicationContext, MainActivity::class.java)  //  ^_^
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        vkAccessTokenTracker.startTracking()

        // Инициализация
        InstPreferences.initializeInstance(applicationContext)
        ImageLoaderInst.set(applicationContext)
        VKSdk.initialize(applicationContext)

        // Проверка на подключение к сети
        if (!NetworkUtil.isNetworkConnected(applicationContext))
            Toast.makeText(applicationContext, R.string.toast_noNetwork, Toast.LENGTH_LONG).show()
    }
}