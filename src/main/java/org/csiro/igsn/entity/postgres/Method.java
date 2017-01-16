package org.csiro.igsn.entity.postgres;

// Generated 09/01/2017 4:40:37 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Method generated by hbm2java
 */
@Entity
@Table(name = "method")
public class Method implements java.io.Serializable {

	private int methodId;
	private String method;
	private String methodUri;
	

	public Method() {
	}



	public Method(String method, String methodUri) {		
		this.method = method;
		this.methodUri = methodUri;		
	}

	@Id
	@Column(name = "method_id", unique = true, nullable = false)
	@SequenceGenerator(name="method_id_seq",schema="version30",sequenceName="method_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="method_id_seq")
	public int getMethodId() {
		return this.methodId;
	}

	public void setMethodId(int methodId) {
		this.methodId = methodId;
	}

	@Column(name = "method")
	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Column(name = "method_uri")
	public String getMethodUri() {
		return this.methodUri;
	}

	public void setMethodUri(String methodUri) {
		this.methodUri = methodUri;
	}


}
