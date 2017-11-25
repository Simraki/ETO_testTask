package yeapcool.testtask.mvp.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import kotlinx.android.synthetic.main.activity_main.*
import yeapcool.testtask.R
import yeapcool.testtask.adapter.UsersAdapter
import yeapcool.testtask.mvp.profile.ProfileActivity


class MainActivity : AppCompatActivity(), IMain.View, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    // Interface
    override lateinit var searchBar: SearchView
    override lateinit var recyclerView: RecyclerView

    private lateinit var presenter: MainPresenter
    private lateinit var adapter: UsersAdapter

    private val toolbar by lazy {
        findViewById<Toolbar>(R.id.toolbar_main)
    }


    // _Android-life

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    override fun onResume() {
        super.onResume()

        presenter.bind(this, adapter, adapter)
    }

    override fun onPause() {
        super.onPause()

        presenter.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.clearApp()
    }


    // Инициализация

    private fun init() {
        setSupportActionBar(toolbar)

        adapter = UsersAdapter(this)

        searchBar = et_search
        recyclerView = recycler_main
        recyclerView.adapter = adapter

        presenter = MainPresenter()

        if (VKSdk.isLoggedIn()) {
            btn_auth.visibility = View.GONE
            showTv_help()
            presenter.getMyProfile()
        }

        swipe_layout_main.setOnRefreshListener(this)
        swipe_layout_main.setColorSchemeColors(Color.BLUE)

        btn_auth.setOnClickListener(this)
    }


    // _Android-logic

    override fun onRefresh() {
        presenter.refreshUsers()
    }

    override fun onClick(v: View?) {
        login()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {

            override fun onResult(res: VKAccessToken) {
                presenter.saveTokenAndId(res.accessToken, Integer.parseInt(res.userId))
                btn_auth.visibility = View.GONE
                showTv_help()
                presenter.getMyProfile()
            }

            override fun onError(error: VKError) {
            }
        }))
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.menu_main_profile)
            presenter.showMyProfile()

        return super.onOptionsItemSelected(item)
    }


    // Авторизация

    private fun login() {
        if (!VKSdk.isLoggedIn())
            VKSdk.login(this, VKScope.FRIENDS)
    }


    // Show-Hide

    override fun showTv_help() {
        tv_help_main.visibility = View.VISIBLE

        if (searchBar.query.isEmpty())
            tv_help_main.text = getString(R.string.text_find)
        else
            tv_help_main.text = getString(R.string.text_notFoundUsers)
    }

    override fun hideTv_help() {
        tv_help_main.visibility = View.GONE
    }

    override fun showRecyclerView() {
        swipe_layout_main.visibility = View.VISIBLE
        recyclerView.visibility = View.VISIBLE
    }

    override fun hideRecyclerView() {
        swipe_layout_main.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

    override fun hideProgress() {
        swipe_layout_main.isRefreshing = false
    }


    // Ошибки и сообщения

    override fun showError() {
        Toast.makeText(baseContext, R.string.error, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAuth() {
        Toast.makeText(baseContext, R.string.error_auth, Toast.LENGTH_SHORT).show()
    }

    override fun showWait() {
        Toast.makeText(baseContext, R.string.error_load, Toast.LENGTH_SHORT).show()
    }


    // Help

    override fun setRecycleLM() {
        if (recyclerView.layoutManager == null)
            recyclerView.layoutManager = LinearLayoutManager(this)
    }


    // Навигация

    override fun toProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
