package com.example.konyvguilde

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var edArticle: EditText
    private lateinit var edName: EditText
    private lateinit var edMeaning: EditText
    private lateinit var edPlural: EditText
    private lateinit var btnAdd: ImageView
    private lateinit var btnView: ImageView
    private lateinit var btnUpdate: ImageView
    private lateinit var btnHelp: ImageView
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: WordAdapter? = null
    private var wrd: WordModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initRecyclerView()
        sqLiteHelper = SQLiteHelper(this)
        btnAdd.setOnClickListener { addWords() }
        btnView.setOnClickListener { viewWords() }
        btnUpdate.setOnClickListener { updateWords() }
        btnHelp.setOnClickListener {gotoHelp()}
        adapter = WordAdapter()
        adapter?.setOnClickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            edArticle.setText(it.article)
            edName.setText(it.name)
            edMeaning.setText(it.meaning)
            edPlural.setText(it.plural)
            wrd = it
        }


        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = WordAdapter()
        recyclerView.adapter = adapter
    }
    private fun addWords() {
        val article = edArticle.text.toString()
        val name = edName.text.toString()
        val meaning = edMeaning.text.toString()
        val plural = edPlural.text.toString()
        if (name.isEmpty() || meaning.isEmpty() ) {
            Toast.makeText(this, "Kérjük írja be a kötelező mezőket", Toast.LENGTH_SHORT).show()
            edName.error = "Kötelező mező"
            edMeaning.error = "Kötelező mező"
            edName.setHintTextColor(ContextCompat.getColor(this, R.color.red))
            edMeaning.setHintTextColor(ContextCompat.getColor(this, R.color.red))

        } else {
            edName.setHintTextColor(ContextCompat.getColor(this, R.color.black))
            edMeaning.setHintTextColor(ContextCompat.getColor(this, R.color.black))
            edName.error = null
            edMeaning.error = null

            val wrd = WordModel(article = article,name = name, meaning = meaning, plural = plural)
            val status = sqLiteHelper.insertWords(wrd)

            if (status > -1) {
                Toast.makeText(this, "Szó felvétel sikeres", Toast.LENGTH_SHORT).show()
                clearEditText()
                getWords()
            } else {
                Toast.makeText(this, "Szó nincs felvétel", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun updateWords() {
        val article = edArticle.text.toString()
        val name = edName.text.toString()
        val meaning = edMeaning.text.toString()
        val plural = edPlural.text.toString()

        if (wrd != null && ( article != wrd!!.article || name != wrd!!.name || meaning != wrd!!.meaning || plural != wrd!!.plural )) {
            val updatedWrd = WordModel(id = wrd!!.id, article = article, name = name, meaning = meaning, plural = plural)
            val rowsAffected = sqLiteHelper.updateWords(updatedWrd)

            if (rowsAffected > 0) {
                Toast.makeText(this, "Frissítés sikeres", Toast.LENGTH_SHORT).show()
                clearEditText()
                getWords()
            } else {
                Toast.makeText(this, "Frissítés sikertelen", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Nem változott semmi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getWords() {
        val wrdList = sqLiteHelper.getAllWords()
        adapter?.addItems(wrdList)
    }
    private fun clearEditText() {
        edArticle.setText("")
        edName.setText("")
        edMeaning.setText("")
        edPlural.setText("")
    }
    private fun viewWords() {
        val wordList = sqLiteHelper.getAllWords()
        val intent = Intent(this, SecondActivity::class.java)
        intent.putParcelableArrayListExtra("WORD_LIST", ArrayList(wordList))
        startActivity(intent)

    }
    private fun initView() {
        edName = findViewById(R.id.edName)
        edArticle = findViewById(R.id.edArticle)
        edPlural = findViewById(R.id.edPlural)
        edMeaning = findViewById(R.id.edMeaning)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnHelp=findViewById(R.id.btnHelp)
        recyclerView = findViewById(R.id.recyclerView)
    }
    private fun gotoHelp()
    {
        val intent = Intent(this, AppHelper::class.java)
        startActivity(intent)
    }
}



