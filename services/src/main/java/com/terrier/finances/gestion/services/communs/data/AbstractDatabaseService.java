/**
 * 
 */
package com.terrier.finances.gestion.services.communs.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.terrier.finances.gestion.services.statut.business.StatusApplicationService;
import com.terrier.finances.gestion.services.statut.model.DependencyName;
import com.terrier.finances.gestion.services.statut.model.StatutStateEnum;

/**
 * DataServices
 * @author vzwingma
 *
 */
public abstract class AbstractDatabaseService {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDatabaseService.class);

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private StatusApplicationService statutApplicationService;
	
	/**
	 * Constructeur permettant de définir les composants utilisés en DATA
	 */
	public AbstractDatabaseService(){
		LOGGER.info("[INIT] Service {}", this.getClass());
	}

	
	/**
	 * @return opérations MongoDB
	 */
	public MongoOperations getMongoOperation(){
		updateMongoStatus();
		return mongoTemplate;
	}
	
	
	/**
	 * 
	 */
	private void updateMongoStatus(){
        StatutStateEnum statutDB = mongoTemplate.getDb().getName() != null ? StatutStateEnum.OK : StatutStateEnum.FATAL;
        LOGGER.trace("Statut DB : {} -> {}", mongoTemplate.getDb().getName(), statutDB);        
		statutApplicationService.updateDependencyStatut(DependencyName.DATABASE, statutDB);
	}
}