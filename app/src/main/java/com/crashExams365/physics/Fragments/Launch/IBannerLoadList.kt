package com.crashExams365.physics.Fragments.Launch

interface IBannerLoadList {
    fun onBannerLoadDoneListener(banners:SliderModel)
    fun onBannerLoadFailed(messgae:String)
}