package yeapcool.testtask.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import yeapcool.testtask.R
import yeapcool.testtask.mvp.model.person.Person


class UsersAdapter(private val context: Context) : RecyclerView.Adapter<UsersViewHolder>(), IUsersAdapter.Model, IUsersAdapter.View {

    // Interface
    override var onClick: ((Int) -> Unit)? = null

    private lateinit var persons: ArrayList<Person>


    // Создание VH

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.raw_person, parent, false)
        return UsersViewHolder(view, onClick)
    }


    // Привязка VH

    override fun onBindViewHolder(holder: UsersViewHolder?, position: Int) {
        persons[position].let {
            holder?.bind(persons[position], position)
        }
    }


    // Инициализация items

    override fun setItems(persons: ArrayList<Person>) {
        this.persons = persons
    }


    // Добавление items

    override fun addItems(persons: ArrayList<Person>) {
        this.persons.addAll(persons)
    }


    // Обновление

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun notifySomeItems(updateSize: Int) {
        notifyItemInserted(updateSize)
    }


    // Получение item и размера items

    override fun getItem(position: Int) = persons[position]

    override fun getItemCount() = persons.size
}