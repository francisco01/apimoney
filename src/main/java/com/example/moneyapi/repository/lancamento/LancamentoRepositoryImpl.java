package com.example.moneyapi.repository.lancamento;

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
import org.springframework.util.StringUtils;

import com.example.moneyapi.model.Categoria_;
import com.example.moneyapi.model.Lancamento;
import com.example.moneyapi.model.Lancamento_;
import com.example.moneyapi.model.Pessoa_;
import com.example.moneyapi.repository.filter.LancamentoFilter;
import com.example.moneyapi.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Lancamento> filtar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		
		Predicate[] predicates = (Predicate[]) criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);  
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		adcionarRestricoesdePaginacao(pageable, query);
		return new PageImpl<Lancamento>(query.getResultList(), pageable, total(lancamentoFilter)) ;
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(
					builder.lower(root.get(Lancamento_.DESCRICAO)),
					"%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}
		
		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(
					root.get(Lancamento_.DATA_VENCIMENTO), 
					lancamentoFilter.getDataVencimentoDe()));
		}
		if (lancamentoFilter.getDataVencimentoAte() != null) {
			
			predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), 
					lancamentoFilter.getDataVencimentoAte()));
			
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	private Long total(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		
		return manager.createQuery(criteria).getSingleResult();
		
	}

	private void adcionarRestricoesdePaginacao(Pageable pageable, TypedQuery<?> query) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistrodaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistrodaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}

	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		criteria.select(builder.construct(ResumoLancamento.class
				, root.get(Lancamento_.CODIGO), root.get(Lancamento_.DATA_VENCIMENTO)
				, root.get(Lancamento_.DATA_PAGAMENTO), root.get(Lancamento_.VALOR)
				, root.get(Lancamento_.TIPO), root.get(Lancamento_.CATEGORIA).get(Categoria_.NOME)
				, root.get(Lancamento_.PESSOA).get(Pessoa_.NOME)));
		
		Predicate[] predicates = (Predicate[]) criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);  
		
		TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
		adcionarRestricoesdePaginacao(pageable, query);
		return new PageImpl<ResumoLancamento>(query.getResultList(), pageable, total(lancamentoFilter)) ;
	}

}
