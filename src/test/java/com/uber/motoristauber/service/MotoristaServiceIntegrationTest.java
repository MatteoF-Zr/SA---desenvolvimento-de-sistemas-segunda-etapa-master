package com.uber.motoristauber.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.uber.motoristauber.dto.MotoristaDTO;
import com.uber.motoristauber.exceptions.DuplicateResourceException;
import com.uber.motoristauber.exceptions.InvalidDataException;
import com.uber.motoristauber.exceptions.ResourceNotFoundException;
import com.uber.motoristauber.repository.MotoristaRepository;

import jakarta.transaction.Transactional;

/**
 * Testes de Integração - MotoristaService
 * Validação de CRUD, regras de negócio e exceções
 */
@SpringBootTest
@Transactional
public class MotoristaServiceIntegrationTest {

    @Autowired
    private MotoristaService motoristaService;

    @Autowired
    private MotoristaRepository motoristaRepository;

    private MotoristaDTO motoristaDTOValido;

    // Setup para cada teste - limpa dados e cria motorista padrão
    @BeforeEach
    public void setup() {
        motoristaRepository.deleteAll();
        
        motoristaDTOValido = new MotoristaDTO();
        motoristaDTOValido.setNrm_motorista("João Silva");
        motoristaDTOValido.setCpf_motorista("12345678901");
        motoristaDTOValido.setTel_motorista("11987654321");
        motoristaDTOValido.setPla_veiculo("ABC1234");
    }

    // Salvar motorista com dados válidos
    @Test
    public void testSalvarMotoristaValido() {
        MotoristaDTO resultado = motoristaService.salvar(motoristaDTOValido);
        
        assertNotNull(resultado);
        assertNotNull(resultado.getId_motorista());
        assertEquals("João Silva", resultado.getNrm_motorista());
        assertEquals("12345678901", resultado.getCpf_motorista());
    }

    // Rejeitar motorista sem nome
    @Test
    public void testSalvarMotoristaSemNome() {
        motoristaDTOValido.setNrm_motorista(null);
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> motoristaService.salvar(motoristaDTOValido)
        );
        
