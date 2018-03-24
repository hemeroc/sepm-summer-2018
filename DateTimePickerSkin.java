package customcontrolexample;

import com.sun.javafx.scene.control.DatePickerContent;
import javafx.animation.AnimationTimer;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.converter.IntegerStringConverter;

import static javafx.scene.control.Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL;
import static javafx.scene.input.MouseButton.PRIMARY;

public class DateTimePickerSkin extends DatePickerSkin {

    private Node popupContent;

    public DateTimePickerSkin(DateTimePicker control) {
        super(control);
        popupContent = super.getPopupContent();
        if (popupContent instanceof DatePickerContent) {
            Spinner<Integer> sHour = getIntegerSpinner(0, 23);
            Spinner<Integer> sMinute = getIntegerSpinner(0, 59);
            HBox hBox = new HBox(new Label("Hour:"), sHour, new Label("Minute:"), sMinute);
            hBox.setSpacing(5);
            hBox.setPadding(new Insets(0, 0, 0, 5));
            hBox.setAlignment(Pos.CENTER);
            ((DatePickerContent) popupContent).getChildren().add(hBox);
        }
    }

    private Spinner<Integer> getIntegerSpinner(int min, int max) {
        Spinner<Integer> spinner = new Spinner<>(min, max, 0);
        spinner.getStyleClass().add(STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        spinner.setEditable(false);
        spinner.setPrefWidth(60);
        spinner.setMaxWidth(Double.MAX_VALUE);
        spinner.getValueFactory().setWrapAround(true);
        spinner.getValueFactory().setConverter(new IntegerStringConverter() {
            @Override
            public String toString(Integer object) {
                return String.format("%02d", object);
            }
        });
        IncrementHandler fastIncrementMouseHandler = new IncrementHandler();
        spinner.addEventFilter(MouseEvent.MOUSE_PRESSED, fastIncrementMouseHandler);
        spinner.addEventFilter(MouseEvent.MOUSE_RELEASED, evt -> {
            if (evt.getButton() == MouseButton.PRIMARY) {
                fastIncrementMouseHandler.stop();
            }
        });
        HBox.setHgrow(spinner, Priority.ALWAYS);
        return spinner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getPopupContent() {
        return popupContent;
    }

    private static final class IncrementHandler implements EventHandler<MouseEvent> {
        private static final PseudoClass PRESSED = PseudoClass.getPseudoClass("pressed");
        private Spinner spinner;
        private boolean increment;
        private long startTimestamp;
        private long nextStep;
        private static final long DELAY = 1000l * 1000L * 500; // 0.5 sec
        private static final long STEP = 1000l * 1000L * 100;
        private Node button;

        private final AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (now - startTimestamp >= nextStep) {
                    nextStep += STEP;
                    if (increment) {
                        spinner.increment();
                    } else {
                        spinner.decrement();
                    }
                }
            }
        };

        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == PRIMARY) {
                Spinner source = (Spinner) event.getSource();
                Node node = event.getPickResult().getIntersectedNode();

                Boolean increment = null;
                // find which kind of button was pressed and if one was pressed
                while (increment == null && node != source) {
                    if (node.getStyleClass().contains("increment-arrow-button")) {
                        increment = Boolean.TRUE;
                    } else if (node.getStyleClass().contains("decrement-arrow-button")) {
                        increment = Boolean.FALSE;
                    } else {
                        node = node.getParent();
                    }
                }
                if (increment != null) {
                    event.consume();
                    source.requestFocus();
                    spinner = source;
                    this.increment = increment;

                    // timestamp to calculate the delay
                    startTimestamp = System.nanoTime();
                    nextStep = DELAY;
                    button = node;

                    // update for css styling
                    node.pseudoClassStateChanged(PRESSED, true);

                    // first value update
                    timer.handle(startTimestamp + DELAY);

                    // trigger timer for more updates later
                    timer.start();
                }
            }
        }

        public void stop() {
            timer.stop();
            if (button != null) {
                button.pseudoClassStateChanged(PRESSED, false);
            }
            button = null;
            spinner = null;
        }
    }
}
