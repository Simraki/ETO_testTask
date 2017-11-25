package yeapcool.testtask.mvp.main

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import yeapcool.testtask.adapter.IUsersAdapter
import yeapcool.testtask.mvp.Common


interface IMain {

    interface Presenter: Common.Presenter {

        fun showMyProfile()

        fun bind(view: IMain.View, modelAdapter: IUsersAdapter.Model, viewAdapter: IUsersAdapter.View)

        fun saveTokenAndId(token: String, id: Int)

        fun refreshUsers()

        fun getMyProfile()

        fun clearApp()

    }

    interface View: Common.View {

        var searchBar: SearchView

        var recyclerView: RecyclerView

        fun setRecycleLM()

        fun hideProgress()

        fun showTv_help()

        fun hideTv_help()

        fun showRecyclerView()

        fun hideRecyclerView()

        fun toProfile()

        fun showErrorAuth()

        fun showWait()
    }

}