        assertEquals("nrm_motorista é obrigatório.", exception.getMessage());
    }

    // CPF deve ter 11 dígitos
    @Test
    public void testSalvarMotoristaCPFInvalido() {
        motoristaDTOValido.setCpf_motorista("123");
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> motoristaService.salvar(motoristaDTOValido)
        );
        
        assertTrue(exception.getMessage().contains("cpf_motorista inválido"));
    }

    // Rejeitar CPF duplicado
    @Test
    public void testSalvarMotoristaCPFDuplicado() {
        motoristaService.salvar(motoristaDTOValido);
        
        MotoristaDTO motoristaDuplicado = new MotoristaDTO();
        motoristaDuplicado.setNrm_motorista("Maria Silva");
        motoristaDuplicado.setCpf_motorista("12345678901");
        motoristaDuplicado.setTel_motorista("11987654321");
        
        DuplicateResourceException exception = assertThrows(
            DuplicateResourceException.class,
            () -> motoristaService.salvar(motoristaDuplicado)
        );
        
        assertEquals("cpf_motorista já cadastrado.", exception.getMessage());
    }

    // Validar formato da placa do veículo
    @Test
    public void testSalvarMotoristaComPlacaInvalida() {
        motoristaDTOValido.setPla_veiculo("INVÁLIDA");
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> motoristaService.salvar(motoristaDTOValido)
        );
        
        assertEquals("pla_veiculo inválida.", exception.getMessage());
    }

    // Aceitar placa formato Mercosul
    @Test
    public void testSalvarMotoristaComPlacaNovoFormato() {
        motoristaDTOValido.setPla_veiculo("ABC1D23");
        
        MotoristaDTO resultado = motoristaService.salvar(motoristaDTOValido);
        
        assertNotNull(resultado);
        assertEquals("ABC1D23", resultado.getPla_veiculo());
    }

    // Buscar motorista por ID
    @Test
    public void testBuscarPorIdExistente() {
        MotoristaDTO salvo = motoristaService.salvar(motoristaDTOValido);
        
        MotoristaDTO resultado = motoristaService.buscarPorId(salvo.getId_motorista());
        
        assertNotNull(resultado);
        assertEquals(salvo.getId_motorista(), resultado.getId_motorista());
        assertEquals("João Silva", resultado.getNrm_motorista());
    }

    // Rejeitar busca de motorista inexistente
    @Test
    public void testBuscarPorIdNaoExistente() {
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> motoristaService.buscarPorId(999)
        );
        
        assertTrue(exception.getMessage().contains("Motorista não encontrado"));
    }

    // Listar todos os motoristas cadastrados
    @Test
    public void testListarTodos() {
        motoristaService.salvar(motoristaDTOValido);
        
        MotoristaDTO motorista2 = new MotoristaDTO();
        motorista2.setNrm_motorista("Maria Silva");
        motorista2.setCpf_motorista("98765432101");
        motorista2.setTel_motorista("11912345678");
        motorista2.setPla_veiculo("XYZ9876");
        motoristaService.salvar(motorista2);
        
        List<MotoristaDTO> resultado = motoristaService.listarTodos();
        
        assertEquals(2, resultado.size());
    }

    // Atualizar dados do motorista
    @Test
    public void testAtualizarMotoristaValido() {
        MotoristaDTO salvo = motoristaService.salvar(motoristaDTOValido);
        
        MotoristaDTO atualizacao = new MotoristaDTO();
        atualizacao.setNrm_motorista("João Silva Atualizado");
        atualizacao.setTel_motorista("11999999999");
        
        MotoristaDTO resultado = motoristaService.atualizar(salvo.getId_motorista(), atualizacao);
        
        assertNotNull(resultado);
        assertEquals("João Silva Atualizado", resultado.getNrm_motorista());
        assertEquals("11999999999", resultado.getTel_motorista());
        assertEquals("12345678901", resultado.getCpf_motorista());
    }

    // Validar placa ao atualizar
    @Test
    public void testAtualizarMotoristaComPlacaInvalida() {
        MotoristaDTO salvo = motoristaService.salvar(motoristaDTOValido);
        
        MotoristaDTO atualizacao = new MotoristaDTO();
        atualizacao.setPla_veiculo("XXXZZZ");
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> motoristaService.atualizar(salvo.getId_motorista(), atualizacao)
        );
        
        assertEquals("pla_veiculo inválida.", exception.getMessage());
    }

    // Rejeitar atualização de motorista inexistente
    @Test
    public void testAtualizarMotoristaInexistente() {
        MotoristaDTO atualizacao = new MotoristaDTO();
        atualizacao.setNrm_motorista("Novo Nome");
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> motoristaService.atualizar(999, atualizacao)
        );
        
        assertTrue(exception.getMessage().contains("Motorista não encontrado"));
    }

    // Excluir motorista existente
    @Test
    public void testExcluirMotoristaExistente() {
        MotoristaDTO salvo = motoristaService.salvar(motoristaDTOValido);
        
        motoristaService.excluir(salvo.getId_motorista());
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> motoristaService.buscarPorId(salvo.getId_motorista())
        );
        
        assertTrue(exception.getMessage().contains("Motorista não encontrado"));
    }

    // Rejeitar exclusão de motorista inexistente
    @Test
    public void testExcluirMotoristaInexistente() {
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> motoristaService.excluir(999)
        );
        
        assertTrue(exception.getMessage().contains("Motorista não encontrado"));
    }

    // Motorista sem placa (placa é opcional)
    @Test
    public void testSalvarMotoristaComPlacaNull() {
        motoristaDTOValido.setPla_veiculo(null);
        
        MotoristaDTO resultado = motoristaService.salvar(motoristaDTOValido);
        
        assertNotNull(resultado);
        assertNull(resultado.getPla_veiculo());
    }

    // Atualizar apenas o nome do motorista
    @Test
    public void testAtualizarApenasNome() {
        MotoristaDTO salvo = motoristaService.salvar(motoristaDTOValido);
        
        MotoristaDTO atualizacao = new MotoristaDTO();
        atualizacao.setNrm_motorista("Novo Nome");
        
        MotoristaDTO resultado = motoristaService.atualizar(salvo.getId_motorista(), atualizacao);
        
        assertEquals("Novo Nome", resultado.getNrm_motorista());
        assertEquals(motoristaDTOValido.getCpf_motorista(), resultado.getCpf_motorista());
        assertEquals(motoristaDTOValido.getTel_motorista(), resultado.getTel_motorista());
    }
}
