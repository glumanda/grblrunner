package de.jungierek.grblrunner.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jungierek.grblrunner.constants.IContextKey;
import de.jungierek.grblrunner.parts.groups.ControlAutolevelGroup;
import de.jungierek.grblrunner.parts.groups.ControlMoveGroup;
import de.jungierek.grblrunner.parts.groups.ControlProbeGroup;
import de.jungierek.grblrunner.parts.groups.ControlSpindleGroup;
import de.jungierek.grblrunner.parts.groups.StateCoolantGroup;
import de.jungierek.grblrunner.parts.groups.StateCoordinatesGroup;
import de.jungierek.grblrunner.parts.groups.StateDistanceGroup;
import de.jungierek.grblrunner.parts.groups.StateFeedrateGroup;
import de.jungierek.grblrunner.parts.groups.StateGroup;
import de.jungierek.grblrunner.parts.groups.StateModalModeGroup;
import de.jungierek.grblrunner.parts.groups.StatePlaneGroup;
import de.jungierek.grblrunner.parts.groups.StateSpindleGroup;
import de.jungierek.grblrunner.parts.groups.StateToolGroup;
import de.jungierek.grblrunner.parts.groups.StateUnitGroup;

public class GrblPart {

    private static final Logger LOG = LoggerFactory.getLogger ( GrblPart.class );

    @Inject
    @Named(IServiceConstants.ACTIVE_SHELL)
    private Shell shell;

    // prevent groups from garbage collection
    private StateCoordinatesGroup stateCoordinatesGroup;
    private StateGroup stateGroup;
    private StateFeedrateGroup stateFeedrateGroup;
    private StateToolGroup stateToolGroup;
    private StateSpindleGroup stateSpindleGroup;
    private StateCoolantGroup stateCoolantGroup;
    private StateModalModeGroup stateModalModeGroup;
    private StatePlaneGroup statePlaneGroup;
    private StateUnitGroup stateUnitsGroup;
    private StateDistanceGroup stateDistanceGroup;
    private ControlMoveGroup controlMoveGroup;
    private ControlSpindleGroup controlSpindleGroup;
    private ControlProbeGroup controlProbeGroup;
    private ControlAutolevelGroup controlAutolevelGroup;

    @Inject
    public GrblPart () {}

    @PostConstruct
    public void createGui ( Composite parent, IEclipseContext context ) {

        LOG.debug ( "createGui:" );

        final int cols = 4;
        parent.setLayout ( new GridLayout ( cols, true ) );

        // collect groups
        context.set ( IContextKey.PART_COLS, cols );

        // state
        context.set ( IContextKey.PART_GROUP_COLS, cols );
        context.set ( IContextKey.PART_GROUP_ROWS, 1 );
        stateGroup = ContextInjectionFactory.make ( StateGroup.class, context );

        // coordinates
        context.set ( IContextKey.PART_GROUP_COLS, cols );
        context.set ( IContextKey.PART_GROUP_ROWS, 3 );
        stateCoordinatesGroup = ContextInjectionFactory.make ( StateCoordinatesGroup.class, context );

        // XY
        context.set ( IContextKey.PART_GROUP_COLS, cols / 2 );
        context.set ( IContextKey.PART_GROUP_ROWS, 2 );
        controlMoveGroup = ContextInjectionFactory.make ( ControlMoveGroup.class, context );

        // spindle
        context.set ( IContextKey.PART_GROUP_COLS, cols / 2 );
        context.set ( IContextKey.PART_GROUP_ROWS, 1 );
        controlSpindleGroup = ContextInjectionFactory.make ( ControlSpindleGroup.class, context );

        // probe
        context.set ( IContextKey.PART_GROUP_COLS, cols / 2 );
        context.set ( IContextKey.PART_GROUP_ROWS, 1 );
        controlProbeGroup = ContextInjectionFactory.make ( ControlProbeGroup.class, context );

        // all following groups allocating only 1 column
        context.set ( IContextKey.PART_GROUP_COLS, 1 );

        // feedrate
        stateFeedrateGroup = ContextInjectionFactory.make ( StateFeedrateGroup.class, context );

        // tool
        stateToolGroup = ContextInjectionFactory.make ( StateToolGroup.class, context );

        // spindle
        stateSpindleGroup = ContextInjectionFactory.make ( StateSpindleGroup.class, context );

        // coolant
        stateCoolantGroup = ContextInjectionFactory.make ( StateCoolantGroup.class, context );

        // modal mode
        stateModalModeGroup = ContextInjectionFactory.make ( StateModalModeGroup.class, context );

        // Plane
        statePlaneGroup = ContextInjectionFactory.make ( StatePlaneGroup.class, context );

        // Units
        stateUnitsGroup = ContextInjectionFactory.make ( StateUnitGroup.class, context );

        // Distance
        stateDistanceGroup = ContextInjectionFactory.make ( StateDistanceGroup.class, context );

        // scan
        context.set ( IContextKey.PART_GROUP_COLS, cols );
        context.set ( IContextKey.PART_GROUP_ROWS, 1 );
        controlAutolevelGroup = ContextInjectionFactory.make ( ControlAutolevelGroup.class, context );

    }
    
}