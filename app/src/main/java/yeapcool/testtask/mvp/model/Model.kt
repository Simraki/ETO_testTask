package yeapcool.testtask.mvp.model

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import yeapcool.testtask.mvp.model.api.Api
import yeapcool.testtask.mvp.model.person.ModelUser
import yeapcool.testtask.mvp.model.person.Person
import yeapcool.testtask.mvp.model.preferences.InstPreferences
import yeapcool.testtask.mvp.model.preferences.ModelPreferences


class Model : IModel {

    private val pref = InstPreferences.getInstance()
    private val api = Api.getService()
    private val users = ModelUser

    override fun pref(): ModelPreferences = pref

    override fun userSearch(q: String, offset: Int, count: Int, fields: String, accessToken: String?): Observable<Person>? =
            api?.userSearch(q, offset, count, fields, accessToken)
                    ?.subscribeOn(Schedulers.io())
                    ?.filter { it -> it.response != null }
                    ?.filter { it -> it.response?.items != null }
                    ?.flatMap { it -> Observable.fromIterable(it.response?.items) }

    override fun getProfileInfo(accessToken: String?): Observable<Person>? =
            api?.getProfileInfo(accessToken)
                    ?.subscribeOn(Schedulers.io())
                    ?.filter { it -> it.response != null }
                    ?.flatMap { it -> Observable.just(it.response) }

    override fun getMyPhoto(owner_id: Int, album_id: String, rev: Boolean, count: Int, accessToken: String?): Observable<Person>? =
            api?.getMyPhoto(owner_id, album_id, rev, count, accessToken)
                    ?.subscribeOn(Schedulers.io())
                    ?.filter { it -> it.response != null }
                    ?.filter { it -> it.response?.items != null }
                    ?.flatMap { it -> Observable.fromIterable(it.response?.items) }

    override fun getFriends(user_id: Int, offset: Int, count: Int, fields: String, accessToken: String?): Observable<Person>? =
            api?.getFriends(user_id, offset, count, fields, accessToken)
                    ?.subscribeOn(Schedulers.io())
                    ?.filter { it -> it.response != null }
                    ?.filter { it -> it.response?.items != null }
                    ?.flatMap { it -> Observable.fromIterable(it.response?.items) }

    override fun users(): ModelUser = users
}