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

	@Enumerated(EnumType.STRING)
	@Column(name = "sexo", length = 1)
	private Sexo sexo;

	@Column(name = "endereco", columnDefinition = "text")
	private String endereco;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_pessoa", length = 1)
	private TipoPessoa tipoPessoa = TipoPessoa.F;

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
		this.nome = nome;
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
		this.endereco = endereco;
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
		this.mae = mae;
	}

	public String getPai() {
		return pai;
	}

	public void setPai(String pai) {
		this.pai = pai;
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
