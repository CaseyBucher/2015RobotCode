package org.usfirst.frc.team4624.robot.input;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

// extend joystick?
public class XboxController{
	
	/*
	 * RETURN VALUES
	 */
	
	/*
	 * Thumbstick
	 * double	x()
	 * double	y()
	 * Button	pressed
	 */
	public Thumbstick leftStick;
	public Thumbstick rightStick;
	
	/*
	 * Trigger
	 * double	x()
	 * double	y()
	 * boolean	get()
	 */
	public Trigger lt;
	public Trigger rt;
	
	/*
	 * DirectonalPad
	 * int		angle()
	 * boolean	get~
	 */
	public DirectionalPad dPad;
	
	/*
	 * Button
	 * I dunno, check the docs
	 */
	public Button a;
	public Button b;
	public Button x;
	public Button y;
	public Button lb;
	public Button rb;
	public Button back;
	public Button start;
	
	
	
	/*
	 * CONSTANTS
	 */
	// Buttons
	private static final int aButtonID					= 1;
	private static final int bButtonID					= 2;
	private static final int xButtonID					= 3;
	private static final int yButtonID					= 4;
	private static final int lbButtonID					= 5;
	private static final int rbButtonID					= 6;
	private static final int backButtonID				= 7;
	private static final int startButtonID				= 8;
	private static final int leftThumbstickButtonID		= 9;
	private static final int rightThumbstickButtonID	= 10;
	
	// Axis's
	private static final int leftXThumbstickID	= 1;
	private static final int leftYThumbstickID	= 2;
	private static final int triggerAxisID		= 3;
	private static final int rightXThumbstickID	= 4;
	private static final int rightYThumbstickID	= 5;
	
	// Something dumb
	private static final int dPadID	= 0;
	
	// Modifiers
	private static final double thumbstickDeadZone	= 0.1;	// Jiggle room for the thumbsticks
	private static final double triggerSensitivity	= 0.6;	// If the trigger is beyond this limit, say it has been pressed
	
	
	/*
	 * LOCAL VARIBLES
	 */
	private DriverStation driverStation;
	private int joystickPort;
	
	/*
	 * Constructor
	 */
	public XboxController() {
		//super( 0 );	// extend joystick?
		createXboxControllerInstance( 0 );
	}
	
	public XboxController( int port ) {
		//super( port );	// extend joystick?
		createXboxControllerInstance( port );
	}
	
	/*
	 * Primary constructor (for when you want to specify the port)
	 */
	private final void createXboxControllerInstance( int port ) {
		
		driverStation		= DriverStation.getInstance();	// Driver station. Needed to access some buttons (Looking at you DPad)
		this.joystickPort	= port;
		
		
		Joystick controller	= new Joystick( port );		// Joystick referenced by all buttons and thumbsticks
		
		// Thumbsticks
		this.leftStick	= new Thumbstick( controller, Hand.LEFT );
		this.rightStick	= new Thumbstick( controller, Hand.RIGHT );
		
		// DPads
		this.dPad	= new DirectionalPad( driverStation );	// Voted this year's "Most likely to fail so don't rely on me"
		
		// Triggers (Remember, they're not buttons [but we could emulate them])
		this.lt		= new Trigger( controller, Hand.LEFT );
		this.rt		= new Trigger( controller, Hand.RIGHT );
		
		// Buttons
		this.a		= new JoystickButton( controller, aButtonID );
		this.b		= new JoystickButton( controller, bButtonID );
		this.x		= new JoystickButton( controller, xButtonID );
		this.y		= new JoystickButton( controller, yButtonID );
		this.lb		= new JoystickButton( controller, lbButtonID );
		this.rb		= new JoystickButton( controller, rbButtonID );
		this.back	= new JoystickButton( controller, backButtonID );
		this.start	= new JoystickButton( controller, startButtonID );
	}
	
	/*
	 * Hand
	 * Used in some classes because int's are bad mm'k?
	 */
	enum Hand {
		LEFT, RIGHT
	}
	
	/*
	 * Thumbstick
	 * Every Xbox Controller has 2
	 */
	public class Thumbstick {
		
