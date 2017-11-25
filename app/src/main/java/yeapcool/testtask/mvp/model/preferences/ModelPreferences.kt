package yeapcool.testtask.mvp.model.preferences

import android.content.Context
import android.content.SharedPreferences
import yeapcool.testtask.utils.Constants


class ModelPreferences(context: Context) : IModelPreferences {

    private var pref: SharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

    override fun clear() {
        pref.edit()
                .clear()
                .apply()
    }

    override fun getAccessToken(): String? =
            pref.getString(Constants.ACCESS_TOKEN, Constants.DEFAULT_TOKEN)


    override fun setAccessToken(token: String) {
        pref.edit()
                .putString(Constants.ACCESS_TOKEN, token)
                .apply()
    }

    override fun getId(): Int =
            pref.getInt(Constants.ID, Constants.DEFAULT_ID)


    override fun setId(id: Int) {
        pref.edit()
                .putInt(Constants.ID, id)
                .apply()
    }

}