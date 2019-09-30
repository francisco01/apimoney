package com.example.moneyapi.repository.projection;

import com.example.moneyapi.model.Endereco;

public class ResumoPessoa {
	
	private Long codigo;
	private String nome;
	private Endereco estadoEndereco;
	private Boolean ativo;
	
	public ResumoPessoa(Long codigo, String nome, Endereco estadoEndereco, Boolean ativo) {
		this.codigo = codigo;
		this.nome = nome;
		this.estadoEndereco = estadoEndereco;
		this.ativo = ativo;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Endereco getEstadoEndereco() {
		return estadoEndereco;
	}

	public void setEstadoEndereco(Endereco estadoEndereco) {
		this.estadoEndereco = estadoEndereco;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
}