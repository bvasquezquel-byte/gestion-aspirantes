package edu.ingsoft.bvasquez.quel.controller;

import edu.ingsoft.bvasquez.quel.exception.AppException;
import edu.ingsoft.bvasquez.quel.service.DashBoardService;
import edu.ingsoft.bvasquez.quel.util.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML private Label lblTotalAspirantes;

    private final DashBoardService dashBoardService;

    public DashboardController(DashBoardService dashBoardService) {
        this.dashBoardService = dashBoardService;
    }

    @FXML
    private void initialize() {
        try {
            lblTotalAspirantes.setText(String.valueOf(dashBoardService.totalAspirantes()));
        } catch (AppException e) {
            AlertHelper.error("Error", e.getMessage());
        }
    }
}
