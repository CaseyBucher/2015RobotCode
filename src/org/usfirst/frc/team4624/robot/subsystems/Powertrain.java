
package org.usfirst.frc.team4624.robot.subsystems;

import org.usfirst.frc.team4624.robot.RobotMap;	// ENUMS for the ports
import org.usfirst.frc.team4624.robot.commands.DriveCommand;
import org.usfirst.frc.team4624.robot.input.XboxController;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Powertrain extends Subsystem {
    
    /* Instance Values */
    Jaguar leftMotor;
    Jaguar rightMotor;
    
    
    
    /**
     * Constructor
     * 
     * This initializes the powertrain
     */
    public Powertrain() {
        
        /* Initialize */
        this.init();
    }
    
    
    
    double inputFunction( double input ) {
        double x        = Math.abs( input );
        double thing    = -Math.sqrt( 1 - Math.pow( x, 2 ) ) + 1;
        if ( input > 0 ) {
            return thing;
        } else {
            return -thing;
        }
    }
    
    
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand( new DriveCommand() );
    }
    
    
    public void init() {
        leftMotor   = new Jaguar( RobotMap.PORT_MOTOR_LEFT );
        rightMotor  = new Jaguar( RobotMap.PORT_MOTOR_RIGHT );
        
        leftMotor.setSafetyEnabled( true );
        rightMotor.setSafetyEnabled( true );
        leftMotor.setExpiration( 0.5 );
        rightMotor.setExpiration( 0.5 );
        stop();
    }
    
    public void setRaw( double l, double r ) {  // Avoid using this. Use set instead
        double left = Math.max(-1, Math.min(1, l));     // Clamp
        double right = Math.max(-1, Math.min(1, r));    // Clamp
        
        leftMotor.set( left );
        rightMotor.set( right );
    }
    
    public void set( double l, double r ) {
        setRaw( l, -r );	// To go straight, we inverted one of the motors (Clockwise && Counter-Clockwise = Straight)
    }
    
    public void setFromThumbstick( XboxController.Thumbstick stick ) {
        
        /* Old method
        double x = stick.getX();
        double y = stick.getY();
        
        double left		= x + y;
        double right	= -x + y;
        
        left = left > 1 ? 1 : left;
        left = left < -1 ? -1 : left;
        right = right > 1 ? 1 : right;
        right = right < -1 ? -1 : right;
        
        left = inputFunction( left );
        right = inputFunction( right );
        
        this.set( left, right );
        */
        
        // Formula taken from here: http://home.kendra.com/mauser/Joystick.html
        final double x      = -stick.getRawX();
        final double y      = stick.getRawY();
        
        final double v      = (1 - Math.abs(x)) * y + y;
        final double w      = (1 - Math.abs(y)) * x + x;
        
        final double left   = (v-w) / 2;
        final double right  = (v+w) / 2;
        
        set(inputFunction(left), inputFunction(right));
    }
    
    public void stop() {
        leftMotor.set( 0 );
        rightMotor.set( 0 );
    }
}