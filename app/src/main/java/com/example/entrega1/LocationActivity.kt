import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.entrega1.R

class LocationActivity : AppCompatActivity() {

    private lateinit var btnAgregarFoto: Button
    private lateinit var imagenLugar: ImageView

    private val takePicture: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                handleImageResult(data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        btnAgregarFoto = findViewById(R.id.btnAgregarFoto)
        imagenLugar = findViewById(R.id.imagenLugar)

        btnAgregarFoto.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            takePicture.launch(takePictureIntent)
        }
    }

    private fun handleImageResult(data: Intent?) {
        val extras = data?.extras
        val imageBitmap = extras?.get("data") as Bitmap
        imagenLugar.setImageBitmap(imageBitmap)
    }
}
