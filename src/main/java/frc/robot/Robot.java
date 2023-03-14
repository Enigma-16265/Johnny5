// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class. If you change the name of this class or the
 * package after creating this project, you must also update the build.gradle file in the project.
 */
public class Robot extends TimedRobot {

  private static final Logger log = LogManager.getLogger( Robot.class );
  
  private RobotContainer robotContainer;
  
  @Override
  public void robotInit()
  {
      log.info( "roboInit" );
      robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void testInit() {
    log.info( "testInit" );
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void teleopPeriodic() {
    robotContainer.modeCheck();
  }

  @Override
  public void simulationInit() {
    log.info( "simulationInit" );
    robotContainer.simulationInit();
  }

  @Override
  public void simulationPeriodic()
  {
    robotContainer.simulationPeriodic();
  }

  @Override
  public void autonomousInit()
  {
    log.info( "autonomousInit" );
  }

  @Override
  public void autonomousPeriodic()
  {
    
  }

}