package com.saibal.logincontent.dashboard.controller

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saibal.logincontent.R
import com.saibal.logincontent.model.Chemical

class ChemicalAdapter(private var context: Context, private var chemicalList: MutableList<Chemical>) : RecyclerView.Adapter<ChemicalAdapter.ItemViewHolder>(){

    private var filteredChemicalList = chemicalList.toMutableList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chemical_details, parent, false)
        return ItemViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val chemical = chemicalList.get(position)
        holder.headerText.text = chemical.Name
        val layoutParams = holder.llForDropdownHeader.layoutParams
        holder.toogleIv.setOnClickListener { view ->
            if(holder.bodyView.visibility == View.VISIBLE){

                holder.bodyView.visibility = View.GONE
                holder.imageIv.visibility = View.VISIBLE
                holder.severityIvBar.visibility = View.GONE
                layoutParams.height = 165
                holder.headerText.gravity = Gravity.CENTER_VERTICAL
                holder.toogleIv.setImageDrawable(context.resources.getDrawable(R.drawable.round_arrow_down))

            }else if(holder.bodyView.visibility == View.GONE){

                holder.bodyView.visibility = View.VISIBLE
                holder.severityIvBar.visibility = View.VISIBLE
                holder.imageIv.visibility = View.GONE
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                holder.headerText.gravity = Gravity.BOTTOM
                holder.toogleIv.setImageDrawable(context.resources.getDrawable(R.drawable.round_arrow_up))
            }
        }
       // holder.symptomsTv.text = chemical.allergy_symptoms
        when(chemical.Severity){
            "Medium" ->{ holder.imageIv.setImageDrawable(context.resources.getDrawable(R.drawable.severity_medium))
                holder.severityIvBar.setImageDrawable(context.resources.getDrawable(R.color.bg_yellow))
            }
            "Low" -> {holder.imageIv.setImageDrawable(context.resources.getDrawable(R.drawable.severity_low))
                holder.severityIvBar.setImageDrawable(context.resources.getDrawable(R.color.bg_green))
            }
            "High" -> {holder.imageIv.setImageDrawable(context.resources.getDrawable(R.drawable.severity_high))
                holder.severityIvBar.setImageDrawable(context.resources.getDrawable(R.color.bg_high))
            }

            "Very High" -> {holder.imageIv.setImageDrawable(context.resources.getDrawable(R.drawable.severity_very_high))
                holder.severityIvBar.setImageDrawable(context.resources.getDrawable(R.color.bg_very_high))
            }

        }

    }

    override fun getItemCount(): Int {
       return chemicalList.size
    }


//    override fun getFilter(): Filter {
//        TODO()
//    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val bodyView:View = itemView.findViewById(R.id.body)
        val headerText:TextView = itemView.findViewById(R.id.ingredient_nameTv)
        val imageIv:ImageView = itemView.findViewById(R.id.iconIv)
        val symptomsTv:TextView = itemView.findViewById(R.id.symptomsTv)
        val toogleIv:ImageView = itemView.findViewById(R.id.toogleIv)
        val severityIvBar:ImageView = itemView.findViewById(R.id.severity_iv_bar)
        val llForDropdownHeader: LinearLayout = itemView.findViewById(R.id.llForDropdownHeader)

    }


}