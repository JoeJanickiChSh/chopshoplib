package com.chopshop166.chopshoplib.commands;

import static edu.wpi.first.wpilibj2.command.Commands.*;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ProxyCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

/**
 * Utility class for storing command helpers.
 */
final public class CommandUtils {

    private CommandUtils() {}

    /**
     * Repeat a {@link Command} a given number of times.
     *
     * @param numTimesToRun The number of times to run the command.
     * @param cmd The command to repeat.
     * @return A newly constructed command.
     */
    public static CommandBase repeat(final int numTimesToRun, final Command cmd) {
        return repeat(numTimesToRun, () -> new ProxyCommand(cmd));
    }

    /**
     * Repeat a {@link Command} a given number of times.
     *
     * @param numTimesToRun The number of times to run the command.
     * @param cmd A way to create the command to repeat.
     * @return A newly constructed command group.
     */
    public static CommandBase repeat(final int numTimesToRun, final Supplier<Command> cmd) {
        return sequence(Stream.generate(cmd).limit(numTimesToRun).toArray(Command[]::new));
    }

    /**
     * Create a command builder with a given name.
     *
     * @param requirements The subsystems that the command needs (can be empty).
     * @return A new command builder.
     */
    public static BuildCommand cmd(final Subsystem... requirements) {
        return new BuildCommand(requirements);
    }

    /**
     * Create a command builder with a given name.
     *
     * @param name The command name.
     * @param requirements The subsystems that the command needs (can be empty).
     * @return A new command builder.
     */
    public static BuildCommand cmd(final String name, final Subsystem... requirements) {
        return new BuildCommand(name, requirements);
    }

    /**
     * Run a {@link Runnable} and then wait until a condition is true.
     *
     * @param init The action to take.
     * @param until The condition to wait until.
     * @return A new command.
     */
    public static CommandBase initAndWait(final Runnable init, final BooleanSupplier until) {
        return parallel(runOnce(init), new WaitUntilCommand(until));
    }

    /**
     * Create a command to call a consumer function.
     *
     * @param <T> The type to wrap.
     * @param value The value to call the function with.
     * @param func The function to call.
     * @return A new command.
     */
    public static <T> CommandBase setter(final T value, final Consumer<T> func) {
        return runOnce(() -> {
            func.accept(value);
        });
    }

    /**
     * Create a command that sets a motor to a speed while the command is running.
     *
     * @param value The value to set the motor to.
     * @param motor The motor to use.
     * @return A new command.
     */
    public static CommandBase runWhile(final double value, final MotorController motor) {
        return startEnd(() -> {
            motor.set(value);
        }, motor::stopMotor);
    }

    /**
     * Create a command to call a consumer function and wait.
     *
     * @param <T> The type to wrap.
     * @param value The value to call the function with.
     * @param func The function to call.
     * @param until The condition to wait until.
     * @return A new command.
     */
    public static <T> CommandBase callAndWait(final T value, final Consumer<T> func,
            final BooleanSupplier until) {
        return initAndWait(() -> {
            func.accept(value);
        }, until);
    }

    /**
     * Create a command that runs only if a condition is true.
     * 
     * @param condition The condition to test beforehand.
     * @param cmd The command to run.
     * @return The conditional command.
     */
    public static CommandBase runIf(final BooleanSupplier condition, final Command cmd) {
        return either(cmd, none(), condition);
    }

    /**
     * Create a command that selects which command to run from a function.
     *
     * @param selector The function to determine which command should be run.
     * @return The wrapper command object.
     */
    public static CommandBase select(final Supplier<Command> selector) {
        return new ProxyCommand(selector);
    }

    /**
     * Create a command to run at regular intervals.
     * 
     * @param timeDelta Time in seconds to wait between calls.
     * @param periodic The runnable to execute.
     * @return A new command.
     */
    public static CommandBase every(final double timeDelta, final Runnable periodic) {
        return new IntervalCommand(timeDelta, periodic);
    }

    /**
     * Create a command that waits for the duration provided by a DoubleSupplier
     * 
     * @param durationSupplier Function that returns the number of seconds to wait
     * @return The wait command
     */
    public static CommandBase waitFor(final DoubleSupplier durationSupplier) {
        return new FunctionalWaitCommand(durationSupplier);
    }

}
