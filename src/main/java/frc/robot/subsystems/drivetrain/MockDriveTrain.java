/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;

public class MockDriveTrain extends DriveTrain {
  /**
   * Creates a new MockDriveTrain.
   */
  public MockDriveTrain() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void init() {
  }

  public void move(double forwardBackSpeed, double rotateAmount, boolean squaredInput) {
  }

  @Override
  public double getRobotPositionInches() {
    return 0;
  }

  @Override
  public double getRobotVelocityInches() {
    return 0;
  }

  @Override
  public void moveUsingGyro(double forwardBackward, double rotation, boolean useSquaredInputs,
      double heading) {
  }

  @Override
  public Pose2d getPose() {
    return new Pose2d(0, 0, new Rotation2d(0));
  }

  @Override
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(0, 0);
  }

  @Override
  public void tankDriveVolts(double leftVolts, double rightVolts) {

  }

  @Override
  public ParkingBrakeStates getParkingBrakeState() {
    return ParkingBrakeStates.BRAKESON;
  }

  @Override
  public void resetOdometry(Pose2d pose) {

  }

  @Override
  public void enableParkingBrakes() {

  }

  @Override
  public void disableParkingBrakes() {

  }

  @Override
  public double getLeftRateMetersPerSecond() {
    return 0;
  }

  @Override
  public double getRightRateMetersPerSecond() {
    return 0;
  }

  @Override
  public boolean hasParkingBrake() {
    return false;
  }

}
