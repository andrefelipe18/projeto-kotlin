package com.example.p2.ui.home

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.p2.DatabaseHelper
import com.example.p2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonSave.setOnClickListener {
            saveData()
        }

        return root
    }

    private fun saveData() {
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

        val newRowId = db.insert("manutencoes", null, contentValues)
        db.close()

        if (newRowId != -1L) {
            Toast.makeText(requireContext(), "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show()
            binding.editTextNomePecaServico.text.clear()
            binding.editTextDataManutencao.text.clear()
            binding.editTextValorGasto.text.clear()
            binding.editTextNomePrestador.text.clear()
            binding.editTextQuilometragem.text.clear()
            binding.editTextObservacoes.text.clear()
        } else {
            Toast.makeText(requireContext(), "Erro ao salvar os dados.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveNotification(){
        val dbHelper = DatabaseHelper.getInstance(requireContext())
        val db = dbHelper.writableDatabase

        val contentValues = ContentValues().apply {
            put("title", binding.editTextTituloNotificacao.text.toString())
            put("value", binding.editTextTipoNotificacao.text.toString())
            put("type", binding.editTextValorNotificacao.text.toString())
        }

        val newRowId = db.insert("notifications", null, contentValues)
        db.close()

        if (newRowId != -1L) {
            Toast.makeText(requireContext(), "Notificação Personalizada Criada", Toast.LENGTH_SHORT).show()
            binding.editTextTituloNotificacao.text.clear()
            binding.editTextTipoNotificacao.text.clear()
            binding.editTextValorNotificacao.text.clear()
        } else {
            Toast.makeText(requireContext(), "Erro ao salvar os dados.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}