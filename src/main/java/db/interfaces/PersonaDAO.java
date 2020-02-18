package db.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import pojos.Persona;
import pojos.PersonaData;
import pojos.PersonaFields;
import pojos.PersonaMetadata;
import pojos.Product;
import pojos.ProductData;
import pojos.ProductFeature;
import pojos.User;

public interface PersonaDAO {

	public Persona createPersona(Persona persona, Integer userId) throws SQLException;

	public Persona updatePersona(Persona persona, Integer userId) throws SQLException;

	public Boolean deletePersona(Integer personaId) throws SQLException;

	public PersonaData createPersonaData(PersonaData personaData) throws SQLException;

	public PersonaData updatePersonaData(PersonaData personaData) throws SQLException;

	public Boolean deletePersonaData(Integer personadataid) throws SQLException;

	public ArrayList<Persona> viewPersonaByUserId(Integer userId) throws SQLException;

	public Persona findById(Integer personaId) throws SQLException;

	public PersonaFields getPersonaCreationFields(User user) throws SQLException;

	public PersonaMetadata createPersonaMetaData(PersonaMetadata personaMetadata) throws SQLException;

	public PersonaMetadata updatePersonaMetaData(PersonaMetadata personaMetadata) throws SQLException;

	public Boolean deletePersonaMetaData(Integer personaMeatadataid) throws SQLException;

	public String getPersonaName(int personaId) throws SQLException;

	public Persona getPersonaMappingData(Persona persona) throws NumberFormatException, SQLException;

	public Boolean createPersonaMapping(Persona persona) throws SQLException;

 
	public ArrayList<Product> personaDataProductFeature(Integer personadataId, Integer userI) throws SQLException;

	public Boolean addProductFeatureInNeedMapping(ArrayList<ProductFeature> productFeatures) throws SQLException;

	public PersonaMetadata getPersonaByMetadataId(Integer metadataId) throws SQLException;
 
}
