package ui;

import simulation.Result;
import simulation.Settings;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.function.Consumer;

class SettingsBox extends JPanel {
    private static final String TITLE = "Settings";

    private class Input {
        private final JTextField textField;
        private final JLabel label;

        private Input(JTextField textField, JLabel label) {
            this.textField = textField;
            this.label = label;
        }

        void setEnabled(boolean enabled) {
            textField.setEnabled(enabled);
            label.setEnabled(enabled);
        }
    }

    private final Input currentFlowInput;
    private final Input maximumFlowInput;
    private final Input splitRatioInput;
    private Consumer<Result> changeSettingsResultCallback;

    private Settings settings;

    SettingsBox(Consumer<Result> changeSettingsResultCallback) {
        setMinimumSize(new Dimension(500, 500));

        GridLayout layout = new GridLayout(0, 2, 5, 5);
        setLayout(layout);

        TitledBorder border = BorderFactory.createTitledBorder(TITLE);
        setBorder(border);

        this.currentFlowInput = createFieldWithLabel("Current flow", (floatVal) -> settings.setCurrentFlow(floatVal));
        this.maximumFlowInput = createFieldWithLabel("Maximum flow", (floatVal) -> settings.setMaxFlow(floatVal));
        this.splitRatioInput = createFieldWithLabel("Split ratio", (floatVal) -> settings.splitRatio = floatVal);

        this.changeSettingsResultCallback = changeSettingsResultCallback;

        setCurrentSettingsReference(null);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        currentFlowInput.setEnabled(enabled);
        maximumFlowInput.setEnabled(enabled);
        splitRatioInput.setEnabled(enabled);
    }

    private Input createFieldWithLabel(String text, Consumer<Float> fieldUpdateFunc) {
        JTextField textField = new JTextField();
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkIsValid();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkIsValid();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkIsValid();
            }

            private void checkIsValid() {
                if (settings == null) {
                    return;
                }

                try {
                    // TODO refactor this, so it doesn't need to use a second settings object
                    Settings oldSettings = new Settings(0, 0, null);
                    oldSettings.applyValues(settings);

                    if(!textField.getText().isEmpty())
                    {
                        Float floatVal = Float.parseFloat(textField.getText());
                        floatVal = (float)Math.round(floatVal * 100) / 100;
                        fieldUpdateFunc.accept(floatVal);
                    }
                    if (!settings.isValid()) {
                        settings.applyValues(oldSettings);
                        changeSettingsResultCallback.accept(Result.InvalidSettings);
                    } else {
                        changeSettingsResultCallback.accept(Result.Success);
                        SettingsBox.this.resetFieldValidation(textField);
                    }
                } catch (NumberFormatException e) {
                    textField.setBackground(Color.RED);
                    changeSettingsResultCallback.accept(Result.InvalidSettings);
                }
            }
        });

        JLabel label = new JLabel(text);
        label.setLabelFor(textField);

        add(label);
        add(textField);
        return new Input(textField, label);
    }

    private void resetFieldValidation(JTextField textField) {
        textField.setBackground(Color.WHITE);
    }

    void setCurrentSettingsReference(Settings settings) {
        this.settings = settings;
        if (settings != null) {
            setEnabled(true);
            setFieldsWithSettings(settings);
            resetFieldsValidation();
        } else {
            clearAllFields();
            setEnabled(false);
        }
    }

    private void setFieldsWithSettings(Settings settings) {
        currentFlowInput.textField.setText(Float.toString(settings.getCurrentFlow()));
        maximumFlowInput.textField.setText(Float.toString(settings.getMaxFlow()));
        if (settings.splitRatio != null) {
            splitRatioInput.textField.setText(settings.splitRatio.toString());
            splitRatioInput.setEnabled(true);
        } else {
            splitRatioInput.textField.setText("");
            splitRatioInput.setEnabled(false);
        }
        if(!settings.generatesFlow){
            currentFlowInput.setEnabled(false);
        } else {
            currentFlowInput.textField.setText(Float.toString(settings.getCurrentFlow()));
            currentFlowInput.setEnabled(true);
        }
    }

    private void resetFieldsValidation() {
        resetFieldValidation(currentFlowInput.textField);
        resetFieldValidation(maximumFlowInput.textField);
        resetFieldValidation(splitRatioInput.textField);
    }

    private void clearAllFields() {
        currentFlowInput.textField.setText("");
        maximumFlowInput.textField.setText("");
        splitRatioInput.textField.setText("");
    }

    Settings getSettings() {
        return settings;
    }

}
