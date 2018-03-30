// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2018
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.sl;

import de.mossgrabers.framework.controller.DefaultControllerDefinition;

import java.util.UUID;


/**
 * Definition class for the Novation SL controller extension.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class SLControllerDefinition extends DefaultControllerDefinition
{
    private static final UUID EXTENSION_ID_MK_I  = UUID.fromString ("A9041F50-0407-11E5-B939-0800200C9A66");
    private static final UUID EXTENSION_ID_MK_II = UUID.fromString ("D1CEE920-1E51-11E4-8C21-0800200C9A66");


    /**
     * Constructor.
     *
     * @param isMkII True if is Mk II other Mk I
     */
    public SLControllerDefinition (final boolean isMkII)
    {
        super ("SLMkII4Bitwig", "Jürgen Moßgraber", "5.10", isMkII ? EXTENSION_ID_MK_II : EXTENSION_ID_MK_I, isMkII ? "SL MkII" : "SL MkI", "Novation", 2, 1);
    }
}
