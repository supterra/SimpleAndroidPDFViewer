package com.example.simplepdfviewer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView

class MainActivity : AppCompatActivity() {

    private lateinit var pdfView: PDFView

    private val filePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                uri?.let {
                    contentResolver.openInputStream(it)?.use { inputStream ->
                        pdfView.fromStream(inputStream)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .enableAntialiasing(true)
                            .scrollHandle(com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle(this))
                            .load()
                    }
                }
            } else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pdfView = findViewById(R.id.pdfView)

        // Open file picker
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/pdf"
        filePicker.launch(intent)
    }
}
