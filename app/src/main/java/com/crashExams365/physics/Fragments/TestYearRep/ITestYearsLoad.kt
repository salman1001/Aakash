package com.crashExams365.physics.Fragments.TestYearRep

interface ITestYearsLoad {
    fun onLoadsuccess(list: List<YearModel>)
    fun onfailed(message:String)
}