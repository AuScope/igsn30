package org.csiro.igsn.entity.postgres;

// Generated 09/01/2017 4:40:37 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Location generated by hbm2java
 */
@Entity
@Table(name = "location")
public class Location implements java.io.Serializable {

	private int locationId;
	private String locality;
	private String localityUri;
	private String srid;
	private String verticalDatum;
	private String geometryUri;
	private Geometry geometry;
	

	public Location() {
	}

	
	public Location(String locality, String localityUri,
			String srid, String verticalDatum, String geometryUri,
			Geometry geometry) {		
		this.locality = locality;
		this.localityUri = localityUri;
		this.srid = srid;
		this.verticalDatum = verticalDatum;
		this.geometryUri = geometryUri;
		this.geometry = geometry;
		
	}

	@Id
	@Column(name = "location_id", unique = true, nullable = false)
	@SequenceGenerator(name="location_id_seq",schema="version30",sequenceName="location_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="location_id_seq")
	public int getLocationId() {
		return this.locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	@Column(name = "locality")
	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	@Column(name = "locality_uri")
	public String getLocalityUri() {
		return this.localityUri;
	}

	public void setLocalityUri(String localityUri) {
		this.localityUri = localityUri;
	}

	@Column(name = "srid")
	public String getSrid() {
		return this.srid;
	}

	public void setSrid(String srid) {
		this.srid = srid;
	}

	@Column(name = "vertical_datum")
	public String getVerticalDatum() {
		return this.verticalDatum;
	}

	public void setVerticalDatum(String verticalDatum) {
		this.verticalDatum = verticalDatum;
	}

	@Column(name = "geometry_uri")
	public String getGeometryUri() {
		return this.geometryUri;
	}

	public void setGeometryUri(String geometryUri) {
		this.geometryUri = geometryUri;
	}

	@Column(name = "geometry")
	@Type(type="org.hibernate.spatial.GeometryType")
	public Geometry getGeometry() {
		return this.geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	

}
