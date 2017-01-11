package simulation.elements;

import ui.ImageLibrary;
import util.Point;

import java.awt.*;

public class AdjustableSplitter extends Splitter implements java.io.Serializable{


    public AdjustableSplitter(Float splitRatio, Point position) {
        super(position);
        assert splitRatio != null;
        this.settings.splitRatio = splitRatio;
    }

    @Override
    void recalculateFlow() {
        settings.currentFlow = input.getFlow();
        outputA.recalculateFlow(settings.splitRatio * settings.currentFlow);
        outputB.recalculateFlow((1 - settings.splitRatio) * settings.currentFlow);
    }

    @Override
    public void render(Graphics graphics) {
        ImageLibrary.drawImage(ImageLibrary.Images.AdjustableSplitter, graphics, getPosition());
        input.render(graphics);
        outputA.render(graphics);
        outputB.render(graphics);
    }
}
