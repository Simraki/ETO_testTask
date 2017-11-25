package yeapcool.testtask.mvp.model.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import yeapcool.testtask.mvp.model.response.PersonResponse
import yeapcool.testtask.mvp.model.response.Response


interface IApi {

    @GET("users.search?v=5.69")
    fun userSearch(@Query("q") q: String,
                   @Query("offset") offset: Int,
                   @Query("count") count: Int,
                   @Query("fields") fields: String,
                   @Query("access_token") accessToken: String?)
            : Observable<Response>

    @GET("account.getProfileInfo?v=5.69")
    fun getProfileInfo(@Query("access_token") accessToken: String?)
            : Observable<PersonResponse>

    @GET("photos.get?v=5.69")
    fun getMyPhoto(@Query("owner_id") id: Int,
                   @Query("album_id") album_id: String,
                   @Query("rev") rev: Boolean,
                   @Query("count") count: Int,
                   @Query("access_token") accessToken: String?)
            : Observable<Response>

    @GET("friends.get?v=5.69")
    fun getFriends(@Query("user_id") user_id: Int,
                   @Query("offset") offset: Int,
                   @Query("count") count: Int,
                   @Query("fields") fields: String,
                   @Query("access_token") accessToken: String?)
            : Observable<Response>
}