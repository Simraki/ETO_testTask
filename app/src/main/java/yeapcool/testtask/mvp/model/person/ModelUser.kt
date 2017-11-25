package yeapcool.testtask.mvp.model.person


object ModelUser {
    private var personUser: Person? = null
    private var personMy: Person? = null
    private var isMy: Boolean = false

    @Synchronized
    fun saveUser(p: Person?) {
        personUser = p
    }

    @Synchronized
    fun getUser(): Person? = personUser

    @Synchronized
    fun saveMy(p: Person?) {
        personMy = p
    }

    @Synchronized
    fun getMy(): Person? = personMy

    @Synchronized
    fun setIsMy(b: Boolean) {
        isMy = b
    }

    @Synchronized
    fun isMy(): Boolean = isMy
}