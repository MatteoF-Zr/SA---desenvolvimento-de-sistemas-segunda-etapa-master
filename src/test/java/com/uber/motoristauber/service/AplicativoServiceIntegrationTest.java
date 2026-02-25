package com.uber.motoristauber.service;

import java.math.BigDecimal;
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

import com.uber.motoristauber.dto.AplicativoDTO;
import com.uber.motoristauber.exceptions.InvalidDataException;
import com.uber.motoristauber.exceptions.ResourceNotFoundException;
import com.uber.motoristauber.repository.AplicativoRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class AplicativoServiceIntegrationTest {

    @Autowired
    private AplicativoService aplicativoService;

    @Autowired
    private AplicativoRepository aplicativoRepository;

    private AplicativoDTO aplicativoDTOValido;

    // Setup inicial para cada teste
    @BeforeEach
    public void setup() {
        aplicativoRepository.deleteAll();
        
        aplicativoDTOValido = new AplicativoDTO();
        aplicativoDTOValido.setNom_aplicativo("Uber");
        aplicativoDTOValido.setTax_plataforma(new BigDecimal("0.15"));
        aplicativoDTOValido.setTax_cancelamento(new BigDecimal("0.05"));
        aplicativoDTOValido.setEma_suporte("suporte@uber.com");
    }

    // Salvar aplicativo com dados válidos
    @Test
    public void testSalvarAplicativoValido() {
        AplicativoDTO resultado = aplicativoService.salvar(aplicativoDTOValido);
        
        assertNotNull(resultado);
        assertNotNull(resultado.getId_aplicativo());
        assertEquals("Uber", resultado.getNom_aplicativo());
        assertEquals(new BigDecimal("0.15"), resultado.getTax_plataforma());
    }

    // Nome do aplicativo é obrigatório
    @Test
    public void testSalvarAplicativoSemNome() {
        aplicativoDTOValido.setNom_aplicativo(null);
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> aplicativoService.salvar(aplicativoDTOValido)
        );
        
        assertEquals("nom_aplicativo é obrigatório.", exception.getMessage());
    }

    // Nome vazio não é aceito
    @Test
    public void testSalvarAplicativoComNomeVazio() {
        aplicativoDTOValido.setNom_aplicativo("   ");
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> aplicativoService.salvar(aplicativoDTOValido)
        );
        
        assertEquals("nom_aplicativo é obrigatório.", exception.getMessage());
    }

    // Taxa de plataforma não pode ser negativa
    @Test
    public void testSalvarAplicativoComTaxPlataformaInvalida() {
        aplicativoDTOValido.setTax_plataforma(new BigDecimal("-0.10"));
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> aplicativoService.salvar(aplicativoDTOValido)
        );
        
        assertEquals("tax_plataforma inválida.", exception.getMessage());
    }

    // Taxa de plataforma é obrigatória
    @Test
    public void testSalvarAplicativoComTaxPlataformaNull() {
        aplicativoDTOValido.setTax_plataforma(null);
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> aplicativoService.salvar(aplicativoDTOValido)
        );
        
        assertEquals("tax_plataforma inválida.", exception.getMessage());
    }

    // Taxa de cancelamento não pode ser negativa
    @Test
    public void testSalvarAplicativoComTaxCancelamentoInvalida() {
        aplicativoDTOValido.setTax_cancelamento(new BigDecimal("-0.05"));
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> aplicativoService.salvar(aplicativoDTOValido)
        );
        
        assertEquals("tax_cancelamento inválida.", exception.getMessage());
    }

    // Email deve ser válido
    @Test
    public void testSalvarAplicativoComEmailInvalido() {
        aplicativoDTOValido.setEma_suporte("emailsinvalido");
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> aplicativoService.salvar(aplicativoDTOValido)
        );
        
        assertEquals("ema_suporte inválido.", exception.getMessage());
    }

    // Buscar aplicativo por ID
    @Test
    public void testBuscarPorIdExistente() {
        AplicativoDTO salvo = aplicativoService.salvar(aplicativoDTOValido);
        
        AplicativoDTO resultado = aplicativoService.buscarPorId(salvo.getId_aplicativo());
        
        assertNotNull(resultado);
        assertEquals(salvo.getId_aplicativo(), resultado.getId_aplicativo());
        assertEquals("Uber", resultado.getNom_aplicativo());
    }

    // Rejeitar busca de aplicativo inexistente
    @Test
    public void testBuscarPorIdNaoExistente() {
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> aplicativoService.buscarPorId(999)
        );
        
        assertTrue(exception.getMessage().contains("Aplicativo não encontrado"));
    }

    // Listar todos os aplicativos cadastrados
    @Test
    public void testListarTodos() {
        aplicativoService.salvar(aplicativoDTOValido);
        
        AplicativoDTO aplicativo2 = new AplicativoDTO();
        aplicativo2.setNom_aplicativo("99");
        aplicativo2.setTax_plataforma(new BigDecimal("0.20"));
        aplicativo2.setTax_cancelamento(new BigDecimal("0.07"));
        aplicativo2.setEma_suporte("contato@99.com");
        aplicativoService.salvar(aplicativo2);
        
        List<AplicativoDTO> resultado = aplicativoService.listarTodos();
        
        assertEquals(2, resultado.size());
    }

    // Atualizar dados do aplicativo
    @Test
    public void testAtualizarAplicativoValido() {
        AplicativoDTO salvo = aplicativoService.salvar(aplicativoDTOValido);
        
        AplicativoDTO atualizacao = new AplicativoDTO();
        atualizacao.setNom_aplicativo("Uber Atualizado");
        atualizacao.setTax_plataforma(new BigDecimal("0.18"));
        
        AplicativoDTO resultado = aplicativoService.atualizar(salvo.getId_aplicativo(), atualizacao);
        
        assertNotNull(resultado);
        assertEquals("Uber Atualizado", resultado.getNom_aplicativo());
        assertEquals(new BigDecimal("0.18"), resultado.getTax_plataforma());
    }

    // Validar taxa ao atualizar
    @Test
    public void testAtualizarAplicativoComTaxInvalida() {
        AplicativoDTO salvo = aplicativoService.salvar(aplicativoDTOValido);
        
        AplicativoDTO atualizacao = new AplicativoDTO();
        atualizacao.setTax_plataforma(new BigDecimal("-0.10"));
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> aplicativoService.atualizar(salvo.getId_aplicativo(), atualizacao)
        );
        
        assertEquals("tax_plataforma inválida.", exception.getMessage());
    }

    // Rejeitar atualização de aplicativo inexistente
    @Test
    public void testAtualizarAplicativoInexistente() {
        AplicativoDTO atualizacao = new AplicativoDTO();
        atualizacao.setNom_aplicativo("Novo Nome");
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> aplicativoService.atualizar(999, atualizacao)
        );
        
        assertTrue(exception.getMessage().contains("Aplicativo não encontrado"));
    }

    // Excluir aplicativo existente
    @Test
    public void testExcluirAplicativoExistente() {
        AplicativoDTO salvo = aplicativoService.salvar(aplicativoDTOValido);
        
        aplicativoService.excluir(salvo.getId_aplicativo());
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> aplicativoService.buscarPorId(salvo.getId_aplicativo())
        );
        
        assertTrue(exception.getMessage().contains("Aplicativo não encontrado"));
    }

    // Rejeitar exclusão de aplicativo inexistente
    @Test
    public void testExcluirAplicativoInexistente() {
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> aplicativoService.excluir(999)
        );
        
        assertTrue(exception.getMessage().contains("Aplicativo não encontrado"));
    }

    // Salvar aplicativo sem taxa de cancelamento (opcional)
    @Test
    public void testSalvarAplicativoSemTaxCancelamento() {
        aplicativoDTOValido.setTax_cancelamento(null);
        
        AplicativoDTO resultado = aplicativoService.salvar(aplicativoDTOValido);
        
        assertNotNull(resultado);
        assertNull(resultado.getTax_cancelamento());
    }

    // Salvar aplicativo sem email de suporte (opcional)
    @Test
    public void testSalvarAplicativoSemEmail() {
        aplicativoDTOValido.setEma_suporte(null);
        
        AplicativoDTO resultado = aplicativoService.salvar(aplicativoDTOValido);
        
        assertNotNull(resultado);
        assertNull(resultado.getEma_suporte());
    }
}
