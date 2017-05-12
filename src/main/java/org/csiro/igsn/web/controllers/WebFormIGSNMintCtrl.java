package org.csiro.igsn.web.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.csiro.igsn.entity.postgres.Registrant;
import org.csiro.igsn.entity.service.ControlledValueEntityService;
import org.csiro.igsn.entity.service.PrefixEntityService;
import org.csiro.igsn.entity.service.RegistrantEntityService;
import org.csiro.igsn.entity.service.ResourceEntityService;
import org.csiro.igsn.exception.DatabaseErrorCode;
import org.csiro.igsn.exception.ExceptionWrapper;
import org.csiro.igsn.exception.MintErrorCode;
import org.csiro.igsn.exception.MintEventLog;
import org.csiro.igsn.jaxb.registration.bindings.EventType;
import org.csiro.igsn.jaxb.registration.bindings.JsonToSchemaConverterCSIRO;
import org.csiro.igsn.jaxb.registration.bindings.ObjectFactory;
import org.csiro.igsn.jaxb.registration.bindings.Resources;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource.IsPublic;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource.ResourceIdentifier;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource.ResourceTypes;
import org.csiro.igsn.service.MintService;
import org.csiro.igsn.utilities.IGSNDateUtil;
import org.csiro.igsn.utilities.IGSNUtil;
import org.csiro.igsn.utilities.SpatialUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vividsolutions.jts.geom.Geometry;

@RestController
@RequestMapping(value = "/web/")
public class WebFormIGSNMintCtrl {
	
	final Logger log = Logger.getLogger(WebFormIGSNMintCtrl.class);
	ResourceEntityService resourceEntityService;
	MintService mintService;
	PrefixEntityService prefixEntityService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	ControlledValueEntityService controlledValueEntityService;
	RegistrantEntityService registerantEntityService;
	JsonToSchemaConverterCSIRO jsonToSchemaConverterCSIRO;
	
	ObjectFactory objectFactory;
	
	@Value("#{configProperties['IGSN_CSIRO_XSD_URL']}")
	private String IGSN_CSIRO_XSD_URL;

	@Value("#{configProperties['IGSN_PREFIX']}")
	private String IGSN_PREFIX;
	
	
	@Autowired
	public WebFormIGSNMintCtrl(ResourceEntityService resourceEntityService,MintService mintService,PrefixEntityService prefixEntityService,
			ControlledValueEntityService controlledValueEntityService,JsonToSchemaConverterCSIRO jsonToSchemaConverterCSIRO,
			RegistrantEntityService registerantEntityService){
		this.resourceEntityService = resourceEntityService;
		this.mintService = mintService;
		this.prefixEntityService = prefixEntityService;
		this.controlledValueEntityService = controlledValueEntityService;
		this.registerantEntityService = registerantEntityService;
		this.jsonToSchemaConverterCSIRO = jsonToSchemaConverterCSIRO;
		this.objectFactory = new ObjectFactory();
	}
	


