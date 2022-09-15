package com.crashExams365.physics.Fragments.Doubts

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.crashExams365.physics.Fragments.Launch.SpacesItemDecoration
import com.crashExams365.physics.R
import com.crashExams365.physics.databinding.DoubtsFragmentBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DoubtsFragment : Fragment() {

    private var _binding: DoubtsFragmentBinding? = null
    private val binding get() = _binding!!
    private var mInterstitialAd: InterstitialAd? = null

    var navController: NavController? = null
    var layoutAnimationController: LayoutAnimationController?=null
    lateinit var adpater: MyQueAndAns



     var viewModel: DoubtsViewModel?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel=ViewModelProvider(this).get(DoubtsViewModel::class.java)
        _binding = DoubtsFragmentBinding.inflate(inflater, container, false)



        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),"ca-app-pub-3543422873793913/6196479041", adRequest, object : InterstitialAdLoadCallback() {
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




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // Toast.makeText(requireContext(),"Salman",Toast.LENGTH_LONG);

        navController = Navigation.findNavController(view)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = "Doubts"
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3")
                )
            )
        }
        binding.prodoubts.visibility=View.VISIBLE




        viewModel!!.getMesssageError().observe(viewLifecycleOwner,{
          //  binding.pronoti.visibility=View.GONE
            binding.prodoubts.visibility=View.GONE



            try {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)).show()
            }catch (e: Exception){

            }


        })
        viewModel!!.getTopicsList().observe(viewLifecycleOwner,{
            binding.prodoubts.visibility=View.GONE

            // binding.pronoti.visibility=View.GONE
            if (it.size>0){
                binding.displayMessage.visibility=View.GONE;
            }
            else{
                binding.displayMessage.visibility=View.VISIBLE;

            }

            adpater= MyQueAndAns(requireContext(),it)
            binding.recQueAndAns.addItemDecoration(SpacesItemDecoration(8))
            binding.recQueAndAns.adapter=adpater
            binding.recQueAndAns.layoutAnimation=layoutAnimationController


        })







        binding.floatingActionButton.setOnClickListener{

            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
            }
            val action = DoubtsFragmentDirections.actionDoubtsFragmentToAskNewDoubtsFragment()
            navController!!.navigate(action)


        }
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    fun DoubtsClicked(event: DoubtsClicked){
        if (event.issuccess){

            val action= DoubtsFragmentDirections.actionDoubtsFragmentToShowAnsDoubts(event.doubts)
            navController!!.navigate(action)
        }
        else{
            Toast.makeText(requireContext(),"Not Answered Yet",Toast.LENGTH_LONG).show();
        }

    }



}