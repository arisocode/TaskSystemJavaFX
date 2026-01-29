package gm.tasks.controller;

import gm.tasks.domain.Task;
import gm.tasks.service.TaskService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class IndexController implements Initializable {

    private static final Logger logger =
            LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private TaskService taskService;
    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, Integer> idTaskColumn;
    @FXML
    private TableColumn<Task, String> taskColumn;
    @FXML
    private TableColumn<Task, String> responsibleColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;

    private final ObservableList<Task> taskList =
            FXCollections.observableArrayList();

    @FXML
    private TextField taskText;
    @FXML
    private TextField responsibleText;
    @FXML
    private TextField statusText;
    private Integer idTaskSelected;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taskTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        configureColumn();
        getTaskList();
    }

    private void configureColumn() {
        idTaskColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("nameTask"));
        responsibleColumn.setCellValueFactory(new PropertyValueFactory<>("responsible"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void getTaskList() {
        logger.info("Ejecutando listado de tareas");
        taskList.clear();
        taskList.addAll(taskService.getAllTask());
        taskTable.setItems(taskList);
    }

    public void insertTask() {
        if (taskText.getText().isEmpty() || taskText.getText().isBlank()) {
            showMessage("Error de validación", "Debe proporcionar una tarea");
            taskText.requestFocus();
            return;
        } else {
            var task = new Task();
            getDataFromForm(task);
            task.setId(null);
            taskService.saveTask(task);
            showMessage("Información", "Tarea agregada");
            cleanForm();
            getTaskList();
        }
    }

    public void updateTask(){
        if(idTaskSelected == null){
            showMessage("Informacion", "Debe seleccionar una tarea de la tabla");
            return;
        }
        if(taskText.getText().isEmpty() || taskText.getText().isBlank()){
            showMessage("Error de validacion", "Debe proporcionar una tarea");
            taskText.requestFocus();
            return;
        }
        var task = new Task();
        getDataFromForm(task);
        taskService.saveTask(task);
        showMessage("Informacion", "Tarea modificada");
        cleanForm();
        getTaskList();
    }

    public void deleteTask(){
        var task = taskTable.getSelectionModel().getSelectedItem();
        if(task != null && idTaskSelected != null){
            taskService.deleteTask(idTaskSelected);
            showMessage("Informacion", "Tarea eliminada: " + task.getId());
            cleanForm();
            getTaskList();
        }else{
            showMessage("Error de validacion", "Debe seleccionar una tarea");
        }
    }

    public void loadTaskForm(){
        var task = taskTable.getSelectionModel().getSelectedItem();
        if(task != null){
            idTaskSelected = task.getId();
            taskText.setText(task.getNameTask());
            responsibleText.setText(task.getResponsible());
            statusText.setText(task.getStatus());
        }
    }

    private void getDataFromForm(Task task) {
        if(idTaskSelected != null){
            task.setId(idTaskSelected);
        }
        task.setNameTask(taskText.getText());
        task.setResponsible(responsibleText.getText());
        task.setStatus(statusText.getText());
    }

    public void cleanForm() {
        idTaskSelected = null;
        taskText.clear();
        responsibleText.clear();
        statusText.clear();
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
