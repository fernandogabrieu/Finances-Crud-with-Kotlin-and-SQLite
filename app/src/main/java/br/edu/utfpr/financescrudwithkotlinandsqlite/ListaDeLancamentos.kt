package br.edu.utfpr.financescrudwithkotlinandsqlite

import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.utfpr.financescrudwithkotlinandsqlite.adapter.FinancaAdapter
import br.edu.utfpr.financescrudwithkotlinandsqlite.database.DataBaseHandler
import br.edu.utfpr.financescrudwithkotlinandsqlite.databinding.ActivityListaDeLancamentosBinding
import br.edu.utfpr.financescrudwithkotlinandsqlite.entity.Financa

class ListaDeLancamentos : AppCompatActivity() {

    private lateinit var binding : ActivityListaDeLancamentosBinding
    private lateinit var banco : DataBaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaDeLancamentosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DataBaseHandler(this)

        val financas : MutableList<Financa> = banco.list()
        val adapter = FinancaAdapter(financas)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
}