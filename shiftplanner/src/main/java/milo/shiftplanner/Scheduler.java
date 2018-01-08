package milo.shiftplanner;

import milo.shiftplanner.shifts.Shift;
import milo.shiftplanner.shifts.ShiftsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class Scheduler {

    private final Logger logger = Logger.getLogger(Scheduler.class.getName());

    @Inject
    private ShiftsService shiftsService;

    @Scheduled(fixedDelay = 60*1000) // every minute
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
