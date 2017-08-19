package milo.shiftplanner.agents;

import milo.utils.jpa.EntityService;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@Named
public class AgentsService extends EntityService<Agent, Long> {

	private static final Logger LOGGER = Logger.getLogger(AgentsService.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	public AgentsService() {
		super(Agent.class, Long.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

}
