// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2018
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.daw;

import com.bitwig.extension.api.graphics.Bitmap;
import com.bitwig.extension.api.graphics.Image;
import com.bitwig.extension.controller.api.UsbDevice;

import java.nio.ByteBuffer;


/**
 * Interface to the Host.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public interface IHost
{
    /**
     * Get the hosts name.
     *
     * @return The name
     */
    String getName ();


    /**
     * Returns true if the DAW supports a clip based view.
     *
     * @return True if the DAW supports a clip based view
     */
    boolean hasClips ();


    /**
     * Returns true if the DAW supports track/device pinning.
     *
     * @return True if the DAW supports track/device pinning
     */
    boolean hasPinning ();


    /**
     * Returns true if the DAW supports a crossfader.
     *
     * @return True if the DAW supports a crossfader
     */
    boolean hasCrossfader ();


    /**
     * Returns true if the DAW supports Drum Device options.
     *
     * @return True if the DAW supports Drum Device options
     */
    boolean hasDrumDevice ();


    /**
     * Schedules the given task for execution after the given delay.
     *
     * @param task The task to execute
     * @param delay The duration after which the callback function will be called in milliseconds
     */
    void scheduleTask (Runnable task, long delay);


    /**
     * Print the error to the console.
     *
     * @param text The description text
     */
    void error (String text);


    /**
     * Print the exception to the console.
     *
     * @param text The description text
     * @param ex The exception
     */
    void error (String text, Exception ex);


    /**
     * Print a text to the console.
     *
     * @param text The text to print
     */
    void println (String text);


    /**
     * Display a notification in the DAW.
     *
     * @param message The message to display
     */
    void showNotification (String message);


    /**
     * Send a datagram package to the given server. TODO: Remove when USB API is available
     *
     * @param hostAddress The IP address of the server
     * @param port The port
     * @param data The data to send
     */
    void sendDatagramPacket (String hostAddress, int port, byte [] data);


    /**
     * Loads a SVG image. The memory used by this image is guaranteed to be freed once this
     * extension exits.
     *
     * @param imageName The path to the image
     * @param scale The scaling factor
     * @return The loaded SVG image
     */
    Image loadSVG (String imageName, int scale);


    /**
     * Creates an offscreen bitmap that the extension can use to render into. The memory used by
     * this bitmap is guaranteed to be freed once this extension exits.
     *
     * @param width The width of the bitmap
     * @param height The height of the bitmap
     * @return The created bitmap
     */
    Bitmap createBitmap (int width, int height);


    /**
     * Creates a direct byte buffer of the supplied size that is guaranteed to be freed once this
     * extension exits.
     *
     * @param size The size of the buffer
     * @return The created buffer
     */
    ByteBuffer createByteBuffer (int size);


    /**
     * Gets the USB Device at the specified index.
     *
     * @param index The index
     * @return The USB device
     */
    UsbDevice getUsbDevice (int index);
}