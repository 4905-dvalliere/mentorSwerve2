// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.shooterFeederCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.feederCommands.RunFeeder;
import frc.robot.commands.intakeCommands.DeployAndRunIntake;
import frc.robot.commands.shooterCommands.MoveShooterAlignment;
import frc.robot.commands.shooterCommands.RunShooterRPM;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.subsystems.shooter.ShooterWheelBase;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class UnstickCargo extends SequentialCommandGroup {
  /** Creates a new UnstickCargo. */
  public UnstickCargo(FeederBase feeder, ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel, ShooterAlignmentBase shooterAlignment,
      IntakeBase intakeBase) {

    final double m_shooterSetpoint = -1000.00;
    final double m_feederSetpoint = 0.5;
    final double m_shooterAngle = 25.0;

    addCommands(new ParallelCommandGroup(
        new RunShooterRPM(topShooterWheel, bottomShooterWheel, () -> m_shooterSetpoint),
        new RunFeeder(feeder, () -> m_feederSetpoint, true, () -> false),
        new DeployAndRunIntake(intakeBase, true),
        new MoveShooterAlignment(shooterAlignment, () -> m_shooterAngle)));
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    super.initialize();
  }

  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    super.end(interrupted);
  }
}
