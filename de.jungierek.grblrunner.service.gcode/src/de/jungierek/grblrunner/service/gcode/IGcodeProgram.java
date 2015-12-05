package de.jungierek.grblrunner.service.gcode;

import java.io.File;

public interface IGcodeProgram {

    public static final double EPSILON = 0.001;

    IGcodeLine [] getAllGcodeLines ();

    public File getGcodeProgramFile ();
    public String getGcodeProgramName ();
    public void loadGcodeProgram ( File gcodeFile );
    public boolean isLoaded ();
    public void clear ();

    public void appendLine ( String line );
    public int getLineCount ();

    public IGcodePoint getMin ();
    public IGcodePoint getMax ();

    public void parse ();

    public void resetProcessed ();

    public void rotate ( double angle );
    double getRotationAngle ();

    // ---------------------------------------------------------------------

    public File getAutolevelDataFile ();

    public void loadAutolevelData ();
    public void saveAutolevelData ();
    public void clearAutolevelData ();

    void prepareAutolevelScan (); // force creation of new grid
    public void prepareAutolevelScan ( int xSteps, int ySteps );
    boolean isAutolevelScanPrepared ();

    public int getXSteps ();
    public int getYSteps ();
    public double getStepWidthX ();
    public double getStepWidthY ();
    public int getNumProbePoints ();

    public void setAutolevelScanCompleted ();
    public boolean isAutolevelScanComplete ();

    public void setProbePoint ( IGcodePoint probe );
    public IGcodePoint getProbePointAt ( int ix, int iy );

    public IGcodePoint [] interpolateLine ( IGcodePoint point1, IGcodePoint point2 );

}