package com.example.moneyapi.resource;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.moneyapi.event.RecursoCriadoEvent;
import com.example.moneyapi.exceptionhandler.MoneyExceptionHandler.Erro;
import com.example.moneyapi.model.Lancamento;
import com.example.moneyapi.repository.LancamentoRepository;
import com.example.moneyapi.repository.filter.LancamentoFilter;
import com.example.moneyapi.repository.projection.ResumoLancamento;
import com.example.moneyapi.service.LancamentoService;
import com.example.moneyapi.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
		@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
		@GetMapping
		public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable){
			return lancamentoRepository.filtar(lancamentoFilter, pageable);
		}
		
		@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
		@GetMapping(params = "resumo")
		public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable){
			return lancamentoRepository.resumir(lancamentoFilter, pageable);
		}
		
		@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
		@GetMapping("/{codigo}")
		public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
			Lancamento lancamento = lancamentoRepository.findByCodigo(codigo);
			return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
		}
		
		@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
		@PostMapping
		public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento,
				HttpServletResponse response){
			Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
			
			publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
			return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
		}
		
		@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
		@DeleteMapping("/{codigo}")
		@ResponseStatus(HttpStatus.NO_CONTENT)
		public void remover(@PathVariable Long codigo) {
			lancamentoRepository.delete(codigo);
		}
		
		@PutMapping("/{codigo}")
		@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
		public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo,
				@Valid @RequestBody Lancamento lancamento){
			try {
				Lancamento lancamentoSalvo = lancamentoService.atualizar(codigo, lancamento);
				return ResponseEntity.ok(lancamentoSalvo);
			}catch(IllegalArgumentException e) {
				return ResponseEntity.notFound().build();
			}
		}
		
		@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
		public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex){
			String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
			String mensagemDesenvolvedor = ex.toString();
			List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
			return ResponseEntity.badRequest().body(erros);
		}
		


}
