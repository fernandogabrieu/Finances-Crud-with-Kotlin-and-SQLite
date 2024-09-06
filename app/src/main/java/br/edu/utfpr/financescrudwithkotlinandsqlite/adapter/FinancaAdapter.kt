package br.edu.utfpr.financescrudwithkotlinandsqlite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.utfpr.financescrudwithkotlinandsqlite.R
import br.edu.utfpr.financescrudwithkotlinandsqlite.entity.Financa

class FinancaAdapter(private val financas: MutableList<Financa>) : RecyclerView.Adapter<FinancaAdapter.FinancaViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FinancaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_financa, parent, false)
        return FinancaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FinancaViewHolder, position: Int) {
        val currentFinanca = financas[position]
        holder.tvTipo.text = String.format("%s - ", currentFinanca.tipo)
        holder.tvDetalhe.text = String.format("%s - ", currentFinanca.detalhe)
        holder.tvValor.text = String.format("%.2f", currentFinanca.valor)
        holder.tvDataLancamento.text = String.format("%s - ", currentFinanca.dataLancamento)
    }

    override fun getItemCount() = financas.size

    class FinancaViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTipo: TextView = itemView.findViewById(R.id.tvTipo)
        val tvDetalhe: TextView = itemView.findViewById(R.id.tvDetalhe)
        val tvValor: TextView = itemView.findViewById(R.id.tvValor)
        val tvDataLancamento: TextView = itemView.findViewById(R.id.tvDataLancamento)
    }

}