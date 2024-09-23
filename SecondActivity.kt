package com.example.konyvguilde

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.Locale

class SecondActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WordAdapter
    private lateinit var sqLiteHelper: SQLiteHelper
    private var wordList: ArrayList<WordModel> = ArrayList()
    private lateinit var edSearch: EditText
    private var isSearchVisible: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        edSearch = findViewById<EditText>(R.id.edSearch)
        val btnSearch = findViewById<ImageView>(R.id.btnSearch)
        btnSearch.setOnClickListener {
            toggleSearchVisibility()
        }
        adapter = WordAdapter()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        wordList = intent.getParcelableArrayListExtra<WordModel>("WORD_LIST")!!
        adapter.addItems(wordList)

        sqLiteHelper = SQLiteHelper(this)

       val btnSave = findViewById<ImageView>(R.id.btnSave)
        btnSave.setOnClickListener {
            saveDataToTxtFile()
        }

        val btnDownload = findViewById<ImageView>(R.id.btnDownload)
        btnDownload.setOnClickListener {
            downloadData()
        }

        adapter?.setOnClickDeleteItem {
            deleteStudent(it.id)
        }


        edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().toLowerCase(Locale.getDefault())
                val filteredList = ArrayList<WordModel>()

                for (word in wordList) {
                    if (word.name.toLowerCase(Locale.getDefault()).contains(searchText) ||
                        word.meaning.toLowerCase(Locale.getDefault()).contains(searchText) ||
                        word.article.toLowerCase(Locale.getDefault()).contains(searchText) ||
                        word.plural.toLowerCase(Locale.getDefault()).contains(searchText)
                    ) {
                        filteredList.add(word)
                    }
                }

                adapter.filterList(filteredList)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun deleteStudent(id: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Biztosan szeretnéd törölni az adatokat?")

        alertDialogBuilder.setPositiveButton("Nem") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Igen") { _, _ ->
            dodeleteStudent(id)
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    private fun dodeleteStudent(id: Int) {
        val rowsAffected = sqLiteHelper.deleteWordsByID(id)
        runOnUiThread {
            if (rowsAffected > 0) {
                Toast.makeText(this, "Törlés sikeres", Toast.LENGTH_SHORT).show()
                getWords()
            } else {
                Toast.makeText(this, "Törlés sikertelen", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getWords() {
        val wrdList = sqLiteHelper.getAllWords()
        adapter?.addItems(wrdList)
    }

    private fun toggleSearchVisibility() {

        if (edSearch.visibility == View.VISIBLE) {
            edSearch.visibility = View.GONE
        } else {
            edSearch.visibility = View.VISIBLE
        }
    }


    private fun saveDataToTxtFile() {
        val dataToSave = wordList
        val fileName = "mentett_adatok.txt"

        try {
            val fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)

            for (item in dataToSave) {
                val line = "${item.article} - ${item.name} - ${item.plural} - ${item.meaning}\n"
                outputStreamWriter.write(line)
            }

            outputStreamWriter.close()
            Toast.makeText(this, "Adatok mentve: $fileName", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Hiba történt a mentés során.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun downloadData() {
        val fileName = "mentett_adatok.txt"
        val sourceFile = File(filesDir, fileName)
        val destinationFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)

        try {

            sourceFile.copyTo(destinationFile, overwrite = true)


            val fileUri = Uri.parse("https://example.com/downloads/mentett_adatok.txt")

            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(fileUri)
                .setTitle("Mentett Adatok")
                .setDescription("Letöltött fájl")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            downloadManager.enqueue(request)
        } catch (e: Exception) {
            Toast.makeText(this, " ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}
