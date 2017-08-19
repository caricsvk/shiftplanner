package milo.shiftplanner.agents;

import milo.shiftplanner.shifts.Shift;
import milo.shiftplanner.shifts.ShiftsService;
import milo.utils.jpa.EntityService;
import milo.utils.jpa.search.TableSearchQuery;
import milo.utils.rest.EntityRestApi;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.util.List;

@Path("/agents")
public class AgentsResource implements EntityRestApi<Agent, Long> {

	@Inject
	private AgentsService agentsService;
	@Inject
	private ShiftsService shiftsService;

	@PUT
	@Path("activate")
	public void activateAgent(@NotNull @Valid Agent agent) {
		Shift shift = new Shift();
		shift.setAgent(agent);
		shiftsService.deployShift(shift);
	}

	@Override
	public Agent create(@NotNull @Valid Agent entity) {
		return agentsService.persist(entity);
	}

	@Override
	public Agent update(@NotNull @Valid Agent entity) {
		Agent storedAgent = agentsService.find(entity.getId());
		if (! storedAgent.getName().equals(entity.getName())) {
			throw new ForbiddenException();
		}
		return agentsService.merge(entity);
	}

	@Override
	public void delete(@NotNull Long aLong) {
		throw new ForbiddenException();
	}

	@Override
	public Agent read(@NotNull Long id) {
		return agentsService.find(id);
	}

	@Override
	public EntityService.NumericValue count(@BeanParam TableSearchQuery tableSearchQuery) {
		return agentsService.count(tableSearchQuery);
	}

	@Override
	public List<Agent> read(@BeanParam TableSearchQuery tableSearchQuery) throws InterruptedException {
		return agentsService.search(tableSearchQuery);
	}

}