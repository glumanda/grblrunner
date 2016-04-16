 
package de.jungierek.grblrunner.handlers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.AboutToHide;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.ItemType;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sarxos.webcam.Webcam;

import de.jungierek.grblrunner.constants.ICommandId;
import de.jungierek.grblrunner.service.webcam.IWebcamService;
import de.jungierek.grblrunner.tools.CommandTools;

public class CameraResolutionDynMenuHandler {

    private static final Logger LOG = LoggerFactory.getLogger ( CameraResolutionDynMenuHandler.class );

    @Inject
    private MApplication application;

    @Inject
    private EModelService modelService;

    @Inject
    private IWebcamService webcamService;

    @Inject
    private CommandTools commandTool;

    @PostConstruct
    public void initMenu () {}

    @AboutToShow
    public void aboutToShow ( List<MMenuElement> items ) {

        LOG.debug ( "aboutToShow:" );

        Webcam webcam = webcamService.getWebcam ();

        if ( webcam == null ) {

            MHandledMenuItem item = MMenuFactory.INSTANCE.createHandledMenuItem ();
            item.setLabel ( "no Cam selected ..." );
            items.add ( item );

        }
        else {

            String [] webcamSizes = webcamService.getWebcamSizes ();

            for ( String size : webcamSizes ) {

                LOG.debug ( "size=" + size );

                MCommand command = commandTool.findCommand ( ICommandId.CAMERA_RESOLUTION );

                MParameter parameter = MCommandsFactory.INSTANCE.createParameter ();
                parameter.setElementId ( ICommandId.CAMERA_RESOLUTION_PARAMETER + "." + size );
                parameter.setName ( ICommandId.CAMERA_RESOLUTION_PARAMETER ); // this is the importend "id"
                parameter.setValue ( size );

                MCommandParameter commandParameter = MCommandsFactory.INSTANCE.createCommandParameter ();
                commandParameter.setElementId ( ICommandId.CAMERA_RESOLUTION_PARAMETER );
                commandParameter.setName ( ICommandId.CAMERA_RESOLUTION_PARAMETER );
                commandParameter.setOptional ( true );

                MHandledMenuItem item = MMenuFactory.INSTANCE.createHandledMenuItem ();
                item.setLabel ( size );
                item.setType ( ItemType.RADIO );
                item.setCommand ( command );
                item.getParameters ().add ( parameter );
                if ( size.equals ( webcamService.getWebcamSize () ) ) {
                    item.setSelected ( true );
                }
                items.add ( item );

            }

        }

    }

    @AboutToHide
    public void aboutToHide () {

    }

}