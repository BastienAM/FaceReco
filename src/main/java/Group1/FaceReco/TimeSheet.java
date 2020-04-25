package main.java.Group1.FaceReco;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSheet {
    Map<Integer, Boolean> studentsMap = new HashMap<>();

    Path trainedGroupPath;

    public TimeSheet(List<Integer> studentsIDList){
        for(Integer studentID : studentsIDList){
            studentsMap.put(studentID, false);
        }
    }

    public Map<Integer, Boolean> getStudentsMap() {
        return studentsMap;
    }

    public void setStudent(Integer studentID, Boolean isPresent){
        studentsMap.put(studentID, isPresent);
    }

    public Path getTrainedGroupPath() {
        return trainedGroupPath;
    }

    public void setTrainedGroupPath(Path trainedGroupPath) {
        this.trainedGroupPath = trainedGroupPath;
    }
}
