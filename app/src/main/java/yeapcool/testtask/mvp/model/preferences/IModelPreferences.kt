package yeapcool.testtask.mvp.model.preferences


interface IModelPreferences {

    fun clear()

    fun getAccessToken(): String?

    fun setAccessToken(token: String)

    fun getId(): Int

    fun setId(id: Int)
}