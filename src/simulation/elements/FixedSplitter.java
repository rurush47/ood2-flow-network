package simulation.elements;

import ui.ImageLibrary;
import util.Point;

import java.awt.*;

public class FixedSplitter extends Splitter implements java.io.Serializable {

    public FixedSplitter(Point position){
        super(position);
    }

    @Override
    void recalculateFlow() {
        this.settings.currentFlow = input.getFlow();
        this.outputA.recalculateFlow(settings.currentFlow/2);
        this.outputB.recalculateFlow(settings.currentFlow/2);
    }

    @Override
    public void render(Graphics graphics) {
        ImageLibrary.drawImage(ImageLibrary.Images.Splitter, graphics, getPosition());
        input.render(graphics);
        outputA.render(graphics);
        outputB.render(graphics);
    }
}
