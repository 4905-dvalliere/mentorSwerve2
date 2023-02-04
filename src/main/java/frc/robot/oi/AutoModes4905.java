package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.commands.groupCommands.autonomousCommands.AutoPlaceAndLeave;
import frc.robot.commands.groupCommands.autonomousCommands.AutoPlaceLeaveAndPickup;
import frc.robot.commands.groupCommands.autonomousCommands.EngageAutoDock;
import frc.robot.commands.groupCommands.autonomousCommands.TaxiAuto;
import frc.robot.commands.groupCommands.romiCommands.AllianceAnticsScoring;
import frc.robot.commands.groupCommands.romiCommands.AllianceAnticsSimple;
import frc.robot.commands.groupCommands.topGunAutonomousCommands.DoNothingAuto;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class AutoModes4905 {
  static SendableChooser<Command> m_autoChooser;

  public static void initializeAutoChooser(SubsystemsContainer subsystemsContainer,
      SensorsContainer sensorsContainer, SendableChooser<Command> autoChooser) {
    m_autoChooser = autoChooser;
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();

    m_autoChooser.setDefaultOption("DoNothing", new DoNothingAuto());

    if (Config4905.getConfig4905().isRomi()) {
      m_autoChooser.addOption("1: Simple Park", new AllianceAnticsSimple(driveTrain));
      m_autoChooser.addOption("2: Scoring And Park", new AllianceAnticsScoring(driveTrain));
    }
    // This line of code will need to be changed to check for the S.A.M. Robot
    if (Config4905.getConfig4905().isTopGun() || Config4905.getConfig4905().isShowBot()) {
      m_autoChooser.addOption("1: Taxi", new TaxiAuto());
      m_autoChooser.addOption("2: DockEngage", new EngageAutoDock());
      m_autoChooser.addOption("3: PlaceLeaveDockEngage", new AutoPlaceAndLeave());
      m_autoChooser.addOption("4: PlaceLeavePickupDockEngage", new AutoPlaceLeaveAndPickup());
    }
    SmartDashboard.putData("autoModes", m_autoChooser);
  }

}