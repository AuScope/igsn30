package org.csiro.igsn.jaxb.oai.bindings.igsn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.csiro.igsn.entity.postgres.AlternateIdentifiers;
import org.csiro.igsn.entity.postgres.Classifications;
import org.csiro.igsn.entity.postgres.Contributors;
import org.csiro.igsn.entity.postgres.CurationDetails;
import org.csiro.igsn.entity.postgres.MaterialTypes;
import org.csiro.igsn.entity.postgres.RelatedResources;
import org.csiro.igsn.entity.postgres.ResourceTypes;
import org.csiro.igsn.entity.postgres.Resources;
import org.csiro.igsn.entity.postgres.SampledFeatures;
import org.csiro.igsn.jaxb.oai.bindings.JAXBConverterInterface;
import org.csiro.igsn.jaxb.oai.bindings.igsn.Resource.Collectors.Collector;
import org.csiro.igsn.utilities.IGSNDateUtil;
import org.postgis.Geometry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EntityToSchemaConverterIGSN implements JAXBConverterInterface{
	
	ObjectFactory objectFactory;
	final String METAPREFIX="igsn";
	final String NAMESPACE_FOR_BINDING="http://schema.igsn.org/description/1.0";
	final String SCHEMA_LOCATION_FOR_BINDING="https://raw.githubusercontent.com/IGSN/metadata/dev/description/resource.xsd";
	final Class XML_ROOT_CLASS = org.csiro.igsn.jaxb.oai.bindings.igsn.Resource.class;
	
	@Value("#{configProperties['REGISTRANT_AFFILIATION_NAME']}")
	private String REGISTRANT_AFFILIATION_NAME;	
	
	@Value("#{configProperties['REGISTRANT_AFFILIATION_URI']}")
	private String REGISTRANT_AFFILIATION_URI;
	
	@Value("#{configProperties['REGISTRANT_NAME']}")
	private String REGISTRANT_NAME;
	
	
	public EntityToSchemaConverterIGSN(){
		this.objectFactory = new ObjectFactory();		 
	}
	

	@Override
	public boolean supports(String metadataPrefix){
	
		if(metadataPrefix==null || !metadataPrefix.toLowerCase().equals(this.METAPREFIX)){
			return false;
		}else{
			return true;
		}
			
	}

	
	

	@Override
	public String getMetadataPrefix() {
		return METAPREFIX;
	}

	@Override
	public String getNamespace() {
		return NAMESPACE_FOR_BINDING;
	}

	@Override
	public String getSchemaLocation() {		
		return SCHEMA_LOCATION_FOR_BINDING;
	}
	
	@Override
	public Class getXMLRootClass() {		
		return XML_ROOT_CLASS;
	}

	@Override
	public org.csiro.igsn.jaxb.oai.bindings.igsn.Resource convert(Resources resource) throws NumberFormatException, DatatypeConfigurationException {		
		Resource resourceXML = this.objectFactory.createResource();
		
		resourceXML.setRegistedObjectType(mapRegisteredObjectType(resource.getRegisteredObjectType()));
		
		resourceXML.setIdentifier(this.objectFactory.createResourceIdentifier());
		resourceXML.getIdentifier().setValue(resource.getResourceIdentifier());
		resourceXML.getIdentifier().setType(IdentifierType.IGSN);
		
		
		

		
		if(resource.getIsPublic()){
			resourceXML.setIsMetadataPublic(AccessType.PUBLIC);
		}else{
			resourceXML.setIsMetadataPublic(AccessType.PRIVATE);
		}
		
		resourceXML.setTitle(resource.getResourceTitle());
		
		if(resource.getAlternateIdentifierses()!=null && !resource.getAlternateIdentifierses().isEmpty()){			
			resourceXML.setAlternateIdentifiers(this.objectFactory.createResourceAlternateIdentifiers());
			resourceXML.getAlternateIdentifiers().alternateIdentifier = new ArrayList<Resource.AlternateIdentifiers.AlternateIdentifier>(); 
			for(AlternateIdentifiers alternateIdentifiers:resource.getAlternateIdentifierses()){
				Resource.AlternateIdentifiers.AlternateIdentifier alternateIdentiferXML = new Resource.AlternateIdentifiers.AlternateIdentifier();
				alternateIdentiferXML.setValue(alternateIdentifiers.getAlternateIdentifier());
				resourceXML.getAlternateIdentifiers().alternateIdentifier.add(alternateIdentiferXML);
			}
		}
		
		resourceXML.setResourceTypes(this.objectFactory.createResourceResourceTypes());
		
		for(ResourceTypes resourceTypes:resource.getResourceTypeses()){
			if(resourceXML.getResourceTypes().getResourceType() == null || resourceXML.getResourceTypes().getResourceType().isEmpty()){
				resourceXML.getResourceTypes().setResourceType(mapResourceType(resourceTypes.getCvResourceType().getResourceType()));
			}else{
				if(resourceXML.getResourceTypes().alternateResourceTypes == null){
					Resource.ResourceTypes.AlternateResourceTypes alternateResourceTypeXML = new Resource.ResourceTypes.AlternateResourceTypes();
					alternateResourceTypeXML.alternateResourceType = new ArrayList<String>();
					resourceXML.getResourceTypes().setAlternateResourceTypes(alternateResourceTypeXML);
				}
				resourceXML.getResourceTypes().alternateResourceTypes.getAlternateResourceType().add(mapResourceType(resourceTypes.getCvResourceType().getResourceType()));
				
			}
		}
		
		resourceXML.setMaterials(this.objectFactory.createResourceMaterials());
		resourceXML.getMaterials().material = new ArrayList<MaterialType>();
		for(MaterialTypes materialType:resource.getMaterialTypeses()){			
			resourceXML.getMaterials().material.add(mapMaterialType(materialType.getCvMaterialTypes().getMaterialType()));
		}
		
		
		if(resource.getLocation()!=null){

			Resource.Locations locationXML = this.objectFactory.createResourceLocations();
			if(mapGeometryType(resource.getLocation().getWkt())!=null){
				Resource.Locations.Geometry geometryXML = this.objectFactory.createResourceLocationsGeometry();
				geometryXML.setSridType("4326");			
				geometryXML.setType(mapGeometryType(resource.getLocation().getWkt()));
				geometryXML.setValue(resource.getLocation().getWkt());
				locationXML.setGeometry(geometryXML);
			}
			
			Resource.Locations.Toponym toponym = null;
			if((resource.getLocation().getLocality()!=null && !resource.getLocation().getLocality().isEmpty()) 
					|| (resource.getLocation().getLocalityUri()!=null && !resource.getLocation().getLocalityUri().isEmpty())){
				toponym = this.objectFactory.createResourceLocationsToponym();
				
				if(resource.getLocation().getLocality()!=null && !resource.getLocation().getLocality().isEmpty()){
					toponym.setName(resource.getLocation().getLocality());
				}
				if(resource.getLocation().getLocalityUri()!=null && !resource.getLocation().getLocalityUri().isEmpty()){
					toponym.setIdentifier(this.objectFactory.createResourceLocationsToponymIdentifier());
					toponym.getIdentifier().setValue(resource.getLocation().getLocalityUri());
					toponym.getIdentifier().setType(IdentifierType.URI);
				}
			}			
			locationXML.setToponym(toponym);
			resourceXML.setLocations(locationXML);
		}
				
		if(resource.getResourceDate()!=null && !(resource.getResourceDate().getTimeInstant()==null && resource.getResourceDate().getTimePeriodStart()==null)){				
			Resource.Date date = new Resource.Date();
			if(resource.getResourceDate().getTimeInstant()!=null && !resource.getResourceDate().getTimeInstant().isEmpty()){
				date.setTimeInstant(IGSNDateUtil.parseForGregorianCalendar(resource.getResourceDate().getTimeInstant()));
			}else{
				Resource.Date.TimePeriod timeperiod = new Resource.Date.TimePeriod();
				timeperiod.setStart(IGSNDateUtil.parseForGregorianCalendar(resource.getResourceDate().getTimePeriodStart()));
				timeperiod.setEnd(IGSNDateUtil.parseForGregorianCalendar(resource.getResourceDate().getTimePeriodEnd()));
				date.setTimePeriod(timeperiod);
			}			
			resourceXML.setDate(date);
		}
		
		
		
		if(resource.getCurationDetailses()!=null && !resource.getCurationDetailses().isEmpty()){
			resourceXML.setContributors(this.objectFactory.createResourceContributors());		
			resourceXML.getContributors().contributor = new ArrayList<Resource.Contributors.Contributor>();
			for(CurationDetails curationDetails:resource.getCurationDetailses()){
				Resource.Contributors.Contributor contributorXML = new Resource.Contributors.Contributor();
				contributorXML.setType(ContributorType.HOSTING_INSTITUTION);
				contributorXML.setName(curationDetails.getCuratingInstitution() + (curationDetails.getCurator()==null || curationDetails.getCurator().isEmpty()?"":":"+curationDetails.getCurator()));
				if(curationDetails.getInstitutionUri()!=null && !curationDetails.getInstitutionUri().isEmpty()){
					contributorXML.setIdentifier(this.objectFactory.createResourceContributorsContributorIdentifier());
					contributorXML.getIdentifier().setType(IdentifierType.URI);
					contributorXML.getIdentifier().setValue(curationDetails.getInstitutionUri());
				}
				resourceXML.getContributors().contributor.add(contributorXML);
			}
		}
		
		
		
		
		if(resource.getContributorses()!=null && !resource.getContributorses().isEmpty()){			
			for(Contributors contributor:resource.getContributorses()){
				if(contributor.getContributorType().equalsIgnoreCase("http://registry.it.csiro.au/def/isotc211/CI_RoleCode/originator")){					
					if(resourceXML.getCollectors()==null){
						resourceXML.setCollectors(this.objectFactory.createResourceCollectors());
						resourceXML.getCollectors().collector = new ArrayList<Resource.Collectors.Collector>();
					}
					Collector collectorXML = this.objectFactory.createResourceCollectorsCollector();
					collectorXML.setName(contributor.getContributorName());
					collectorXML.setIdentifier(this.objectFactory.createResourceCollectorsCollectorIdentifier());					
					collectorXML.getIdentifier().setType(mapIdentifierType(contributor.getCvIdentifierType().getIdentifierType()));
					collectorXML.getIdentifier().setValue(contributor.getContributorIdentifier());
					resourceXML.getCollectors().collector.add(collectorXML);
					
				}else{					
					if(mapContributorType(contributor.getContributorType())!=null){					
						if(resourceXML.getContributors()==null){
							resourceXML.setContributors(this.objectFactory.createResourceContributors());		
							resourceXML.getContributors().contributor = new ArrayList<Resource.Contributors.Contributor>();
						}
						Resource.Contributors.Contributor contributorXML = this.objectFactory.createResourceContributorsContributor();
						contributorXML.setType(mapContributorType(contributor.getContributorType()));
						contributorXML.setName(contributor.getContributorName());
						
						if(contributor.getCvIdentifierType()!=null){
							contributorXML.setIdentifier(this.objectFactory.createResourceContributorsContributorIdentifier());
							contributorXML.getIdentifier().setType(mapIdentifierType(contributor.getCvIdentifierType().getIdentifierType()));
							contributorXML.getIdentifier().setValue(contributor.getContributorIdentifier());
						}
						
						
						resourceXML.getContributors().contributor.add(contributorXML);
						
					}
				}
				
			}
		}
		
		if(resource.getRelatedResourceses()!=null && !resource.getRelatedResourceses().isEmpty()){
			resourceXML.setRelatedResources(this.objectFactory.createResourceRelatedResources());
			resourceXML.getRelatedResources().relatedResource = new ArrayList<Resource.RelatedResources.RelatedResource>();
			for(RelatedResources relatedResources:resource.getRelatedResourceses()){
				//VT: only if its mappable we map it.
				if(mapRelationType(relatedResources.getRelationType())!=null){
					Resource.RelatedResources.RelatedResource relatedResourcesXML = new Resource.RelatedResources.RelatedResource();
					relatedResourcesXML.setType(mapIdentifierType(relatedResources.getCvIdentifierType().getIdentifierType()));
					relatedResourcesXML.setRelationType(mapRelationType(relatedResources.getRelationType()));
					relatedResourcesXML.setValue(relatedResources.getRelatedResource());
					resourceXML.getRelatedResources().relatedResource.add(relatedResourcesXML);
				}
			}
			if(resourceXML.getRelatedResources().relatedResource.isEmpty()){
				resourceXML.setRelatedResources(null);
			}
		}
		
		resourceXML.setRegistrant(this.objectFactory.createResourceRegistrant());
		resourceXML.getRegistrant().setName(this.REGISTRANT_NAME);
		resourceXML.getRegistrant().setAffiliation(this.objectFactory.createResourceRegistrantAffiliation());
		resourceXML.getRegistrant().getAffiliation().setIdentifier(this.objectFactory.createResourceRegistrantAffiliationIdentifier());
		resourceXML.getRegistrant().getAffiliation().getIdentifier().setType(IdentifierType.URI);
		resourceXML.getRegistrant().getAffiliation().getIdentifier().setValue(this.REGISTRANT_AFFILIATION_URI);
		resourceXML.getRegistrant().getAffiliation().setName(this.REGISTRANT_AFFILIATION_NAME);
		
		
		return resourceXML;
	}


	private GeometryType mapGeometryType(String trim) {
		try{
			if(trim.contains("(")){
				trim = trim.substring(0,trim.indexOf('(')).trim();
			}
			return GeometryType.fromValue(trim);
		}catch(Exception e){
			return null;
		}
		
	}


	private ContributorType mapContributorType(String contributorType) {
		switch(contributorType){
			case "http://registry.it.csiro.au/def/isotc211/CI_RoleCode/pointOfContact": return ContributorType.CONTACT_PERSON;
			case "http://registry.it.csiro.au/def/isotc211/CI_RoleCode/funder": return ContributorType.FUNDER;
			case "http://registry.it.csiro.au/def/isotc211/CI_RoleCode/rightsHolder": return ContributorType.RIGHTS_HOLDER;
			case "http://registry.it.csiro.au/def/isotc211/CI_RoleCode/sponsor": return ContributorType.SPONSOR;	
			case "http://registry.it.csiro.au/def/isotc211/CI_RoleCode/originator" : return null;
			default: return ContributorType.OTHER;
		}
	}


	private String mapRegisteredObjectType(String registeredObjectType) {
		switch(registeredObjectType){
		case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/PhysicalSample": return "http://schema.igsn.org/vocab/PhysicalSample";
		case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/SampleCollection" : return "http://schema.igsn.org/vocab/SampleCollection";			
		case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/SamplingFeature" : return "http://schema.igsn.org/vocab/SamplingFeature";				
		default: return null;
	}
	}


	private RelationType mapRelationType(String relationType) {
		String trimedrelationType= relationType.substring(relationType.lastIndexOf("/")+1,relationType.length());	
		try{
			return RelationType.fromValue(trimedrelationType);
		}catch(Exception e){
			return null;
		}
	}


	private IdentifierType mapIdentifierType(String fromValue) {
		String trimedFromValue = fromValue.substring(fromValue.lastIndexOf("/") + 1,fromValue.length());	
		if(trimedFromValue.equalsIgnoreCase("url") || trimedFromValue.equalsIgnoreCase("urn")){
			return IdentifierType.URI;
		}else{
			return IdentifierType.fromValue(trimedFromValue);
		}
		
	}


	private MaterialType mapMaterialType(String materialType) {
		switch(materialType){
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/air": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_AIR;
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/gas": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_GAS;			
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/ice": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_ICE;
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/liquidAqueous": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_LIQUID_AQUEOUS;
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/liquidOrganic": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_LIQUID_ORGANIC;
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/mineral": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_MINERAL;
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/organism": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_ORGANISM;
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/other": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_OTHER;
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/particulate": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_PARTICULATE;
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/rock": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_ROCK;
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/sediment": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_SEDIMENT;
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/snow": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_SNOW;
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/soil": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_SOIL;
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/tissue": return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_TISSUE;			
			default: return MaterialType.HTTP_VOCABULARY_ODM_2_ORG_MEDIUM_UNKNOWN;
		}
		
	}


	private String mapResourceType(String resourceType) {
		switch(resourceType){
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/automated": return "http://vocabulary.odm2.org/specimentype/automated";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/core" : return "http://vocabulary.odm2.org/specimentype/core";			
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/coreHalfRound" : return "http://vocabulary.odm2.org/specimentype/coreHalfRound";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/corePiece" : return "http://vocabulary.odm2.org/specimentype/corePiece";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/coreQuarterRound" : return "http://vocabulary.odm2.org/specimentype/coreQuarterRound";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/coreSection": return "http://vocabulary.odm2.org/specimentype/coreSection";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/coreSectionHalf": return "http://vocabulary.odm2.org/specimentype/coreSectionHalf";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/coreSub-Piece": return"http://vocabulary.odm2.org/specimentype/coreSub-Piece";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/coreWholeRound" : return "http://vocabulary.odm2.org/specimentype/coreWholeRound";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/cuttings" : return "http://vocabulary.odm2.org/specimentype/cuttings";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/dredge" : return "http://vocabulary.odm2.org/specimentype/dredge";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/foliageDigestion" : return "http://vocabulary.odm2.org/specimentype/foliageDigestion";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/foliageLeaching": return "http://vocabulary.odm2.org/specimentype/foliageLeaching";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/forestFloorDigestion" : return "http://vocabulary.odm2.org/specimentype/forestFloorDigestion";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/individualSample" : return "http://vocabulary.odm2.org/specimentype/individualSample";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/litterFallDigestion" : return "http://vocabulary.odm2.org/specimentype/litterFallDigestion";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/petriDishDryDeposition" : return "http://vocabulary.odm2.org/specimentype/petriDishDryDeposition";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/precipitationBulk" : return "http://vocabulary.odm2.org/specimentype/precipitationBulk";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/rockPowder" : return "http://vocabulary.odm2.org/specimentype/rockPowder";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/standardReferenceSpecimen" : return "http://vocabulary.odm2.org/specimentype/standardReferenceSpecimen";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/terrestrialSection" : return "http://vocabulary.odm2.org/specimentype/terrestrialSection";		
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/thinSection" : return "http://vocabulary.odm2.org/specimentype/thinSection";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/orientedCore" : return "http://vocabulary.odm2.org/specimentype/orientedCore";
			case "http://pid.geoscience.gov.au/def/voc/igsn-codelists/grab" : return "http://vocabulary.odm2.org/specimentype/grab";			
			default: return "http://vocabulary.odm2.org/specimentype/other";
		}
		
	}
	

}

