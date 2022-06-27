package com.crashExams365.physics.Fragments.Notification

interface INotifLoadSuccess {
    fun onLoadsuccess(list: List<MessageClass>)
    fun onLoadFailed(message:String)

}