package br.edu.utfpr.financescrudwithkotlinandsqlite

import android.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.utfpr.financescrudwithkotlinandsqlite.database.DataBaseHandler
import br.edu.utfpr.financescrudwithkotlinandsqlite.databinding.ActivityMainBinding
import br.edu.utfpr.financescrudwithkotlinandsqlite.entity.Financa
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var banco : DataBaseHandler
    // Arrays com as opções para os spinners
    private val tipos = arrayOf("C - Crédito", "D - Débito")
    private val detalhesCredito = arrayOf("Salário", "Extras")
    private val detalhesDebito = arrayOf("Alimentação", "Transporte", "Saúde", "Moradia")//, "Internet", "Fatura Cartão", "Combustível")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DataBaseHandler(this)

        // Configura os spinners
        setupSpinners()

        setButtonListener()

        setAutoCommaEtValor()

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

        // Obter Locale do dispositivo
        val locale = Locale.getDefault()
        val symbols = DecimalFormatSymbols(locale)
        val decimalSeparator = symbols.decimalSeparator

        // Limpa a string removendo qualquer símbolo de moeda e separadores de milhar antes de salvar no banco
        val valor = binding.etValor.text.toString().replace("[R$,.]".toRegex(), "").replace(decimalSeparator, '.')//.toDouble()


        val dataLancamento = binding.etDataLancamento.text.toString()

        banco.insert(Financa(0, tipo, detalhe, valor.toDouble()/100, dataLancamento))
        Toast.makeText(this, "Sucesso!!", Toast.LENGTH_LONG).show()
    }

    private fun btLancamentosOnClick() {
        val intent = Intent(this, ListaDeLancamentos::class.java)
        startActivity(intent)
    }

    private fun btSaldoOnClick() {
        val saldo = banco.calcularSaldo()

        //binding.tvSaldo.text = String.format("Saldo: R$ %.2f", saldo)

        // Formatação do saldo para exibir com só duas casas decimais
        val saldoFormatado = String.format("R$ %.2f", saldo)

        // Criação e exibiçao do AlertDialog
        AlertDialog.Builder(this)
            .setTitle("Saldo Atual")
            .setMessage(saldoFormatado)
            .setPositiveButton("OK", null)  // Apenas um botão OK para fechar
            .create()
            .show()
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
        tipoAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spTipo.adapter = tipoAdapter

        // Listener para o Spinner de Tipo para alterar o Spinner de Detalhe conforme a escolha do usuario
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
                        R.layout.simple_spinner_item,
                        detalhesCredito
                    )

                    1 -> ArrayAdapter(
                        this@MainActivity,
                        R.layout.simple_spinner_item,
                        detalhesDebito
                    )

                    else -> ArrayAdapter(
                        this@MainActivity,
                        R.layout.simple_spinner_item,
                        arrayOf()
                    )
                }
                detalheAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                binding.spDetalhe.adapter = detalheAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun setAutoCommaEtValor() {
        binding.etValor.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (s.toString() != current) {
                    binding.etValor.removeTextChangedListener(this)

                    // Obtem Locale do dispositivo
                    val locale = Locale.getDefault()
                    val symbols = DecimalFormatSymbols(locale)
                    //val decimalSeparator = symbols.decimalSeparator

                    // Limpa a string removendo qualquer símbolo de moeda e separadores
                    val cleanString = s.toString().replace("[R$,.]".toRegex(), "").trim()

                    if (cleanString.isNotEmpty()) {
                        val parsed = cleanString.toDouble()
                        val formatted = DecimalFormat("#,##0.00", symbols).format(parsed / 100)

                        current = formatted
                        binding.etValor.setText(current)
                        binding.etValor.setSelection(current.length) // Move o cursor para o final
                    }

                    binding.etValor.addTextChangedListener(this)
                }
            }
        })
    }
}