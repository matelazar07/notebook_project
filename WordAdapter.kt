package com.example.konyvguilde

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordAdapter() : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    private var wrdList: ArrayList<WordModel> = ArrayList()
    private var onClickItem: ((WordModel) -> Unit)? = null
    private var onClickDeleteItem: ((WordModel) -> Unit)? = null

    fun addItems(items: ArrayList<WordModel>) {
        this.wrdList = items
        notifyDataSetChanged()
    }
    fun setOnClickItem(callback: (WordModel) -> Unit) {
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback: (WordModel) -> Unit) {
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word_items_wrd, parent, false)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return wrdList.size
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val wrd = wrdList[position]
        holder.bindView(wrd)
        holder.itemView.setOnClickListener { onClickItem?.invoke(wrd) }

        // Ellenőrizd, hogy melyik Activity-ben vagyunk, és csak az activity_second.xml-ben jelenítsd meg a delete gombot
        if (holder.itemView.context is SecondActivity) {
            holder.btnDelete.visibility = View.VISIBLE
            holder.btnDelete.setOnClickListener { onClickDeleteItem?.invoke(wrd) }
        } else {
            holder.btnDelete.visibility = View.GONE
        }
    }
    fun filterList(filteredList: ArrayList<WordModel>) {
        wrdList = filteredList
        notifyDataSetChanged()
    }

    class WordViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var article = view.findViewById<TextView>(R.id.tvArticle)
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var meaning = view.findViewById<TextView>(R.id.tvMeaning)
        private var plural = view.findViewById<TextView>(R.id.tvPlural)
        var btnDelete = view.findViewById<ImageView>(R.id.btnDelete)

        fun bindView(wrd: WordModel) {
            id.text = wrd.id.toString()
            article.text = wrd.article
            name.text = wrd.name
            meaning.text = wrd.meaning
            plural.text = wrd.plural

        }
    }
}
