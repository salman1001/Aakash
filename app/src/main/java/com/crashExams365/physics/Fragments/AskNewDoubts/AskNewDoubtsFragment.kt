package com.crashExams365.physics.Fragments.AskNewDoubts

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.crashExams365.physics.Fragments.Doubts.DoubtsFragmentDirections
import com.crashExams365.physics.Fragments.Doubts.DoubtsViewModel
import com.crashExams365.physics.Fragments.Launch.LaunchViewModel
import com.crashExams365.physics.MainActivity
import com.crashExams365.physics.R
import com.crashExams365.physics.common.UserInfo
import com.crashExams365.physics.databinding.AskNewDoubtsFragmentBinding
import com.crashExams365.physics.databinding.DoubtsFragmentBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.io.File
import java.util.*
import java.util.jar.Manifest

class AskNewDoubtsFragment : Fragment(),EasyPermissions.PermissionCallbacks {



    private val ImageList: ArrayList<Uri> = ArrayList<Uri>()
    private var uploads = 0
    private var databaseReference: DatabaseReference? = null
    private var progressDialog: ProgressDialog? = null
    var index = 0
    var textView: TextView? = null
    var choose: Button? = null
    var send: Button? = null




    //  protected fun onCreate(savedInstanceState: Bundle?) {
    //     super.onCreate(savedInstanceState)
    //      setContentView(R.layout.activity_main)
    // databaseReference = FirebaseDatabase.getInstance().getReference().child("User_one")
    //     progressDialog = ProgressDialog(this)
    //     progressDialog.setMessage("Uploading ..........")
    //     textView = findViewById(R.id.text)
    //     choose = findViewById(R.id.choose)
    //     send = findViewById(R.id.upload)
    //  }

//    fun choose(view: View?) {
//        //we will pick images
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.setType("image/*")
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//        startActivityForResult(intent, PICK_IMG)
//    }

    // @SuppressLint("SetTextI18n")
//    protected fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMG) {
//            if (resultCode == RESULT_OK) {
//                if (data.getClipData() != null) {
//                    val count: Int = data.getClipData().getItemCount()
//                    var CurrentImageSelect = 0
//                    while (CurrentImageSelect < count) {
//                        val imageuri: Uri =
//                            data.getClipData().getItemAt(CurrentImageSelect).getUri()
//                        ImageList.add(imageuri)
//                        CurrentImageSelect = CurrentImageSelect + 1
//                    }
//                    textView.setVisibility(View.VISIBLE)
//                    textView.setText(
//                        "You Have Selected " + ImageList.size().toString() + " Pictures"
//                    )
//                    choose.setVisibility(View.GONE)
//                }
//            }
//        }
//    }

    // @SuppressLint("SetTextI18n")
//    fun upload(view: View?) {
//     //   textView.setText("Please Wait ... If Uploading takes Too much time please the button again ")
//       // progressDialog.show()
//        val ImageFolder: StorageReference =
//            FirebaseStorage.getInstance().getReference().child("ImageFolder")
//        uploads = 0
//        while (uploads < ImageList.size()) {
//            val Image: Uri = ImageList[uploads]
//            val imagename: StorageReference =
//                ImageFolder.child("image/" + Image.getLastPathSegment())
//            imagename.putFile(ImageList[uploads])
//                .addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot?>() {
//                    fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
//                        imagename.getDownloadUrl()
//                            .addOnSuccessListener(object : OnSuccessListener<Uri?>() {
//                                fun onSuccess(uri: Uri?) {
//                                    val url: String = java.lang.String.valueOf(uri)
//                                    SendLink(url)
//                                }
//                            })
//                    }
//                })
//            uploads++
//        }
//    }

//    private fun SendLink(url: String) {
//        val hashMap: HashMap<String, String> = HashMap()
//        hashMap["link"] = url
//        databaseReference.push().setValue(hashMap)
//            .addOnCompleteListener(object : OnCompleteListener<Void?>() {
//                fun onComplete(@NonNull task: Task<Void?>?) {
//                    progressDialog.dismiss()
//                    textView.setText("Image Uploaded Successfully")
//                    send.setVisibility(View.GONE)
//                    ImageList.clear()
//                }
//            })
//    }

