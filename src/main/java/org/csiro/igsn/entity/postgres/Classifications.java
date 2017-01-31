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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Classifications generated by hbm2java
 */
@Entity
@Table(name = "classifications")
public class Classifications implements java.io.Serializable {

	private int classificationsId;
	private Resources resources;
	private String classificationUri;
	private String classification;

	public Classifications() {
	}



	public Classifications(Resources resources,
			String classificationUri, String classification) {	
		this.resources = resources;
		this.classificationUri = classificationUri;
		this.classification = classification;
	}

	@Id
	@Column(name = "classifications_id", unique = true, nullable = false)
	@SequenceGenerator(name="classifications_id_seq",schema="version30",sequenceName="classifications_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="classifications_id_seq")
	public int getClassificationsId() {
		return this.classificationsId;
	}

	public void setClassificationsId(int classificationsId) {
		this.classificationsId = classificationsId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resource_identifier")
	@JsonIgnore
	public Resources getResources() {
		return this.resources;
	}

	public void setResources(Resources resources) {
		this.resources = resources;
	}

	@Column(name = "classification_uri")
	public String getClassificationUri() {
		return this.classificationUri;
	}

	public void setClassificationUri(String classificationUri) {
		this.classificationUri = classificationUri;
	}

	@Column(name = "classification")
	public String getClassification() {
		return this.classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

}