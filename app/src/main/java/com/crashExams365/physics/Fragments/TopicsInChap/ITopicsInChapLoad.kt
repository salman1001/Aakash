package com.crashExams365.physics.Fragments.TopicsInChap

interface ITopicsInChapLoad {
    fun onLoadsuccess(list: List<TopicsInChapModel>)
    fun onfailed(message:String)
}