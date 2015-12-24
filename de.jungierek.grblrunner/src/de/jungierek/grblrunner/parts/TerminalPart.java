package de.jungierek.grblrunner.parts;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jungierek.grblrunner.constants.IEvents;
import de.jungierek.grblrunner.constants.IPreferences;
import de.jungierek.grblrunner.service.gcode.IGcodeLine;
import de.jungierek.grblrunner.service.gcode.IGcodePoint;
import de.jungierek.grblrunner.service.gcode.IGcodeService;
import de.jungierek.grblrunner.service.gcode.IGrblRequest;
import de.jungierek.grblrunner.service.gcode.IGrblResponse;
import de.jungierek.grblrunner.tools.IPersistenceKeys;
import de.jungierek.grblrunner.tools.PartTools;

public class TerminalPart {

    private static final Logger LOG = LoggerFactory.getLogger ( TerminalPart.class );

    private static final String JUSTIFY_PLACE = "                    ";

    @Inject
    private IGcodeService gcodeService;

    @Inject
    private Display display;

    @Inject
    private PartTools partTools;

    private Color WHITE, RED, GREEN, GRAY, LIGHT_GREEN, LIGHT_GRAY, YELLOW;

    // private Text terminalText;
    private StyledText terminalText;

    private boolean showSuppressedLines = false;

    private boolean showGrblStateLines = true;
    private boolean showGcodeModeLines = true;

    @PostConstruct
    public void createGui ( Composite parent, IEclipseContext context, MApplication application ) {

        WHITE = display.getSystemColor ( SWT.COLOR_WHITE );
        RED = display.getSystemColor ( SWT.COLOR_RED );
        GREEN = display.getSystemColor ( SWT.COLOR_DARK_GREEN );
        LIGHT_GREEN = display.getSystemColor ( SWT.COLOR_GREEN );
        GRAY = display.getSystemColor ( SWT.COLOR_DARK_GRAY );
        LIGHT_GRAY = display.getSystemColor ( SWT.COLOR_GRAY );
        YELLOW = display.getSystemColor ( SWT.COLOR_YELLOW );


        // terminalText = new Text ( parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL );
        // terminalText = new Text ( parent, SWT.MULTI | SWT.V_SCROLL );
        terminalText = new StyledText ( parent, SWT.MULTI | SWT.V_SCROLL );
        // TODO_PREF to pref
        terminalText.setFont ( new Font ( display, "Courier", 10, SWT.NONE ) );
        terminalText.setEditable ( false );
        terminalText.setBackground ( display.getSystemColor ( SWT.COLOR_WHITE ) );

        final Map<String, String> persistedState = application.getPersistedState ();
        showSuppressedLines = partTools.parseBoolean ( persistedState.get ( IPersistenceKeys.KEY_TERMINAL_SUPPRESS_LINES ) );
        showGrblStateLines = partTools.parseBoolean ( persistedState.get ( IPersistenceKeys.KEY_TERMINAL_GRBL_STATE ) );
        showGcodeModeLines = partTools.parseBoolean ( persistedState.get ( IPersistenceKeys.KEY_TERMINAL_GRBL_MODES ) );
        // LOG.info ( "createGui: lines=" + showSuppressedLines + " state=" + showGrblStateLines + " gcode=" + showGcodeModeLines );

    }

    private void scrollToEnd () {

        terminalText.setTopIndex ( terminalText.getLineCount () - 1 );

    }

    private void appendText ( String line, final Color foreground, final Color background, final int textStyle ) {
    
        int start = terminalText.getCharCount ();
        terminalText.append ( line );
        int len = terminalText.getCharCount () - start;
        terminalText.setStyleRange ( new StyleRange ( start, len, foreground, background, textStyle ) );

        scrollToEnd ();

    }

    private void appendText ( String line, final Color foreground, final Color background ) {
    
        appendText ( line, foreground, background, SWT.NONE );
    
    }