    companion object {
        private const val PICK_IMG = 1
        const val PERMISSION_CAMARA_REQUEST_CODE = 2;
        const val PERMISSION_EXTERNAL_REQUEST_CODE = 3;

    }


     var uri: Uri?=null


    private var _binding: AskNewDoubtsFragmentBinding? = null
    private val binding get() = _binding!!
    var navController: NavController? = null
    private lateinit var viewModel: AskNewDoubtsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[AskNewDoubtsViewModel::class.java]
        _binding = AskNewDoubtsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = "Ask Your Question"
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3")
                )
            )
        }
        requestCamaraPermissioin()
        //  requestExtrenalPermissioin()


        binding.buttonsubmit.setOnClickListener {
            if (uri!=null){
                binding.buttonsubmit.visibility = View.GONE
                binding.prosub.visibility = View.VISIBLE
                val storageRef = FirebaseStorage.getInstance().getReference()
                val riversRef = storageRef.child("Question").child(
                    FirebaseAuth.getInstance()
                        .currentUser!!.uid
                ).child("${uri!!.lastPathSegment}")
                var uploadTask = riversRef.putFile(uri!!)

// Register observers to listen for when the download is done or if it fails
                // viewModel.getMesssageOfUpload(uploadTask,riversRef,binding.optionText.text.toString()).observe
                viewModel.getMesssageOfUpload(uploadTask,riversRef,binding.optionText.text.toString()).observe(viewLifecycleOwner,{
                    if (it.equals("1")){
                        Toast.makeText(requireContext(), "Done Mate", Toast.LENGTH_LONG).show()
                        binding.prosub.visibility = View.GONE
                        val action = AskNewDoubtsFragmentDirections.actionAskNewDoubtsFragmentToDoubtsFragment()
                        navController!!.navigate(action)



                    }else {
                        Toast.makeText(requireContext(), "Error Upload Image", Toast.LENGTH_LONG).show()

                    }
                })

            }
            else{
                Toast.makeText(requireContext(), "Please Select an Image", Toast.LENGTH_LONG).show()

            }



//            uploadTask.addOnFailureListener {
//                // Handle unsuccessful uploads
//            }.addOnSuccessListener { taskSnapshot ->
//                // uploadTask.
//                // val ref = storageRef.child("images/mountains.jpg")
//                riversRef.downloadUrl.addOnCompleteListener {
//                    Log.d(TAG, "onViewCreated: ${it.result}")
//                    var userref: DatabaseReference
//                    var userdatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
//                    userref = userdatabase.getReference("Question")
//                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
//                    //   var questionids=userdatabase.getReference("QuestionIds")
//
//
//                    val modeljh = AskQuestionModel()
//                    modeljh.questionText = binding.optionText.text.toString()
//                    modeljh.questionurl = it.result.toString()
//                    modeljh.userId = FirebaseAuth.getInstance().currentUser!!.uid
//                    val timestamp = Calendar.getInstance().timeInMillis.toString()
//                    modeljh.timestamp = timestamp
//                    modeljh.ansUrl = ""
//                    modeljh.boolean = false
//                    //  val adminQuestionIds=AdminQuestionIds()
//                    //   adminQuestionIds.timestamp=timestamp
//                    //   adminQuestionIds.uid=FirebaseAuth.getInstance().currentUser!!.uid
//                    userref.child(timestamp).setValue(modeljh).addOnSuccessListener {
//                        //  questionids.child(timestamp).setValue(adminQuestionIds).addOnSuccessListener {
//
//                        Toast.makeText(requireContext(), "Done Mate", Toast.LENGTH_LONG).show()
//                        binding.prosub.visibility = View.GONE
//
//                        //   requireActivity().onBackPressed()
//                        val action =
//                            AskNewDoubtsFragmentDirections.actionAskNewDoubtsFragmentToDoubtsFragment()
//                        navController!!.navigate(action)
//
//
//                        //    }
//
//
//                    }.addOnFailureListener {
//
//                    }
//
//
//                }
//
//
//                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
//                // ...
//            }

//                .addOnProgressListener {
//                    (bytesTransferred, totalByteCount) ->
//                val progress = (100.0 * bytesTransferred) / totalByteCount
//                Log.d(TAG, "Upload is $progress% done")
//            }
//            uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
//                val progress = (100.0 * bytesTransferred) / totalByteCount
//                Log.d(TAG, "Upload is $progress% done")
//            }.addOnPausedListener {
//                Log.d(TAG, "Upload is paused")
//            }


        }
        binding.pickIMAGE.setOnClickListener {
            if (seeIfCamaraPermissionGranted()) {
                ImagePicker.with(this)
                    .crop()      //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080,
                        1080
                    )  //Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
//            val intent=Intent()
//            intent.setType("image/*")
//            intent.setAction(Intent.ACTION_GET_CONTENT)
//            startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE)


            } else if (!seeIfCamaraPermissionGranted()) {
                requestCamaraPermissioin()
                if (seeIfCamaraPermissionGranted()) {
                    ImagePicker.with(this)
                        .crop()      //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(
                            1080,
                            1080
                        )  //Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                } else requestCamaraPermissioin()


            }
