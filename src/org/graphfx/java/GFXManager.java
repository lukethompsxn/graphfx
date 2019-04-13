import org.graphstream.ui.fx_viewer.FxViewPanel;
import util.GFXGraph;
import util.GraphWindow;

import java.util.concurrent.*;

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

    public FxViewPanel createGraph() {
        graphWindow = new GraphWindow(graphData, stylesPath);
        generateGraph();
        registerUpdater();
        return graphWindow.getViewPanel();
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
