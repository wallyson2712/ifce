/*
 * @version     1.0.0
 * @author      Edivando J. Alves
 * @contact     edivando@j7ss.com ( http://www.j7ss.com )
 * 
 * @copyright  	Copyright 2010 - 2016 J7 Smart Solutions, all rights reserved.
 * 
 */
package com.j7ss.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.j7ss.entity.constraint.DocumentoStatus;
import com.j7ss.util.DAO;
import com.j7ss.util.DAOException;
import com.j7ss.util.IGenericEntity;

/**
 * 
 * @author Edivando Alves
 * @date  10/02/2016
 * 
 */
@Entity
@Table(name = "documento_vaga_estagio")
@ToString @EqualsAndHashCode(of={"id"})
public class DocumentoVagaEstagio implements IGenericEntity<DocumentoVagaEstagio> {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Getter @Setter
	private Integer id;
	@Getter @Setter
	private Integer ordem;
	@Getter @Setter
	private DocumentoStatus status;

	@ManyToOne
	@Setter
	private VagaEstagio vagaEstagio;
	
	@ManyToOne
	@Setter
	private Documento documento;;
	
	@OneToMany(mappedBy="documentoVagaEstagio", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("date")
	@Setter
	private List<DocumentoVagaEstagioMessage> documentoAlunoMessages;
	

//******************************************************************************************************************************
//## Builder
	public DocumentoVagaEstagio id(Integer id){
		this.id = id;
		return this;
	}
	
	public DocumentoVagaEstagio ordem(Integer ordem){
		this.ordem = ordem;
		return this;
	}
	
	public DocumentoVagaEstagio status(DocumentoStatus status){
		this.status = status;
		return this;
	}
	
	public DocumentoVagaEstagio documento(Documento documento){
		this.documento = documento;
		return this;
	}
	
	public DocumentoVagaEstagio vagaEstagio(VagaEstagio vagaEstagio){
		this.vagaEstagio = vagaEstagio;
		return this;
	}
	
	
//******************************************************************************************************************************
//## Getters Setters
	@Override
	public boolean isNew() {
		return id == null;
	}
	
	public String getLinkPagina(){
		if(documento == null || status == DocumentoStatus.INDISPONIVEL){
			return "";
		}
		return "alunoDocumento.html?id="+documento.getId();
	}
	
	public VagaEstagio getVagaEstagio() {
		return vagaEstagio == null ? vagaEstagio = new VagaEstagio() : vagaEstagio;
	}
	
	public Documento getDocumento() {
		return documento == null ? documento = new Documento() : documento;
	}
	
	public List<DocumentoVagaEstagioMessage> getDocumentoAlunoMessages() {
		return documentoAlunoMessages == null ? documentoAlunoMessages = new ArrayList<>() : documentoAlunoMessages;
	}
	
	
//******************************************************************************************************************************
//## DAO
	private static DAO<DocumentoVagaEstagio> dao = new DAO<DocumentoVagaEstagio>(DocumentoVagaEstagio.class);
	
	@Override
	public DocumentoVagaEstagio save() throws DAOException{
		return isNew() ? dao.add(this) : dao.update(this);
	}

	@Override
	public boolean remove() throws DAOException {
		return dao.remove(this);
	}
	
	public static List<DocumentoVagaEstagio> findByVagaEstagio(VagaEstagio vagaEstagio){
		return dao.findByQuery("Select d From DocumentoVagaEstagio d Where d.vagaEstagio = ?1", vagaEstagio);
	}
	
	public static List<DocumentoVagaEstagio> findByDocumentoStatus(DocumentoStatus status){
		return dao.findByQuery("Select d From DocumentoVagaEstagio d WHERE d.status = ?1", status);
	}
	
}