	/**
	 * Parse in json string, convert to Resources and mint it.
	 * @param resources
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/mintJson", method = { RequestMethod.POST, RequestMethod.HEAD } )
	public  ResponseEntity<?> mint(@RequestBody(required = true) String resourcesjson,
			 HttpServletRequest request,
			Principal user) {
		
		try{
			JsonElement resourceElement = new JsonParser().parse(resourcesjson);	        
			String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + request.getContextPath(); 
			Resources resourcesXML = this.objectFactory.createResources();
			resourcesXML.getResource().add(this.jsonToSchemaConverterCSIRO.convert(resourceElement,baseUrl));
			return this.mint(resourcesXML,false,user);
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<Object>(new ExceptionWrapper("Fail to mint resource",e.getMessage()==null?"There are error in the form. Correct them before submitting":e.getMessage()),HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	@RequestMapping("convertutm.do")
	public ResponseEntity<Object> convertutm(
			@RequestParam(required = true, value ="zone") String zone,
			@RequestParam(required = true, value ="easting") String easting,
			@RequestParam(required = true, value ="northing") String northing,
			Principal user) {
		
		try {
			return new ResponseEntity<Object>(SpatialUtilities.convertUTM_MGA942Geographic_EPSG4326(easting, northing, zone),HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new ExceptionWrapper("Conversion fail",e.getMessage()),HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	/**
	 * This is a duplicate of IGSNMintCtrl from the api package. Duplicate due to the use of beans + multi facet http security for api and form makes it make to reused
	 * the code without jumping through hoops. Hence the duplication.
	 * @param resources
	 * @param test
	 * @param user
	 * @return
	 */
	public ResponseEntity<?> mint(Resources resources, boolean test,Principal user){
		
		boolean isXMLValid = true;
		
		Schema schema = null;

		// 2. VALIDATE XML ====================================================
	
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {				
			URL schemaUrl = new URL(this.IGSN_CSIRO_XSD_URL);				
			schema = sf.newSchema(schemaUrl);
		} catch (SAXException e) {
			e.printStackTrace();
			return   new ResponseEntity<String>("Failure retriving schema : " + e.getLocalizedMessage(),
					HttpStatus.BAD_REQUEST);
		} catch (MalformedURLException e) {
			log.error("URL malformed for schema location. Recheck config.properties file again.");
			return  new ResponseEntity<String>("Failure retriving schema : " + e.getLocalizedMessage(),
					HttpStatus.BAD_REQUEST);	
		}

		try {			
			JAXBContext jc = JAXBContext.newInstance(Resources.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setSchema(schema);		
			marshaller.marshal(resources, new DefaultHandler());		
		} catch (JAXBException e) {
			e.printStackTrace();
			isXMLValid = false;
			return new ResponseEntity<Object>(new ExceptionWrapper("XML Validation failed",e.getLocalizedMessage()==null?e.getLinkedException().getLocalizedMessage():e.getLocalizedMessage()),HttpStatus.BAD_REQUEST);			
		}
		
		// 3. VALIDATE SUBNAMESPACE BASED ON USER NAME
				// =============================
		String usr = null;
		List<MintEventLog> mintEventLogs = new ArrayList<MintEventLog>();
		
		if (isXMLValid) {						
			usr = user.getName();			
			Registrant registrant = registerantEntityService.searchActiveRegistrantAndPrefix(usr);
			
			for (Resource r : resources.getResource()) {
				MintEventLog mintEventLog= new MintEventLog(r.getResourceIdentifier().getValue());
				
				if(IGSNUtil.resourceStartsWithAllowedPrefix(registrant.getPrefixes(),r)){		
					
					try{
						SimpleDateFormat metadataDateFormat = IGSNDateUtil.getISODateFormatter();
						String igsn=this.mintService.createRegistryXML(r.getResourceIdentifier().getValue(), r.getLandingPage(), metadataDateFormat.format(new Date()), test, r.getLogDate().getEventType().value());							
						mintEventLog.setMintLog(MintErrorCode.MINT_SUCCESS, null);
						mintEventLog.setHandle("http://hdl.handle.net/"+igsn);							
					}catch(Exception e){
						mintEventLog.setMintLog(MintErrorCode.MINT_FAILURE, e.getMessage());							
						mintEventLog.setDatabaseLog(DatabaseErrorCode.NOT_ATTEMPTED, "");
						mintEventLogs.add(mintEventLog);
						continue;
					}
					
					
					try{
						if(test){
							resourceEntityService.testInsertResource(r,registrant);
						}else if(r.getLogDate().getEventType().equals(EventType.REGISTERED)){
							resourceEntityService.insertResource(r,registrant,true);
						}else if(r.getLogDate().getEventType().equals(EventType.DESTROYED)){
							resourceEntityService.destroyResource(r,true);
						}else if(r.getLogDate().getEventType().equals(EventType.DEPRECATED)){
							resourceEntityService.deprecateResource(r,true);
						}else if(r.getLogDate().getEventType().equals(EventType.UPDATED)){
							resourceEntityService.updateResource(r,registrant,true);
						}
						mintEventLog.setDatabaseLog(DatabaseErrorCode.UPDATE_SUCCESS, null);
						mintEventLogs.add(mintEventLog);
					}catch(Exception e){
						if(e instanceof javax.persistence.PersistenceException && e.getCause().getCause().getMessage().contains("duplicate key value")){
							mintEventLog.setDatabaseLog(DatabaseErrorCode.DUPLICATE_KEY, e.getMessage());
							mintEventLogs.add(mintEventLog);
							
						}else{
							mintEventLog.setDatabaseLog(DatabaseErrorCode.UPDATE_ERROR, e.getMessage());
							mintEventLogs.add(mintEventLog);
							
						}
						
					}											
					
				}else{
					mintEventLog.setMintLog(MintErrorCode.PREFIX_UNREGISTERED, "The prefix is not registered to the user:" + user.getName());
					mintEventLog.setDatabaseLog(DatabaseErrorCode.NOT_ATTEMPTED, "");
					mintEventLogs.add(mintEventLog);
				}
			}
			
		}
		
		return new ResponseEntity<List<MintEventLog>>(mintEventLogs,HttpStatus.OK);
		
		

	
	}
	
	

}
