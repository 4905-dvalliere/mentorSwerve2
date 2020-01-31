package frc.robot.subsystems.shooter;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import frc.robot.Config4905;
import frc.robot.actuators.*;

public class RealShooter extends ShooterBase {

  private SparkMaxController m_shooterOne;
  private SparkMaxController m_shooterTwo;
  private SpeedControllerGroup m_shooterGroup;
  private DoubleSolenoid4905 m_shooterHoodOne;
  private DoubleSolenoid4905 m_shooterHoodTwo;
  // TODO Add Talon And pneumatic Controllers

  public RealShooter() {
    Config shooterConfig = Config4905.getConfig4905().getShooterConfig();

    m_shooterOne = new SparkMaxController(shooterConfig, "shooterone");

    m_shooterTwo = new SparkMaxController(shooterConfig, "shootertwo");

    m_shooterGroup = new SpeedControllerGroup(m_shooterOne, m_shooterTwo);

    m_shooterHoodOne = new DoubleSolenoid4905(shooterConfig, "hoodone");

    m_shooterHoodTwo = new DoubleSolenoid4905(shooterConfig, "hoodtwo");

  }

  @Override
  public double getShooterVelocity() {
    // Averaging Both Motor Controllers
    double average = m_shooterOne.getEncoderVelocityTicks() + m_shooterTwo.getEncoderVelocityTicks();
    return average / 2;
  }

  @Override
  public void setShooterPower(double power) {
    m_shooterGroup.set(power);
  }

  @Override
  public double getShooterPower() {
    return m_shooterGroup.get();
  }

  @Override
  public void openShooterHood() {
    m_shooterHoodOne.extendPiston();
    m_shooterHoodTwo.extendPiston();
  }

  @Override
  public void closeShooterHood() {
    m_shooterHoodOne.retractPiston();
    m_shooterHoodTwo.retractPiston();
  }

}