package com.crashExams365.physics.Fragments.ResultFragment

import com.crashExams365.physics.Fragments.Test.DataObjectStore

interface IRanksLoadSuccess {
    fun onLoadsuccess(list: List<DataObjectStore>,rank:Int)
    fun onFailedLoad(message:String)
}