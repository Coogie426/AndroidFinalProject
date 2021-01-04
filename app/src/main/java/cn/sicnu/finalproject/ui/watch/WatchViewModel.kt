package cn.sicnu.finalproject.ui.watch

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WatchViewModel : ViewModel() {
    private var _second : MutableLiveData<Int> = MutableLiveData()
    //表示second是可变的
    private var running =false
    val seconds: LiveData<Int> = _second
    //外部不能修改，所以定义为LiveData
    init {
        runTimer()
    }
    fun start(){
        running=true
    }
    fun stop(){
        running=false
    }
    fun restart(){
        running=true
        _second.value=0
    }
    fun runTimer(){
        val handler =  Handler()
        val runnable=object :Runnable{
            override fun run() {
                if(running){
                    val sec=_second.value ?:0
                    _second.value=sec+1
                }
                handler.postDelayed(this,1000)
            }
        }
        handler.post(runnable)
    }
}