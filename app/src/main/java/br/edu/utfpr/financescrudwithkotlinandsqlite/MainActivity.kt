package br.edu.utfpr.financescrudwithkotlinandsqlite

import android.R
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.utfpr.financescrudwithkotlinandsqlite.database.DataBaseHandler
import br.edu.utfpr.financescrudwithkotlinandsqlite.databinding.ActivityMainBinding
import br.edu.utfpr.financescrudwithkotlinandsqlite.entity.Financa
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var banco : DataBaseHandler
    // Arrays com as opções para os spinners
    private val tipos = arrayOf("C - Crédito", "D - Débito")
    private val detalhesCredito = arrayOf("Salário", "Extras")
    private val detalhesDebito = arrayOf("Casa", "Aluguel", "Luz", "Água", "Internet", "Fatura Cartão", "Combustível")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DataBaseHandler(this)

        // Configura os spinners
        setupSpinners()

        setButtonListener()
    }

    private fun setButtonListener() {
        binding.btLancar.setOnClickListener {
            btLancarOnClick()
        }

        binding.etDataLancamento.setOnClickListener {
            showDatePickerDialog()
        }


        binding.btLancamentos.setOnClickListener {
            btLancamentosOnClick()
        }

        binding.btSaldo.setOnClickListener {
            btSaldoOnClick()
        }
    }

    private fun btLancarOnClick() {

        val tipo = if (binding.spTipo.selectedItemPosition == 0) "C" else "D"
        val detalhe = binding.spDetalhe.selectedItem.toString()
        val valor = binding.etValor.text.toString().toDouble()
        val dataLancamento = binding.etDataLancamento.text.toString()

        banco.insert(Financa(0, tipo, detalhe, valor, dataLancamento))
        Toast.makeText(this, "Sucesso!!", Toast.LENGTH_LONG).show()
    }

    private fun btLancamentosOnClick() {
        val intent = Intent(this, ListaDeLancamentos::class.java)
        startActivity(intent)
    }

    private fun btSaldoOnClick() {
        val saldo = banco.calcularSaldo()
        binding.tvSaldo.text = String.format("Saldo: R$ %.2f", saldo)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
                binding.etDataLancamento.setText(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun setupSpinners() {
        // Adapter para o Spinner de Tipo
        val tipoAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, tipos)
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spTipo.adapter = tipoAdapter

        // Listener para o Spinner de Tipo para alterar o Spinner de Detalhe conforme a escolha
        binding.spTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val detalheAdapter: ArrayAdapter<String> = when (position) {
                    0 -> ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        detalhesCredito
                    )

                    1 -> ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        detalhesDebito
                    )

                    else -> ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        arrayOf()
                    )
                }
                detalheAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spDetalhe.adapter = detalheAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }
}