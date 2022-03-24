/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Config4905;
import frc.robot.commands.groupCommands.shooterFeederCommands.PickUpCargo;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootFender;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootLaunchPad;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootTarmac;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootWall;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * All subsystemController buttons get mapped here with descriptive names so
 * they are easier to find.
 */
public class SubsystemController extends ControllerBase {
  private SubsystemsContainer m_subsystemsContainer;

  public SubsystemController(SubsystemsContainer subsystemsContainer) {
    m_subsystemsContainer = subsystemsContainer;
    setController(new XboxController(1));
    if (Config4905.getConfig4905().doesIntakeExist()) {
      setUpIntakeButtons();
    }
    if (Config4905.getConfig4905().doesShooterExist()) {
      setupShooterButtons();
    }
  }

  public double getElevatorAdjustElevationStick() {
    return (getLeftStickForwardBackwardValue());
  }

  private void setUpIntakeButtons() {
    getRightBumperButton().whileHeld(new PickUpCargo(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment(), m_subsystemsContainer.getIntake(), false));
    getBackButton().whileHeld(new PickUpCargo(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment(), m_subsystemsContainer.getIntake(), true));
  }

  private void setupShooterButtons() {

    // Y = fender, X = launchpad, A = wall, B = tarmac
    getYbutton().whileHeld(new ShootFender(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment(), false));
    getXbutton().whileHeld(new ShootLaunchPad(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment(), false));
    getAbutton().whileHeld(new ShootWall(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment(), false));
    getBbutton().whileHeld(new ShootTarmac(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment(), false, false));
  }

  public boolean getRunIntakeButtonReleased() {
    return getRightBumperReleased();
  }

  public boolean getRunIntakeinReverseButtonReleased() {
    return getBackButtonReleased();
  }

  public boolean getShootLowHubButtonPressed() {
    if (getLeftBumperPressed()) {
      System.out.println("getShootLowHubButtonPressed True");
      return true;
    } else {
      System.out.println("getShootLowHubButtonPressed False");
      return false;
    }
  }

  public boolean getPauseFeederButtonPressed() {
    if (getLeftTriggerValue() > 0.3) {
      System.out.println("getPauseFeederButtonPressed True");
      return true;
    } else {
      System.out.println("getPauseFeederButtonPressed False");
      return false;
    }
  }
}
