package simulation;

import simulation.elements.Component;
import simulation.elements.*;
import util.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FlowNetwork implements java.io.Serializable{
    private List<Component> components = new ArrayList<>();
    private List<Pipeline> pipelines = new ArrayList<>();

    public void addComponent(Component component) {
        assert component != null;

        components.add(component);
    }

    public void addPipeline(Pipeline pipeline) {
        assert pipeline != null;

        pipelines.add(pipeline);
        pipeline.attach();
    }

    public void removeElement(Element element) {
        assert element != null;

        if (element instanceof Pipeline) {
            removePipeline((Pipeline) element);
        } else {
            Component component = (Component) element;
            component.setToBeDeleted();
            for (Pipeline pipeline : component.getPipelines()) {
                if (pipeline != null) {
                    removePipeline(pipeline);
                }
            }
            components.remove(component);
        }
    }

    private void removePipeline(Pipeline pipeline) {
        assert pipeline != null;

        pipeline.detach();
        pipelines.remove(pipeline);
    }

    public Element findElement(Point point) {
        assert point != null;

        for (Element element : components) {
            if (element.inBoundingArea(point)) {
                return element;
            }
        }
        for (Element element : pipelines) {
            if (element.inBoundingArea(point)) {
                return element;
            }
        }
        return null;
    }

    public boolean isOverlapping(Component component) {
        assert component != null;

        return components
                .stream()
                .anyMatch(component::isOverlapping);
    }

    //Render the components and pipelines of this flow network
    void render(Graphics g) {
        components.forEach(c -> c.render(g));
        pipelines.forEach(c -> c.render(g));
    }
    void renderLast(Graphics g){
        components.forEach(c -> c.renderInputsAndOutputs(g));
    }

    public Output findOutput(Point point) {
        Component component = findComponent(point);
        if (component != null) {
            return component.getOutputs()
                    .stream()
                    .filter(o -> o.inBoundingArea(point))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public Input findInput(Point point) {
        Component component = findComponent(point);
        if (component != null) {
            return component.getInputs()
                    .stream()
                    .filter(o -> o.inBoundingArea(point))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private Component findComponent(Point point) {
        assert point != null;
        return components.stream()
                .filter(c -> c.inBoundingArea(point))
                .findFirst()
                .orElse(null);
    }

    boolean isEmpty(){
        return (components.isEmpty() && pipelines.isEmpty());
    }

}
