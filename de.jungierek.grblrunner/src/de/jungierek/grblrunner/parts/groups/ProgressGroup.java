package de.jungierek.grblrunner.parts.groups;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jungierek.grblrunner.constants.IEvents;
import de.jungierek.grblrunner.service.gcode.IGcodeGrblState;
import de.jungierek.grblrunner.service.gcode.IGcodeLine;
import de.jungierek.grblrunner.service.gcode.IGcodeModel;
import de.jungierek.grblrunner.service.gcode.IGcodePoint;
import de.jungierek.grblrunner.service.gcode.IGcodeService;
import de.jungierek.grblrunner.tools.GuiFactory;
import de.jungierek.grblrunner.tools.IPersistenceKeys;

public class ProgressGroup {

    private static final Logger LOG = LoggerFactory.getLogger ( ProgressGroup.class );

    private static final String GROUP_NAME = "Progress";

    @Inject
    private IGcodeService gcodeService;

    @Inject
    private IGcodeModel gcodeModel;

    private ProgressBar progressBar;

    @PostConstruct
    public void createGui ( Composite parent, IEclipseContext context ) {

        LOG.debug ( "createGui: parent=" + parent );

        int partCols = ((Integer) context.get ( IPersistenceKeys.KEY_PART_COLS )).intValue ();
        int groupCols = ((Integer) context.get ( IPersistenceKeys.KEY_PART_GROUP_COLS )).intValue ();
        Group group = GuiFactory.createGroup ( parent, GROUP_NAME, groupCols, 1, true );

        final int cols = 1;
        group.setLayout ( new GridLayout ( cols, true ) );

        progressBar = new ProgressBar ( group, SWT.SMOOTH );
        progressBar.setLayoutData ( new GridData ( SWT.FILL, SWT.CENTER, true, false, cols, 1 ) );

        progressBar.setMinimum ( 0 );
        progressBar.setMaximum ( 1 );
        progressBar.setState ( SWT.NORMAL );

    }

    @Inject
    @Optional
    public void alarmNotified ( @UIEventTopic(IEvents.GRBL_ALARM) String line ) {

        LOG.debug ( "alarmNotified: line=" + line );

        progressBar.setState ( SWT.ERROR );

    }

    @Inject
    @Optional
    public void updateStateNotified ( @UIEventTopic(IEvents.UPDATE_STATE) IGcodeGrblState grblState ) {

        LOG.trace ( "updateStateNotified: grblState=" + grblState );

        switch ( grblState.getGrblState () ) {
            case IDLE:
            case RUN:
            case HOME:
                progressBar.setState ( SWT.NORMAL );
                break;

            case HOLD:
            case QUEUE:
            case CHECK:
                progressBar.setState ( SWT.PAUSED );
                break;

            case ALARM:
                progressBar.setState ( SWT.ERROR );
                break;

            default:
                break;
        }

    }

    @Inject
    @Optional
    public void disconnectedNotified ( @UIEventTopic(IEvents.SERIAL_DISCONNECTED) String port ) {

        LOG.trace ( "connectedNotified: port=" + port );

        progressBar.setSelection ( 0 );

    }

    @SuppressWarnings("deprecation")
    @Inject
    @Optional
    public void playerStartNotified ( @UIEventTopic(IEvents.PLAYER_START) String timestamp ) {

        LOG.trace ( "playerStartNotified: timestamp=" + timestamp );

        progressBar.setMinimum ( 0 );
        progressBar.setMaximum ( gcodeModel.getLineCount () );
        progressBar.setSelection ( 0 );

    }

    @Inject
    @Optional
    public void playerLineNotified ( @UIEventTopic(IEvents.PLAYER_LINE) IGcodeLine line ) {

        LOG.trace ( "gcodePlayerLineNotified: line=" + line );

        progressBar.setSelection ( progressBar.getSelection () + 1 );

    }

    @Inject
    @Optional
    public void scanStartNotified ( @UIEventTopic(IEvents.AUTOLEVEL_START) String timestamp ) {

        LOG.trace ( "scanStartNotified:" );

        progressBar.setMinimum ( 0 );
        progressBar.setMaximum ( gcodeModel.getNumProbePoints () );
        progressBar.setSelection ( 0 );
        // progressBar.setState ( SWT.NORMAL );

    }

    @Inject
    @Optional
    public void updateProbeNotified ( @UIEventTopic(IEvents.AUTOLEVEL_UPDATE) IGcodePoint probe ) {

        LOG.trace ( "updateProbeNotified: probe=" + probe );

        if ( gcodeService.isScanning () ) {

            progressBar.setSelection ( progressBar.getSelection () + 1 );
            // progressBar.setState ( SWT.NORMAL );

        }

    }

}
