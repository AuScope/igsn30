package org.csiro.igsn.api.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.csiro.igsn.exception.MintErrorCode;
import org.csiro.igsn.exception.MintEventLog;
import org.csiro.igsn.jaxb.registration.bindings.EventType;
import org.csiro.igsn.jaxb.registration.bindings.Resources;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource;
import org.csiro.igsn.service.MintService;
import org.csiro.igsn.utilities.IGSNDateUtil;
import org.csiro.igsn.utilities.IGSNUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@RestController
@RequestMapping(value = "/api/igsn/30")
public class IGSNMintCtrl {
	
	final Logger log = Logger.getLogger(IGSNMintCtrl.class);
	ResourceEntityService resourceEntityService;
	MintService mintService;
	PrefixEntityService prefixEntityService;	
	ControlledValueEntityService controlledValueEntityService;
	RegistrantEntityService registerantEntityService;

	
	@Value("#{configProperties['IGSN_CSIRO_XSD_URL']}")
	private String IGSN_CSIRO_XSD_URL;

	@Value("#{configProperties['IGSN_PREFIX']}")
	private String IGSN_PREFIX;
	
	
	@Autowired
	public IGSNMintCtrl(ResourceEntityService resourceEntityService,MintService mintService,PrefixEntityService prefixEntityService,
			ControlledValueEntityService controlledValueEntityService,
			RegistrantEntityService registerantEntityService){
		this.resourceEntityService = resourceEntityService;
		this.mintService = mintService;
		this.prefixEntityService = prefixEntityService;
		this.controlledValueEntityService = controlledValueEntityService;
		this.registerantEntityService = registerantEntityService;
	}
	

	
	
	@RequestMapping(value = "/test/mint", method = { RequestMethod.POST, RequestMethod.HEAD })
	public  ResponseEntity<?> mintTest(@RequestBody Resources resources,Principal user) {
		ResponseEntity<?> result=this.mint(resources,true,user);
		return result;
	}
	
	@RequestMapping(value = "/mint", method = { RequestMethod.POST, RequestMethod.HEAD } )
	public  ResponseEntity<?> mint(@RequestBody Resources resources,Principal user) {
		
		return this.mint(resources,false,user);
		
	}
	
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
			return new ResponseEntity<String>("XML validation failed : " + e.getLocalizedMessage(),
					HttpStatus.BAD_REQUEST);			
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
							resourceEntityService.insertResource(r,registrant,false);
						}else if(r.getLogDate().getEventType().equals(EventType.DESTROYED)){
							resourceEntityService.destroyResource(r,false);
						}else if(r.getLogDate().getEventType().equals(EventType.DEPRECATED)){
							resourceEntityService.deprecateResource(r,false);
						}else if(r.getLogDate().getEventType().equals(EventType.UPDATED)){
							resourceEntityService.updateResource(r,registrant,false);
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
