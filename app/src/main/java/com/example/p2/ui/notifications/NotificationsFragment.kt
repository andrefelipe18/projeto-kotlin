package com.example.p2.ui.notifications

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.p2.DatabaseHelper
import com.example.p2.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupMonthButtons()

        return root
    }

    private fun setupMonthButtons() {
        val monthButtons = listOf(
            binding.buttonJanuary, binding.buttonFebruary, binding.buttonMarch,
            binding.buttonApril, binding.buttonMay, binding.buttonJune,
            binding.buttonJuly, binding.buttonAugust, binding.buttonSeptember,
            binding.buttonOctober, binding.buttonNovember, binding.buttonDecember
        )

        monthButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                loadManutencoesForMonth(index + 1)
            }
        }
    }

    private fun loadManutencoesForMonth(month: Int) {
        val dbHelper = DatabaseHelper.getInstance(requireContext())
        val db = dbHelper.readableDatabase

        val cursor: Cursor = db.query(
            "manutencoes",
            null,
            "data_manutencao = ?",
            arrayOf(month.toString()),
            null,
            null,
            null
        )

        val manutencoesList = mutableListOf<String>()
        var totalGasto = 0.0
        var totalQuilometragem = 0
        var count = 0

        while (cursor.moveToNext()) {
            val nomePecaServico = cursor.getString(cursor.getColumnIndexOrThrow("nome_peca_servico"))
            val dataManutencao = cursor.getInt(cursor.getColumnIndexOrThrow("data_manutencao"))
            val valorGasto = cursor.getDouble(cursor.getColumnIndexOrThrow("valor_gasto"))
            val nomePrestador = cursor.getString(cursor.getColumnIndexOrThrow("nome_prestador"))
            val quilometragem = cursor.getInt(cursor.getColumnIndexOrThrow("quilometragem"))
            val observacoes = cursor.getString(cursor.getColumnIndexOrThrow("observacoes"))

            totalGasto += valorGasto
            totalQuilometragem += quilometragem
            count++

            val manutencao = "Peça/Serviço: $nomePecaServico, Mês: $dataManutencao, Valor: $valorGasto, Prestador: $nomePrestador, Quilometragem: $quilometragem, Observações: $observacoes"
            manutencoesList.add(manutencao)
        }
        cursor.close()
        db.close()

        val mediaQuilometragem = if (count > 0) totalQuilometragem / count else 0



        binding.textViewTotalGasto.text = "Valor total gasto no mês: $totalGasto"
        binding.textViewMediaQuilometragem.text = "Média de quilometragem por manutenção: $mediaQuilometragem"

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, manutencoesList)
        binding.listViewManutencoes.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}