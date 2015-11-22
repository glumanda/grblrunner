package de.jungierek.grblrunner.constants;


public interface IConstants {

    public static final String MAIN_WINDOW_ID = "de.jungierek.grblrunner.window.main";

    public static final String FORMAT_COORDINATE = "%.3f";

    public final static double ONE_DEGREE = Math.PI / 180.0;

    public static final String [] AXIS = { "X", "Y", "Z" };

    public static final int GCODE_QUEUE_LENGTH = 20;

    public static final String GCODE_SCAN_START = "SCAN_START";
    public static final String GCODE_SCAN_END = "SCAN_END";

    public static final char CR = 0x0D;
    public static final char LF = 0x0A;
    public static final char GRBL_RESET_CODE = 0x18;
    public static final String GRBL_RESET_STRING = new String ( new byte [] { GRBL_RESET_CODE } );

}
