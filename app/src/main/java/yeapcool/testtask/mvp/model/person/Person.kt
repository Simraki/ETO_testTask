package yeapcool.testtask.mvp.model.person

import android.graphics.Bitmap

data class Person(
        var first_name: String,
        var last_name: String,
        var nickname: String?,
        var screen_name: String,
        var sex: Int,
        var relation: Int,
        var photo_200: String?, // ТОЛЬКО для фото чужого профиля
        var photo_50: String?, // ТОЛЬКО для превью чужого профиля
        var image: Bitmap?,
        var photo_604: String, // ТОЛЬКО для фото собственного профиля
        var id: Int,
        var deactivated: String?,
        var hidden: Int?
)