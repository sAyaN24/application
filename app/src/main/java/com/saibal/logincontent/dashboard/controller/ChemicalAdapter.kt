package com.saibal.logincontent.dashboard.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.saibal.logincontent.R
import com.saibal.logincontent.model.Chemical

class ChemicalAdapter(private var context: Context, private var chemicalList: MutableList<Chemical>) : RecyclerView.Adapter<ChemicalAdapter.ItemViewHolder>(){

    private var filteredChemicalList = chemicalList.toMutableList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chemical_details, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val chemical = chemicalList.get(position)
        holder.headerText.text = chemical.name
        holder.toogleIv.setOnClickListener { view ->
            if(holder.bodyView.visibility == View.VISIBLE){

                holder.bodyView.visibility = View.GONE
                holder.toogleIv.setImageDrawable(context.resources.getDrawable(R.drawable.round_arrow_down))
            }else if(holder.bodyView.visibility == View.GONE){

                holder.bodyView.visibility = View.VISIBLE
                holder.toogleIv.setImageDrawable(context.resources.getDrawable(R.drawable.round_arrow_up))
            }
        }
        holder.symptomsTv.text = chemical.allergy_symptoms
        when(chemical.severity){
            "Medium" -> holder.linearLayoutForIngredients.setBackgroundColor(context.resources.getColor(R.color.bg_yellow))
            "High" -> holder.linearLayoutForIngredients.setBackgroundColor(context.resources.getColor(R.color.bg_high))
        }
        holder.severityTv.text = "Severity: "+ chemical.severity
    }

    override fun getItemCount(): Int {
       return chemicalList.size
    }


//    override fun getFilter(): Filter {
//        TODO()
//    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val headerView:View = itemView.findViewById(R.id.header)
        val bodyView:View = itemView.findViewById(R.id.body)
        val headerText:TextView = itemView.findViewById(R.id.ingredient_nameTv)
        val severityTv:TextView = itemView.findViewById(R.id.severityTv)
        val symptomsTv:TextView = itemView.findViewById(R.id.symptomsTv)
        val toogleIv:ImageView = itemView.findViewById(R.id.toogleIv)
        val linearLayoutForIngredients:LinearLayout = itemView.findViewById(R.id.llForIngredient)

    }


}