package hr.konjetic.fishy.fragment.activityAdmin

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import hr.konjetic.fishy.R
import hr.konjetic.fishy.activity.viewmodel.AdminActivityViewModel
import hr.konjetic.fishy.databinding.FragmentNewFishBinding
import hr.konjetic.fishy.network.model.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class NewFishFragment : Fragment() {

    private var _binding : FragmentNewFishBinding? = null
    private val binding get() = _binding!!
    private val viewModel : AdminActivityViewModel by activityViewModels()
    private var onFishCreatedListener: OnFishCreatedListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewFishBinding.inflate(inflater, container, false)

        var waterType: WaterType? = null
        var fishFamily: FishFamily? = null
        var habitat: Habitat? = null
        var gender: String? = null



        binding.MinSchoolSizeET.setOnClickListener {
            setupNumericKeyboard(binding.MinSchoolSizeET)
        }

        binding.MaxNumberOfSameGenderET.setOnClickListener {
            setupNumericKeyboard(binding.MaxNumberOfSameGenderET)
        }

        binding.AvgSchoolSizeET.setOnClickListener {
            setupNumericKeyboard(binding.AvgSchoolSizeET)
        }


        fun setupSpinners() {
            setupSpinnerG()

            viewModel.listOfWaterTypes.observe(viewLifecycleOwner) {
                setupSpinnerWT()
            }

            viewModel.listOfHabitats.observe(viewLifecycleOwner) {
                setupSpinnerH()
            }

            viewModel.listOfFishFamilies.observe(viewLifecycleOwner) {
                setupSpinnerFF()
            }

            binding.waterTypeSelector.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        val chosenItem = p0?.getItemAtPosition(p2).toString()
                        waterType = viewModel.listOfWaterTypes.value?.first { it -> it.type == chosenItem }!!
                        print(waterType)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        /**/
                    }
                }

            binding.FishFamilySelector.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        val chosenItem = p0?.getItemAtPosition(p2).toString()
                        fishFamily = viewModel.listOfFishFamilies.value?.first { it -> it.name == chosenItem }!!
                        print(fishFamily)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        /**/
                    }
                }

            binding.HabitatSelector.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        val chosenItem = p0?.getItemAtPosition(p2).toString()
                        habitat = viewModel.listOfHabitats.value?.first { it -> it.name == chosenItem }!!
                        print(habitat)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        /**/
                    }
                }

            binding.GenderSelector.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        val chosenItem = p0?.getItemAtPosition(p2).toString()
                        gender = chosenItem
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        /**/
                    }
                }
        }

        setupSpinners()

        binding.createNewFishButton.setOnClickListener {
            if (!isFormValid()) {
                Toast.makeText(
                    requireContext(),
                    "Please fill out all the fields.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val newFish = FishDTO(
                    name = binding.NameET.text.toString(),
                    description = binding.descriptionET.text.toString(),
                    waterTypeId = waterType!!.id,
                    waterType = waterType!!,
                    fishFamilyId = fishFamily!!.id,
                    fishFamily = fishFamily!!,
                    habitat = habitat!!,
                    habitatId = habitat!!.id,
                    image = binding.ImageET.text.toString(),
                    minSchoolSize = binding.MinSchoolSizeET.text.toString().toInt(),
                    avgSchoolSize = binding.AvgSchoolSizeET.text.toString().toInt(),
                    gender = gender!!,
                    maxNumberOfSameGender = binding.MaxNumberOfSameGenderET.text.toString().toInt()
                )

                viewModel.createNewFish(newFish)
                Toast.makeText(
                    requireContext(),
                    "New Fish Created Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }

            viewModel.newFishId.observe(viewLifecycleOwner) {
                val fishId = it
                val qrCodeBitmap = generateQRCode(fishId)
                showImageDialog(requireContext(), qrCodeBitmap, it)
                clearOutFrom(binding)
                onFishCreatedListener?.onFishCreated()
            }
        }

        return binding.root
    }

    private fun isFormValid(): Boolean {
        val name = binding.NameET.text.toString().trim()
        val description = binding.descriptionET.text.toString().trim()
        val image = binding.ImageET.text.toString().trim()
        val minSchoolSize = binding.MinSchoolSizeET.text.toString().trim()
        val avgSchoolSize = binding.AvgSchoolSizeET.text.toString().trim()
        val maxNumberOfSameGender = binding.MaxNumberOfSameGenderET.text.toString().trim()
        return name.isNotEmpty() && description.isNotEmpty() && image.isNotEmpty() &&
                minSchoolSize.isNotEmpty() && avgSchoolSize.isNotEmpty() &&
                maxNumberOfSameGender.isNotEmpty()
    }

    private fun clearOutFrom(binding: FragmentNewFishBinding){
        binding.NameET.text.clear()
        binding.descriptionET.text.clear()
        binding.ImageET.text.clear()
        binding.MinSchoolSizeET.text.clear()
        binding.AvgSchoolSizeET.text.clear()
        binding.MaxNumberOfSameGenderET.text.clear()

        binding.waterTypeSelector.setSelection(0)
        binding.FishFamilySelector.setSelection(0)
        binding.HabitatSelector.setSelection(0)
        binding.GenderSelector.setSelection(0)
    }

    private fun showImageDialog(context: Context, bitmap: Bitmap, fishId: String) {
        val imageView = ImageView(context).apply {
            setImageBitmap(bitmap)
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            adjustViewBounds = true
        }

        AlertDialog.Builder(context)
            .setTitle("New Fish QR Code")
            .setView(imageView)
            .setPositiveButton("Download") { _, _ ->
                saveImageToGallery(context, bitmap, fishId)
            }
            .setNeutralButton("Cancel", null)
            .show()
    }

    private fun saveImageToGallery(context: Context, bitmap: Bitmap, fishId: String) {
        val filename = "qrCode_$fishId.jpg"
        val saveDir = File(context.getExternalFilesDir(null), "Images")
        saveDir.mkdirs()

        val file = File(saveDir, filename)
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
            }

            MediaStore.Images.Media.insertImage(context.contentResolver, file.absolutePath, filename, null)
            Toast.makeText(requireContext(), "QR Code Image saved to device successfully!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }




    private fun setupSpinnerWT() {

        val headerSelectorValues = viewModel.listOfWaterTypes.value?.map { it.type }
        val headerSelectorValuesEmpty = listOf(" ")
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            if (headerSelectorValues?.isEmpty() == true){
                headerSelectorValuesEmpty
            } else{
                headerSelectorValues!!
            }
        )
        binding.waterTypeSelector.adapter = arrayAdapter

    }

    private fun setupSpinnerFF() {

        val headerSelectorValues = viewModel.listOfFishFamilies.value?.map { it.name }
        val headerSelectorValuesEmpty = listOf(" ")
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            if (headerSelectorValues?.isEmpty() == true){
                headerSelectorValuesEmpty
            } else{
                headerSelectorValues!!
            }
        )
        binding.FishFamilySelector.adapter = arrayAdapter

    }

    private fun setupSpinnerH() {

        val headerSelectorValues = viewModel.listOfHabitats.value?.map { it.name }
        val headerSelectorValuesEmpty = listOf(" ")
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            if (headerSelectorValues?.isEmpty() == true){
                headerSelectorValuesEmpty
            } else{
                headerSelectorValues!!
            }
        )
        binding.HabitatSelector.adapter = arrayAdapter

    }

    private fun setupSpinnerG() {

        val headerSelectorValues = listOf("MALE", "FEMALE")
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            headerSelectorValues

        )
        binding.GenderSelector.adapter = arrayAdapter

    }

    private fun generateQRCode(content: String): Bitmap {
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 500, 500)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                }
            }
            return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.RGB_565)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
    }

    interface OnFishCreatedListener {
        fun onFishCreated()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFishCreatedListener) {
            onFishCreatedListener = context
        }
    }

    private fun setupNumericKeyboard(editText: EditText) {
        editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
    }

}