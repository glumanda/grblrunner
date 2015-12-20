 
package de.jungierek.grblrunner.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jungierek.grblrunner.service.gcode.IGcodeService;
import de.jungierek.grblrunner.service.serial.ISerialService;

public class GcodeSimpleCommandHandler {

    private static final Logger LOG = LoggerFactory.getLogger ( GcodeSimpleCommandHandler.class );

    // this works only for toolbar items
    @Execute
    @Optional
    // public void execute ( IGcodeService gcode, MHandledToolItem item ) {
    public void execute ( IGcodeService gcode, MHandledItem item ) {

        LOG.debug ( "execute: item=" + item );

        if ( item != null ) {

            String id = item.getElementId ();
            boolean isSuppressLines = item.getPersistedState ().get ( "no_suppress_lines" ) == null;

            sendCommand ( gcode, id, isSuppressLines );

        }

    }

    private void sendCommand ( IGcodeService gcodeService, String id, boolean isSuppressLines ) {

        String grblCommand = id.substring ( 1 + id.lastIndexOf ( '.' ) );
        LOG.debug ( "execute: grblCommand=" + grblCommand );

        String simpleCOmmand = null;

        switch ( grblCommand ) {
            case "home":
                simpleCOmmand = "$H";
                break;
            case "unlock":
                simpleCOmmand = "$X";
                break;
            case "check":
                simpleCOmmand = "$C";
                break;

            default:
                if ( grblCommand.startsWith ( "$" ) ) {
                    simpleCOmmand = grblCommand;
                }
                break;
        }

        LOG.debug ( "execute: simpleCommand=" + simpleCOmmand );

        if ( simpleCOmmand != null ) {
            LOG.debug ( "execute: isSuppressLines=" + isSuppressLines );
            if ( isSuppressLines ) {
                gcodeService.sendCommandSuppressInTerminal ( simpleCOmmand );
            }
            else {
                gcodeService.sendCommand ( simpleCOmmand );
            }
        }
    }
	
	@CanExecute
    public boolean canExecute ( ISerialService serial, IGcodeService gcodeService ) {

        LOG.trace ( "canExecute:" );
		
        return serial.isOpen () && !gcodeService.isPlaying () && !gcodeService.isScanning ();

	}
		
}