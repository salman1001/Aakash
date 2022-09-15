package com.crashExams365.physics.Fragments.showAnsDoubts

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crashExams365.physics.Fragments.Doubts.QuestionAndAns
import com.crashExams365.physics.Fragments.ResultFragment.ObjectShowAns
import com.crashExams365.physics.Fragments.ShowAnswers.ShowAnswersArgs
import com.crashExams365.physics.R
import com.crashExams365.physics.databinding.ShowAnsDoubtsFragmentBinding
import com.crashExams365.physics.databinding.ShowAnswersFragmentBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class ShowAnsDoubts : Fragment() {
    private var mInterstitialAd: InterstitialAd? = null


    private var _binding: ShowAnsDoubtsFragmentBinding?=null
    private val binding get() =_binding!!
    var navController: NavController?=null
    val args: ShowAnsDoubtsArgs by navArgs<ShowAnsDoubtsArgs>()
    var objectShowAns: QuestionAndAns?=null



    private lateinit var viewModel: ShowAnsDoubtsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= ShowAnsDoubtsFragmentBinding.inflate(inflater,container,false)

        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),"ca-app-pub-3543422873793913/5019128745", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(ContentValues.TAG, adError?.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                //  Log.d(TAG, 'Ad was dismissed.')
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                //    Log.d(TAG, 'Ad failed to show.')
            }

            override fun onAdShowedFullScreenContent() {
                //    Log.d(TAG, 'Ad showed fullscreen content.')
                mInterstitialAd = null
            }
        }

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        objectShowAns=args.doubtsAnsModel;
        Glide.with(requireContext()).load(objectShowAns!!.questionurl).diskCacheStrategy(
            DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true).into(binding.myqueimge1)
        binding.questionString.text=objectShowAns!!.questionText
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(binding.testTool5)
            (activity as AppCompatActivity).supportActionBar?.title = "Question"
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3")
                )
            )
        }


        binding.showclicked1.setOnClickListener{
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
            }
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setCancelable(true)



            val inflater = layoutInflater
            val dialoglayout: View = inflater.inflate(R.layout.show_ans_doubts_pop_up_item, null)
            val imageView=dialoglayout.findViewById<ImageView>(R.id.myqueimge4);
            Glide.with(requireContext()).load(objectShowAns!!.ansUrl).diskCacheStrategy(
                DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true).into(imageView)

            builder.setView(dialoglayout)
            builder.show()
        }
    }



}