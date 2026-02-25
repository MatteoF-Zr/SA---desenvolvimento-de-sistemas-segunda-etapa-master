package com.uber.motoristauber.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uber.motoristauber.dto.AplicativoDTO;
import com.uber.motoristauber.exceptions.InvalidDataException;
import com.uber.motoristauber.exceptions.ResourceNotFoundException;
import com.uber.motoristauber.model.Aplicativo;
import com.uber.motoristauber.repository.AplicativoRepository;

@Service
public class AplicativoService {

    private final AplicativoRepository repo;

    public AplicativoService(AplicativoRepository repo) {
        this.repo = repo;
    }

    public List<AplicativoDTO> listarTodos() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AplicativoDTO buscarPorId(Integer id) {
        Aplicativo a = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aplicativo não encontrado: " + id));
        return toDTO(a);
    }

    @Transactional
    public AplicativoDTO salvar(AplicativoDTO dto) {
        if (dto.getNom_aplicativo() == null || dto.getNom_aplicativo().trim().isEmpty())
            throw new InvalidDataException("nom_aplicativo é obrigatório.");
        if (dto.getTax_plataforma() == null || dto.getTax_plataforma().signum() < 0)
            throw new InvalidDataException("tax_plataforma inválida.");
        if (dto.getTax_cancelamento() != null && dto.getTax_cancelamento().signum() < 0)
            throw new InvalidDataException("tax_cancelamento inválida.");
        if (dto.getEma_suporte() != null && !dto.getEma_suporte().contains("@"))
            throw new InvalidDataException("ema_suporte inválido.");

        Aplicativo a = new Aplicativo();
        a.setNom_aplicativo(dto.getNom_aplicativo().trim());
        a.setTax_plataforma(dto.getTax_plataforma());
        a.setTax_cancelamento(dto.getTax_cancelamento());
        a.setEma_suporte(dto.getEma_suporte());

        Aplicativo saved = repo.save(a);
        return toDTO(saved);
    }

    @Transactional
    public AplicativoDTO atualizar(Integer id, AplicativoDTO dto) {
        Aplicativo existente = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aplicativo não encontrado: " + id));
        if (dto.getNom_aplicativo() != null) existente.setNom_aplicativo(dto.getNom_aplicativo().trim());
        if (dto.getTax_plataforma() != null) {
            if (dto.getTax_plataforma().signum() < 0) throw new InvalidDataException("tax_plataforma inválida.");
            existente.setTax_plataforma(dto.getTax_plataforma());
        }
        if (dto.getTax_cancelamento() != null) {
            if (dto.getTax_cancelamento().signum() < 0) throw new InvalidDataException("tax_cancelamento inválida.");
            existente.setTax_cancelamento(dto.getTax_cancelamento());
        }
        if (dto.getEma_suporte() != null) {
            if (!dto.getEma_suporte().contains("@")) throw new InvalidDataException("ema_suporte inválido.");
            existente.setEma_suporte(dto.getEma_suporte());
        }
        Aplicativo updated = repo.save(existente);
        return toDTO(updated);
    }

    public void excluir(Integer id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Aplicativo não encontrado: " + id);
        repo.deleteById(id);
    }

    private AplicativoDTO toDTO(Aplicativo a) {
        AplicativoDTO dto = new AplicativoDTO();
        dto.setId_aplicativo(a.getId_aplicativo());
        dto.setNom_aplicativo(a.getNom_aplicativo());
        dto.setTax_plataforma(a.getTax_plataforma());
        dto.setTax_cancelamento(a.getTax_cancelamento());
        dto.setEma_suporte(a.getEma_suporte());
        return dto;
    }
}