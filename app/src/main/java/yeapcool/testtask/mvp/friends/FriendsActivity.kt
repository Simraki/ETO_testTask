package yeapcool.testtask.mvp.friends

import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_friends.*
import yeapcool.testtask.R
import yeapcool.testtask.adapter.UsersAdapter


class FriendsActivity : AppCompatActivity(), IFriends.View, SwipeRefreshLayout.OnRefreshListener {

    // Interface
    override lateinit var recyclerView: RecyclerView

    private lateinit var presenter: FriendsPresenter
    private lateinit var adapter: UsersAdapter

    private val toolbar by lazy {
        findViewById<Toolbar>(R.id.toolbar_friends)
    }


    // Инициализация

    private fun init() {
        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        adapter = UsersAdapter(this)

        recyclerView = recycler_friends
        recyclerView.adapter = adapter

        presenter = FriendsPresenter()

        swipe_layout_friends.setOnRefreshListener(this)
        swipe_layout_friends.setColorSchemeColors(Color.BLUE)
    }

    override fun setRecycleLM() {
        if (recyclerView.layoutManager == null)
            recyclerView.layoutManager = LinearLayoutManager(this)
    }


    // Show-Hide

    override fun showTv_help() {
        if (tv_help_friends.visibility != View.VISIBLE) {
            tv_help_friends.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }

    override fun showRecyclerView() {
        if (recyclerView.visibility != View.VISIBLE) {
            recyclerView.visibility = View.VISIBLE
            tv_help_friends.visibility = View.GONE
        }
    }

    override fun hideProgress() {
        swipe_layout_friends.isRefreshing = false
    }


    // Ошибки и сообщения

    override fun showError() {
        Toast.makeText(baseContext, R.string.error, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorGet() {
        Toast.makeText(baseContext, R.string.error_get, Toast.LENGTH_SHORT).show()
    }

    override fun showToast() {
        Toast.makeText(this, R.string.toast_avoid, Toast.LENGTH_SHORT).show()
    }


    // _Android-logic

    override fun onRefresh() {
        presenter.refreshUsers()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == android.R.id.home)
            onBackPressed()

        return super.onOptionsItemSelected(item)
    }


    // _Android-life

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

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

        presenter.clearTh()
    }
}