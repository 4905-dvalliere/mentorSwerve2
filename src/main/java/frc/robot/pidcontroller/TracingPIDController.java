package frc.robot.pidcontroller;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;

public class TracingPIDController extends PIDController {
  private String m_name;

  public TracingPIDController(String name, double p, double i, double d) {
    super(p, i, d);
    m_name = name;
  }

  @Override
  public double calculate(double measurement) {
    double output = super.calculate(measurement);
    Trace.getInstance().addTrace(true, m_name, new TracePair("Measurement", measurement),
        new TracePair("Setpoint", super.getSetpoint()));
    return output;
  }
}