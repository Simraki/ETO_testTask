package yeapcool.testtask.mvp.friends

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import yeapcool.testtask.adapter.IUsersAdapter
import yeapcool.testtask.utils.Constants
import yeapcool.testtask.mvp.model.Model
import yeapcool.testtask.mvp.model.person.Person


class FriendsPresenter : IFriends.Presenter {

    // View and Model
    private var view: IFriends.View? = null
    private val model = Model()

    // Adapter
    private var modelAdapter: IUsersAdapter.Model? = null
    private var viewAdapter: IUsersAdapter.View? = null
        set(value) {
            field = value
            field?.onClick = { showToast() }
        }

    // Presenter
    private var list = ArrayList<Person>()
    private var number = 0
    private val disposables = CompositeDisposable()
    private var isInitScrollTh = true


    // Bind | Undind | Clear

    override fun bind(view: IFriends.View, modelAdapter: IUsersAdapter.Model, viewAdapter: IUsersAdapter.View) {
        this.view = view
        this.modelAdapter = modelAdapter
        this.viewAdapter = viewAdapter

        getFriends(true)
    }

    override fun unbind() {
        view = null
        modelAdapter = null
        viewAdapter = null
        view?.recyclerView?.adapter = null
    }

    override fun clearTh() {
        disposables.clear()
        disposables.dispose()
    }


    // Получение друзей пользователя

    private fun getFriends(clear: Boolean) {

        val id = if (model.users().isMy()) model.pref().getId() else model.users().getUser()?.id
        val token = model.pref().getAccessToken()

        if (id != null && token != Constants.DEFAULT_TOKEN && id != Constants.DEFAULT_ID) {

            list = ArrayList()
            if (clear)
                number = 0


            val offset = Constants.COUNT * number

            val th = model.getFriends(id, offset, Constants.COUNT, Constants.FIELDS_FRIENDS, token)

            if (th != null) {
                number++

                val d = th
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnTerminate {
                            terminateGetFriends(offset)
                        }
                        .subscribe { it ->
                            if (it != null && it.deactivated == null && it.hidden == null)
                                list.add(it)
                        }

                addDisposable(d)
            } else
                view?.showErrorGet()
        } else
            view?.showError()
    }

    private fun terminateGetFriends(offset: Int) {
        if (list.isEmpty()) {
            if (number == 1)
                view?.showTv_help()
        } else {
            if (number == 1) {
                modelAdapter?.setItems(list)
                viewAdapter?.notifyAdapter()
                view?.setRecycleLM()

            } else if (number > 1) {
                modelAdapter?.addItems(list)
                viewAdapter?.notifySomeItems(offset)

            }

            if (isInitScrollTh) {
                val d = initScrollEvent()
                addDisposable(d)
                isInitScrollTh = false
            }

            view?.showRecyclerView()
        }

        view?.hideProgress()
    }


    // Обновление друзей пользователя

    override fun refreshUsers() {
        getFriends(true)
    }


    //region >-- Пагинация --<

    private fun initScrollEvent(): Disposable =
            getScrollObservable()
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe { getFriends(false) }


    private fun getScrollObservable(): Observable<Unit> =
            Observable.create<Unit>({ subscriber ->

                val listener = object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (!subscriber.isDisposed) {
                            val offset = Constants.COUNT * (number - 1)
                            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                            val position = layoutManager.findLastVisibleItemPosition()
                            val updatePosition = offset + Constants.VIEW_OFFSET

                            if (position >= updatePosition) {
                                subscriber.onNext(Unit)
                            }
                        }
                    }
                }

                view?.recyclerView?.addOnScrollListener(listener)
                subscriber.setCancellable({ view?.recyclerView?.removeOnScrollListener(listener) })
                if (view?.recyclerView?.adapter?.itemCount == 0)
                    subscriber.onNext(Unit)
            })

    //endregion


    // Show

    private fun showToast() {
        view?.showToast()
    }


    // Help

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}