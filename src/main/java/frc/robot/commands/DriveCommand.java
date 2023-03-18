package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.RobotDrive;

public class DriveCommand extends CommandBase{
    private final RobotDrive       robotDrive;
    private final Supplier<Double> leftXAxisInput;
    private final Supplier<Double> leftYAxisInput;
    private final Supplier<Double> rightXAxisInput;
    private final Supplier<Double> rightYAxisInput;

    public DriveCommand(
        RobotDrive       robotDrive,
        Supplier<Double> leftXAxisInput,
        Supplier<Double> leftYAxisInput,
        Supplier<Double> rightXAxisInput,
        Supplier<Double> rightYAxisInput )
    {
        this.robotDrive = robotDrive;
        this.leftXAxisInput  = leftXAxisInput;
        this.leftYAxisInput  = leftYAxisInput;
        this.rightXAxisInput = rightXAxisInput;
        this.rightYAxisInput = rightYAxisInput;
        addRequirements( robotDrive );
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        robotDrive.drive( leftXAxisInput.get(),
                          leftYAxisInput.get(),
                          rightXAxisInput.get(),
                          rightYAxisInput.get() );
    }

    @Override
    public void end( boolean interrupted ) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
