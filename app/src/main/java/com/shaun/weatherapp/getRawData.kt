package com.shaun.weatherapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.URL

class getRawData(private val listener :OndownloadComplete) {
    interface OndownloadComplete {
        fun onDownloadComplete(rawdata: String)
    }


    fun download(link:String){
        var rawdata=""
        GlobalScope.launch {
          try {

             rawdata=URL(link).readText()
          }catch (e:Exception){
               rawdata="fileNotfound"
          }
            withContext(Dispatchers.Main){
                listener.onDownloadComplete(rawdata)
            }
        }
    }

}