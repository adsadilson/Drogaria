package com.br.apss.drogaria.model;

import java.io.Serializable;
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
		this.email = email;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
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
