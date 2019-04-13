import org.graphstream.ui.fx_viewer.FxViewPanel;
import util.Node;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class GFXManager {

    private GraphWindow graphWindow;
    private ScheduledFuture task;

    private util.Graph graphData;
    private String stylesPath;
    private boolean hasCompleted;

    private Set<Node> scheduledNodes;

    public GFXManager(util.Graph graphData, String stylesPath) {
        this.graphData = graphData;
        this.stylesPath = stylesPath;
        this.hasCompleted = false;
        this.scheduledNodes = new HashSet<>();
    }

    public FxViewPanel createGraph() {
        graphWindow = new GraphWindow(graphData, stylesPath);
        generateGraph();
        registerUpdater();
        return graphWindow.getViewPanel();
    }

    public synchronized void updateNodes(Set<Node> scheduledNodes) {
        this.scheduledNodes = scheduledNodes;
    }

    private synchronized Set<Node> getScheduledNodes() {
        return scheduledNodes;
    }

    /**
     * Once called, this method will periodically update the GUI window which
     * visualises the state space search done by the scheduler.
     */
    private void registerUpdater() {
        Runnable updateSchedule = () -> {
            if (!hasCompleted) {
                graphWindow.drawHighlighting(getScheduledNodes());
            } else {
                graphWindow.drawHighlighting(getScheduledNodes());
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
