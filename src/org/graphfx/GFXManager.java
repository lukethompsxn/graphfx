package org.graphfx;

import org.graphstream.ui.fx_viewer.FxViewPanel;

import org.graphfx.util.GFXGraph;
import org.graphfx.util.GraphWindow;
import java.util.concurrent.*;

/**
 * @author Luke Thompson
 */
public class GFXManager {

    private GraphWindow graphWindow;
    private ScheduledFuture task;

    private GFXGraph graphData;
    private GFXStateManager stateManager;
    private String stylesPath;
    private boolean hasCompleted;

    public GFXManager(GFXGraph graphData, GFXStateManager stateManager, String stylesPath) {
        this.graphData = graphData;
        this.stateManager = stateManager;
        this.stylesPath = stylesPath;
        this.hasCompleted = false;
    }

    /**
     * This method is used to create the actual graph. It sends off the building
     * of the graph on another thread, registers the updater service and returns
     * the actual JavaFX panel.
     *
     * @return FxViewPanel for use in the JavaFX application
     */
    public FxViewPanel createGraph() {
        graphWindow = new GraphWindow(graphData, stylesPath);
        generateGraph();
        registerUpdater();
        return graphWindow.getViewPanel();
    }

    public void setCompleted() {
        hasCompleted = true;
    }

    /**
     * Once called, this method will periodically update the GUI window which
     * visualises the state space search done by the scheduler.
     */
    private void registerUpdater() {
        Runnable updateSchedule = () -> {
            if (!hasCompleted) {
                graphWindow.drawHighlighting(stateManager.getCurrentNodes());
            } else {
                graphWindow.drawHighlighting(stateManager.getOptimalNodes());
                task.cancel(true);
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        task = executor.scheduleAtFixedRate(updateSchedule, 1000, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * Builds the tree graph on a separate thread
     */
    private void generateGraph() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(graphWindow::drawTree);
        executorService.shutdown();
    }
}
