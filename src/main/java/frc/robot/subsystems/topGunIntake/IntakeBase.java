// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.topGunIntake;

import frc.robot.subsystems.SubsystemInterface;

public interface IntakeBase extends SubsystemInterface {
  public abstract void runIntakeWheels(double speed);

  public abstract void stopIntakeWheels();

  public abstract void deployIntake();

  public abstract void retractIntake();
}
