package yeapcool.testtask.adapter

import yeapcool.testtask.mvp.model.person.Person


interface IUsersAdapter {

    interface Model {

        fun setItems(persons: ArrayList<Person>)

        fun addItems(persons: ArrayList<Person>)

        fun getItem(position: Int): Person?

    }

    interface View {

        var onClick: ((Int) -> Unit)?

        fun notifyAdapter()

        fun notifySomeItems(updateSize: Int)

    }

}