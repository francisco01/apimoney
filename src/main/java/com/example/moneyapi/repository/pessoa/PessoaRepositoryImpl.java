package com.example.moneyapi.repository.pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.moneyapi.model.Pessoa;
import com.example.moneyapi.model.Pessoa_;
import com.example.moneyapi.repository.filter.PessoaFilter;
import org.springframework.util.StringUtils;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery{
	
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);
		Root<Pessoa> root = criteria.from(Pessoa.class);
		
		
		Predicate[] predicates = (Predicate[]) criarRestricoes(pessoaFilter, builder, root);
		criteria.where(predicates);  
		
		TypedQuery<Pessoa> query = manager.createQuery(criteria);
		
		return new PageImpl<Pessoa>(query.getResultList(), pageable, 0) ;
	}
	
	private Predicate[] criarRestricoes(PessoaFilter pessoaFilter, CriteriaBuilder builder,
			Root<Pessoa> root) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (!StringUtils.isEmpty(pessoaFilter.getNome())) {
			predicates.add(builder.like(
					builder.lower(root.get(Pessoa_.NOME)),
					"%" + pessoaFilter.getNome() + "%"));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	
}
