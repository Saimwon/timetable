/*
Van Braeckel Simon
 */

package timetable;

import database.DTO.LocationDTO;
import database.DTO.StudentGroupDTO;
import database.DTO.TeacherDTO;
import database.interfaces.DataAccessContext;
import database.interfaces.LocationDAO;
import database.interfaces.StudentGroupDAO;
import database.interfaces.TeacherDAO;
import database.interfaces.implementations.SQLiteDataAccessProvider;
import database.interfaces.implementations.SQLiteTeacherDAO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));

        Parent root = loader.load();
        primaryStage.setTitle("Timetable");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        setFullScreen(primaryStage);

        this.controller = loader.getController();

        List<String> parameterlijst = getParameters().getRaw();
        System.out.println(parameterlijst.size());
        startWithParameters(parameterlijst, scene);
        primaryStage.show();
    }

    public void setFullScreen(Stage stage){
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        double height = bounds.getHeight();
        double width = bounds.getWidth();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(width);
        stage.setHeight(height);
    }

    public void startWithParameters(List<String> param, Scene scene){
        if (param.size() == 0) {
            //doe niets
        } else if (param.size() == 1){
            printInfo(param);
        } else if (param.size() == 2){
            showStartData(param);
        } else if (param.size() == 3){
            showStartData(param);
            takeScreenshot(scene, param.get(2));
        } else {
            error();
        }
    }

    private void takeScreenshot(Scene scene, String destination){
        WritableImage screenshot = scene.snapshot(null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(screenshot, null);
        File file = new File(destination);
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (Exception e){
            System.out.println("Error writing screenshot to file.");
            e.printStackTrace();
            System.out.println();
            error();
        }
    }

    private void error(){
        System.out.println("There was an error in handling your start parameters.");
        Platform.exit();
    }

    public void showStartData(List<String> param){
        String columnName = param.get(0) + "_id";
        String name = param.get(1);
        int id = 0;

        DataAccessContext context = new SQLiteDataAccessProvider().getDataAccessContext();
        if (param.get(0).equals("teacher")){
            TeacherDAO dao = context.getTeacherDAO();

            List<TeacherDTO> results = dao.getTeachersByName(name);
            if (results.isEmpty()){
                System.out.println("No results found for " + param.get(1));
                error();
            } else {
                id = results.get(0).getId();
                controller.updateTableContents(columnName, id);
            }
        } else if (param.get(0).equals("students")){
            StudentGroupDAO dao = context.getStudentDAO();

            List<StudentGroupDTO> results = dao.getStudentGroupsByName(name);
            if (results.isEmpty()){
                System.out.println("No results found for " + param.get(1));
                error();
            } else {
                id = results.get(0).getId();
                controller.updateTableContents(columnName, id);
            }
        } else if (param.get(0).equals("location")){
            LocationDAO dao = context.getLocationDAO();

            List<LocationDTO> results = dao.getLocationsByName(name);
            if (results.isEmpty()){
                System.out.println("No results found for " + param.get(1));
                error();
            } else {
                id = results.get(0).getId();
                controller.updateTableContents(columnName, id);
            }
        } else {
            error();
        }

    }

    public void printInfo(List<String> param){
        DataAccessContext context = new SQLiteDataAccessProvider().getDataAccessContext();
        if (param.get(0).equals("students")){
            List<StudentGroupDTO> students = context.getStudentDAO().getStudentGroups();
            System.out.println(students.size());
            for (StudentGroupDTO group : students){
                System.out.println(group.getName());
            }
        } else if (param.get(0).equals("teacher")){

            List<TeacherDTO> teachers = context.getTeacherDAO().getTeachers();
            System.out.println(teachers.size());
            for (TeacherDTO teach : teachers){
                System.out.println(teach.getName());
            }
        } else if (param.get(0).equals("location")){
            context.getLocationDAO();
            List<LocationDTO> locations = context.getLocationDAO().getLocations();
            System.out.println(locations.size());
            for (LocationDTO loc : locations){
                System.out.println(loc.getName());
            }
        } else {
            error();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
