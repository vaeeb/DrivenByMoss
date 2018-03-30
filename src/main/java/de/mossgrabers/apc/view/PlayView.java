// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2018
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.apc.view;

import de.mossgrabers.apc.APCConfiguration;
import de.mossgrabers.apc.controller.APCControlSurface;
import de.mossgrabers.framework.controller.color.ColorManager;
import de.mossgrabers.framework.daw.IChannelBank;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.data.ITrack;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.framework.view.AbstractPlayView;
import de.mossgrabers.framework.view.SceneView;


/**
 * The play view.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class PlayView extends AbstractPlayView<APCControlSurface, APCConfiguration> implements SceneView
{
    /**
     * Constructor.
     *
     * @param surface The surface
     * @param model The model
     */
    public PlayView (final APCControlSurface surface, final IModel model)
    {
        super ("Play", surface, model, surface.isMkII ());
    }


    /** {@inheritDoc} */
    @Override
    public void onGridNote (final int note, final int velocity)
    {
        if (!this.model.canSelectedTrackHoldNotes () || this.noteMap[note] == -1)
            return;
        // Mark selected notes
        this.setPressedKeys (this.noteMap[note], velocity);
        this.surface.sendMidiEvent (0x90, this.noteMap[note], velocity);
    }


    /** {@inheritDoc} */
    @Override
    public void updateSceneButtons ()
    {
        this.surface.updateButton (APCControlSurface.APC_BUTTON_SCENE_LAUNCH_1, ColorManager.BUTTON_STATE_ON);
        this.surface.updateButton (APCControlSurface.APC_BUTTON_SCENE_LAUNCH_2, ColorManager.BUTTON_STATE_ON);
        this.surface.updateButton (APCControlSurface.APC_BUTTON_SCENE_LAUNCH_3, ColorManager.BUTTON_STATE_OFF);
        this.surface.updateButton (APCControlSurface.APC_BUTTON_SCENE_LAUNCH_4, ColorManager.BUTTON_STATE_ON);
        this.surface.updateButton (APCControlSurface.APC_BUTTON_SCENE_LAUNCH_5, ColorManager.BUTTON_STATE_ON);
    }


    /** {@inheritDoc} */
    @Override
    public void updateArrows ()
    {
        final IChannelBank tb = this.model.getCurrentTrackBank ();
        final ITrack sel = tb.getSelectedTrack ();
        this.canScrollLeft = sel != null && sel.getIndex () > 0 || tb.canScrollTracksUp ();
        this.canScrollRight = sel != null && sel.getIndex () < 7 || tb.canScrollTracksDown ();

        super.updateArrows ();
    }


    /** {@inheritDoc} */
    @Override
    public void onScene (final int scene, final ButtonEvent event)
    {
        if (event != ButtonEvent.DOWN)
            return;

        switch (scene)
        {
            case 0:
                this.scales.nextScale ();
                this.updateScale ();
                break;

            case 1:
                this.scales.prevScale ();
                this.updateScale ();
                break;

            case 2:
                this.scales.toggleChromatic ();
                final boolean isChromatic = this.scales.isChromatic ();
                this.surface.getConfiguration ().setScaleInKey (!isChromatic);
                this.surface.getDisplay ().notify (isChromatic ? "Chromatic" : "In Key");
                break;

            case 3:
                this.onOctaveUp (event);
                break;

            case 4:
                this.onOctaveDown (event);
                break;
        }
        this.updateNoteMapping ();
    }


    private void updateScale ()
    {
        final String name = this.scales.getScale ().getName ();
        this.surface.getConfiguration ().setScale (name);
        this.surface.getDisplay ().notify (name);
    }
}