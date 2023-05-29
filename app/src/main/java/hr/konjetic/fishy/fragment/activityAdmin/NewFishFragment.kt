package hr.konjetic.fishy.fragment.activityAdmin

import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Bitmap
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import coil.load
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import hr.konjetic.fishy.R
import hr.konjetic.fishy.activity.viewmodel.AdminActivityViewModel
import hr.konjetic.fishy.databinding.FragmentNewFishBinding
import hr.konjetic.fishy.network.model.*


class NewFishFragment : Fragment() {

    private var _binding : FragmentNewFishBinding? = null
    private val binding get() = _binding!!
    private val viewModel : AdminActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewFishBinding.inflate(inflater, container, false)



        var waterType : WaterType? = null
        var fishFamily: FishFamily? = null
        var habitat: Habitat? = null
        var gender: String? = null

        //min school size
        binding.MinSchoolSizeET.setOnClickListener {
            val editext = binding.MinSchoolSizeET

            //logika za numeričku tipkovnicu
            editext.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            editext.isFocusableInTouchMode = true
            editext.requestFocus()
                val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editext, InputMethodManager.SHOW_FORCED)
        }

        //max number of same gender
        binding.MaxNumberOfSameGenderET.setOnClickListener {
            val editext = binding.MaxNumberOfSameGenderET

            //logika za numeričku tipkovnicu
            editext.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            editext.isFocusableInTouchMode = true
            editext.requestFocus()
            val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editext, InputMethodManager.SHOW_FORCED)
        }

        //avg school size
        binding.AvgSchoolSizeET.setOnClickListener {
            val editext = binding.AvgSchoolSizeET

            //logika za numeričku tipkovnicu
            editext.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            editext.isFocusableInTouchMode = true
            editext.requestFocus()
            val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editext, InputMethodManager.SHOW_FORCED)
        }

        //min aquarium size
        binding.MinAquariumSizeET.setOnClickListener {
            val editext = binding.MinAquariumSizeET

            //logika za numeričku tipkovnicu
            editext.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            editext.isFocusableInTouchMode = true
            editext.requestFocus()
            val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editext, InputMethodManager.SHOW_FORCED)
        }

        //quantity
        binding.AvailabilityET.setOnClickListener {
            val editext = binding.AvailabilityET

            //logika za numeričku tipkovnicu
            editext.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            editext.isFocusableInTouchMode = true
            editext.requestFocus()
            val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editext, InputMethodManager.SHOW_FORCED)
        }

        //spinner setups

        setupSpinnerG()

        viewModel.listOfWaterTypes.observe(viewLifecycleOwner){
             setupSpinnerWT()
        }

        viewModel.listOfHabitats.observe(viewLifecycleOwner){
            setupSpinnerH()
        }

        viewModel.listOfFishFamilies.observe(viewLifecycleOwner){
             setupSpinnerFF()
        }

        binding.waterTypeSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val chosenItem = p0?.getItemAtPosition(p2).toString()
                waterType = viewModel.listOfWaterTypes.value?.first { it -> it.type == chosenItem.toString() }!!
                print(waterType)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                /**/
            }
        }

        binding.FishFamilySelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val chosenItem = p0?.getItemAtPosition(p2).toString()
                fishFamily = viewModel.listOfFishFamilies.value?.first { it -> it.name == chosenItem.toString() }!!
                print(fishFamily)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                /**/
            }
        }

        binding.HabitatSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val chosenItem = p0?.getItemAtPosition(p2).toString()
                habitat = viewModel.listOfHabitats.value?.first { it -> it.name == chosenItem.toString() }!!
                print(habitat)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                /**/
            }
        }


        binding.GenderSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val chosenItem = p0?.getItemAtPosition(p2).toString()
                gender = chosenItem.toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                /**/
            }
        }



        binding.createNewFishButton.setOnClickListener {
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
                minAquariumSizeInL = binding.MinAquariumSizeET.text.toString().toInt(),
                gender = gender!!,
                maxNumberOfSameGender = binding.MaxNumberOfSameGenderET.text.toString().toInt(),
                availableInStore = binding.AvailabilityET.text.toString().toInt()
            )

            viewModel.createNewFish(newFish)
            Toast.makeText(requireContext(), "New Fish Created Successfully", Toast.LENGTH_SHORT).show()



        }

        viewModel.newFishId.observe(viewLifecycleOwner){
            val fishId = it

            val qrCodeBitmap = generateQRCode(fishId)
            binding.qrCode.visibility = View.VISIBLE
            binding.qrCode.load(qrCodeBitmap)

        }

        return binding.root
    }


    fun setupSpinnerWT() {

        val headerSelectorValues = viewModel.listOfWaterTypes.value?.map { it.type }
        val headerSelectorValuesEmpty = listOf<String>(" ")
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

    fun setupSpinnerFF() {

        val headerSelectorValues = viewModel.listOfFishFamilies.value?.map { it.name }
        val headerSelectorValuesEmpty = listOf<String>(" ")
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

    fun setupSpinnerH() {

        val headerSelectorValues = viewModel.listOfHabitats.value?.map { it.name }
        val headerSelectorValuesEmpty = listOf<String>(" ")
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

    fun setupSpinnerG() {

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
            // Handle the exception as per your requirement
        }
        // Return a default blank bitmap if QR code generation fails
        return Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
    }
}