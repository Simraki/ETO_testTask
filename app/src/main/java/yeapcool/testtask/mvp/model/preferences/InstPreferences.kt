package yeapcool.testtask.mvp.model.preferences

import android.content.Context


object InstPreferences {

    var inst: ModelPreferences? = null

    @Synchronized
    fun initializeInstance(context: Context) {
        if (inst == null) {
            inst = ModelPreferences(context)
        }
    }

    @Synchronized
    fun getInstance(): ModelPreferences =
            inst as ModelPreferences
}