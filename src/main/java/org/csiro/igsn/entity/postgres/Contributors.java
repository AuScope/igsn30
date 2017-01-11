package org.csiro.igsn.entity.postgres;

// Generated 09/01/2017 4:40:37 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Contributors generated by hbm2java
 */
@Entity
@Table(name = "contributors")
public class Contributors implements java.io.Serializable {

	private int contributorId;
	private CvIdentifierType cvIdentifierType;
	private Resources resources;
	private String contributorType;
	private String contributorName;
	private String contributorIdentifier;

	public Contributors() {
	}

	

	public Contributors(Resources resources,CvIdentifierType cvIdentifierType,
			String contributorType, String contributorName, String contributorIdentifier) {		
		this.cvIdentifierType = cvIdentifierType;
		this.resources = resources;
		this.contributorType = contributorType;
		this.contributorName = contributorName;
		this.contributorIdentifier = contributorIdentifier;
	}

	@Id
	@Column(name = "contributor_id", unique = true, nullable = false)
	@SequenceGenerator(name="contributor_id_seq",schema="version30",sequenceName="contributor_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="contributor_id_seq")
	public int getContributorId() {
		return this.contributorId;
	}

	public void setContributorId(int contributorId) {
		this.contributorId = contributorId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contributor_identifier_type")
	public CvIdentifierType getCvIdentifierType() {
		return this.cvIdentifierType;
	}

	public void setCvIdentifierType(CvIdentifierType cvIdentifierType) {
		this.cvIdentifierType = cvIdentifierType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resource_identifier")
	public Resources getResources() {
		return this.resources;
	}

	public void setResources(Resources resources) {
		this.resources = resources;
	}

	@Column(name = "contributor_type")
	public String getContributorType() {
		return this.contributorType;
	}

	public void setContributorType(String contributorType) {
		this.contributorType = contributorType;
	}

	@Column(name = "contributor_name")
	public String getContributorName() {
		return this.contributorName;
	}

	public void setContributorName(String contributorName) {
		this.contributorName = contributorName;
	}

	@Column(name = "contributor_identifier")
	public String getContributorIdentifier() {
		return this.contributorIdentifier;
	}

	public void setContributorIdentifier(String contributorIdentifier) {
		this.contributorIdentifier = contributorIdentifier;
	}

}
