package yeapcool.testtask.mvp.profile

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_profile.*
import yeapcool.testtask.R
import yeapcool.testtask.mvp.friends.FriendsActivity


class ProfileActivity : AppCompatActivity(), IProfile.View, View.OnClickListener {

    // Interface
    override lateinit var image: ImageView


    private lateinit var presenter: ProfilePresenter

    private val toolbar: Toolbar by lazy {
        findViewById<Toolbar>(R.id.toolbar_profile)
    }


    // _Android-life

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        init()
    }

    override fun onResume() {
        super.onResume()

        presenter.bind(this)
    }

    override fun onPause() {
        super.onPause()

        presenter.unbind()
    }


    // Инициализация

    private fun init() {
        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        image = image_person
        presenter = ProfilePresenter()

        btn_friends.setOnClickListener(this)
    }


    // Инициализация профиля

    override fun setProfileInfo(first_name: String, last_name: String, nickname: String,
                                screen_name: String, gender: String, family: String) {
        name_person.text = first_name
        surname_person.text = last_name
        patron_person.text = nickname
        screenName_person.text = screen_name
        gender_person.text = gender
        family_person.text = family
    }


    // _Android-logic

    override fun onClick(v: View?) {
        presenter.clickFriends()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if (presenter.isMyProfile())
            menuInflater.inflate(R.menu.menu_profile, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.menu_profile_logout)
            presenter.logout()
        if (item?.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }


    // Заголовок

    override fun setTitle(title: String) {
        this.title = title
    }


    // Help

    override fun finishApp() {
        finishAffinity()
    }


    // Ошибки

    override fun showError() {
        Toast.makeText(baseContext, R.string.error, Toast.LENGTH_SHORT).show()
    }


    // Навигация

    override fun toFriends() {
        val intent = Intent(this, FriendsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}