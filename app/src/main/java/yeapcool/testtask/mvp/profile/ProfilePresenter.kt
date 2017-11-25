package yeapcool.testtask.mvp.profile

import com.vk.sdk.VKSdk
import yeapcool.testtask.utils.Constants
import yeapcool.testtask.mvp.model.ImageLoaderInst
import yeapcool.testtask.mvp.model.Model
import yeapcool.testtask.mvp.model.person.Person


class ProfilePresenter : IProfile.Presenter {

    // Interface
    override fun isMyProfile() = users.isMy()

    // View and Model
    private var view: IProfile.View? = null
    private val model = Model()

    //Presenter
    private val users = model.users()


    // Bind | Unbind | Clear

    override fun bind(view: IProfile.View) {
        this.view = view
        getProfile()
    }

    override fun unbind() {
        view = null
    }


    // Инициализировать профиль

    private fun getProfile() {
        val p = if (users.isMy()) users.getMy() else users.getUser()

        if (p != null) {
            val (first_name, last_name, nickname, screen_name, sex, relation) = p
            val patron = if (nickname != null && !nickname.isEmpty()) nickname else Constants.UNSPECIFIED
            val gender = Constants.GENDER[sex]
            val family = if (sex == 1) Constants.RELATION_WOMAN[relation] else Constants.RELATION_MAN[relation]

            setImageProfile(p)

            view?.setProfileInfo(first_name, last_name, patron, screen_name, gender, family)

            val title = p.first_name + " " + p.last_name
            view?.setTitle(title)
        } else
            view?.showError()
    }

    private fun setImageProfile(p: Person) {

        if (users.isMy()) {
            if (p.image != null)
                view?.image?.setImageBitmap(p.image)
            else
                view?.showError()
        } else {
            if (p.photo_200 != null)
                ImageLoaderInst.get().displayImage(p.photo_200, view?.image)
            else if (p.photo_200 == null && p.photo_50 != null)
                ImageLoaderInst.get().displayImage(p.photo_50, view?.image)
            else
                view?.showError()
        }

    }


    // Выход из профиля

    override fun logout() {
        VKSdk.logout()
        model.pref().clear()
        view?.finishApp()
    }


    // К друзьям пользователя

    override fun clickFriends() {
        view?.toFriends()
    }
}