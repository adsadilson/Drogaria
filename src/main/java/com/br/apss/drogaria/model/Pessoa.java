package com.br.apss.drogaria.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.br.apss.drogaria.enums.Estado;
import com.br.apss.drogaria.enums.EstadoCivil;
import com.br.apss.drogaria.enums.Sexo;

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

	@Column(name = "fantasia", length = 80)
	private String fantasia;

	@Column(name = "responsavel", length = 80)
	private String responsavel;

	@Column(name = "cmv", length = 30)
	private String cmv;

	@Column(name = "tipo_profissional", length = 80)
	private String tipoProfissional;

	@Column(name = "cpf_cnpj", nullable = true, length = 20)
	private String cpfCnpj;

	@Column(name = "email", length = 200)
	private String email;

	@Column(name = "site", length = 200)
	private String site;

	@Temporal(TemporalType.DATE)
	@Column(name = "nascimento", length = 10)
	private Date nascimento;

	@Column(name = "nacionalidade", length = 80)
	private String nacionalidade;

	@Column(name = "celular", length = 20)
	private String celular;

	@Column(name = "telefone", length = 20)
	private String telefone;

	@Column(name = "telefone2", length = 20)
	private String telefone2;

	@Column(name = "contato", length = 80)
	private String contato;

	@Enumerated(EnumType.STRING)
	@Column(name = "sexo", nullable = true, length = 1)
	private Sexo sexo;

	@Column(name = "estado_civil", length = 30)
	@Enumerated(EnumType.STRING)
	private EstadoCivil estadoCivil;

	@Column(name = "conjuge", length = 80)
	private String conjuge;

	@Column(name = "nome_pai", length = 80)
	private String pai;

	@Column(name = "nome_mae", length = 80)
	private String mae;

	@OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Endereco> enderecos = new ArrayList<>();

	@OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Telefone> telefones = new ArrayList<>();

	@Column(name = "num_doc", length = 25)
	private String numDoc;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_emissao", length = 10)
	private Date dataEmissao;

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

	@Column(name = "tipo_pessoa", length = 1)
	private String tipoPessoa = "F";

	@Column(name = "status", length = 1)
	private Boolean status = true;

	@Column(name = "trabalha", length = 1)
	private Boolean trabalha;

	@Column(name = "empresa", length = 1)
	private Boolean empresa;

	@Column(name = "funcionario", length = 1)
	private Boolean funcionario = false;

	@Column(name = "cliente", length = 1)
	private Boolean cliente = false;

	@Column(name = "fornecedor", length = 1)
	private Boolean fornecedor = false;

	@Column(name = "profissional", length = 1)
	private Boolean profissional = false;

	@Column(name = "obs", columnDefinition = "text")
	private String obs;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setFantasia(String fantasia) {
		this.fantasia = email == null ? null : fantasia.toUpperCase();
	}

	public String getFantasia() {
		return fantasia;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public String getCmv() {
		return cmv;
	}

	public void setCmv(String cmv) {
		this.cmv = cmv;
	}

	public String getTipoProfissional() {
		return tipoProfissional;
	}

	public void setTipoProfissional(String tipoProfissional) {
		this.tipoProfissional = tipoProfissional;
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
		this.email = email == null ? email : email.toLowerCase();
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

	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
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

	public String getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato == null ? contato : contato.toUpperCase();
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
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
		this.conjuge = conjuge == null ? conjuge : conjuge.toUpperCase();
		;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel == null ? null : responsavel.toUpperCase();
	}

	public String getPai() {
		return pai;
	}

	public void setPai(String pai) {
		this.pai = pai == null ? pai : pai.toUpperCase();
		;
	}

	public String getMae() {
		return mae;
	}

	public void setMae(String mae) {
		this.mae = mae == null ? mae : mae.toUpperCase();
		;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
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

	public String getNumConta() {
		return numConta;
	}

	public void setNumConta(String numConta) {
		this.numConta = numConta;
	}

	public String getDigAgencia() {
		return digAgencia;
	}

	public void setDigAgencia(String digAgencia) {
		this.digAgencia = digAgencia;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getTrabalha() {
		return trabalha;
	}

	public void setTrabalha(Boolean trabalha) {
		this.trabalha = trabalha;
	}

	public Boolean getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Boolean empresa) {
		this.empresa = empresa;
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

	public Boolean getProfissional() {
		return profissional;
	}

	public void setProfissional(Boolean profissional) {
		this.profissional = profissional;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
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
