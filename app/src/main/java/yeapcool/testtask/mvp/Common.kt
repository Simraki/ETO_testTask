package yeapcool.testtask.mvp


interface Common {

    interface View {
        fun showError()
    }

    interface Presenter {
        fun unbind()
    }

}