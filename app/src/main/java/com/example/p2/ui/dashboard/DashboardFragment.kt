package com.example.p2.ui.dashboard

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.p2.DatabaseHelper
import com.example.p2.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadManutencoes()

        viewModel.manutencaoDeleted.observe(viewLifecycleOwner) { deleted ->
            if (deleted) {
                loadManutencoes()
                viewModel.setManutencaoDeleted(false)
            }
        }

        viewModel.manutencaoUpdated.observe(viewLifecycleOwner) { updated ->
            if (updated) {
                loadManutencoes()
                viewModel.setManutencaoUpdated(false)
            }
        }

        return root
    }

    private fun loadManutencoes() {
        val dbHelper = DatabaseHelper.getInstance(requireContext())
        val db = dbHelper.readableDatabase

        val cursor: Cursor = db.query(
            "manutencoes",
            null,
            null,
            null,
            null,
            null,
            null
        )

        val manutencoesList = mutableListOf<String>()
        val manutencoesIds = mutableListOf<Int>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nomePecaServico = cursor.getString(cursor.getColumnIndexOrThrow("nome_peca_servico"))
            val dataManutencao = cursor.getString(cursor.getColumnIndexOrThrow("data_manutencao"))
            val valorGasto = cursor.getDouble(cursor.getColumnIndexOrThrow("valor_gasto"))
            val nomePrestador = cursor.getString(cursor.getColumnIndexOrThrow("nome_prestador"))
            val quilometragem = cursor.getInt(cursor.getColumnIndexOrThrow("quilometragem"))
            val observacoes = cursor.getString(cursor.getColumnIndexOrThrow("observacoes"))

            val manutencao = "Peça/Serviço: $nomePecaServico\nMês: $dataManutencao\nValor: $valorGasto\nPrestador: $nomePrestador\nQuilometragem: $quilometragem\nObservações: $observacoes"
            manutencoesList.add(manutencao)
            manutencoesIds.add(id)
        }
        cursor.close()
        db.close()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, manutencoesList)
        binding.listViewManutencoes.adapter = adapter

        binding.listViewManutencoes.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val manutencaoDetail = manutencoesList[position]
            val manutencaoId = manutencoesIds[position]
            val dialog = ManutencaoDetailDialogFragment.newInstance(manutencaoDetail, manutencaoId)
            dialog.show(parentFragmentManager, "ManutencaoDetailDialogFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}