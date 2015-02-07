package org.usfirst.frc.team4624.robot.subsystems;

import org.usfirst.frc.team4624.robot.RobotMap;
import org.usfirst.frc.team4624.robot.commands.LiftManual;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Forklift extends Subsystem {
    
    Jaguar lift;
    
    Encoder encoder;
    
    private final int codesPerRev = 497;
    private double goal; // In Rotations
    private double lastDistance;
    private double lastTime;
    
    public Forklift() {
        lift = new Jaguar(RobotMap.PWM_LIFT_PORT);
        encoder = new Encoder(RobotMap.LIFT_ENCODER_A, RobotMap.LIFT_ENCODER_B);
        
        lastDistance = 0.0;
        lastTime = 0.0;
    }
    
    /**
     * Sets the target of the Forklift to its current position. Effectively stopping it.
     */
    public void stop() {
        lift.set(0.0);
    }
    
    public void setRaw(double raw){
        SmartDashboard.putNumber("Encoder Position", encoder.getDistance() / 250);
        SmartDashboard.putNumber("Rate of Change", (encoder.getDistance() - lastDistance) / (System.currentTimeMillis() - lastTime));
        lastDistance = encoder.getDistance();
        lastTime = System.currentTimeMillis();
        lift.set(raw);
    }
    
    public void setRate(double rate){
        
    }
    
    private double getRateOfChange(){
        return (encoder.getDistance() - lastDistance) / (System.currentTimeMillis() - lastTime);
    }
    
    public double getRotations(){
        return encoder.getDistance() / 250;
    }
    
    /**
     * Snaps the Forklift to a tote level relative to its current position
     * @param changeLevel
     */
    public void changeLevel(int changeLevel) { // Convert to rotations here (changeLevel is in levels)
        // TODO Auto-generated method stub
    }
    
    /**
     * Changes the Forklift height relative to its current position
     * @param changeHeight
     */
    public void changeHeight(double changeHeight) { // Convert to rotations here (changeHeight is in distance)
        changeGoal(changeHeight);
    }
    
    /**
     * Changes the target of the motor relative to what it is
     * @param changeGoal
     */
    private void changeGoal(double changeGoal) { // Takes in Rotations
        goal += changeGoal;
        goal = clamp(goal, 0, RobotMap.FORKLIFT_MAX_ROTATIONS);
        //liftParent.set(goal);
    }
    
    /**
     * Clamps a value between two other values
     * @param input
     * @param min
     * @param max
     * @return A clamped value between min and max
     */
    private double clamp(double input, double min, double max) {
        return Math.max(min, Math.min(max, input));
    }
    
    /**
     * Converts vertical distance to rotations required to reach that height
     * @param distance
     * @return Number of rotations from the initial value (0)
     */
    private double distanceToRotations(double distance) { //This is the important one
        return 0.0; //TODO Add Maths
    }
    
    /**
     * For use with commands
     * @return Returns true if the Forklift has reached its goal
     */
    public boolean isFinished() {
        //return (Math.abs(liftParent.getPosition() - goal) < RobotMap.MOTOR_ACCURACY);
        return false;
    }
    
    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new LiftManual());
    }
    
    public double getHeight(double curveDistance) {
        return (27.7 - RobotMap.ARM_LENGTH * getAngle(curveDistance));
        //Is 27.7 actually cornerToPivot???
    }
    /**
     * 
     * @param curveDistance
     * @return
     */
    public double getAngle(double curveDistance) {
        return Math.toDegrees(Math.cos(43.2 + (360 * curveDistance) / (76 * Math.PI)));
        //TODO Get 43.2 and 76 into robotmap
    }
    
    public double getStrapLength(double angle) {
        return Math.sqrt(Math.pow(RobotMap.CORNER_TO_PIVOT, 2) + Math.pow(RobotMap.ARM_LENGTH, 2) - (2 * RobotMap.CORNER_TO_PIVOT
                * RobotMap.ARM_LENGTH * Math.cos(angle)));
    }
    
    public double distanceToRevolutions(double distance) {
        return distance / 2.5; //TODO Adjust for strap wrapping around
    }
    
    /**
     * Update the system. Call this from a default command.
     */
    public void execute() {
        
        //printStatus();
    }
    
    /* Smart Dashboard */
    /**
     * For use with the Smart Dashboard
     * @return The current rotation count of the motor
     */
    public double getPosition() {
        //return liftParent.getPosition();
        return 0.0;
    }
    
    /**
     * For use with the Smart Dashboard
     * @return The current goal of the motor
     */
    public double getGoal() {
        return goal;
    }
    
    /**
     * For use with the Smart Dashboard.
     * Sets the goal to what is inputed
     * @param newGoal
     */
    public void setGoal(double newGoal) {
        goal = newGoal;
        //liftParent.set(goal);
    }
    
    /**
     * For use with the Smart Dashboard.
     * Resets the P I and D values of the Forklift motor
     */
    public void reinit() {
        //liftParent.setPositionMode(CANJaguar.kQuadEncoder, codesPerRev,
        //        RobotMap.p, RobotMap.i, RobotMap.d);
    }
    
    public void printStatus() {
        //SmartDashboard.putNumber("Forklift Height", liftParent.get());
        SmartDashboard.putNumber("Forklift Goal  ", goal);
    }
}
