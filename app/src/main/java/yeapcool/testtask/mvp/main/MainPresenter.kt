package yeapcool.testtask.mvp.main

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.vk.sdk.VKSdk
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import yeapcool.testtask.adapter.IUsersAdapter
import yeapcool.testtask.utils.Constants
import yeapcool.testtask.mvp.model.ImageLoaderInst
import yeapcool.testtask.mvp.model.Model
import yeapcool.testtask.mvp.model.person.Person
import java.util.*
import java.util.concurrent.TimeUnit


class MainPresenter : IMain.Presenter {

    // View and Model
    private var view: IMain.View? = null
    private val model = Model()

    // Adapter
    private var modelAdapter: IUsersAdapter.Model? = null
    private var viewAdapter: IUsersAdapter.View? = null
        set(value) {
            field = value
            field?.onClick = { showUserProfile(it) }
        }

    // Presenter
    private var number: Int = 0
    private var list = ArrayList<Person>()
    private val users = model.users()
    private val disposables = CompositeDisposable()

    private var isInitScrollTh: Boolean = true


    // Bind | Unbind | Clear

    override fun bind(view: IMain.View, modelAdapter: IUsersAdapter.Model, viewAdapter: IUsersAdapter.View) {
        this.view = view
        this.modelAdapter = modelAdapter
        this.viewAdapter = viewAdapter

        val d_text = RxSearchView.queryTextChanges(view.searchBar)
                .filter { t -> !t.isEmpty() }
                .filter { t -> t == t.trim() }
                .debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: CharSequence ->
                    view.recyclerView.scrollToPosition(0)
                    searchUser(t.toString(), true)
                })

        addDisposable(d_text)
    }

    override fun unbind() {
        view = null
        modelAdapter = null
        viewAdapter = null
        view?.recyclerView?.adapter = null
    }

    override fun clearApp() {
        users.saveUser(null)
        users.saveMy(null)

        disposables.clear()
        disposables.dispose()
    }


    // Поиск пользователя

    private fun searchUser(q: String, clear: Boolean) {
        if (VKSdk.isLoggedIn()) {

            list = ArrayList()
            if (clear) {
                number = 0
            }

            val token = model.pref().getAccessToken()
            val offset = Constants.COUNT * number

            if (token != Constants.DEFAULT_TOKEN) {

                val th_search = model.userSearch(q, offset, Constants.COUNT, Constants.FIELDS_SEARCH, token)

                if (th_search != null) {
                    number++

                    val d_search = th_search
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnTerminate {
                                terminateSearchUser(offset)
                            }
                            .subscribe { it ->
                                if (it != null && it.deactivated == null && it.hidden == null)
                                    list.add(it)
                            }

                    addDisposable(d_search)

                }
            } else
                view?.showError()
        } else
            view?.showErrorAuth()
    }

    private fun terminateSearchUser(offset: Int) {
        if (list.isEmpty()) {
            if (number == 1) {
                view?.hideRecyclerView()
                view?.showTv_help()
            }
        } else {
            if (number == 1) {

                view?.hideTv_help()

                modelAdapter?.setItems(list)
                viewAdapter?.notifyAdapter()

                view?.showRecyclerView()
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
        }
        view?.hideProgress()
    }


    // Загрузка собственного профиля

    override fun getMyProfile() {
        var p: Person? = null
        val token = model.pref().getAccessToken()
        val id = model.pref().getId()

        if (token != Constants.DEFAULT_TOKEN && id != Constants.DEFAULT_ID) {
            val th_main = model.getProfileInfo(token)

            if (th_main != null) {
                val d_main = th_main
                        .map { it -> p = it }
                        .switchMap { model.getMyPhoto(id, "profile", true, 1, token) }
                        .observeOn(Schedulers.single())
                        .doOnTerminate { users.saveMy(p) }
                        .subscribe { it ->
                            if (it != null) {
                                p?.image = ImageLoaderInst.get().loadImageSync(it.photo_604)
                            }
                        }
                addDisposable(d_main)
            }
        } else
            view?.showError()
    }


    // Обновление пользователей

    override fun refreshUsers() {
        searchUser(view?.searchBar?.query.toString(), true)
    }


    //region >-- Пагинация --<

    private fun initScrollEvent(): Disposable =
            getScrollObservable()
                    .subscribe { searchUser(view?.searchBar?.query.toString(), false) }


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

    override fun showMyProfile() {
        if (VKSdk.isLoggedIn() && users.getMy() != null
                && users.getMy()?.image != null) {
            users.setIsMy(true)
            view?.toProfile()
        } else if (VKSdk.isLoggedIn()) {
            view?.showWait()
        } else if (!VKSdk.isLoggedIn()) {
            view?.showErrorAuth()
        }
    }

    private fun showUserProfile(pos: Int) {
        if (VKSdk.isLoggedIn()) {
            val p = modelAdapter?.getItem(pos)
            model.users().saveUser(p)
            users.setIsMy(false)
            view?.toProfile()
        } else
            view?.showErrorAuth()
    }


    // Сохранение token и id

    override fun saveTokenAndId(token: String, id: Int) {
        model.pref().setAccessToken(token)
        model.pref().setId(id)
    }


    // Help

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}