package com.crashExams365.physics.Fragments.Test

import java.io.File

interface ILoadTestPdfOptioins {
    fun inLoadSuccess( file:File,optionClass: OptionClass)
    fun onFailed(message:String)
}