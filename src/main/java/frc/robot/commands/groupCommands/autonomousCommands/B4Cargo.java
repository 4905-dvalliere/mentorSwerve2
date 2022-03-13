// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.Timer;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.commands.groupCommands.shooterFeederCommands.PickUpCargo;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootCargo;
import frc.robot.commands.shooterCommands.MoveShooterAlignment;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.subsystems.shooter.ShooterWheelBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class B4Cargo extends SequentialCommandGroup {
  /** Creates a new B4Cargo. */
  public B4Cargo() {
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    FeederBase feeder = subsystemsContainer.getFeeder();
    ShooterWheelBase topShooterWheel = subsystemsContainer.getTopShooterWheel();
    ShooterWheelBase bottomShooterWheel = subsystemsContainer.getBottomShooterWheel();
    ShooterAlignmentBase shooterAlignment = subsystemsContainer.getShooterAlignment();
    IntakeBase intakeBase = subsystemsContainer.getIntake();
    final double shooterSetpoint = 5000;
    final double shooterAngle = 71.0;
    final double feederSetpoint = 1.0;
    long pickUpWaitTime = 500;
    long shootWaitTime = 3300;

    addCommands(new BHighHub2(), new TurnToCompassHeading(164),

        new ParallelDeadlineGroup(new MoveUsingEncoder(driveTrain, 157, 1),
            new PickUpCargo(feeder, topShooterWheel, bottomShooterWheel, shooterAlignment,
                intakeBase, false)),
        new ParallelDeadlineGroup(new Timer(pickUpWaitTime),
            new PickUpCargo(feeder, topShooterWheel, bottomShooterWheel, shooterAlignment,
                intakeBase, false),
            new PauseRobot(pickUpWaitTime, driveTrain)),
        new ParallelCommandGroup(new TurnToCompassHeading(155),
            new MoveShooterAlignment(shooterAlignment, () -> shooterAngle)),
        new ParallelDeadlineGroup(new Timer(shootWaitTime),
            new ShootCargo(feeder, topShooterWheel, bottomShooterWheel, shooterAlignment,
                () -> shooterSetpoint, () -> shooterAngle, () -> feederSetpoint),
            new PauseRobot(shootWaitTime, driveTrain)));

  }
}
