package cn.sicnu.finalproject.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.sicnu.finalproject.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread

//凡是需要引用的需要从安卓派生
class CityViewModel (application:Application):AndroidViewModel(application){
    //表示初始没有，可以改变
    private var _cities:MutableLiveData<List<CityItem>> = MutableLiveData()

    //准备好给外面观察的数据
    val cities:LiveData<List<CityItem>> = _cities

    init {
        thread {
            val str=readFileFromRaw(R.raw.citycode)
            val gson= Gson()
            val CityType=object: TypeToken<City>(){}.type
            var cts:List<CityItem> =gson.fromJson(str,CityType)
            //保留citycode不为空的值
            cts=cts.filter { it.city_code!="" }
            //更新值，主线程用value，子线程使用postvalue改变值
            _cities.postValue(cts)
        }
    }

    fun readFileFromRaw(rawName: Int): String? {
        try {
            val inputReader = InputStreamReader(getApplication<Application>().resources.openRawResource(rawName))
            val bufReader = BufferedReader(inputReader)
            var line: String? = ""
            var result: String? = ""
            while (bufReader.readLine().also({ line = it }) != null) {
                result += line
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

}