    private void appendText ( String line, final int textStyle ) {
    
        appendText ( line, null, null, textStyle );
    
    }

    @PersistState
    public void persistState ( MApplication application ) {
        
        LOG.debug ( "persistState:" );

        final Map<String, String> persistedState = application.getPersistedState ();

        persistedState.put ( IPersistenceKeys.KEY_TERMINAL_SUPPRESS_LINES, "" + showSuppressedLines );
        persistedState.put ( IPersistenceKeys.KEY_TERMINAL_GRBL_STATE, "" + showGrblStateLines );
        persistedState.put ( IPersistenceKeys.KEY_TERMINAL_GRBL_MODES, "" + showGcodeModeLines );

    }

    @Inject
    @Optional
    public void alarmNotified ( @UIEventTopic(IEvents.GRBL_ALARM) String line ) {
        
        LOG.trace ( "alarmNotified: line=" + line );

        appendText ( line, WHITE, RED, SWT.BOLD );

    }

    @Inject
    @Optional
    public void playerStartNotified ( @UIEventTopic(IEvents.PLAYER_START) String timestamp ) {

        LOG.trace ( "playerStartNotified: timestamp=" + timestamp );

        appendText ( "Gcode Player started at " + timestamp + "\n", null, YELLOW );
    
    }

    @Inject
    @Optional
    public void playerLineSegmentNotified ( @UIEventTopic(IEvents.PLAYER_SEGMENT) String gcodeSegment ) {

        LOG.trace ( "playerLineNotified: gcodeSegment=" + gcodeSegment );

        terminalText.append ( "- " + gcodeSegment + "\n" );
        scrollToEnd ();

    }

    @Inject
    @Optional
    public void playerLineNotified ( @UIEventTopic(IEvents.PLAYER_LINE) IGcodeLine gcodeLine ) {
    
        LOG.trace ( "playerLineNotified: gcodeLine=" + gcodeLine );

        if ( gcodeLine == null ) return;

        String terminalLine = gcodeLine.getLine ();

        if ( IPreferences.SHOW_GCODE_LINE ) {
            int alignLength = JUSTIFY_PLACE.length () - terminalLine.length ();
            if ( alignLength > 0 ) {
                terminalLine += JUSTIFY_PLACE.substring ( 0, alignLength );
            }
            terminalLine += gcodeLine;
        }

        terminalText.append ( terminalLine + "\n" );
        scrollToEnd ();
    
    }

    @Inject
    @Optional
    public void playerStopNotified ( @UIEventTopic(IEvents.PLAYER_STOP) String timestamp ) {

        LOG.trace ( "playerStopNotified: timestamp=" + timestamp );

        appendText ( "Gcode Player stopped at " + timestamp + "\n", null, YELLOW );
        terminalText.append ( "-------------------------------------------------------------------------------------\n" );
        scrollToEnd ();

    }

    @Inject
    @Optional
    public void sentNotified ( @UIEventTopic(IEvents.GRBL_SENT) IGrblRequest command, EModelService modelService, MApplication application ) {

        LOG.trace ( "sentNotified: command=" + command );

        if ( command == null ) {
            LOG.warn ( "receivedNotified: response == null" );
            return;
        }

        if ( command.isReset () ) return;

        String line = command.getMessage ();

        if ( !command.isSuppressInTerminal () ) {
            appendText ( line, SWT.BOLD );
        }
        else if ( showSuppressedLines ) {

            boolean show = true;

            if ( line.startsWith ( "$G" ) ) show = showGcodeModeLines;

            if ( show ) appendText ( line, LIGHT_GRAY, null, SWT.BOLD );

        }

    }
    
    private boolean ignoreNextOk = false;

