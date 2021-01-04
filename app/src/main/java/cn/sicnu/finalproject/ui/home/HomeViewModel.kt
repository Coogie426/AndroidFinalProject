package cn.sicnu.finalproject.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.sicnu.finalproject.ui.home.model.CardMatchingGame

class HomeViewModel : ViewModel() {

    companion object {
        private lateinit var game: CardMatchingGame
    }
    private var _game:MutableLiveData<CardMatchingGame> = MutableLiveData()
    val games=_game

    init {
        //初始化_game为空
        _game.value=CardMatchingGame(24)
    }

    fun reset(){
        _game.value?.reset()
    }

    fun chooseCardAtIndex(i:Int){
        _game.value?.chooseCardAtIndex(i)
    }

}