// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmExtendRetractCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.ArmRotationExtensionSingleton;
import frc.robot.pidcontroller.FeedForward;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase.RetractLimitSwitchState;
import frc.robot.telemetries.Trace;
import frc.robot.utils.InterpolatingMap;

public class ExtendRetract extends SequentialCommandGroup4905 {
  /** Creates a new ExtRetSeq. */
  public ExtendRetract(SamArmExtRetBase armExtRet, boolean needToEnd, boolean useSmartDashboard) {
    addCommands(new InitializeArmExtRet(armExtRet),
        new ExtendRetractInternal(armExtRet, needToEnd, useSmartDashboard));
  }

  public ExtendRetract(SamArmExtRetBase armExtRet, boolean useSmartDashboard) {
    this(armExtRet, true, true);
    SmartDashboard.putNumber("Extend Arm Position Value", 0);
    SmartDashboard.putNumber("Extend Arm P Value", 0);
    SmartDashboard.putNumber("Extend Arm I Value", 0);
    SmartDashboard.putNumber("Extend Arm D Value", 0);
    SmartDashboard.putNumber("Extend Arm minOutputToMove Value", 0);
  }

  public ExtendRetract(SamArmExtRetBase armExtRet) {
    this(armExtRet, true, false);
  }

  @Override
  public void additionalInitialize() {
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

  private class ExtendRetractInternal extends PIDCommand4905 {
    private SamArmExtRetBase m_armExtRet;
    private boolean m_needToEnd = false;
    private boolean m_useSmartDashboard = false;
    private double m_extendRetractJoystickPvalue = 0.001;
    private FeedForward m_feedForward = new ExtRetFeedForward();
    private InterpolatingMap m_kMap;

    public ExtendRetractInternal(SamArmExtRetBase armExtRet, boolean needToEnd,
        boolean useSmartDashboard) {

      super(new PIDController4905SampleStop("ArmExtRet"), armExtRet::getPosition, () -> 0,
          output -> {
            armExtRet.extendRetract(output);
          });
      addRequirements(armExtRet);
      m_armExtRet = armExtRet;
      m_needToEnd = needToEnd;
      m_useSmartDashboard = useSmartDashboard;

      // m_kMap = new
      // InterpolatingMap(Config4905.getConfig4905().getSamArmExtensionConfig(),
      // "armExtKValues");
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      m_armExtRet.disengageArmBrake();
      Config pidConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();
      super.initialize();
      double kP = 0;
      double kI = 0;
      double kD = 0;
      double minOutputToMove = 0;

      if (m_useSmartDashboard) {
        ArmRotationExtensionSingleton.getInstance()
            .setPosition(SmartDashboard.getNumber("Extend Arm Position Value", 0));
        kP = SmartDashboard.getNumber("Extend Arm P Value", 0);
        kI = SmartDashboard.getNumber("Extend Arm I Value", 0);
        kD = SmartDashboard.getNumber("Extend Arm D Value", 0);
        minOutputToMove = SmartDashboard.getNumber("Extend Arm minOutputToMove Value", 0);
      } else {
        kP = pidConstantsConfig.getDouble("ArmExtRet.Kp");
        kI = pidConstantsConfig.getDouble("ArmExtRet.Ki");
        kD = pidConstantsConfig.getDouble("ArmExtRet.Kd");
        minOutputToMove = pidConstantsConfig.getDouble("ArmExtRet.minOutputToMove");
      }
      setSetpoint(ArmRotationExtensionSingleton.getInstance().getPosition());

      getController().setP(kP);
      getController().setI(kI);
      getController().setD(kD);
      getController().setMinOutputToMove(minOutputToMove);
      getController().setTolerance(pidConstantsConfig.getDouble("ArmExtRet.tolerance"));
      getController().setMaxOutput(1);
      getController().setFeedforward(m_feedForward);
      Trace.getInstance().logCommandStart(this);
      Trace.getInstance().logCommandInfo(this,
          "Extend Retract Arm to: " + getController().getSetpoint());
    }

    @Override
    public void execute() {
      super.execute();
      double joystickValue = Robot.getInstance().getOIContainer().getSubsystemController()
          .getArmExtendRetractJoystickValue();
      if (DriverStation.isTeleop() && (joystickValue != 0)) {
        ArmRotationExtensionSingleton.getInstance()
            .setPosition(ArmRotationExtensionSingleton.getInstance().getPosition().getAsDouble()
                + (joystickValue * m_extendRetractJoystickPvalue));
      }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      super.end(interrupted);
      m_armExtRet.extendRetract(0);
      Trace.getInstance().logCommandStop(this);
      Trace.getInstance().logCommandInfo(this, "Ending Position: " + m_armExtRet.getPosition());
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      if (m_needToEnd && isOnTarget()) {
        return true;
      }
      return false;
    }

    public boolean isOnTarget() {
      return getController().atSetpoint();
    }
  }

  private class InitializeArmExtRet extends CommandBase {
    private SamArmExtRetBase m_armExtRetBase;

    /** Creates a new InitializeArmExtRet. */
    public InitializeArmExtRet(SamArmExtRetBase samArmExtRet) {
      m_armExtRetBase = samArmExtRet;
      addRequirements(m_armExtRetBase);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      m_armExtRetBase.disengageArmBrake();
      Trace.getInstance().logCommandStart(this);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
      if (!m_armExtRetBase.isInitialized()) {
        m_armExtRetBase.retractArmInitialize();
      }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      if (!m_armExtRetBase.isInitialized()) {
        m_armExtRetBase.setZeroOffset();
        m_armExtRetBase.setInitialized();
      }
      Trace.getInstance().logCommandStop(this);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return (m_armExtRetBase.getRetractLimitSwitchState() == RetractLimitSwitchState.CLOSED)
          || m_armExtRetBase.isInitialized();
    }
  }

  private class ExtRetFeedForward implements FeedForward {

    @Override
    public double calculate() {

      return 0;
    }

  }
}
