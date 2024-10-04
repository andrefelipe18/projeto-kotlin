package com.example.p2.ui.dashboard

import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.example.p2.DatabaseHelper
import com.example.p2.databinding.DialogManutencaoDetailBinding

class ManutencaoDetailDialogFragment : DialogFragment() {

    private val viewModel: DashboardViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogManutencaoDetailBinding.inflate(layoutInflater)
        val manutencaoDetail = arguments?.getString("manutencao_detail")
        val manutencaoId = arguments?.getInt("manutencao_id")

        // Preencher os campos com os valores atuais
        val details = manutencaoDetail?.split("\n")
        binding.editTextNomePecaServico.setText(details?.get(0)?.split(": ")?.get(1))
        binding.editTextDataManutencao.setText(details?.get(1)?.split(": ")?.get(1))
        binding.editTextValorGasto.setText(details?.get(2)?.split(": ")?.get(1))
        binding.editTextNomePrestador.setText(details?.get(3)?.split(": ")?.get(1))
        binding.editTextQuilometragem.setText(details?.get(4)?.split(": ")?.get(1))
        binding.editTextObservacoes.setText(details?.get(5)?.split(": ")?.get(1))

        binding.buttonDelete.setOnClickListener {
            manutencaoId?.let { id ->
                deleteManutencao(id)
            }
        }

        binding.buttonUpdate.setOnClickListener {
            manutencaoId?.let { id ->
                updateManutencao(id, binding)
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok, null)
            .create()
    }

    private fun deleteManutencao(id: Int) {
        val dbHelper = DatabaseHelper.getInstance(requireContext())
        val db = dbHelper.writableDatabase

        val deletedRows = db.delete("manutencoes", "id = ?", arrayOf(id.toString()))
        db.close()

        if (deletedRows > 0) {
            Toast.makeText(requireContext(), "Registro excluído com sucesso", Toast.LENGTH_SHORT).show()
            viewModel.setManutencaoDeleted(true)
            dismiss()
        } else {
            Toast.makeText(requireContext(), "Erro ao excluir o registro", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateManutencao(id: Int, binding: DialogManutencaoDetailBinding) {
        val dbHelper = DatabaseHelper.getInstance(requireContext())
        val db = dbHelper.writableDatabase

        val contentValues = ContentValues().apply {
            put("nome_peca_servico", binding.editTextNomePecaServico.text.toString())
            put("data_manutencao", binding.editTextDataManutencao.text.toString())
            put("valor_gasto", binding.editTextValorGasto.text.toString().toDoubleOrNull() ?: 0.0)
            put("nome_prestador", binding.editTextNomePrestador.text.toString())
            put("quilometragem", binding.editTextQuilometragem.text.toString().toIntOrNull() ?: 0)
            put("observacoes", binding.editTextObservacoes.text.toString())
        }

        val updatedRows = db.update("manutencoes", contentValues, "id = ?", arrayOf(id.toString()))
        db.close()

        if (updatedRows > 0) {
            Toast.makeText(requireContext(), "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show()
            viewModel.setManutencaoUpdated(true)
            dismiss()
        } else {
            Toast.makeText(requireContext(), "Erro ao atualizar o registro", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(manutencaoDetail: String, manutencaoId: Int): ManutencaoDetailDialogFragment {
            val args = Bundle()
            args.putString("manutencao_detail", manutencaoDetail)
            args.putInt("manutencao_id", manutencaoId)
            val fragment = ManutencaoDetailDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}