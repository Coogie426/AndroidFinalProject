package cn.sicnu.finalproject.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.sicnu.finalproject.R

class HomeFragment : Fragment() {
    lateinit var adapter:CardRecyclerViewAdapter
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(HomeViewModel::class.java)
        viewModel.games.observe(viewLifecycleOwner, Observer {
            Log.d("games","${it}")
            adapter = CardRecyclerViewAdapter(it)
            val recylerView = view.findViewById<RecyclerView>(R.id.recylerView2)
            val reset = view.findViewById<Button>(R.id.reset)
            recylerView.adapter = adapter
            val configuration = resources.configuration
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                recylerView.layoutManager = GridLayoutManager(activity, 6)
            }else{
                val gridLayoutManager = GridLayoutManager(activity, 5)
                recylerView.layoutManager = gridLayoutManager
            }
            updateUI()
            adapter.setOnCardClickListener {
                viewModel.chooseCardAtIndex(it)
                updateUI()
            }
            reset.setOnClickListener {
                viewModel.reset()
                updateUI()
            }
        })
    }

    private fun updateUI() {
        val score = view?.findViewById<TextView>(R.id.score)
        adapter.notifyDataSetChanged()
        viewModel.games.observe(viewLifecycleOwner, Observer {
            score?.text = String.format("%s%d",getString(R.string.score),it.score)
            score?.text = getString(R.string.score) + it.score
        })
    }
}

