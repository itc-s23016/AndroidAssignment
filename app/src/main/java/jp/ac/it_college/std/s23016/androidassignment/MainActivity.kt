package jp.ac.it_college.std.s23016.androidassignment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.github.kittinunf.fuel.Fuel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import jp.ac.it_college.std.s23016.androidassignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonFetch.setOnClickListener {
            fetchDogImage()
        }
    }

    private fun fetchDogImage() {
        val apiUrl = "https://dog.ceo/api/breeds/image/random"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // APIリクエストを行う
                val (request, response, result) = Fuel.get(apiUrl).responseString()

                // レスポンスの本文を取得
                val jsonString = result.get()

                // レスポンスをデシリアライズしてDogImageResponseに変換
                val dogImageResponse = Json.decodeFromString<DogImageResponse>(jsonString)

                // メインスレッドでUI更新
                runOnUiThread {
                    binding.imageView.load(dogImageResponse.message) {
                        placeholder(R.drawable.placeholder)
                        error(R.drawable.error)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    binding.imageView.setImageResource(R.drawable.error)
                }
            }
        }
    }
}

@Serializable
data class DogImageResponse(
    val message: String,
    val status: String
)
