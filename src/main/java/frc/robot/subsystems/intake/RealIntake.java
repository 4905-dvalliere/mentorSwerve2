// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.actuators.TalonSRXController;

public class RealIntake extends IntakeBase {
  private TalonSRXController m_intakeController;
  private DoubleSolenoid4905 m_intakeDoubleSolenoid4905;

  /** Creates a new RealIntake. */
  public RealIntake() {
    Config intakeConfig = Config4905.getConfig4905().getIntakeConfig();
    m_intakeController = new TalonSRXController(intakeConfig, "IntakeSRXController");
    m_intakeDeploymentSolenoid = new DoubleSolenoid4905(intakeConfig, "IntakeSolonoid");
  }

  public void runIntake(double speed) {
    // Run intake motors to bring game piece (ball) into robot
    m_intakeController.set(speed)
  }

  public void stopIntake() {
    // Stop intake motors
    m_intakeController.stopMotor();
  }  

  @Override
  public void deployIntake() {
    m_intakeDeploymentSolenoid.extendPiston();
    System.out.println("Deploying Intake");
  }

  @Override
  public void retractIntake() {
    m_intakeDoubleSolenoid4905.retractPiston();
  }  
  
//  TBD - The periodic() call is in the new subsystem base, but not in 4905 base/real/mock
//  @Override
//  public void periodic() {
    // This method will be called once per scheduler run

}
