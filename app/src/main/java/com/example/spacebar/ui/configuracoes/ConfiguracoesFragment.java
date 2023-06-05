package com.example.spacebar.ui.configuracoes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spacebar.criador_conteudo;
import com.example.spacebar.databinding.FragmentConfiguracoesBinding;
import com.example.spacebar.minha_conta;
import com.example.spacebar.perfil_config;
import com.example.spacebar.recursos_adicionais;
import com.example.spacebar.verificado;

public class ConfiguracoesFragment extends Fragment {

    private FragmentConfiguracoesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ConfiguracoesViewModel ConfiguracoesViewModel =
                new ViewModelProvider(this).get(ConfiguracoesViewModel.class);

        // Ocultar a ActionBar
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        binding = FragmentConfiguracoesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.ctnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), perfil_config.class);
                startActivity(intent);
            }
        });

        binding.ctnConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), minha_conta.class);
                startActivity(intent);
            }
        });

        binding.ctnVerificado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), verificado.class);
                startActivity(intent);
            }
        });

        binding.ctnCriador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), criador_conteudo.class);
                startActivity(intent);
            }
        });

        binding.ctnRecursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), recursos_adicionais.class);
                startActivity(intent);
            }
        });




        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
