package com.crashExams365.physics.Fragments.TestNumbers

import com.crashExams365.physics.Fragments.Test.PassObj


interface IPdfLoad {
    fun onPdfLoadDoneListener(obj:PassObj)
    fun onloadFiled(message:String)
}