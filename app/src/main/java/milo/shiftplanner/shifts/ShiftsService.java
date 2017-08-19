package milo.shiftplanner.shifts;

import milo.shiftplanner.BaseResource;
import milo.shiftplanner.mail.EmailService;
import milo.utils.jpa.EntityService;
import milo.utils.jpa.search.TableSearchQuery;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Named
public class ShiftsService extends EntityService<Shift, Long> {

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private EmailService emailService;

	public ShiftsService() {
		super(Shift.class, Long.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	@Transactional
	public void deployShift(Shift shift) {
		LocalDateTime switchDateTime = LocalDateTime.now();
		Shift currentShift = getCurrentShift();
		if (currentShift != null) {
			currentShift.setEnd(switchDateTime);
			merge(currentShift);
		}
		shift.setStart(switchDateTime);
		shift.setState(Shift.State.DEPLOYED);
		merge(shift);
		try {
			emailService.send(currentShift.getAgent().getEmail(),
					"Your shift ends", "Hurray, you are free now! :)");
			emailService.send(shift.getAgent().getEmail(),
					"Your shift just started", "Good luck!");
			BaseResource.emailSentException = null;
		} catch (Exception ex) { // do not rollback if e-mail sending fails
			BaseResource.emailSentException = ex;
		}
	}

	public Shift getCurrentShift() {
		try {
			return entityManager.createNamedQuery(Shift.FIND_BY_STATE, Shift.class)
					.setParameter("state", Shift.State.DEPLOYED)
					.setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	public Shift findFirsOverlapped() {
		try {
			return entityManager.createNamedQuery(Shift.FIND_OVERLAPPED, Shift.class)
					.setParameter("state", Shift.State.PLANNED)
					.setParameter("now", new Timestamp(System.currentTimeMillis()))
					.setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	public NumericValue sumDuration(TableSearchQuery tableSearchQuery) {
		return this.sum("duration", tableSearchQuery);
	}

}
