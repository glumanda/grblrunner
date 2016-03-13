package de.jungierek.grblrunner.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jungierek.grblrunner.constants.IConstants;
import de.jungierek.grblrunner.constants.IContextKey;
import de.jungierek.grblrunner.parts.groups.GcodeViewGroup;

public class GcodeViewPart {

    private static final Logger LOG = LoggerFactory.getLogger ( GcodeViewPart.class );

    @Inject
    @Preference(nodePath = IConstants.PREFERENCE_NODE)
    private IEclipsePreferences preferences;

    // prevent from garbage collection
    @SuppressWarnings("unused")
    private GcodeViewGroup gcodeViewGroup;

    @Inject
    public GcodeViewPart () {}

    @PostConstruct
    public void createGui ( Composite parent, IEclipseContext context ) {
        
        LOG.debug ( "createGui:" );

        final int cols = 1;
        parent.setLayout ( new GridLayout ( cols, true ) );
        context.set ( IContextKey.KEY_PART_COLS, cols );
        context.set ( IContextKey.KEY_PART_GROUP_COLS, 1 ); // all groups have a width of 1 column

        // collect groups
        gcodeViewGroup = ContextInjectionFactory.make ( GcodeViewGroup.class, context );

    }

    public GcodeViewGroup getGcodeViewGroup () {

        return gcodeViewGroup;

    }
    
    @PersistState
    public void persistState () {

        LOG.debug ( "persistState:" );

        gcodeViewGroup.savePersistedState ();

    }
    
}