		/*
		 * RETURN VALUES
		 */
		public final Button pressed;
		
		/*
		 * LOCAL VARIABLES
		 */
		private final Joystick parent;
		private final Hand hand;
		private final int xAxisID;
		private final int yAxisID;
		
		
		/*
		 * Constructor
		 */
		Thumbstick( Joystick parent, Hand hand ) {
			
			this.parent	= parent;
			this.hand	= hand;
			
			if( hand == Hand.LEFT ) {
				this.xAxisID	= leftXThumbstickID;
				this.yAxisID	= leftYThumbstickID;
				this.pressed	= new JoystickButton( parent, leftThumbstickButtonID );
			} else {
				this.xAxisID	= rightXThumbstickID;
				this.yAxisID	= rightYThumbstickID;
				this.pressed	= new JoystickButton( parent, rightThumbstickButtonID );
			}
		}
		
		
		public double x() {
			double rawInput		= parent.getRawAxis( xAxisID );	// Input from the driver
			boolean isNegative	= rawInput < 0;	// Duh
			double adjusted		= Math.abs( rawInput ) - thumbstickDeadZone;	// Subtract the deadzone from the magnitude
			adjusted = adjusted < 0 ? 0 : adjusted;	// if the new input is negative, make it zero
			adjusted = adjusted / ( 1 - thumbstickDeadZone );	// Adjust the adjustment so it can max at 1
			
			if( isNegative ) {
				return -1 * adjusted;
			} else {
				return adjusted;
			}
		}
		public double y() {
			double rawInput		= parent.getRawAxis( xAxisID );	// Input from the driver
			boolean isNegative	= rawInput < 0;	// Duh
			double adjusted		= Math.abs( rawInput ) - thumbstickDeadZone;	// Subtract the deadzone from the magnitude
			adjusted = adjusted < 0 ? 0 : adjusted;	// if the new input is negative, make it zero
			adjusted = adjusted / ( 1 - thumbstickDeadZone );	// Adjust the adjustment so it can max at 1
			
			if( isNegative ) {
				return -1 * adjusted;
			} else {
				return adjusted;
			}
		}
	}
	
	/*
	 * Trigger
	 * Again, every controller has 2
	 */
	public class Trigger{
		
		/*
		 * LOCAL VARIABLES
		 */
		private final Joystick parent;
		private final Hand hand;
		
		private final double sensitivity	= triggerSensitivity;	// If the trigger is beyond this limit, say it has been pressed
		
		/*
		 * Constructor
		 */
		public Trigger( Joystick joystick, Hand hand ) {
			this.parent = joystick;
			this.hand = hand;
		}
		
		
		public boolean get() {
			if( hand == Hand.LEFT ) {
				return parent.getRawAxis( triggerAxisID ) > sensitivity;
			} else {
				return parent.getRawAxis( triggerAxisID ) < -sensitivity;
			}
		}
		
		public double x() {
			if( hand == Hand.LEFT ) {
				return parent.getRawAxis( triggerAxisID );	// The two triggers share an axis
			} else
				return -parent.getRawAxis( triggerAxisID );	// Its almost as dumb as the DPad
		}
		public double y() {
			return this.x();	// One dimensional movement. Use ^ that one instead
		}
		
	}
	
	public class DirectionalPad {
		
		/*
		 * LOCAL VARIABLES
		 */
		private DriverStation driverStation;
		
		/*
		 * Constructor
		 */
		public DirectionalPad( DriverStation driverStationInstance ) {
			this.driverStation	= driverStationInstance;
		}
		
		
		public int angle() {
			return driverStation.getStickPOV( joystickPort, dPadID );	// I have no information about this
		}
		
		// TODO LATER
		public boolean getUp() {
			return false;
		}
		public boolean getUpRight() {
			return false;
		}
		public boolean getRight() {
			return false;
		}
		public boolean getDownRight() {
			return false;
		}
		public boolean getDown() {
			return false;
		}
		public boolean getDownLeft() {
			return false;
		}
		public boolean getLeft() {
			return false;
		}
		public boolean getUpLeft() {
			return false;
		}
	}
}