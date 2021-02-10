package com.example.myapplication.views.list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.BR
import com.example.myapplication.R
import com.example.myapplication.model.User
import com.example.myapplication.viewmodels.MainViewModel

class UsersRecyclerViewAdapter(
    private val viewModel: MainViewModel,
    val users: MutableList<User>
) : RecyclerView.Adapter<UsersRecyclerViewAdapter.GenericViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return GenericViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size

    override fun getItemViewType(position: Int): Int = R.layout.user_item

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        holder.bind(viewModel as MainViewModel, position)
        holder.itemView.setBackgroundColor(Color.WHITE)
        holder.itemView.setOnLongClickListener {
            it.setBackgroundColor(Color.LTGRAY)
            viewModel.deleteUser(users[position])
            true
        }
    }

    fun addItems(users: List<User>) {
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    fun addItem(user: User) {
        this.users.add(user)
        notifyDataSetChanged()
    }

    fun removeUser(user: User) {
        this.users.remove(user)
        notifyDataSetChanged()
    }

    inner class GenericViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ViewModel, position: Int?) {
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }
}