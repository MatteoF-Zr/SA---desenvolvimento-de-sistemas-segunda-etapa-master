package com.uber.motoristauber.service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uber.motoristauber.dto.MotoristaDTO;
import com.uber.motoristauber.exceptions.DuplicateResourceException;
import com.uber.motoristauber.exceptions.InvalidDataException;
import com.uber.motoristauber.exceptions.ResourceNotFoundException;
import com.uber.motoristauber.model.Motorista;
import com.uber.motoristauber.repository.MotoristaRepository;

@Service
public class MotoristaService {

    private final MotoristaRepository repo;
    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern PLACA_PATTERN = Pattern.compile("^[A-Z]{3}\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$");

    public MotoristaService(MotoristaRepository repo) {
        this.repo = repo;
    }

    public List<MotoristaDTO> listarTodos() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public MotoristaDTO buscarPorId(Integer id) {
        return repo.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado com ID: " + id));
    }



    @Transactional
    public MotoristaDTO salvar(MotoristaDTO dto) {
        if (dto.getNrm_motorista() == null || dto.getNrm_motorista().trim().isEmpty()) {
            throw new InvalidDataException("nrm_motorista é obrigatório.");
        }
        if (dto.getCpf_motorista() == null || !CPF_PATTERN.matcher(dto.getCpf_motorista()).matches()) {
            throw new InvalidDataException("cpf_motorista inválido. Deve conter 11 dígitos.");
        }

        String cpf = dto.getCpf_motorista();

        if (repo.findByCpf_motorista(cpf).isPresent()) {
            throw new DuplicateResourceException("cpf_motorista já cadastrado.");
        }

        String placa = dto.getPla_veiculo() != null ? dto.getPla_veiculo().toUpperCase().trim() : null;
        if (placa != null && !PLACA_PATTERN.matcher(placa).matches()) {
            throw new InvalidDataException("pla_veiculo inválida.");
        }


        Motorista m = new Motorista();
        m.setNrm_motorista(dto.getNrm_motorista().trim());
        m.setCpf_motorista(cpf);
        m.setTel_motorista(dto.getTel_motorista());
        m.setPla_veiculo(placa);

        Motorista saved = repo.save(m);
        return toDTO(saved);
    }

    @Transactional
    public MotoristaDTO atualizar(Integer id, MotoristaDTO dto) {
        Motorista existente = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado: " + id));

        if (dto.getNrm_motorista() != null)
            existente.setNrm_motorista(dto.getNrm_motorista());
        if (dto.getTel_motorista() != null)
            existente.setTel_motorista(dto.getTel_motorista());

        if (dto.getPla_veiculo() != null) {
            String placa = dto.getPla_veiculo().toUpperCase().trim();
            if (!PLACA_PATTERN.matcher(placa).matches())
                throw new InvalidDataException("pla_veiculo inválida.");
            existente.setPla_veiculo(placa);
        }

        return toDTO(repo.save(existente));
    }

    @Transactional
    public void excluir(Integer id) {
        Motorista existente = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado: " + id));
        repo.delete(existente);
    }

    private MotoristaDTO toDTO(Motorista m) {
            MotoristaDTO dto = new MotoristaDTO();
            dto.setId_motorista(m.getId_motorista());
            dto.setNrm_motorista(m.getNrm_motorista());
            dto.setCpf_motorista(m.getCpf_motorista());
            dto.setTel_motorista(m.getTel_motorista());
            dto.setPla_veiculo(m.getPla_veiculo());
            return dto;
        }

}