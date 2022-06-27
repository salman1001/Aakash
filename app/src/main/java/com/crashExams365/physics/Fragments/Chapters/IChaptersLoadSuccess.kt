package com.crashExams365.physics.Fragments.Chapters

interface IChaptersLoadSuccess {
    fun onLoadsuccess(list: List<ChapterModel>)
    fun onChapLoadFailed(messgae:String)

}