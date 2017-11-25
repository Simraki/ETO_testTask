package yeapcool.testtask.mvp.model.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import yeapcool.testtask.utils.Constants


object Api {

    private var service: IApi? = null
    private val client = OkHttpClient()
    private val gsonFactory = GsonConverterFactory.create()
    private val rxJavaAdapter = RxJava2CallAdapterFactory.create()

    fun getService(): IApi? {
        return if (service == null) {
            val retrofit = Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(rxJavaAdapter)
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(gsonFactory)
                    .build()
            service = retrofit.create<IApi>(IApi::class.java)
            service
        } else {
            service
        }
    }
}