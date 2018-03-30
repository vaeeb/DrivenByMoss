// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2018
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.push.command.trigger;

import de.mossgrabers.framework.command.core.AbstractTriggerCommand;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.mode.ModeManager;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.push.PushConfiguration;
import de.mossgrabers.push.controller.PushControlSurface;
import de.mossgrabers.push.mode.Modes;


/**
 * Command to edit the Volume of tracks.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class VolumeCommand extends AbstractTriggerCommand<PushControlSurface, PushConfiguration>
{
    /**
     * Constructor.
     *
     * @param model The model
     * @param surface The surface
     */
    public VolumeCommand (final IModel model, final PushControlSurface surface)
    {
        super (model, surface);
    }


    /** {@inheritDoc} */
    @Override
    public void execute (final ButtonEvent event)
    {
        if (event != ButtonEvent.DOWN)
            return;

        final ModeManager modeManager = this.surface.getModeManager ();
        final Integer currentMode = modeManager.getActiveModeId ();

        // Layer mode selection for Push 1
        final PushConfiguration config = this.surface.getConfiguration ();
        if (!config.isPush2 () && this.surface.isSelectPressed () && Modes.isLayerMode (currentMode))
        {
            modeManager.setActiveMode (Modes.MODE_DEVICE_LAYER_VOLUME);
            return;
        }

        if (Modes.MODE_VOLUME.equals (currentMode))
            modeManager.setActiveMode (Modes.MODE_CROSSFADER);
        else
            modeManager.setActiveMode (Modes.MODE_VOLUME);
    }
}
