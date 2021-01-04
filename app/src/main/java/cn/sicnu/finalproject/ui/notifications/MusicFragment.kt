package cn.sicnu.finalproject.ui.notifications


import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.sicnu.finalproject.R
import kotlinx.android.synthetic.main.fragment_notifications.*
import java.io.IOException
import kotlin.concurrent.thread

class MusicFragment: Fragment() {

    private lateinit var viewModel: MusicViewModel
    val mediaPlayer= MediaPlayer()
    private val Channel_ID="my channel"
    private val Notification_ID=1
    //线程是否开启
    var flag:Boolean=true

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MusicViewModel::class.java)
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mediaPlayer.setOnPreparedListener{
            it.start()
        }
        mediaPlayer.setOnCompletionListener {
            viewModel.OnCompletionListener()
            //play()
        }
        if(ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }
        else{
            getMusicList()
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
            {
                if(fromUser){
                    mediaPlayer.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        button_play.setOnClickListener {
            play()
        }
        button_pause.setOnClickListener{
            if(viewModel.isPauses.value!!){
                mediaPlayer.start()
                viewModel.nopause()
                //isPause=false
            }else{
                mediaPlayer.pause()
                //isPause=true
                viewModel.pause()
            }
        }
        button_stop.setOnClickListener {
            mediaPlayer.stop()
        }
        button_next.setOnClickListener {
            viewModel.OnCompletionListener()
            play()
        }
        button_prev.setOnClickListener {
            viewModel.onprev()
            play()
        }
        thread {
            while (true){
                if(flag.equals(false)){
                    break
                }else{
                    requireActivity().runOnUiThread{
                        seekBar.max=mediaPlayer.duration
                        seekBar.progress=mediaPlayer.currentPosition
                    }
                }
                Thread.sleep(1000)
            }
        }
    }
    private fun play(){
        if(viewModel.musicLists.value?.size==0) return
        val path=viewModel.musicLists.value?.get(viewModel.currents.value!!)
        mediaPlayer.reset()
        try {
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepareAsync()
            textView_mn.text=viewModel.musicNameLists.value!![viewModel.currents.value!!]
            textView_count.text="${viewModel.currents.value!!+1}/${viewModel.musicLists.value?.size}"
            notification()
        }catch(e: IOException){
            e.printStackTrace()
        }

    }
    private fun notification(){
        //将当前歌曲名字显示在通知中
        val notificationManager=requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder: Notification.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel= NotificationChannel(Channel_ID,"test", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
            builder= Notification.Builder(requireContext(),Channel_ID)
        }else{
            builder= Notification.Builder(requireContext())
        }
        val intent= Intent(requireActivity(), MusicFragment::class.java)
        val pendingIntent= PendingIntent.getActivity(requireContext(),1,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification=builder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentText(viewModel.musicNameLists.value!![viewModel.currents.value!!])
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        notificationManager.notify(Notification_ID,notification)
    }
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getMusicList()
    }
    override fun onPause() {
        super.onPause()
        flag=false
        Log.d("onPause","已结束")
    }
    override fun onStop() {
        super.onStop()
        Log.d("onStop","已结束")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("onDestroyView","已结束")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestroy","已结束")
    }
    override fun onDetach() {
        super.onDetach()
        Log.d("flag+++++++++++++++++","${flag}")

        mediaPlayer.release()
        Log.d("onDetach","已结束")
    }
    private fun getMusicList(){
        viewModel.getMusicList()

    }
}