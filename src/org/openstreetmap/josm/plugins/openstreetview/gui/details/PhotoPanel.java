/*
 *  Copyright 2016 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.openstreetview.gui.details;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.ServiceConfig;
import org.openstreetmap.josm.tools.Pair;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Displays the selected image.
 *
 * @author Beata
 * @version $Revision$
 */
class PhotoPanel extends JPanel implements MouseWheelListener {

    private static final long serialVersionUID = -1550900781158007580L;
    private static final int MAX_ZOOM = 5;

    /** the current image */
    private BufferedImage image;

    /** a rectangle where the image is incorporated related to the outside panel */
    private Rectangle frame;

    /** a part of the image that is currently displayed */
    private Rectangle currentView;

    PhotoPanel() {
        super(new BorderLayout());
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.gray));
        this.addMouseWheelListener(this);
    }


    @Override
    public void paintComponent(final Graphics g) {

        if (image != null) {
            // clean the panel
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            // draw the image
            matchImageOnPanel(g);
        }
    }

    @Override
    public synchronized void mouseWheelMoved(final MouseWheelEvent e) {
        if (image != null) {
            // get the mouse position on the image (avoid the empty space around image)
            final int x = currentView.x + (e.getX() - frame.x) * currentView.width / frame.width;
            final int y = currentView.y + (e.getY() - frame.y) * currentView.height / frame.height;
            zoom(x, y, e.getWheelRotation());
            PhotoPanel.this.repaint();
        }
    }

    void updateUI(final String photoName) {
        removeAll();
        if (photoName != null) {
            final StringBuilder link = new StringBuilder(ServiceConfig.getInstance().getBaseUrl());
            link.append(photoName);
            try {
                image = ImageIO.read(new URL(link.toString()));
                currentView = new Rectangle(0, 0, image.getWidth(), image.getHeight());
            } catch (final IOException e) {
                add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPhotoErrorTxt(),
                        getFont().deriveFont(Font.BOLD, 12), Color.white), BorderLayout.CENTER);
            }
        } else {
            image = null;
            currentView = null;
            frame = null;
        }
    }

    /** The method match the current view of the picture in the panel. */
    private void matchImageOnPanel(final Graphics g) {
        int imageWidth = this.getWidth();
        int imageHeight = (this.getWidth() * currentView.height) / currentView.width;
        int marginLeft = 0;
        int marginTop = (this.getHeight() - imageHeight) / 2;

        if (imageHeight > this.getHeight()) {
            imageHeight = this.getHeight();
            imageWidth = (this.getHeight() * currentView.width) / currentView.height;
            marginLeft = (this.getWidth() - imageWidth) / 2;
            marginTop = 0;
        }
        frame = new Rectangle(marginLeft, marginTop, imageWidth, imageHeight);
        g.drawImage(image, marginLeft, marginTop, marginLeft + imageWidth, marginTop + imageHeight, currentView.x,
                currentView.y, currentView.x + currentView.width, currentView.y + currentView.height, null);
    }

    private void zoom(final int xMouseCoord, final int yMouseCoord, final int wheelRotation) {
        final Pair<Integer, Integer> vertical;
        final Pair<Integer, Integer> horizontal;

        if (this.getWidth() > this.getHeight()) {
            vertical = getImageFixedDimension(yMouseCoord, currentView.y, currentView.height,
                    image.getHeight(), wheelRotation);

            final int newWidth = (vertical.b - vertical.a) * this.getWidth() / this.getHeight();
            horizontal = getImageRelativeDimension(xMouseCoord, currentView.x, currentView.width, newWidth,
                    image.getWidth(), wheelRotation);
        } else {
            horizontal = getImageFixedDimension(xMouseCoord, currentView.x, currentView.width,
                    image.getWidth(), wheelRotation);

            final int newHeight = (horizontal.b - horizontal.a) * this.getHeight() / this.getWidth();
            vertical = getImageRelativeDimension(yMouseCoord, currentView.y, currentView.height, newHeight,
                    image.getHeight(), wheelRotation);
        }

        if ((horizontal.b - horizontal.a > image.getWidth() / MAX_ZOOM)
                && (vertical.b - vertical.a > image.getHeight() / MAX_ZOOM)) {
            currentView = new Rectangle(horizontal.a, vertical.a, horizontal.b - horizontal.a, vertical.b - vertical.a);
        }
    }

    private Pair<Integer, Integer> getPart(final int mouseCoord, final int firstReference, final int secondReference,
            final int croppedSize) {

        final int firstPartMargin = mouseCoord - croppedSize;
        final int secondPartMargin = mouseCoord + croppedSize;

        final Pair<Integer, Integer> result = new Pair<>(firstPartMargin, secondPartMargin);
        result.a = firstPartMargin;
        result.b = secondPartMargin;

        if (firstPartMargin < firstReference) {
            result.a = firstReference;
            result.b = result.b - firstPartMargin + firstReference;
        }

        if (secondPartMargin > secondReference) {
            result.a = result.a - secondPartMargin + secondReference;
            result.b = secondReference;
        }

        if (result.a < firstReference) {
            result.a = firstReference;
        }
        if (result.b > secondReference) {
            result.b = secondReference;
        }
        return result;
    }

    private Pair<Integer, Integer> getImageFixedDimension(final int mouseCoord, final int currentViewMinCoord,
            final int currentViewDimension, final int imageDimension, final int wheelRotation) {
        Pair<Integer, Integer> pair;
        if (wheelRotation < 0) {
            pair = getPart(mouseCoord, currentViewMinCoord, currentViewMinCoord + currentViewDimension,
                    (currentViewDimension * 4 / 5) / 2);
        } else {
            if (currentViewDimension != imageDimension) {
                final int dif = (currentViewDimension * 5 / 4) / 2;
                int firstReference = currentViewMinCoord - dif;
                int secondReference = currentViewMinCoord + currentViewDimension + dif;
                if (firstReference < 0) {
                    firstReference = 0;
                }
                if (secondReference > imageDimension) {
                    secondReference = imageDimension;
                }
                pair = getPart(mouseCoord, firstReference, secondReference, dif);
            } else {
                pair = new Pair<>(0, imageDimension);
            }
        }

        return pair;
    }

    private Pair<Integer, Integer> getImageRelativeDimension(final int mouseCoord,
            final int currentViewMinCoord, final int currentViewDimension, final int newDimension, final int imageDimension,
            final int wheelRotation) {
        Pair<Integer, Integer> pair;
        int dimension = newDimension;
        if (wheelRotation < 0) {  // zoom in
            if (dimension > currentViewDimension) {
                dimension = currentViewDimension;
            }
            pair = getPart(mouseCoord, currentViewMinCoord, currentViewMinCoord + currentViewDimension,
                    dimension / 2);
        } else {                  // zoom out
            if (currentViewDimension != imageDimension) {
                if (dimension > imageDimension) {
                    dimension = imageDimension;
                }
                int firstReference = currentViewMinCoord - (dimension / 2 - currentViewDimension / 2);
                int secondReference =
                        currentViewMinCoord + currentViewDimension + (dimension / 2 - currentViewDimension / 2);
                if (firstReference < 0) {
                    firstReference = 0;
                }
                if (secondReference > imageDimension) {
                    secondReference = imageDimension;
                }
                pair = getPart(mouseCoord, firstReference, secondReference, dimension / 2);
            } else {
                pair = new Pair<>(0, imageDimension);
            }
        }
        return pair;
    }

}