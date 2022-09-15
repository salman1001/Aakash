package com.crashExams365.physics.Fragments.Doubts

import com.crashExams365.physics.Fragments.Notification.MessageClass

interface IQueAnsLoad {
    fun onLoadsuccess(list: List<QuestionAndAns>)
    fun onLoadFailed(message:String)

}