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
        val score: TextView = v.findViewById(R.id.txtScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_top_score, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val entry = items[position]
        android.util.Log.d("TAL_DEBUG", "bind pos=$position score=${entry.score}")
        holder.score.text = entry.score.toString()

        holder.itemView.setOnClickListener {
            onScoreClicked?.invoke(entry.lat, entry.lon)
        }
    }

    override fun getItemCount(): Int {
        android.util.Log.d("TAL_DEBUG", "itemCount=${items.size}")
        return items.size
    }

    fun submit(list: List<ScoreEntry>) {
        android.util.Log.d("TAL_DEBUG", "submit size=${list.size} scores=${list.map { it.score }}")
        items = list
        notifyDataSetChanged()
    }
}


//class TopTenAdapter : RecyclerView.Adapter<TopTenAdapter.ScoreViewHolder>() {
//
//    private var items: List<ScoreEntry> = emptyList()
//
//    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val txtRank: TextView = itemView.findViewById(R.id.txtRank)
//        val txtScore: TextView = itemView.findViewById(R.id.txtScore)
//        val txtLocation: TextView = itemView.findViewById(R.id.txtLocation)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_top_score, parent, false)
//        return ScoreViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
//        val entry = items[position]
//
//        holder.txtRank.text = "#${position + 1}"
//        holder.txtScore.text = "Score: ${entry.score}"
//
//        if (entry.lat != 0.0 && entry.lon != 0.0) {
//            holder.txtLocation.text =
//                "(${format(entry.lat)}, ${format(entry.lon)})"
//        } else {
//            holder.txtLocation.text = "Location unavailable"
//        }
//        android.util.Log.d("TAL_DEBUG", "bind pos=$position score=${entry.score}")
//
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    fun submit(list: List<ScoreEntry>) {
//        items = list
//        notifyDataSetChanged()
//    }
//
//    private fun format(value: Double): String {
//        return String.format("%.3f", value)
//    }
//}
//
//
