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

import com.uber.motoristauber.dto.AplicativoDTO;
import com.uber.motoristauber.service.AplicativoService;

@RestController
@RequestMapping("/api/aplicativos")
public class AplicativoController {

    private final AplicativoService service;

    public AplicativoController(AplicativoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AplicativoDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id_aplicativo}")
    public ResponseEntity<AplicativoDTO> buscar(@PathVariable Integer id_aplicativo) {
        return ResponseEntity.ok(service.buscarPorId(id_aplicativo));
    }

    @PostMapping
    public ResponseEntity<AplicativoDTO> criar(@RequestBody AplicativoDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @PutMapping("/{id_aplicativo}")
    public ResponseEntity<AplicativoDTO> atualizar(@PathVariable Integer id_aplicativo, @RequestBody AplicativoDTO dto) {
        return ResponseEntity.ok(service.atualizar(id_aplicativo, dto));
    }

    @DeleteMapping("/{id_aplicativo}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id_aplicativo) {
        service.excluir(id_aplicativo);
        return ResponseEntity.noContent().build();
    }
}