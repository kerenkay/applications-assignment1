package com.example.applications_assignment1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScoreAdapter(
    var onScoreClicked: ((Double, Double) -> Unit)? = null
) : RecyclerView.Adapter<ScoreAdapter.VH>() {

    private var items: List<ScoreEntry> = emptyList()

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val rank: TextView = v.findViewById(R.id.txtRank)
        val name: TextView = v.findViewById(R.id.txtName)
        val score: TextView = v.findViewById(R.id.txtScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_top_score, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val entry = items[position]

        holder.rank.text = "#${position + 1}"
        holder.name.text = entry.name
        holder.score.text = entry.score.toString()

        holder.itemView.setOnClickListener {
            onScoreClicked?.invoke(entry.lat, entry.lon)
        }
    }

    override fun getItemCount(): Int = items.size

    fun submit(list: List<ScoreEntry>) {
        items = list.sortedByDescending { it.score }.take(10)
        notifyDataSetChanged()
    }
}

//class ScoreAdapter(
//    var onScoreClicked: ((Double, Double) -> Unit)? = null
//) : RecyclerView.Adapter<ScoreAdapter.VH>() {
//
//    private var items: List<ScoreEntry> = emptyList()
//
//    class VH(v: View) : RecyclerView.ViewHolder(v) {
//        val score: TextView = v.findViewById(R.id.txtScore)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
//        val v = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_top_score, parent, false)
//        return VH(v)
//    }
//
//    override fun onBindViewHolder(holder: VH, position: Int) {
//        val entry = items[position]
//        android.util.Log.d("KEREN_DEBUG", "bind pos=$position score=${entry.score}")
//        holder.score.text = entry.score.toString()
//
//        holder.itemView.setOnClickListener {
//            onScoreClicked?.invoke(entry.lat, entry.lon)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        android.util.Log.d("KEREN_DEBUG", "itemCount=${items.size}")
//        return items.size
//    }
//
//    fun submit(list: List<ScoreEntry>) {
//        android.util.Log.d("KEREN_DEBUG", "submit size=${list.size} scores=${list.map { it.score }}")
//        items = list
//        notifyDataSetChanged()
//    }
//}
