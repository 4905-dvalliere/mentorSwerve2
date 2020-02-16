/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import static frc.robot.sensors.ballfeedersensor.EnumBallLocation.*;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.sensors.ballfeedersensor.BallFeederSensorBase;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.feeder.FeederStates;

public class DefaultFeederCommand extends CommandBase {

  BallFeederSensorBase m_feederSensor;
  FeederBase m_feeder;
  private FeederStates m_feederState;

  /**
   * Creates a new FeederCommand.
   */
  public DefaultFeederCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getInstance().getSubsystemsContainer().getFeeder());
    this.m_feeder = Robot.getInstance().getSubsystemsContainer().getFeeder();
    m_feederSensor = Robot.getInstance().getSensorsContainer().getBallFeederSensor();
    m_feederState = FeederStates.EMPTY;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.getInstance().getSubsystemsContainer().getShooter().closeShooterHood();
    m_feederState = FeederStates.EMPTY;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch(m_feederState){
      case EMPTY:
        if(m_feederSensor.isBall(STAGE_1_LEFT) || m_feederSensor.isBall(STAGE_1_RIGHT) || m_feederSensor.isBall(STAGE_1_END)) {
          m_feederState = FeederStates.ONE_LOADING;
        }
        m_feeder.stopBothStages();
        break;
      
      case ONE_LOADING:
        if(m_feederSensor.isBall(STAGE_2_BEGINNING_MIDDLE) && !m_feederSensor.isBall(STAGE_2_BEGINNING)) {
          m_feederState = FeederStates.ONE_LOADED;
        }
        m_feeder.driveBothStages();
        break;
      
      case ONE_LOADED:
        if(m_feederSensor.isBall(STAGE_1_LEFT) || m_feederSensor.isBall(STAGE_1_RIGHT) || m_feederSensor.isBall(STAGE_1_END)) {
          m_feederState = FeederStates.SECOND_LOADING_1;
        }
        m_feeder.stopBothStages();
        break;
      
      case SECOND_LOADING_1:
        if(m_feederSensor.isBall(STAGE_2_BEGINNING)) {
          m_feederState = FeederStates.SECOND_LOADING_2;
        }
        m_feeder.driveStageOne();
        m_feeder.stopStageTwo();
        break;

      case SECOND_LOADING_2:
        if(m_feederSensor.isBall(STAGE_2_BEGINNING_MIDDLE) && m_feederSensor.isBall(STAGE_2_END_MIDDLE) && !m_feederSensor.isBall(STAGE_2_BEGINNING)) {
          m_feederState = FeederStates.SECOND_LOADED;
        }
        m_feeder.driveBothStages();
        break;

      case SECOND_LOADED:
        m_feeder.stopBothStages();
        break;
      
      default:
        m_feeder.stopBothStages();
        break;
    }

    System.out.println("m_feederState: " + m_feederState );

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_feeder.stopBothStages();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
