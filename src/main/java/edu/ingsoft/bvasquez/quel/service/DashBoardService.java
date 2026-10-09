package edu.ingsoft.bvasquez.quel.service;

import edu.ingsoft.bvasquez.quel.repository.AspiranteRepository;

public class DashBoardService {

    private final AspiranteRepository aspiranteRepository;

    public DashBoardService(AspiranteRepository aspiranteRepository) {
        this.aspiranteRepository = aspiranteRepository;
    }

    public long totalAspirantes() {
        return aspiranteRepository.count();
    }
}
