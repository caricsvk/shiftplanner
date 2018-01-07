package milo.shiftplanner.shifts;

import milo.utils.jpa.EntityService;
import milo.utils.jpa.search.TableSearchQuery;
import milo.utils.rest.EntityRestApi;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/shifts")
public class ShiftsResource implements EntityRestApi<Shift, Long> {

	@Inject
	private ShiftsService shiftsService;

	@Override
	public Shift create(@NotNull @Valid Shift entity) {
		return shiftsService.persist(entity);
	}

	@Override
	public Shift update(@NotNull @Valid Shift entity) {
		Shift shift = shiftsService.find(entity.getId());
		if (Shift.State.DEPLOYED.equals(shift.getState())) {
			throw new ForbiddenException();
		}
		return shiftsService.merge(entity);
	}

	@Override
	public void delete(@NotNull Long id) {
		Shift shift = shiftsService.find(id);
		if (Shift.State.DEPLOYED.equals(shift.getState())) {
			throw new ForbiddenException();
		}
		shiftsService.remove(id);
	}

	@Override
	public Shift read(@NotNull Long id) {
		return shiftsService.find(id);
	}

	@Override
	public EntityService.NumericValue count(@BeanParam TableSearchQuery tableSearchQuery) {
		return shiftsService.count(tableSearchQuery);
	}

	@Override
	public List<Shift> read(@BeanParam TableSearchQuery tableSearchQuery) throws InterruptedException {
		return shiftsService.search(tableSearchQuery);
	}

	@GET
	@Path("sum-duration")
	public EntityService.NumericValue sumDuration(@BeanParam TableSearchQuery tableSearchQuery) {
		return shiftsService.sumDuration(tableSearchQuery);
	}

}
