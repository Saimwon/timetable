package guielements;

import javafx.scene.control.Button;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.ToggleButton;

public class SelectionButton extends ToggleButton {
    private int id;
    private String columnName;

    public SelectionButton(String text, int id, String column){
        super(text);
        this.id = id;
        this.columnName = column;
    }

    public int getDBId() {
        return id;
    }

    public String getColumnName() {
        return columnName;
    }
}
