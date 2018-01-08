package milo.shiftplanner;

import milo.shiftplanner.shifts.Shift;
import milo.shiftplanner.shifts.ShiftsService;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class Scheduler {

    private final Logger logger = Logger.getLogger(Scheduler.class.getName());

    @Inject
    private ShiftsService shiftsService;

    @Schedule(minute = "*", hour = "*", persistent = false) // every minute
    protected void deployNewerShift() {
        Shift firstOverlappedShift = shiftsService.findFirsOverlapped();
        if (firstOverlappedShift == null) {
            BaseResource.lastShiftDeploymentException = null;
            return;
        }
        try {
            shiftsService.deployShift(firstOverlappedShift);
            BaseResource.lastShiftDeploymentException = null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            BaseResource.lastShiftDeploymentException = e;
        }
    }
}
