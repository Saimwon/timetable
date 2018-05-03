/*
Simon Van Braeckel
 */

package usagewindow;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class HelpWindow extends Stage {
    public Text text;
    public Button closeButton;

    public HelpWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../usagewindow/usagewindow.fxml"));
        loader.setController(this);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e){
            //Crash als het programma de fxml file van deze klasse niet vindt
            e.printStackTrace();
            System.exit(1);
        }

        this.setTitle("Usage.");

        closeButton.setOnAction(e -> this.close());
        setContent(text);

        Scene scene = new Scene(root);
        this.setScene(scene);
    }

    private void setContent(Text text){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(HelpWindow.class.getResourceAsStream("usagecontent.txt")));
            String line = bufferedReader.readLine();
            while (line != null){
                text.setText(text.getText() + line + "\n");
                line = bufferedReader.readLine();
            }
        } catch (IOException e){
            System.out.println("Couldn't find file.");
        }
    }
}
