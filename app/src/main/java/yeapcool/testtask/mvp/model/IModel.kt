package yeapcool.testtask.mvp.model

import io.reactivex.Observable
import yeapcool.testtask.mvp.model.person.ModelUser
import yeapcool.testtask.mvp.model.preferences.ModelPreferences
import yeapcool.testtask.mvp.model.person.Person


interface IModel {

    fun pref(): ModelPreferences

    fun users(): ModelUser

    fun userSearch(q: String,
                   offset: Int,
                   count: Int,
                   fields: String,
                   accessToken: String?): Observable<Person>?

    fun getMyPhoto(owner_id: Int,
                   album_id: String,
                   rev: Boolean,
                   count: Int,
                   accessToken: String?): Observable<Person>?

    fun getProfileInfo(accessToken: String?): Observable<Person>?

    fun getFriends(user_id: Int,
                   offset: Int,
                   count: Int,
                   fields: String,
                   accessToken: String?): Observable<Person>?

}