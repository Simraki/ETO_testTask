package yeapcool.testtask.mvp.model

import android.content.Context
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import yeapcool.testtask.R


object ImageLoaderInst {

    private lateinit var inst: ImageLoader

    @Synchronized
    fun set(context: Context) {
        inst = ImageLoader.getInstance()

        val config = ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(400, 400)
                .defaultDisplayImageOptions(
                        DisplayImageOptions.Builder()
                                .showImageOnLoading(R.drawable.ic_smile) // resource or drawable
                                .showImageForEmptyUri(R.drawable.ic_smile) // resource or drawable
                                .showImageOnFail(R.drawable.ic_smile) // resource or drawable
                                .cacheInMemory(true)
                                .cacheOnDisk(false)
                                .build()
                )
                .build();

        inst.init(config)
    }

    @Synchronized
    fun get() = inst


}