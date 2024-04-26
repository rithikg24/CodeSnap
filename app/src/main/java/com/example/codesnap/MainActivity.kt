package com.example.codesnap

import android.R.attr.data
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var textView: TextView
    lateinit var selectImage: Button
    lateinit var predictButton: Button
    lateinit var imageUri: Uri
    lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        setContentView(R.layout.testui)

        imageView = findViewById(R.id.imageView4)
        textView = findViewById(R.id.textView2)
        selectImage = findViewById(R.id.buttonCap)
        predictButton = findViewById(R.id.buttonRun)
        textView.setMovementMethod(ScrollingMovementMethod())

        selectImage.setOnClickListener {
            val intent = Intent()
            intent.type="image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"Select Image"),1)
        }
        predictButton.setOnClickListener {
            textView.setText("Running your Code")
            Toast.makeText(
                this, "Running your Code",
                Toast.LENGTH_LONG
            ).show()
            bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val myApiKey = "AIzaSyB0V4nDuw-lJaoaKGj4eiHkCkChdAk7lrI"
            val generativeModel = GenerativeModel(
                // For text-and-images input (multimodal), use the gemini-pro-vision model
                modelName = "gemini-pro-vision",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                apiKey = myApiKey
            )
            val inputContent = content {
                image(bitmap)
                text("fix the errors in the code in the image and give the output of the code")
            }

            runBlocking {
                val response = generativeModel.generateContent(inputContent)
                print(response.text)
                textView.text = response.text.toString()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1 && resultCode==Activity.RESULT_OK){
            if(data!=null){
                imageView.setImageURI(data.data)
            }
        }
    }
}