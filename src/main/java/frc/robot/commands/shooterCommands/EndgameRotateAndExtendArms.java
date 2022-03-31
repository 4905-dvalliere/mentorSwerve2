// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooterCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class EndgameRotateAndExtendArms extends SequentialCommandGroup {
  private final double m_shooterAngle = 25;

  /** Creates a new EngameRotateAndExtendArms. */
  public EndgameRotateAndExtendArms(ShooterAlignmentBase shooterAlignmentBase) {

    addCommands(new MoveShooterAlignment(shooterAlignmentBase, () -> m_shooterAngle),
        new ExtendShooterArms(shooterAlignmentBase));
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    super.initialize();
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    Trace.getInstance().logCommandStop(this);
  }
}
