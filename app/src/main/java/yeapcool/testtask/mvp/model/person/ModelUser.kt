package yeapcool.testtask.mvp.model.person


object ModelUser {
    private var personUser: Person? = null
    private var personMy: Person? = null
    private var isMy: Boolean = false

    fun saveUser(p: Person?) {
        personUser = p
    }

    fun getUser(): Person? = personUser

    fun saveMy(p: Person?) {
        personMy = p
    }

    fun getMy(): Person? = personMy

    fun setIsMy(b: Boolean) {
        isMy = b
    }

    fun isMy(): Boolean = isMy
}
