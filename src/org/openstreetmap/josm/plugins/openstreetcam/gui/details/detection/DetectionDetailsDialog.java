/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c) 2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.openstreetcam.observer.ClusterObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DetectionChangeObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.RowSelectionObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import com.telenav.josm.common.gui.builder.ContainerBuilder;


/**
 * Defines the logic of the left side "OpenStreetCam Detection Details" panel. The panel displays either the information
 * and corresponding actions of a selected detection or cluster (detection aggregation).
 *
 * @author ioanao
 * @version $Revision$
 */
public final class DetectionDetailsDialog extends ToggleDialog {

    private static final long serialVersionUID = -3824929254682268496L;

    /** preferred size */
    private static final Dimension DIM = new Dimension(150, 120);

    /** dialog default height */
    private static final int DLG_HEIGHT = 120;

    private static final int SCROLL_BAR_UNIT = 100;

    private static DetectionDetailsDialog instance = new DetectionDetailsDialog();

    /** dialog components */
    private JScrollPane cmpInfo;
    private JViewport cmpBtn;
    private DetectionDetailsPanel pnlDetails;
    private ClusterDetailsPanel pnlCluster;
    private DetectionButtonPanel pnlButtons;
    private ClusterButtonPanel pnlClusterButtons;
    private boolean isDetached = false;


    private DetectionDetailsDialog() {
        super(GuiConfig.getInstance().getDetectionDialogTitleName(),
                IconConfig.getInstance().getDetectionDialogShortcutName(),
                GuiConfig.getInstance().getPluginDetectionShortcutLongText(),
                ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getPluginDetectionShortcutText()),
                DLG_HEIGHT, true, PreferenceEditor.class);

        pnlDetails = new DetectionDetailsPanel();
        pnlCluster = new ClusterDetailsPanel();
        pnlButtons = new DetectionButtonPanel();
        pnlButtons.setVisible(false);
        pnlClusterButtons = new ClusterButtonPanel();
        pnlClusterButtons.setVisible(true);
        cmpInfo = ContainerBuilder.buildScrollPane(ContainerBuilder.buildEmptyPanel(Color.WHITE), null, Color.white,
                null, SCROLL_BAR_UNIT, false, DIM);
        cmpBtn = new JViewport();
        add(createLayout(ContainerBuilder.buildBorderLayoutPanel(null, cmpInfo, cmpBtn, null), false, null));

        setPreferredSize(DIM);
    }

    /**
     * Returns the unique instance of the detection details dialog window.
     *
     * @return a {@code DetectionDetailsDialog}
     */
    public static synchronized DetectionDetailsDialog getInstance() {
        if (instance == null) {
            instance = new DetectionDetailsDialog();
        }
        return instance;
    }

    /**
     * Destroys the instance of the dialog.
     */
    public static synchronized void destroyInstance() {
        instance.pnlDetails = null;
        instance.pnlButtons = null;
        instance.pnlCluster = null;
        instance.pnlClusterButtons = null;
        instance.cmpBtn = null;
        instance.cmpInfo = null;
        instance = null;
    }

    /**
     * Registers the observers to the corresponding UI components.
     *
     * @param detectionChangeObserver a {@code DetectionChangeObserver} listens for the detection status/comment changes
     * @param clusterObserver a {@code ClusterObserver} listens for the cluster next/previous photo actions
     * @param rowSelectionObserver a {@code RowSelectionObserver} listens for the selection of a row from the clusters's
     * table
     */
    public void registerObservers(final DetectionChangeObserver detectionChangeObserver,
            final ClusterObserver clusterObserver, final RowSelectionObserver rowSelectionObserver) {
        pnlButtons.registerObserver(detectionChangeObserver);
        pnlClusterButtons.registerObserver(clusterObserver);
        pnlCluster.registerObserver(rowSelectionObserver);

    }

    /**
     * Updates the dialog with a detection information.
     *
     * @param detection a {@code Detection} object
     */
    public void updateDetectionDetails(final Detection detection) {
        setTitle(GuiConfig.getInstance().getDetectionDialogTitleName());
        pnlDetails.updateData(detection);
        cmpInfo.setViewportView(pnlDetails);
        cmpBtn.removeAll();
        cmpBtn.add(pnlButtons);
        if (detection != null) {
            pnlButtons.setVisible(true);
            pnlButtons.enablePanelActions(detection.getEditStatus());
            cmpBtn.setVisible(true);
        } else {
            pnlButtons.setVisible(false);
            cmpBtn.setVisible(false);
            pnlCluster.clearCluster();
        }
        cmpInfo.getViewport().revalidate();
        cmpInfo.revalidate();
        revalidate();
        repaint();
    }

    /**
     * Updates the dialog with a cluster information.
     *
     * @param cluster a {@code Cluster} object
     * @param detection a {@code Detection} object
     */
    public void updateClusterDetails(final Cluster cluster, final Detection detection) {
        if (cluster != null && cluster.equals(pnlCluster.getCluster())) {
            // only the selected detection changed
            pnlCluster.addSelectedDetectionToTable(detection);
            pnlCluster.invalidate();
        } else {
            setTitle(GuiConfig.getInstance().getClusterDialogTitleName());
            pnlCluster.updateData(cluster);
            pnlCluster.addSelectedDetectionToTable(detection);
            pnlCluster.revalidate();
            cmpInfo.setViewportView(pnlCluster);
            pnlClusterButtons.updateUI(cluster);
            cmpBtn.removeAll();
            cmpBtn.add(pnlClusterButtons);
            final boolean enableButtonPanel = cluster != null;
            pnlClusterButtons.setVisible(enableButtonPanel);
            cmpBtn.setVisible(enableButtonPanel);
            cmpInfo.getViewport().revalidate();
        }
        cmpInfo.revalidate();
        revalidate();
        repaint();
    }

    public void clearDetailsDialog() {
        updateDetectionDetails(null);
        updateClusterDetails(null, null);
    }


    @Override
    public void expand() {
        showDialog();
        if (getButton() != null && !getButton().isSelected()) {
            getButton().setSelected(true);
        }
        DetectionDetailsDialog.getInstance().getButton().setSelected(true);
        if (isCollapsed) {
            super.expand();
        }
    }

    /**
     * It is called when the detached dialog is opened.
     */
    @Override
    protected void detach() {
        if (!isDetached) {
            super.detach();
            isDetached = true;
        }
    }

    @Override
    public void hideDialog() {
        super.hideDialog();
        isDetached = false;
    }
}