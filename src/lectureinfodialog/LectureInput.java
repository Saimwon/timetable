/*
Simon Van Braeckel
 */

package lectureinfodialog;

import datatransferobjects.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import timetable.ListViewModel;

import java.io.IOException;
import java.util.List;

public class LectureInput extends Stage {
    /*
    Resultaat van het lectureInput venster. Wordt door controller geset wanneer het venster wordt gesloten.
     */
    private LectureDTO resultLectureDTO;

    public LectureInput(ListViewModel listViewModel, List<String> startHours,
                                        LectureInputController controller, String title){
        resultLectureDTO = null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("lectureinput.fxml"));
        loader.setController(controller);
        Parent root = null;

        try {
            root = loader.load();
        } catch (IOException e){
            //Crash als het programma de fxml file van deze klasse niet vindt of als er een fout in staat.
            e.printStackTrace();
            System.exit(1);
        }

        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setTitle(title);

        controller.setLectureInput(this);

        //Ik zou dit liever doen in de constructor van de Controller maar dat gaat niet omdat
        //FXML de velden eerst moet kunnen invullen.
        controller.setListViewModel(listViewModel);
        controller.fillChoiceBoxes(startHours);
    }

    public LectureDTO getResultLectureDTO() {
        return resultLectureDTO;
    }

    public void setResultLectureDTO(LectureDTO resultLectureDTO) {
        this.resultLectureDTO = resultLectureDTO;
    }
}
