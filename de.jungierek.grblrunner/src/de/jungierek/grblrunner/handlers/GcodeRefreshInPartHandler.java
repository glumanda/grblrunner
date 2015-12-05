package de.jungierek.grblrunner.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jungierek.grblrunner.service.gcode.IGcodeProgram;
import de.jungierek.grblrunner.service.gcode.IGcodeService;
import de.jungierek.grblrunner.service.serial.ISerialService;

public class GcodeRefreshInPartHandler {

    private static final Logger LOG = LoggerFactory.getLogger ( GcodeRefreshInPartHandler.class );

    @Execute
    public void execute ( IGcodeProgram gcodeProgram ) {

        LOG.debug ( "execute: program=" + gcodeProgram );

        gcodeProgram.loadGcodeProgram ( gcodeProgram.getGcodeProgramFile () );

    }

    @CanExecute
    public boolean canExecute ( ISerialService serial, IGcodeService gcodeService, IGcodeProgram gcodeProgram ) {

        LOG.debug ( "canExecute: program=" + gcodeProgram + " isPLaying=" + gcodeService.isPlaying () + " isscanning=" + gcodeService.isScanning () );

        return gcodeProgram != null && gcodeProgram.isLoaded () && !gcodeService.isPlaying () && !gcodeService.isScanning ()
                && gcodeProgram.getGcodeProgramFile ().isFile ();

    }

}