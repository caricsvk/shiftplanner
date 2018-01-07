package milo.shiftplanner;

import milo.shiftplanner.shifts.Shift;
import milo.shiftplanner.shifts.ShiftsService;
import org.springframework.scheduling.annotation.Scheduled;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
public class Scheduler {

    private final Logger logger = Logger.getLogger(Scheduler.class.getName());

    @Inject
    private ShiftsService shiftsService;

    @Scheduled(fixedRate = 60*1000) // every minute
    protected void deployNewerShift() {
        Shift firstOverlappedShift = shiftsService.findFirsOverlapped();
//        System.out.println("ShiftsService.deployNewerShift ------------ " + firstOverlappedShift);
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
