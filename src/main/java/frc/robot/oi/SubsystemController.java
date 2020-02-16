/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.groupcommands.parallelgroup.ShooterParallelSetShooterVelocity;
import frc.robot.lib.ButtonsEnumerated;
import frc.robot.lib.POVDirectionNames;
import frc.robot.subsystems.shooter.ShooterBase;

/**
 * Add your docs here.
 */
public class SubsystemController {
  private XboxController m_subsystemController = new XboxController(1);
  private Config m_shooterConfig;
  private ShooterBase m_shooterSubsystem;
  private JoystickButton m_shootFromInitLine;
  private JoystickButton m_shootFromFrontTrench;
  private JoystickButton m_shootFromBackTrench;
  private JoystickButton m_shootFromTargetZone;
  private JoystickButton m_runIntakeOut;

  public SubsystemController() {
    m_shooterConfig = Config4905.getConfig4905().getShooterConfig();
    m_shooterSubsystem = Robot.getInstance().getSubsystemsContainer().getShooter();

    m_shootFromInitLine = new JoystickButton(m_subsystemController, ButtonsEnumerated.XBUTTON.getValue());
    m_shootFromInitLine.whileHeld(new ShooterParallelSetShooterVelocity(m_shooterSubsystem,
        m_shooterConfig.getDouble("shootingrpm.initline") * 1.5, m_shooterConfig.getDouble("shootingrpm.initline")));
    m_shootFromFrontTrench = new JoystickButton(m_subsystemController, ButtonsEnumerated.BBUTTON.getValue());
    m_shootFromFrontTrench.whileHeld(new ShooterParallelSetShooterVelocity(m_shooterSubsystem,
        m_shooterConfig.getDouble("shootingrpm.fronttrench") * 1.5,
        m_shooterConfig.getDouble("shootingrpm.fronttrench")));
    m_shootFromBackTrench = new JoystickButton(m_subsystemController, ButtonsEnumerated.ABUTTON.getValue());
    m_shootFromBackTrench.whileHeld(new ShooterParallelSetShooterVelocity(m_shooterSubsystem,
        m_shooterConfig.getDouble("shootingrpm.backtrench") * 1.5,
        m_shooterConfig.getDouble("shootingrpm.backtrench")));
    m_shootFromTargetZone = new JoystickButton(m_subsystemController, ButtonsEnumerated.YBUTTON.getValue());
    m_shootFromTargetZone.whileHeld(new ShooterParallelSetShooterVelocity(m_shooterSubsystem,
        m_shooterConfig.getDouble("shootingrpm.targetzone") * 1.5,
        m_shooterConfig.getDouble("shootingrpm.targetzone")));
    m_runIntakeOut = new JoystickButton(m_subsystemController, ButtonsEnumerated.BACKBUTTON.getValue());
  }

  public JoystickButton getDeployAndRunIntakeButton() {
    return ButtonsEnumerated.LEFTBUMPERBUTTON.getJoystickButton(m_subsystemController);
  }

  public JoystickButton getRunIntakeOutButton() {
    return m_runIntakeOut;
  }

  public JoystickButton getFeedWhenReadyButton() {
    return ButtonsEnumerated.RIGHTBUMPERBUTTON.getJoystickButton(m_subsystemController);
  }

  public double getLeftStickForwardBackwardValue() {
    return deadband(-m_subsystemController.getY(GenericHID.Hand.kLeft));
  }

  public double getRightStickForwardBackwardValue() {
    return deadband(-m_subsystemController.getY(GenericHID.Hand.kRight));
  }

  private double deadband(double stickValue) {
    if (Math.abs(stickValue) < 0.05) {
      return 0.0;
    } else {
      return stickValue;
    }
  }
}
