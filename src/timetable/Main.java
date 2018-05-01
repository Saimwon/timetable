/*
Van Braeckel Simon
 */

package timetable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));

        Parent root = loader.load();
        primaryStage.setTitle("Timetable");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        setFullScreen(primaryStage);

        primaryStage.show();
    }

    private void setFullScreen(Stage stage){
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        double height = bounds.getHeight();
        double width = bounds.getWidth();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(width);
        stage.setHeight(height);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
