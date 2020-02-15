package com.chopshop166.chopshoplib.outputs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * {@link SpeedController} that has modifiers applied to it.
 *
 * This is a generic system that can support many changes to speed.
 */
public class ModSpeedController implements SendableSpeedController {

    final private SendableSpeedController wrapped;
    final private List<Modifier> modifiers;

    /**
     * Wrap a speed controller in limits.
     * 
     * @param wrapped   The speed controller to limit.
     * @param modifiers Modifiers to use by default.
     */
    public ModSpeedController(final SendableSpeedController wrapped, final Modifier... modifiers) {
        super();
        this.wrapped = wrapped;
        this.modifiers = new ArrayList<>(Arrays.asList(modifiers));
    }

    /**
     * Get the original speed controller.
     * 
     * @return The wrapped object.
     */
    public SendableSpeedController getWrapped() {
        return wrapped;
    }

    public void add(final Modifier m, final Modifier... ms) {
        modifiers.add(m);
        modifiers.addAll(Arrays.asList(ms));
    }

    public void addAll(final Collection<? extends Modifier> ms) {
        modifiers.addAll(ms);
    }

    @Override
    public void set(final double speed) {
        wrapped.set(calculate(speed));
    }

    @Override
    public double get() {
        return wrapped.get();
    }

    @Override
    public void setInverted(final boolean isInverted) {
        wrapped.setInverted(isInverted);
    }

    @Override
    public boolean getInverted() {
        return wrapped.getInverted();
    }

    @Override
    public void disable() {
        wrapped.disable();
    }

    @Override
    public void stopMotor() {
        wrapped.stopMotor();
    }

    @Override
    public void pidWrite(final double output) {
        wrapped.set(output);
    }

    @Override
    public void initSendable(final SendableBuilder builder) {
        wrapped.initSendable(builder);
    }

    /**
     * Run all modifiers.
     * 
     * As modifiers could have side effects, this is private.
     * 
     * @param rawSpeed The base speed to run
     * @return The new speed
     */
    private double calculate(final double rawSpeed) {
        double speed = rawSpeed;
        for (final Modifier m : modifiers) {
            speed = m.applyAsDouble(speed);
        }
        return speed;
    }
}