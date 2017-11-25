package yeapcool.testtask.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import yeapcool.testtask.R
import yeapcool.testtask.mvp.model.ImageLoaderInst
import yeapcool.testtask.mvp.model.person.Person


class UsersViewHolder(val view: View, val listener: ((Int) -> Unit)?)
    : RecyclerView.ViewHolder(view) {

    internal var name = itemView.findViewById<TextView>(R.id.name_person_raw)
    internal var cv = itemView.findViewById<CardView>(R.id.person)
    internal var image: ImageView = itemView.findViewById<ImageView>(R.id.image_person_raw)

    fun bind(item: Person?, position: Int) {
        val text = item?.first_name + " " + item?.last_name
        name.text = text

        ImageLoaderInst.get().displayImage(item?.photo_50, image)

        itemView.setOnClickListener { listener?.invoke(position) }
    }
}