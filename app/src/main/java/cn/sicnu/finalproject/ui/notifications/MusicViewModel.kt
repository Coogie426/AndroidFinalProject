package cn.sicnu.finalproject.ui.notifications


import android.app.Application
import android.content.ContentValues.TAG
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MusicViewModel (application: Application):AndroidViewModel(application){
    //从activity拿出需要的数据
    private var _musicList:MutableLiveData<List<String>> = MutableLiveData()

    private var _musicNameList:MutableLiveData<List<String>> = MutableLiveData()
    private var _current :MutableLiveData<Int> = MutableLiveData()  //初始等于0
    private var _isPause:MutableLiveData<Boolean> = MutableLiveData()
    //对应的可读数据
    val musicLists: LiveData<List<String>> =_musicList
    val musicNameLists:LiveData<List<String>> = _musicNameList
    val currents:LiveData<Int> = _current
    val isPauses:LiveData<Boolean>  =_isPause
    //中间数据
    private val musicList= mutableListOf<String>()
    private val musicNameList= mutableListOf<String>()
    init{
        _isPause.value=false
    }
    //当一首歌曲播放完成
    fun OnCompletionListener(){
        val pos=_current.value ?:0
        _current.value=pos+1
        Log.d("xxxxxxxxxxxx","${musicLists.value?.size}")
        if(_current.value!! >= _musicList.value!!.size){
            _current.value=0
        }
    }
    //播放上一首
    fun onprev(){
        val pos=_current.value ?:0
        _current.value=pos-1
        if(_current.value!! <0){
            _current.value=_musicList.value!!.size-1
        }
    }

    //对数据进行操作
    fun pause(){
        _isPause.value=true
    }
    fun nopause(){
        _isPause.value=false
    }

    fun getMusicList(){
        val cursor=getApplication<Application>().contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null)
        if(cursor!=null){
            while(cursor.moveToNext()){
                val musicPath=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                musicList.add(musicPath)
                Log.d("music","${musicPath}")
                val musicName=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                musicNameList.add(musicName)
                Log.d(TAG,"getMusicList:$musicPath name:$musicName")
            }
            //赋值给原始数据
            _musicList.postValue(musicList)
            _musicNameList.postValue(musicNameList)
        }
        cursor?.close()
    }
}