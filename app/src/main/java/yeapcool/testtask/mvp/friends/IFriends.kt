package yeapcool.testtask.mvp.friends

import android.support.v7.widget.RecyclerView
import yeapcool.testtask.adapter.IUsersAdapter
import yeapcool.testtask.mvp.Common


interface IFriends {

    interface Presenter : Common.Presenter {

        fun bind(view: IFriends.View, modelAdapter: IUsersAdapter.Model, viewAdapter: IUsersAdapter.View)

        fun refreshUsers()

        fun clearTh()

    }

    interface View : Common.View {

        var recyclerView: RecyclerView

        fun setRecycleLM()

        fun showToast()

        fun showRecyclerView()

        fun showTv_help()

        fun showErrorGet()

        fun hideProgress()
    }
}