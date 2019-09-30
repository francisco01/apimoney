package com.example.moneyapi.repository.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.moneyapi.model.Pessoa;
import com.example.moneyapi.repository.filter.PessoaFilter;
import com.example.moneyapi.repository.projection.ResumoPessoa;

public interface PessoaRepositoryQuery {

	public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable);
	public Page<ResumoPessoa> resumir(PessoaFilter pessoaFilter, Pageable pageable);
}
