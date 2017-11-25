package yeapcool.testtask.utils


object Constants {

    // Preferences

    val PREF_NAME = "prefTestTask"

    val ACCESS_TOKEN = "access_token"

    val ID = "user_id"

    val DEFAULT_ID = -1

    val DEFAULT_TOKEN = null


    // Vk

    val BASE_URL = "https://api.vk.com/method/"

    val COUNT = 15
    val VIEW_OFFSET = 8

    val FIELDS_SEARCH = "sex, screen_name, nickname, relation, photo_50, photo_200"
    val FIELDS_FRIENDS = "photo_50"

    val UNSPECIFIED = "Не указано"

    val GENDER = listOf("Неизвестно...", "Женщина", "Мужчина")

    val RELATION_MAN = listOf(UNSPECIFIED, "Не женат", "Есть подруга", "Помолвлен", "Женат",
            "Всё сложно :(", "В активном поиске ;)", "Влюблен", "В гражданском браке")

    val RELATION_WOMAN = listOf(UNSPECIFIED, "Не замужем", "Есть друг", "Помолвлена", "Замужем",
            "Всё сложно :(", "В активном поиске ;)", "Влюблена", "В гражданском браке")
}