package services.onboarding;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import pojos.Persona;
import pojos.PersonaData;
import pojos.PersonaMetadata;
import pojos.ProductFeature;

public interface PersonaService {

	public Response createPersona(Persona persona);
	public Response view(Integer personaId);
	public Response updatePersona(Persona persona);
	public Response deletePersona(Integer personaId);
	public Response createPersonaData(PersonaData personaData);
	public Response updatePersonaData(PersonaData personaData);
	public Response deletePersonaData(Integer personadataid);
 	public Response viewByUserId();
 	public Response creationFields();
 	public Response createPersonaMetaData(PersonaMetadata personaMetadata);
 	public Response updatePersonaMetaData(PersonaMetadata personaMetadata);
 	public Response deletePersonaMetaData(Integer personaMetadatId);
 	public Response createMapping(Persona persona);
  	public Response productFeatureInProductData(Integer personaDataId);
 	public Response addProductFeatureInNeedMapping(ArrayList<ProductFeature> productFeatures);
 	public Response metadataDataById(Integer metadatId);
 	
  }
