package com.br.apss.drogaria.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.br.apss.drogaria.enums.Estado;
import com.br.apss.drogaria.enums.EstadoCivil;
import com.br.apss.drogaria.enums.Sexo;
import com.br.apss.drogaria.enums.TipoPessoa;

@Entity
@Table(name = "pessoa")
@SequenceGenerator(name = "PESSOA_ID", sequenceName = "PESSOA_SEQ", allocationSize = 1)
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PESSOA_ID")
	private Long id;

	@Column(name = "nome", nullable = true, length = 80)
	private String nome;

	@Column(name = "apelido", nullable = true, length = 80)
	private String apelido;

	@Column(name = "cpf_cnpj", nullable = true, length = 20)
	private String cpfCnpj;

	@Column(name = "rg_insc", length = 35)
	private String rgInsc;

	@Column(name = "insc_municipal", length = 35)
	private String inscMunicipal;

	@Column(name = "cnae", length = 85)
	private String cnae;

	@Column(name = "contato1", length = 85)
	private String contato1;

	@Column(name = "contato2", length = 85)
	private String contato2;

	@Column(name = "email", length = 200)
	private String email;

	@Column(name = "site", length = 200)
	private String site;

	@Temporal(TemporalType.DATE)
	@Column(name = "nascimento", length = 10)
	private Date nascimento;

	@Column(name = "celular", length = 20)
	private String celular;

	@Column(name = "telefone", length = 20)
	private String telefone;

	@Column(name = "telefone_2", length = 20)
	private String telefone2;

	@Enumerated(EnumType.STRING)
	@Column(name = "sexo", length = 1)
	private Sexo sexo;

	@Column(name = "filho", length = 1)
	private boolean filho;

	@Column(name = "cnh", length = 1)
	private boolean cnh;

	@Column(name = "cat_cnh", length = 3)
	private String catCnh;

	@Column(name = "dependente", length = 10)
	private String dependente;

	@Column(name = "escolaridade", length = 80)
	private String escolaridade;

	@Column(name = "endereco", length = 80)
	private String endereco;

	@Column(name = "cep", length = 12)
	private String cep;

	@Column(name = "num", length = 10)
	private String num;

	@Column(name = "complemento", length = 80)
	private String complemento;

	@Column(name = "bairro", length = 80)
	private String bairro;

	@Column(name = "cidade", length = 80)
	private String cidade;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", length = 2)
	private Estado estado;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_pessoa", length = 1)
	private TipoPessoa tipoPessoa = TipoPessoa.F;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado_civil")
	private EstadoCivil estadoCivil;

	@Column(name = "status", length = 1)
	private Boolean status = true;

	@Column(name = "funcionario", length = 1)
	private Boolean funcionario = false;

	@Column(name = "cliente", length = 1)
	private Boolean cliente = false;

	@Column(name = "fornecedor", length = 1)
	private Boolean fornecedor = false;

	@Column(name = "obs", columnDefinition = "text")
	private String obs;

	@Column(name = "conjuge", length = 80)
	private String conjuge;

	@Column(name = "mae", length = 80)
	private String mae;

	@Column(name = "pai", length = 80)
	private String pai;

	@Column(name = "num_doc", length = 25)
	private String numDoc;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_emissao", length = 10)
	private Date dataEmissao;

	@Temporal(TemporalType.DATE)
	@Column(name = "vidade_cnh", length = 10)
	private Date validadeCnh;

	@Column(name = "orgao_emissor", length = 25)
	private String orgaoEmissor;

	@Enumerated(EnumType.STRING)
	@Column(name = "uf_emissor", length = 2)
	private Estado ufEmissor;

	@Column(name = "num_seguranca", length = 25)
	private String num_seguranca;

	@Column(name = "ocupacao", length = 80)
	private String ocupacao;

	@Column(name = "nome_empresa", length = 80)
	private String nomeEmpresa;

	@Column(name = "emp_telefone", length = 20)
	private String empTelefone;

	@Column(name = "emp_cep", length = 10)
	private String empCep;

	@Column(name = "emp_endereco", length = 80)
	private String empEndereco;

	@Column(name = "emp_num", length = 10)
	private String empNumero;

	@Column(name = "emp_complemento", length = 80)
	private String empComplemento;

	@Column(name = "emp_bairro", length = 80)
	private String empBairro;

	@Column(name = "emp_uf", length = 2)
	@Enumerated(EnumType.STRING)
	private Estado empUf;

	@Column(name = "emp_cidade", length = 80)
	private String empCidade;

	@Column(name = "nome_banco", length = 80)
	private String banco;

	@Column(name = "tipo_conta", length = 80)
	private String tipoConta;

	@Column(name = "agencia", length = 10)
	private String agencia;

	@Column(name = "dig_agencia", length = 5)
	private String digAgencia;

	@Column(name = "num_conta", length = 30)
	private String numConta;

	@Column(name = "dig_conta", length = 5)
	private String digConta;

	@Column(name = "conta_conjunta", length = 3)
	private Boolean contaConjunta;

	@Column(name = "renda", precision = 12, scale = 2)
	private BigDecimal renda;

	@Column(name = "vlr_ultima_compra", precision = 12, scale = 2)
	private BigDecimal vlrUltimaCompra;

	@Column(name = "limite", precision = 12, scale = 2)
	private BigDecimal limite;

	@Column(name = "venda_aprazo", length = 1)
	private Boolean vendaAprazo;

	@Column(name = "bloquear_vencto", length = 1)
	private Boolean bloquearVenco;

	@Column(name = "motivo_bloqueio", length = 40)
	private String motivoBloqueio;

	@Column(name = "titulo_eleitor", length = 20)
	private String tituloEleitor;

	@Column(name = "zona", length = 10)
	private String zona;

	@Column(name = "secao", length = 10)
	private String secao;

	@Column(name = "cateira_reservista", length = 25)
	private String cateiraReservista;

	@Column(name = "ctps", length = 20)
	private String ctps;

	@Column(name = "serie_ctps", length = 10)
	private String serieCtps;

	@Column(name = "uf_ctps", length = 2)
	private String ufCtps;

	@Temporal(TemporalType.DATE)
	@Column(name = "emissao_ctps", length = 10)
	private Date emissaoCtps;

	@Temporal(TemporalType.DATE)
	@Column(name = "admissao", length = 10)
	private Date admissao;

	@Temporal(TemporalType.DATE)
	@Column(name = "demissao", length = 10)
	private Date demissao;

	@Column(name = "pis_pasep", length = 20)
	private String pisPasep;

	@Column(name = "trabalha", length = 1)
	private Boolean trabalha;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.toLowerCase();
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site == null ? null : site.toLowerCase();
	}

	public Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco.toUpperCase();
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento == null ? null : complemento.toUpperCase();
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro.toUpperCase();
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade.toUpperCase();
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Boolean funcionario) {
		this.funcionario = funcionario;
	}

	public Boolean getCliente() {
		return cliente;
	}

	public void setCliente(Boolean cliente) {
		this.cliente = cliente;
	}

	public Boolean getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Boolean fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getRgInsc() {
		return rgInsc;
	}

	public void setRgInsc(String rgInsc) {
		this.rgInsc = rgInsc;
	}

	public String getMae() {
		return mae;
	}

	public void setMae(String mae) {
		this.mae = mae == null ? null : mae.toUpperCase();
	}

	public String getPai() {
		return pai;
	}

	public void setPai(String pai) {
		this.pai = pai == null ? null : pai.toUpperCase();
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getConjuge() {
		return conjuge;
	}

	public void setConjuge(String conjuge) {
		this.conjuge = conjuge == null ? null : conjuge.toUpperCase();
	}

	public String getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public String getInscMunicipal() {
		return inscMunicipal;
	}

	public void setInscMunicipal(String inscMunicipal) {
		this.inscMunicipal = inscMunicipal;
	}

	public String getCnae() {
		return cnae;
	}

	public void setCnae(String cnae) {
		this.cnae = cnae == null ? null : cnae.toUpperCase();
	}

	public String getContato1() {
		return contato1;
	}

	public void setContato1(String contato1) {
		this.contato1 = contato1 == null ? null : contato1.toUpperCase();
	}

	public String getContato2() {
		return contato2;
	}

	public void setContato2(String contato2) {
		this.contato2 = contato2 == null ? null : contato2.toUpperCase();
	}

	public String getNumDoc() {
		return numDoc;
	}

	public void setNumDoc(String numDoc) {
		this.numDoc = numDoc;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getOrgaoEmissor() {
		return orgaoEmissor;
	}

	public void setOrgaoEmissor(String orgaoEmissor) {
		this.orgaoEmissor = orgaoEmissor;
	}

	public Estado getUfEmissor() {
		return ufEmissor;
	}

	public void setUfEmissor(Estado ufEmissor) {
		this.ufEmissor = ufEmissor;
	}

	public String getNum_seguranca() {
		return num_seguranca;
	}

	public void setNum_seguranca(String num_seguranca) {
		this.num_seguranca = num_seguranca;
	}

	public String getOcupacao() {
		return ocupacao;
	}

	public void setOcupacao(String ocupacao) {
		this.ocupacao = ocupacao;
	}

	public String getNomeEmpresa() {
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}

	public String getEmpTelefone() {
		return empTelefone;
	}

	public void setEmpTelefone(String empTelefone) {
		this.empTelefone = empTelefone;
	}

	public String getEmpCep() {
		return empCep;
	}

	public void setEmpCep(String empCep) {
		this.empCep = empCep;
	}

	public String getEmpEndereco() {
		return empEndereco;
	}

	public void setEmpEndereco(String empEndereco) {
		this.empEndereco = empEndereco;
	}

	public String getEmpNumero() {
		return empNumero;
	}

	public void setEmpNumero(String empNumero) {
		this.empNumero = empNumero;
	}

	public String getEmpComplemento() {
		return empComplemento;
	}

	public void setEmpComplemento(String empComplemento) {
		this.empComplemento = empComplemento;
	}

	public String getEmpBairro() {
		return empBairro;
	}

	public void setEmpBairro(String empBairro) {
		this.empBairro = empBairro;
	}

	public Estado getEmpUf() {
		return empUf;
	}

	public void setEmpUf(Estado empUf) {
		this.empUf = empUf;
	}

	public String getEmpCidade() {
		return empCidade;
	}

	public void setEmpCidade(String empCidade) {
		this.empCidade = empCidade;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getDigAgencia() {
		return digAgencia;
	}

	public void setDigAgencia(String digAgencia) {
		this.digAgencia = digAgencia;
	}

	public String getNumConta() {
		return numConta;
	}

	public void setNumConta(String numConta) {
		this.numConta = numConta;
	}

	public String getDigConta() {
		return digConta;
	}

	public void setDigConta(String digConta) {
		this.digConta = digConta;
	}

	public Boolean getContaConjunta() {
		return contaConjunta;
	}

	public void setContaConjunta(Boolean contaConjunta) {
		this.contaConjunta = contaConjunta;
	}

	public BigDecimal getRenda() {
		return renda;
	}

	public void setRenda(BigDecimal renda) {
		this.renda = renda;
	}

	public BigDecimal getVlrUltimaCompra() {
		return vlrUltimaCompra;
	}

	public void setVlrUltimaCompra(BigDecimal vlrUltimaCompra) {
		this.vlrUltimaCompra = vlrUltimaCompra;
	}

	public BigDecimal getLimite() {
		return limite;
	}

	public void setLimite(BigDecimal limite) {
		this.limite = limite;
	}

	public Boolean getVendaAprazo() {
		return vendaAprazo;
	}

	public void setVendaAprazo(Boolean vendaAprazo) {
		this.vendaAprazo = vendaAprazo;
	}

	public Boolean getBloquearVenco() {
		return bloquearVenco;
	}

	public void setBloquearVenco(Boolean bloquearVenco) {
		this.bloquearVenco = bloquearVenco;
	}

	public String getMotivoBloqueio() {
		return motivoBloqueio;
	}

	public void setMotivoBloqueio(String motivoBloqueio) {
		this.motivoBloqueio = motivoBloqueio;
	}

	public String getTituloEleitor() {
		return tituloEleitor;
	}

	public void setTituloEleitor(String tituloEleitor) {
		this.tituloEleitor = tituloEleitor;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getSecao() {
		return secao;
	}

	public void setSecao(String secao) {
		this.secao = secao;
	}

	public String getCateiraReservista() {
		return cateiraReservista;
	}

	public void setCateiraReservista(String cateiraReservista) {
		this.cateiraReservista = cateiraReservista;
	}

	public String getCtps() {
		return ctps;
	}

	public void setCtps(String ctps) {
		this.ctps = ctps;
	}

	public String getSerieCtps() {
		return serieCtps;
	}

	public void setSerieCtps(String serieCtps) {
		this.serieCtps = serieCtps;
	}

	public String getUfCtps() {
		return ufCtps;
	}

	public void setUfCtps(String ufCtps) {
		this.ufCtps = ufCtps;
	}

	public Date getEmissaoCtps() {
		return emissaoCtps;
	}

	public void setEmissaoCtps(Date emissaoCtps) {
		this.emissaoCtps = emissaoCtps;
	}

	public Date getAdmissao() {
		return admissao;
	}

	public void setAdmissao(Date admissao) {
		this.admissao = admissao;
	}

	public Date getDemissao() {
		return demissao;
	}

	public void setDemissao(Date demissao) {
		this.demissao = demissao;
	}

	public String getPisPasep() {
		return pisPasep;
	}

	public void setPisPasep(String pisPasep) {
		this.pisPasep = pisPasep;
	}

	public Boolean getTrabalha() {
		return trabalha;
	}

	public void setTrabalha(Boolean trabalha) {
		this.trabalha = trabalha;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public boolean isFilho() {
		return filho;
	}

	public void setFilho(boolean filho) {
		this.filho = filho;
	}

	public String getDependente() {
		return dependente;
	}

	public void setDependente(String dependente) {
		this.dependente = dependente;
	}

	public String getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	public boolean isCnh() {
		return cnh;
	}

	public void setCnh(boolean cnh) {
		this.cnh = cnh;
	}

	public String getCatCnh() {
		return catCnh;
	}

	public void setCatCnh(String catCnh) {
		this.catCnh = catCnh;
	}

	public Date getValidadeCnh() {
		return validadeCnh;
	}

	public void setValidadeCnh(Date validadeCnh) {
		this.validadeCnh = validadeCnh;
	}

	public boolean isInclusao() {
		return getId() == null ? true : false;
	}

	public boolean isEditando() {
		return !isInclusao();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
	}

}
