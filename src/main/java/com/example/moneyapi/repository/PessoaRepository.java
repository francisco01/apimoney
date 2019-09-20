package com.example.moneyapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.moneyapi.model.Pessoa;
import com.example.moneyapi.repository.pessoa.PessoaRepositoryQuery;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>,
PessoaRepositoryQuery{
	
	public Pessoa findByCodigo(Long codigo);

}
