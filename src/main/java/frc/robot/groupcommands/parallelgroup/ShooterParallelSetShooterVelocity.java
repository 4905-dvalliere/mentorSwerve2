package frc.robot.groupcommands.parallelgroup;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.pidcommands.RunShooterSeriesVelocity;
import frc.robot.commands.pidcommands.RunShooterWheelVelocity;
import frc.robot.oi.SubsystemController;
import frc.robot.subsystems.shooter.ShooterBase;

public class ShooterParallelSetShooterVelocity extends ParallelCommandGroup {

    /**
     * This should only be run on a while held
     * Because there is no isFinished 
     * @param shooter
     * @param controller
     * Requires a controller to allow the subsystem driver to tune the PID setpoint via
     * the controller
     * @param seriesRPM
     * @param shooterRPM
     */
    public ShooterParallelSetShooterVelocity(ShooterBase shooter, SubsystemController controller, double seriesRPM, double shooterRPM) {

        addCommands(new RunShooterWheelVelocity(shooter, controller, shooterRPM),
            new RunShooterSeriesVelocity(shooter, controller, seriesRPM));

        addRequirements(shooter);
    }

}