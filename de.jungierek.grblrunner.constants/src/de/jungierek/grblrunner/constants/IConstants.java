package de.jungierek.grblrunner.constants;

import org.eclipse.swt.SWT;


public interface IConstants {
    
    public static final String KEY_BASE = "de.jungierek.grblrunner";

    public static final String MAIN_WINDOW_ID = KEY_BASE + ".window.main";
    public static final String EDITOR_PARTSTACK_ID = KEY_BASE + ".partstack.editors";
    public static final String EDITOR_PARTDESCRIPTOR_ID = KEY_BASE + ".partdescriptor.gcode.editor";
    public static final String MACRO_PARTDESCRIPTOR_ID = KEY_BASE + ".partdescriptor.gcode.macro";;

    public static final String MACRO_TEXT_ID = KEY_BASE + ".gcode.text";

    public static final String FORMAT_COORDINATE = "%.3f";
    public static final String FORMAT_HEIGHT = "%+.1f";
    public static final String FORMAT_FEEDRATE = "%.0f";

    public static final double ONE_DEGREE = Math.PI / 180.0;

    public static final String [] AXIS = { "X", "Y", "Z" };
    public static final String [] COORDINATE_SYSTEMS = { "G54", "G55", "G56", "G57", "G58", "G59" };

    public static final int GCODE_QUEUE_LENGTH = 20;

    public static final String GCODE_SCAN_START = "SCAN_START";
    public static final String GCODE_SCAN_END = "SCAN_END";

    public static final char CR = 0x0D;
    public static final char LF = 0x0A;
    public static final char GRBL_RESET_CODE = 0x18;
    public static final String GRBL_RESET_STRING = new String ( new byte [] { GRBL_RESET_CODE } );

    public static final String NO_OVERLAY = "NO OVERLAY";

    public static final double EPSILON = 0.001; // never adjust this lesser, because of grid calculation ccordinates vs. indices
    
    public static final String PREFERENCE_NODE = KEY_BASE;

    // index 0 is the color for the axis label
    public static final int COORDINATE_SYSTEM_ARROW_COLORS [] = new int [] { SWT.COLOR_BLACK, SWT.COLOR_RED, SWT.COLOR_GREEN, SWT.COLOR_BLUE };

}
