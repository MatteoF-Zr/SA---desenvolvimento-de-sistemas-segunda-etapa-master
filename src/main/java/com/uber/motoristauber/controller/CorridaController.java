package com.uber.motoristauber.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uber.motoristauber.dto.CorridaDTO;
import com.uber.motoristauber.service.CorridaService;

@RestController
@RequestMapping("/api/corridas")
public class CorridaController {

    private final CorridaService corridaService;

    public CorridaController(CorridaService corridaService) {
        this.corridaService = corridaService;
    }

    @GetMapping
    public ResponseEntity<List<CorridaDTO>> listar() {
        return ResponseEntity.ok(corridaService.listarTodos());
    }

    @GetMapping("/{id_corrida}")
    public ResponseEntity<CorridaDTO> buscar(@PathVariable Integer id_corrida) {
        return ResponseEntity.ok(corridaService.buscarPorId(id_corrida));
    }

    @PostMapping
    public ResponseEntity<CorridaDTO> criar(@RequestBody CorridaDTO dto) {
        return ResponseEntity.ok(corridaService.salvar(dto));
    }

    @PutMapping("/{id_corrida}")
    public ResponseEntity<CorridaDTO> atualizar(@PathVariable Integer id_corrida, @RequestBody CorridaDTO dto) {
        return ResponseEntity.ok(corridaService.atualizar(id_corrida, dto));
    }

    @DeleteMapping("/{id_corrida}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id_corrida) {
        corridaService.excluir(id_corrida);
        return ResponseEntity.noContent().build();
    }
}