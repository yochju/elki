package experimentalcode.students.roedler.parallelCoordinates.visualizer;

import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.result.HierarchicalResult;
import de.lmu.ifi.dbs.elki.result.Result;
import de.lmu.ifi.dbs.elki.result.ResultUtil;
import de.lmu.ifi.dbs.elki.utilities.iterator.IterableIterator;
import de.lmu.ifi.dbs.elki.visualization.VisualizationTask;
import de.lmu.ifi.dbs.elki.visualization.css.CSSClassManager.CSSNamingConflict;
import de.lmu.ifi.dbs.elki.visualization.svg.SVGSimpleLinearAxis;
import de.lmu.ifi.dbs.elki.visualization.visualizers.AbstractVisFactory;
import de.lmu.ifi.dbs.elki.visualization.visualizers.Visualization;
import experimentalcode.students.roedler.parallelCoordinates.projector.ParallelPlotProjector;

/**
 * Generates a SVG-Element containing axes, including labeling.
 * 
 * @author Robert Rödler
 * 
 * @apiviz.uses SVGSimpleLinearAxis
 */
public class ParallelAxisVisualization extends ParallelVisualization<NumberVector<?, ?>> {
  /**
   * Constructor.
   * 
   * @param task VisualizationTask
   */
  public ParallelAxisVisualization(VisualizationTask task) {
    super(task);
    incrementalRedraw();
    context.addResultListener(this);
  }
  
  @Override
  public void destroy() {
    context.removeResultListener(this);
    super.destroy();
  }

  @Override
  protected void redraw() {
    int dim = proj.getVisibleDimensions();
    recalcAxisPositions();

    try {
      for(int i = 0; i < dim; i++) {
        boolean inv = proj.isAxisInverted(i);
        if(!inv) {
          SVGSimpleLinearAxis.drawAxis(svgp, layer, proj.getAxisScale(i), getAxisX(i), getAxisHeight(), getAxisX(i), 0, SVGSimpleLinearAxis.LabelStyle.ENDLABEL, context.getStyleLibrary());
        }
        else {
          SVGSimpleLinearAxis.drawAxis(svgp, layer, proj.getAxisScale(i), getAxisX(i), 0, getAxisX(i), getAxisHeight(), SVGSimpleLinearAxis.LabelStyle.ENDLABEL, context.getStyleLibrary());
        }
      }
    }
    catch(CSSNamingConflict e) {
      throw new RuntimeException("Conflict in CSS naming for axes.", e);
    }
  }

  /**
   * Factory for axis visualizations
   * 
   * @author Robert Rödler
   * 
   * @apiviz.stereotype factory
   * @apiviz.uses ParallelAxisVisualization oneway - - «create»
   */
  public static class Factory extends AbstractVisFactory {
    /**
     * A short name characterizing this Visualizer.
     */
    private static final String NAME = "Parallel Axes";

    /**
     * Constructor, adhering to
     * {@link de.lmu.ifi.dbs.elki.utilities.optionhandling.Parameterizable}
     */
    public Factory() {
      super();
    }

    @Override
    public Visualization makeVisualization(VisualizationTask task) {
      return new ParallelAxisVisualization(task);
    }

    @Override
    public void processNewResult(HierarchicalResult baseResult, Result result) {
      IterableIterator<ParallelPlotProjector<?>> ps = ResultUtil.filteredResults(result, ParallelPlotProjector.class);
      for(ParallelPlotProjector<?> p : ps) {
        final VisualizationTask task = new VisualizationTask(NAME, p, p.getRelation(), this);
        task.put(VisualizationTask.META_LEVEL, VisualizationTask.LEVEL_BACKGROUND);
        baseResult.getHierarchy().add(p, task);
      }
    }

    @Override
    public boolean allowThumbnails(VisualizationTask task) {
      // Don't use thumbnails
      return true;
    }
  }
}