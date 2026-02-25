package com.uber.motoristauber.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uber.motoristauber.dto.CorridaDTO;
import com.uber.motoristauber.exceptions.InvalidDataException;
import com.uber.motoristauber.exceptions.ResourceNotFoundException;
import com.uber.motoristauber.model.Aplicativo;
import com.uber.motoristauber.model.Corrida;
import com.uber.motoristauber.model.Motorista;
import com.uber.motoristauber.repository.AplicativoRepository;
import com.uber.motoristauber.repository.CorridaRepository;
import com.uber.motoristauber.repository.MotoristaRepository;

@Service
public class CorridaService {

    private final CorridaRepository corridaRepository;
    private final MotoristaRepository motoristaRepository;
    private final AplicativoRepository aplicativoRepository;

    public CorridaService(CorridaRepository corridaRepository,
                          MotoristaRepository motoristaRepository,
                          AplicativoRepository aplicativoRepository) {
        this.corridaRepository = corridaRepository;
        this.motoristaRepository = motoristaRepository;
        this.aplicativoRepository = aplicativoRepository;
    }

    public List<CorridaDTO> listarTodos() {
        return corridaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CorridaDTO buscarPorId(Integer id) {
        Corrida c = corridaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Corrida não encontrada: " + id));
        return toDTO(c);
    }

    @Transactional
    public CorridaDTO salvar(CorridaDTO dto) {
        if (dto.getVal_corrida() == null || dto.getVal_corrida().doubleValue() <= 0) {
            throw new InvalidDataException("val_corrida deve ser maior que zero");
        }
        Motorista motorista = motoristaRepository.findById(dto.getId_motorista())
                .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado: " + dto.getId_motorista()));
        Aplicativo aplicativo = aplicativoRepository.findById(dto.getId_aplicativo())
                .orElseThrow(() -> new ResourceNotFoundException("Aplicativo não encontrado: " + dto.getId_aplicativo()));

        Corrida c = new Corrida();
        c.setDat_corrida(dto.getDat_corrida());
        c.setHor_corrida(dto.getHor_corrida());
        c.setOrg_corrida(dto.getOrg_corrida());
        c.setDes_corrida(dto.getDes_corrida());
        c.setVal_corrida(dto.getVal_corrida());
        c.setMotorista(motorista);
        c.setAplicativo(aplicativo);

        Corrida saved = corridaRepository.save(c);
        return toDTO(saved);
    }

    @Transactional
    public CorridaDTO atualizar(Integer id, CorridaDTO dto) {
        Corrida existente = corridaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Corrida não encontrada: " + id));

        existente.setDat_corrida(dto.getDat_corrida());
        existente.setHor_corrida(dto.getHor_corrida());
        existente.setOrg_corrida(dto.getOrg_corrida());
        existente.setDes_corrida(dto.getDes_corrida());
        existente.setVal_corrida(dto.getVal_corrida());

        if (dto.getId_motorista() != null) {
            Motorista m = motoristaRepository.findById(dto.getId_motorista())
                    .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado: " + dto.getId_motorista()));
            existente.setMotorista(m);
        }

        if (dto.getId_aplicativo() != null) {
            Aplicativo a = aplicativoRepository.findById(dto.getId_aplicativo())
                    .orElseThrow(() -> new ResourceNotFoundException("Aplicativo não encontrado: " + dto.getId_aplicativo()));
            existente.setAplicativo(a);
        }

        Corrida updated = corridaRepository.save(existente);
        return toDTO(updated);
    }

    public void excluir(Integer id) {
        if (!corridaRepository.existsById(id)) throw new ResourceNotFoundException("Corrida não encontrada: " + id);
        corridaRepository.deleteById(id);
    }

    private CorridaDTO toDTO(Corrida c) {
        CorridaDTO dto = new CorridaDTO();
        dto.setId_corrida(c.getId_corrida());
        dto.setDat_corrida(c.getDat_corrida());
        dto.setHor_corrida(c.getHor_corrida());
        dto.setOrg_corrida(c.getOrg_corrida());
        dto.setDes_corrida(c.getDes_corrida());
        dto.setVal_corrida(c.getVal_corrida());
        if (c.getMotorista() != null) dto.setId_motorista(c.getMotorista().getId_motorista());
        if (c.getAplicativo() != null) dto.setId_aplicativo(c.getAplicativo().getId_aplicativo());
        return dto;
    }
}