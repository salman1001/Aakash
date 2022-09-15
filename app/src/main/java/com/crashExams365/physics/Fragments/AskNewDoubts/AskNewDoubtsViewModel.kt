package com.crashExams365.physics.Fragments.AskNewDoubts

import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*

class AskNewDoubtsViewModel : ViewModel() {
    private fun getConnectionType(context: Context): Boolean {
        var result = false // Returns connection type. 0: none; 1: mobile data; 2: wifi
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = true
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = true
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_VPN)){
                        result = true
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    } else if(type == ConnectivityManager.TYPE_VPN) {
                        result = true
                    }
                }
            }
        }
        return result
    }
    private var messageError:MutableLiveData<String> = MutableLiveData()


    fun getMesssageOfUpload(uploadTask: UploadTask,riversRef: StorageReference,questionTest:String): MutableLiveData<String> {
        uploadTask.addOnFailureListener {
            messageError.value="2"
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // uploadTask.
            // val ref = storageRef.child("images/mountains.jpg")
            riversRef.downloadUrl.addOnCompleteListener {
                Log.d(ContentValues.TAG, "onViewCreated: ${it.result}")
                var userref: DatabaseReference
                var userdatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
                userref = userdatabase.getReference("Question")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                //   var questionids=userdatabase.getReference("QuestionIds")


                val modeljh = AskQuestionModel()
                modeljh.questionText = questionTest;
                modeljh.questionurl = it.result.toString()
                modeljh.userId = FirebaseAuth.getInstance().currentUser!!.uid
                val timestamp = Calendar.getInstance().timeInMillis.toString()
                modeljh.timestamp = timestamp
                modeljh.ansUrl = ""
                modeljh.boolean = false
                //  val adminQuestionIds=AdminQuestionIds()
                //   adminQuestionIds.timestamp=timestamp
                //   adminQuestionIds.uid=FirebaseAuth.getInstance().currentUser!!.uid
                userref.child(timestamp).setValue(modeljh).addOnSuccessListener {
                    //  questionids.child(timestamp).setValue(adminQuestionIds).addOnSuccessListener {

//                    Toast.makeText(requireContext(), "Done Mate", Toast.LENGTH_LONG).show()
//                    binding.prosub.visibility = View.GONE

                    //   requireActivity().onBackPressed()
//                    val action =
//                        AskNewDoubtsFragmentDirections.actionAskNewDoubtsFragmentToDoubtsFragment()
//                    navController!!.navigate(action)
                    messageError.value="1"


                    //    }


                }.addOnFailureListener {
                    messageError.value="2"

                }


            }


            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }



        return messageError

    }









}