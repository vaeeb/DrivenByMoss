// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.launchpad.view;

import de.mossgrabers.controller.launchpad.LaunchpadConfiguration;
import de.mossgrabers.controller.launchpad.controller.LaunchpadColorManager;
import de.mossgrabers.controller.launchpad.controller.LaunchpadControlSurface;
import de.mossgrabers.framework.controller.ButtonID;
import de.mossgrabers.framework.controller.grid.IPadGrid;
import de.mossgrabers.framework.controller.grid.IVirtualFader;
import de.mossgrabers.framework.controller.grid.IVirtualFaderCallback;
import de.mossgrabers.framework.controller.grid.VirtualFaderImpl;
import de.mossgrabers.framework.daw.DAWColor;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.ITrackBank;
import de.mossgrabers.framework.daw.data.ISend;
import de.mossgrabers.framework.daw.data.ITrack;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.framework.view.AbstractView;


/**
 * A view for mixing with track select, mute, solo, rec arm, stop clip, volume and panorama.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class MixView extends AbstractView<LaunchpadControlSurface, LaunchpadConfiguration> implements IVirtualFaderCallback
{
    private final IVirtualFader fader;


    private enum FaderMode
    {
        VOLUME,
        PAN,
        SEND1,
        SEND2
    }


    private FaderMode faderMode = FaderMode.VOLUME;


    /**
     * Constructor.
     *
     * @param surface The surface
     * @param model The model
     */
    public MixView (final LaunchpadControlSurface surface, final IModel model)
    {
        super ("Mix", surface, model);

        this.fader = new VirtualFaderImpl (model.getHost (), this);
    }


    /** {@inheritDoc} */
    @Override
    public void drawGrid ()
    {
        final IPadGrid padGrid = this.surface.getPadGrid ();
        final ITrackBank tb = this.model.getCurrentTrackBank ();
        for (int i = 0; i < 8; i++)
        {
            final ITrack track = tb.getItem (i);
            if (track.doesExist ())
            {
                final boolean isSelected = track.isSelected ();
                final boolean hasSends = track.getSendBank ().getItemCount () > 0;

                // Volume
                padGrid.light (92 + i, this.colorManager.getColorIndex (DAWColor.getColorIndex (track.getColor ())));
                // Panorama
                padGrid.light (84 + i, isSelected ? LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_GREY_LO);
                // Send 1
                padGrid.light (76 + i, hasSends ? isSelected ? LaunchpadColorManager.LAUNCHPAD_COLOR_ORCHID_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_GREY_LO : LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
                // Send 2
                padGrid.light (68 + i, hasSends ? isSelected ? LaunchpadColorManager.LAUNCHPAD_COLOR_LIME_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_GREY_LO : LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
                // Stop
                padGrid.light (60 + i, this.surface.isPressed (ButtonID.get (ButtonID.PAD25, i)) ? LaunchpadColorManager.LAUNCHPAD_COLOR_RED : LaunchpadColorManager.LAUNCHPAD_COLOR_ROSE);
                // Mute
                padGrid.light (52 + i, track.isMute () ? LaunchpadColorManager.LAUNCHPAD_COLOR_GREY_LO : LaunchpadColorManager.LAUNCHPAD_COLOR_YELLOW_HI);
                // Solo
                padGrid.light (44 + i, track.isSolo () ? LaunchpadColorManager.LAUNCHPAD_COLOR_BLUE_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_GREY_MD);
                // Rec Arm
                padGrid.light (36 + i, track.isRecArm () ? LaunchpadColorManager.LAUNCHPAD_COLOR_RED_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_GREY_LO);
            }
            else
            {
                padGrid.light (92 + i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
                padGrid.light (84 + i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
                padGrid.light (76 + i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
                padGrid.light (68 + i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
                padGrid.light (60 + i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
                padGrid.light (52 + i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
                padGrid.light (44 + i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
                padGrid.light (36 + i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
            }
        }
    }


    /** {@inheritDoc} */
    @Override
    public void onGridNote (final int note, final int velocity)
    {
        if (velocity == 0)
            return;

        final int n = note - 36;
        final int index = n % 8;
        final int what = n / 8;

        final ITrack track = this.model.getCurrentTrackBank ().getItem (index);

        switch (what)
        {
            case 7:
                this.faderMode = FaderMode.VOLUME;
                track.select ();
                break;

            case 6:
                this.faderMode = FaderMode.PAN;
                track.select ();
                break;

            case 5:
                this.faderMode = FaderMode.SEND1;
                track.select ();
                break;

            case 4:
                this.faderMode = FaderMode.SEND2;
                track.select ();
                break;

            case 3:
                track.stop ();
                break;

            case 2:
                track.toggleMute ();
                break;

            case 1:
                track.toggleSolo ();
                break;

            case 0:
                track.toggleRecArm ();
                break;

            default:
                // Not used
                break;
        }
    }


    /** {@inheritDoc} */
    @Override
    public int getButtonColor (final ButtonID buttonID)
    {
        final int index = 7 - (buttonID.ordinal () - ButtonID.SCENE1.ordinal ());

        int color = 0;
        int value = 0;

        final ITrack track = this.model.getCurrentTrackBank ().getSelectedItem ();
        if (track != null)
        {
            switch (this.faderMode)
            {
                default:
                case VOLUME:
                    value = track.getVolume ();
                    color = LaunchpadColorManager.LAUNCHPAD_COLOR_CYAN;
                    break;
                case PAN:
                    value = track.getPan ();
                    color = LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_HI;
                    break;
                case SEND1:
                    final ISend send1 = track.getSendBank ().getItem (0);
                    value = send1.doesExist () ? send1.getValue () : 0;
                    color = LaunchpadColorManager.LAUNCHPAD_COLOR_ORCHID_HI;
                    break;
                case SEND2:
                    final ISend send2 = track.getSendBank ().getItem (1);
                    value = send2.doesExist () ? send2.getValue () : 0;
                    color = LaunchpadColorManager.LAUNCHPAD_COLOR_LIME_HI;
                    break;
            }
        }

        this.fader.setup (color, this.faderMode == FaderMode.PAN);
        this.fader.setValue (value);

        return this.fader.getColorState (index);
    }


    /** {@inheritDoc} */
    @Override
    public void onButton (final ButtonID buttonID, final ButtonEvent event, final int velocity)
    {
        if (!ButtonID.isSceneButton (buttonID) || event != ButtonEvent.DOWN)
            return;
        final ITrack track = this.model.getCurrentTrackBank ().getSelectedItem ();
        if (track == null)
            return;

        final int index = buttonID.ordinal () - ButtonID.SCENE1.ordinal ();
        this.fader.moveTo (7 - index, velocity);
    }


    /** {@inheritDoc} */
    @Override
    public int getValue ()
    {
        final ITrack track = this.model.getCurrentTrackBank ().getSelectedItem ();
        if (track == null)
            return 0;
        switch (this.faderMode)
        {
            default:
            case VOLUME:
                return track.getVolume ();
            case PAN:
                return track.getPan ();
            case SEND1:
                final ISend send1 = track.getSendBank ().getItem (0);
                return send1.doesExist () ? send1.getValue () : 0;
            case SEND2:
                final ISend send2 = track.getSendBank ().getItem (1);
                return send2.doesExist () ? send2.getValue () : 0;
        }
    }


    /** {@inheritDoc} */
    @Override
    public void setValue (final int value)
    {
        final ITrack track = this.model.getCurrentTrackBank ().getSelectedItem ();
        if (track == null)
            return;
        switch (this.faderMode)
        {
            default:
            case VOLUME:
                track.setVolume (value);
                break;
            case PAN:
                track.setPan (value);
                break;
            case SEND1:
                final ISend send1 = track.getSendBank ().getItem (0);
                if (send1.doesExist ())
                    send1.setValue (value);
                break;
            case SEND2:
                final ISend send2 = track.getSendBank ().getItem (1);
                if (send2.doesExist ())
                    send2.setValue (value);
                break;
        }
    }
}