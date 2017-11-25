package yeapcool.testtask.mvp.profile

import android.widget.ImageView
import yeapcool.testtask.mvp.Common


interface IProfile {

    interface Presenter: Common.Presenter {

        fun clickFriends()

        fun bind(view: IProfile.View)

        fun isMyProfile(): Boolean

        fun logout()
    }

    interface View: Common.View {

        var image: ImageView

        fun setProfileInfo(first_name: String, last_name: String, nickname: String, screen_name: String, gender: String, family: String)

        fun toFriends()

        fun setTitle(title: String)

        fun finishApp()
    }

}