// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmRotate;

import com.revrobotics.SparkMaxAbsoluteEncoder;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.subsystems.compressor.CompressorBase;

public class RealSamArmRotate extends SamArmRotateBase {

  private final SparkMaxController m_motor1;
  private SparkMaxAbsoluteEncoder m_armAngleEncoder;
  private double m_minAngle = 0;
  private double m_maxAngle = 0;
  private ArmAngleBrakeState m_armAngleBrakeState = ArmAngleBrakeState.ENGAGEARMBRAKE;
  private DoubleSolenoid4905 m_solenoidBrake;

  /** Creates a new RealSamArmRotate. */
  public RealSamArmRotate(CompressorBase compressorBase) {
    Config armrotateConfig = Config4905.getConfig4905().getSamArmRotateConfig();

    m_motor1 = new SparkMaxController(armrotateConfig, "motor1");
    m_solenoidBrake = new DoubleSolenoid4905(compressorBase, armrotateConfig, "solenoidbrake");
    m_armAngleEncoder = m_motor1.getAbsoluteEncoder(Type.kDutyCycle);
    m_maxAngle = armrotateConfig.getDouble("maxAngle");
    m_minAngle = armrotateConfig.getDouble("minAngle");
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("arm angle", getAngle());
    SmartDashboard.putString("Arm Brake State", getState().toString());
  }

  // Positive speed rotates to the back, negative to the front.
  @Override
  public void rotate(double speed) {
    if (m_armAngleBrakeState == ArmAngleBrakeState.ENGAGEARMBRAKE) {
      m_motor1.setSpeed(0);
      return;
    }
    if ((speed < 0) && (getAngle() <= m_minAngle)) {
      m_motor1.setSpeed(0);
    } else if ((speed > 0) && (getAngle() >= m_maxAngle)) {
      m_motor1.setSpeed(0);
    } else {
      m_motor1.setSpeed(speed * 0.63);
    }
  }

  @Override
  public void engageArmBrake() {
    m_solenoidBrake.retractPiston();
    m_armAngleBrakeState = ArmAngleBrakeState.ENGAGEARMBRAKE;
    m_motor1.setSpeed(0);
  }

  @Override
  public void disengageArmBrake() {
    m_solenoidBrake.extendPiston();
    m_armAngleBrakeState = ArmAngleBrakeState.DISENGAGEARMBRAKE;
  }

  // 90 Degrees is pointing forward, 270 is pointing backwards
  @Override
  public double getAngle() {
    double fixedEncoderValue = (1.56 - m_armAngleEncoder.getPosition());
    if (fixedEncoderValue >= 1) {
      fixedEncoderValue = fixedEncoderValue - 1;
    }
    return fixedEncoderValue * 360;
  }

  public ArmAngleBrakeState getState() {
    return m_armAngleBrakeState;
  }

}
