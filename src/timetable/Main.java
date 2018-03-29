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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Application.Parameters param = getParameters();
        List<String> parameterlijst = param.getRaw();
        primaryStage.setTitle("Timetable");
        primaryStage.setScene(new Scene(root));
        setFullScreen(primaryStage);
        startWithParameters(parameterlijst);
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

    public void startWithParameters(List<String> param){
        if (param.size() == 1){
            printInfo(param);
        } else if (param.size() == 2){
            //showStartData(param);
        } else if (param.size() == 3){
            //showStartData(param);
            //takeScreenshot();
        } else {
            //geef foutboodschap
            //crashofzo();
        }
    }

    public void showStartData(List<String> param){
        String columnName = param.get(0) + "_id";
        String name = param.get(1);

        DataAccessContext context = new SQLiteDataAccessProvider().getDataAccessContext();
        if (param.get(0).equals("teacher")){
            TeacherDAO dao = context.getTeacherDAO();
            int id = dao.getTeachersByName(name).get(0).getId();
        } else if (param.get(0).equals("students")){
            StudentGroupDAO dao = context.getStudentDAO();
            int id = dao.getStudentGroupsByName(name).get(0).getId();
        } else if (param.get(0).equals("location")){
            LocationDAO dao = context.getLocationDAO();
            int id = dao.getLocationsByName(name).get(0).getId();
        } else {
            //invalidArguments();
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
            //error();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