//            else if(seeIfExternalPermissionGranted()&&!seeIfCamaraPermissionGranted()){
//                requestExtrenalPermissioin()
//                if (seeIfExternalPermissionGranted()){
//                    ImagePicker.with(this)
//                        .crop()      //Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
//                        .createIntent { intent ->
//                            startForProfileImageResult.launch(intent)
//                        }
//                }


//          //  else{
//                requestCamaraPermissioin()
//                //requestExtrenalPermissioin()
//                if(seeIfCamaraPermissionGranted()){
//                    ImagePicker.with(this)
//                        .crop()      //Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
//                        .createIntent { intent ->
//                            startForProfileImageResult.launch(intent)
//                        }
//                }

//           }


        }
    }

    private fun seeIfExternalPermissionGranted() = EasyPermissions
        .hasPermissions(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)

    private fun seeIfCamaraPermissionGranted() = EasyPermissions
        .hasPermissions(requireContext(), android.Manifest.permission.CAMERA)

    private fun requestCamaraPermissioin() {
        EasyPermissions.requestPermissions(
            this,
            "We need Access to Camara and storage if you need to upload images from camara",
            PERMISSION_CAMARA_REQUEST_CODE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
//    private fun requestExtrenalPermissioin(){
//        EasyPermissions.requestPermissions(this,"We need Access to your storage if you need to upload images from gallery",
//            PERMISSION_EXTERNAL_REQUEST_CODE,android.Manifest.permission.READ_EXTERNAL_STORAGE)
//    }


//    companion object{
//        val PICK_IMAGE=6060
//    }


    //    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            //Image Uri will not be null for RESULT_OK
//            val uri: Uri = data?.data!!
//
//            // Use Uri object instead of File to avoid storage permissions
//           // binding.getimage.setImageURI(uri)
//        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
//        }
//    }
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                //   mProfileUri = fileUri
                binding.pickIMAGE.setImageURI(fileUri)
                uri = fileUri
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                // Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                // Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {

        if (EasyPermissions.somePermissionPermanentlyDenied(
                requireActivity(),
                listOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        ) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            if (requestCode == PERMISSION_CAMARA_REQUEST_CODE) {
                requestCamaraPermissioin()

            } else if (requestCode == PERMISSION_EXTERNAL_REQUEST_CODE) {
                //   requestExtrenalPermissioin()

            }

        }


    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
//        if (requestCode == PERMISSION_CAMARA_REQUEST_CODE) Toast.makeText(
//            requireContext(),
//            "Permission Camara Done",
//            Toast.LENGTH_LONG
//        ).show()
//        if (requestCode == PERMISSION_EXTERNAL_REQUEST_CODE) Toast.makeText(
//            requireContext(),
//            "Permission External Storage Done",
//            Toast.LENGTH_LONG
//        ).show()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            requireContext()
        )
    }
}
