package com.chopshop166.chopshoplib.sensors.gyro;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/** Wrapper around a WPIlib built in gyro. */
public class WGyro implements SmartGyro {

    /** The object as a gyro. */
    private final Gyro gyro;
    /** The object as a sendable. */
    private final Sendable sendable;
    /** The offset from the zero-position, for manual alignment. */
    private double offset;

    /**
     * Constructor.
     * 
     * @param <GyroBase> A type that is both a {@link Gyro} and {@link Sendable}
     * @param gyro The gyro object.
     */
    public <GyroBase extends Gyro & Sendable> WGyro(final GyroBase gyro) {
        this.gyro = gyro;
        this.sendable = gyro;
    }

    @Override
    public void close() throws Exception {
        this.gyro.close();

    }

    @Override
    public void initSendable(final SendableBuilder builder) {
        this.sendable.initSendable(builder);

    }

    @Override
    public void calibrate() {
        this.gyro.calibrate();
    }

    @Override
    public void reset() {
        this.gyro.reset();
        this.offset = 0;
    }

    @Override
    public double getAngle() {
        return this.gyro.getAngle() - this.offset;
    }

    @Override
    public void setAngle(final double angle) {
        this.offset = angle;
    }

    @Override
    public double getRate() {
        return this.gyro.getRate();
    }

}
