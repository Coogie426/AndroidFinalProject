package cn.sicnu.finalproject.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import cn.sicnu.finalproject.R
import cn.sicnu.finalproject.ui.home.model.CardMatchingGame

class CardRecyclerViewAdapter(val game: CardMatchingGame):RecyclerView.Adapter<CardRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardButton:Button
        init {
            cardButton = itemView.findViewById(R.id.cardButton)
        }
    }
    private var mOnclickListenLiser:((cardIndex:Int)->Unit)? = null
    fun setOnCardClickListener(l:(cardIndex:Int)->Unit) {
        mOnclickListenLiser = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //加载card_item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return game.cards.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = game.cardAtIndex(position)
        holder.cardButton.isEnabled = !card.isMatched
        if (card.isChosen){
            holder.cardButton.text = card.toString()
            holder.cardButton.setBackgroundColor(Color.WHITE)
        }else{
            holder.cardButton.text = ""
            //加载图片
            holder.cardButton.setBackgroundResource(R.drawable.card)
        }
        holder.cardButton.setOnClickListener {
            mOnclickListenLiser?.invoke(position)
        }
    }

}