package cn.sicnu.finalproject.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import cn.sicnu.finalproject.R
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import cn.sicnu.finalproject.ui.dashboard.weather.Forecast
import cn.sicnu.finalproject.ui.dashboard.weather.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_weather2.*

class WeatherActivity2 : AppCompatActivity() {

    //http表示明文访问，不太安全，需要在清单文件声明
    val baseURL="http://t.weather.itboy.net/api/weather/city/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather2)

        val cityCode=intent.getStringExtra("city_code")
        Log.d("city_code","${cityCode}")
        val queue= Volley.newRequestQueue(this)
        //构造一个请求
        val stringRequest= StringRequest(baseURL+cityCode,{
            //成功返回数据
            //声明一个gson对象对结果做解析（Volley自动有多线程),回调时会在主线程，自动封装
            val gson= Gson()
            val WeatherType=object : TypeToken<Weather>(){}.type
            val weather=gson.fromJson<Weather>(it,WeatherType)

            textView_city.text=weather.cityInfo.city
            textView_province.text=weather.cityInfo.parent
            textView_shidu.text=weather.data.shidu
            textView_wendu.text=weather.data.wendu
            val firstday=weather.data.forecast.first()
            when(firstday.type){
                "晴" ->imageView.setImageResource(R.drawable.sun)
                "阴" ->imageView.setImageResource(R.drawable.cloud)
                "多云" ->imageView.setImageResource(R.drawable.mcloud)
                "正雨" ->imageView.setImageResource(R.drawable.rain)
                else -> imageView.setImageResource(R.drawable.thunder)
            }
            val adapter= ArrayAdapter<Forecast>(this,android.R.layout.simple_list_item_1,weather.data.forecast)
            listView.adapter=adapter
            Log.d("MainActivity2","${weather.cityInfo.city}")
            Log.d("MainActivity2","$it")

        },{
            //打印错误信息
            Log.d("MainActivity2","$it")
        })
        queue.add(stringRequest)
    }
}