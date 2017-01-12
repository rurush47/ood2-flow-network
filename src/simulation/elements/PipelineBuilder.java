package simulation.elements;

import simulation.Settings;
import util.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PipelineBuilder {
    private List<Point> points = new ArrayList<>();
    private Input input;
    private Output output;
    private Settings settings;

    public void addPoint(Point point) {
        points.add(point);
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public boolean hasOutput() {
        return output != null;
    }

    public Pipeline build() {
        if (input == null || output == null) {
            throw new NullPointerException("Pipeline input or output is null");
        }
        if (settings == null) {
            throw new NullPointerException("Pipeline settings are null");
        }

        return new Pipeline(output, input, points, settings);
    }

    public void render(Graphics g) {
        Point a, b;
        for (int i = 1; i < points.size(); i++) {
            a = points.get(i - 1);
            b = points.get(i);
            g.drawLine(a.x, a.y, b.x, b.y);
        }
    }
}
