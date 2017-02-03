package org.csiro.igsn.entity.postgres;

// Generated 09/01/2017 4:40:37 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * Prefix generated by hbm2java
 */
@Entity
@Table(name = "prefix")
@NamedQueries({
	@NamedQuery(
			name="Prefix.listAll",
		    query="SELECT p FROM Prefix p where LENGTH(p.prefix) > 2 order by p.prefix"
	),
	@NamedQuery(
			name="Prefix.searchJoinRegistrant",
		    query="SELECT p FROM Prefix p left join fetch p.registrants where p.prefix  = :prefix order by p.prefix"
	),
	@NamedQuery(
			name="Prefix.search",
		    query="SELECT p FROM Prefix p where p.prefix = :prefix order by p.prefix"
	)
})	
public class Prefix implements java.io.Serializable {

	private int id;
	private Integer version;
	private String prefix;
	private Date created;	
	private Set<Registrant> registrants = new HashSet<Registrant>(0);
	private String description;

	public Prefix() {
	}

	public Prefix(String prefix) {
		this.prefix = prefix;
	}

	public Prefix(String prefix, Date created,
		 Set<Registrant> registrants) {	
		this.prefix = prefix;
		this.created = created;		
		this.registrants = registrants;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@SequenceGenerator(name="prefix_id_seq",schema="version30",sequenceName="prefix_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="prefix_id_seq")
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Version
	@Column(name = "version")
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name = "prefix", nullable = false,unique=true, length = 15)
	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", length = 29)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "registrant_prefixes", joinColumns = { @JoinColumn(name = "prefixes", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "registrant", nullable = false, updatable = false) })
	public Set<Registrant> getRegistrants() {
		return this.registrants;
	}

	public void setRegistrants(Set<Registrant> registrants) {
		this.registrants = registrants;
	}

	@Column(name = "description", length = 29)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
