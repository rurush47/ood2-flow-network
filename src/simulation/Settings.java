package simulation;

import util.Point;

import java.awt.*;

public class Settings implements java.io.Serializable{
    public float currentFlow;
    public float maxFlow;
    public Float splitRatio;
    public boolean generatesFlow = false;

    public Settings(float currentFlow, float maxFlow, Float splitRatio) {
        this.currentFlow = currentFlow;
        this.maxFlow = maxFlow;
        this.splitRatio = splitRatio;
    }

    public boolean isValid() {
        boolean splitValid = true;

        if (splitRatio != null) {
            splitValid = splitRatio >= 0.0 && splitRatio <= 1.0;
        }

        return maxFlow >= 0.0 && currentFlow >= 0.0 && splitValid;
    }

    public void applyValues(Settings settings){
        this.currentFlow = settings.currentFlow;
        this.maxFlow = settings.maxFlow;
        this.splitRatio = settings.splitRatio;
    }

    // TODO remove from Settings
    public void renderCurrentFlow(Graphics g, Point position){
        if (currentFlow > maxFlow) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.GREEN);
        }

        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(Float.toString(currentFlow), position.x, position.y);
    }

    public static Settings getDefault() {
        return new Settings(0, 10, null);
    }

    public static Settings forTool(Tool tool) {
        Settings settings = Settings.getDefault();

        if (tool == Tool.Select || tool == Tool.Remove) {
            settings = null;
        } else if (tool == Tool.AddAdjustableSplitter) {
            settings.splitRatio = 0.5f;
        } else if (tool == Tool.AddPump) {
            settings.generatesFlow = true;
        }
        return settings;
    }
}