    @Inject
    @Optional
    public void receivedNotified ( @UIEventTopic(IEvents.GRBL_RECEIVED) IGrblResponse response ) {

        LOG.trace ( "receivedNotified: response=" + response );

        if ( response == null || response.getMessage () == null ) return;

        String line = response.getMessage ();

        if ( !response.isSuppressInTerminal () ) {
            if ( line.startsWith ( "ok" ) ) {
                appendText ( line, GREEN, null, SWT.BOLD );
            }
            else if ( line.startsWith ( "error" ) ) {
                appendText ( line, RED, null, SWT.BOLD );
            }
            else if ( line.startsWith ( "Grbl" ) ) {
                appendText ( line, WHITE, GRAY );
            }
            else {
                terminalText.append ( line );
                scrollToEnd ();
            }
        }
        else if ( showSuppressedLines ) {
            if ( line.startsWith ( "ok" ) ) {
                if ( ignoreNextOk ) ignoreNextOk = false;
                else appendText ( line, LIGHT_GREEN, null, SWT.BOLD );
            }
            else if ( line.startsWith ( "error" ) ) {
                appendText ( line, RED, null, SWT.BOLD );
            }
            else if ( line.startsWith ( "Grbl" ) ) {
                appendText ( line, WHITE, LIGHT_GRAY );
            }
            else {

                boolean show = true;

                if ( line.startsWith ( "<" ) ) show = showGrblStateLines;
                else if ( line.startsWith ( "[G54" ) ) {} // do nothing
                else if ( line.startsWith ( "[G55" ) ) {} // do nothing
                else if ( line.startsWith ( "[G56" ) ) {} // do nothing
                else if ( line.startsWith ( "[G57" ) ) {} // do nothing
                else if ( line.startsWith ( "[G58" ) ) {} // do nothing
                else if ( line.startsWith ( "[G59" ) ) {} // do nothing
                else if ( line.startsWith ( "[G28" ) ) {} // do nothing
                else if ( line.startsWith ( "[G30" ) ) {} // do nothing
                else if ( line.startsWith ( "[G92" ) ) {} // do nothing
                else if ( line.startsWith ( "[G" ) ) {
                    show = showGcodeModeLines;
                    if ( !show ) ignoreNextOk = true;
                }

                if ( show ) appendText ( line, LIGHT_GRAY, null, SWT.BOLD );

            }
        }

    }

    @Inject
    @Optional
    public void scanStartNotified ( @UIEventTopic(IEvents.AUTOLEVEL_START) String timestamp ) {

        LOG.trace ( "scanStartNotified:" );

        appendText ( "Probe Scanning started at " + timestamp + "\n", null, YELLOW );

    }

    @Inject
    @Optional
    public void updateProbeNotified ( @UIEventTopic(IEvents.AUTOLEVEL_UPDATE) IGcodePoint probe ) {

        LOG.trace ( "updateProbeNotified: probe=" + probe );

        // convert from machine to work coordinate system
        final IGcodePoint p = probe.sub ( gcodeService.getFixtureShift () );
        terminalText.append ( "" + probe + "    delta=" + String.format ( IGcodePoint.FORMAT_COORDINATE, p.getZ () ) + "\n" );

        scrollToEnd ();

    }

    @Inject
    @Optional
    public void scanStopNotified ( @UIEventTopic(IEvents.AUTOLEVEL_STOP) String timestamp ) {

        LOG.trace ( "scanStopNotified:" );

        appendText ( "Probe Scanning stopped at " + timestamp + "\n", null, YELLOW );

    }

    // delegate routines for command handlers

    public void toggleSuppressLines () {

        LOG.debug ( "toggleSuppressLines:" );
        showSuppressedLines = !showSuppressedLines;

    }

    public void clearText () {

        LOG.debug ( "clearText:" );
        terminalText.setText ( "" );

    }

    public void setShowGrblState ( boolean show ) {

        LOG.debug ( "setSuppressGrblState: selected=" + show );

        showGrblStateLines = show;

    }

    public void setShowGcodeState ( boolean show ) {

        LOG.debug ( "setSuppressGcodState: selected=" + show );

        showGcodeModeLines = show;

    }
        
}