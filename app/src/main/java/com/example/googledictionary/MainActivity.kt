package com.example.googledictionary

import WordResult
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.googledictionary.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.Objects

class MainActivity : AppCompatActivity() {
    lateinit var outputTV: TextView
    lateinit var micIV: ImageView
    private val REQUEST_CODE_SPEECH_INPUT = 1
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        outputTV = binding.searchInput
        binding.searchBtn.setOnClickListener()
        {
           val word = binding.searchInput.text.toString()
            getMeaning(word)
        }
        binding.idIVMic.setOnClickListener {
            // on below line we are calling speech recognizer intent.
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            // on below line we are passing language model
            // and model free form in our intent
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
            //speechto text
            // on below line we are specifying a prompt
            // message as speak to text on below line.
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            // on below line we are specifying a try catch block.
            // in this block we are calling a start activity
            // for result method and passing our result code.
            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                // on below line we are displaying error message in toast
                Toast
                    .makeText(
                        this@MainActivity, " " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // in this method we are checking request
        // code with our result code.
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            // on below line we are checking if result code is ok
            if (resultCode == RESULT_OK && data != null) {

                // in that case we are extracting the
                // data from our array list
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                // on below line we are setting data
                // to our output text view.
                binding.searchInput.setText(
                    Objects.requireNonNull(res)[0]
                )
            }
        }
    }

    private fun getMeaning(word: String)
    {
        setInProgress(true)
        GlobalScope.launch {
             val response = RetrofitInstance.dictionaryApi.getMeaning(word)
            runOnUiThread {
                setInProgress(false)
                response.body()?.first()?.let {
                    setUI(it)
            }

        }
        }
    }

    private fun setUI(it: WordResult) {


        binding.textSearch.text = it.word.toString()
        binding.textmeaning.text = it.meanings.first().definitions.first().definition.toString()
        binding.licensetext.text = "License"
        binding.textlicenseDes.text = it.license.name.toString()
        binding.textlicenseurl.text = it.license.url.toString()
        binding.textsourcelink.text = "Source link"
        binding.sourcelinkfirst.text = it.sourceUrls[0].toString()
    }


    private fun setInProgress(inProgress: Boolean) {
    if(inProgress)
    {
        binding.searchBtn.visibility =View.INVISIBLE
      //  binding.progress
    }
    else
    {
        binding.searchBtn.visibility =View.VISIBLE
    }
    }
}