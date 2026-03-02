package com.uber.motoristauber.service; 

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List; 

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest; 

import com.uber.motoristauber.dto.AplicativoDTO;
import com.uber.motoristauber.dto.CorridaDTO;
import com.uber.motoristauber.dto.MotoristaDTO;
import com.uber.motoristauber.exceptions.InvalidDataException;
import com.uber.motoristauber.exceptions.ResourceNotFoundException;
import com.uber.motoristauber.repository.AplicativoRepository;
import com.uber.motoristauber.repository.CorridaRepository;
import com.uber.motoristauber.repository.MotoristaRepository;

import jakarta.transaction.Transactional;

// teste de integração Spring Boot
@SpringBootTest
// cada teste executa em transação para rollback automático
@Transactional
// classe de testes para a funcionalidade de corrida
public class CorridaServiceIntegrationTest {

    @Autowired
    private CorridaService corridaService;
    @Autowired
    private MotoristaService motoristaService;
    @Autowired
    private AplicativoService aplicativoService;
    @Autowired
    private CorridaRepository corridaRepository;
    @Autowired
    private MotoristaRepository motoristaRepository;
    @Autowired
    private AplicativoRepository aplicativoRepository;

    // objeto DTO válido para testes
    private CorridaDTO corridaDTOValida;
    // armazenará o id do motorista de teste
    private Integer idMotoristaTeste;
    // armazenará o id do aplicativo de teste
    private Integer idAplicativoTeste;

    // prepara o ambiente antes de cada método de teste
    @BeforeEach
    public void setup() {
        // limpa todas as corridas do repositório
        corridaRepository.deleteAll();
        // limpa todos os motoristas cadastrados
        motoristaRepository.deleteAll();
        // limpa todos os aplicativos cadastrados
        aplicativoRepository.deleteAll();
        
        MotoristaDTO motorista = new MotoristaDTO();
        motorista.setNrm_motorista("João Silva");
        motorista.setCpf_motorista("12345678901");
        motorista.setTel_motorista("11987654321");
        motorista.setPla_veiculo("ABC1234");
        MotoristaDTO motoristaResult = motoristaService.salvar(motorista);
        idMotoristaTeste = motoristaResult.getId_motorista();
        
        AplicativoDTO aplicativo = new AplicativoDTO();
        aplicativo.setNom_aplicativo("Uber");
        aplicativo.setTax_plataforma(new BigDecimal("0.15"));
        aplicativo.setEma_suporte("suporte@uber.com");
        AplicativoDTO aplicativoResult = aplicativoService.salvar(aplicativo);
        idAplicativoTeste = aplicativoResult.getId_aplicativo();
        
        corridaDTOValida = new CorridaDTO();
        corridaDTOValida.setDat_corrida(LocalDate.now());
        corridaDTOValida.setHor_corrida(LocalTime.now());
        corridaDTOValida.setOrg_corrida("Rua A");
        corridaDTOValida.setDes_corrida("Rua B");
        corridaDTOValida.setVal_corrida(new BigDecimal("50.00"));
        corridaDTOValida.setId_motorista(idMotoristaTeste);
        corridaDTOValida.setId_aplicativo(idAplicativoTeste);
    }

    // valida que uma corrida pode ser salva com dados corretos
    @Test
    public void testSalvarCorridaValida() {
        CorridaDTO resultado = corridaService.salvar(corridaDTOValida);
        
        assertNotNull(resultado);
        assertNotNull(resultado.getId_corrida());
        assertEquals(new BigDecimal("50.00"), resultado.getVal_corrida());
        assertEquals("Rua A", resultado.getOrg_corrida());
        assertEquals("Rua B", resultado.getDes_corrida());
    }

    // verifica falha ao salvar corrida com valor zero
    @Test
    public void testSalvarCorridaComValorZero() {
        corridaDTOValida.setVal_corrida(BigDecimal.ZERO);
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> corridaService.salvar(corridaDTOValida)
        );
        
        assertEquals("val_corrida deve ser maior que zero", exception.getMessage());
    }

    // verifica falha ao salvar corrida com valor negativo
    @Test
    public void testSalvarCorridaComValorNegativo() {
        corridaDTOValida.setVal_corrida(new BigDecimal("-10.00"));
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> corridaService.salvar(corridaDTOValida)
        );
        
        assertEquals("val_corrida deve ser maior que zero", exception.getMessage());
    }

    // verifica falha ao salvar corrida sem valor definido
    @Test
    public void testSalvarCorridaComValorNull() {
        corridaDTOValida.setVal_corrida(null);
        
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> corridaService.salvar(corridaDTOValida)
        );
        
        assertEquals("val_corrida deve ser maior que zero", exception.getMessage());
    }

    // teste salvamento com motorista inexistente deve lançar exceção
    @Test
    public void testSalvarCorridaComMotoristaInexistente() {
        corridaDTOValida.setId_motorista(999);
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> corridaService.salvar(corridaDTOValida)
        );
        
        assertTrue(exception.getMessage().contains("Motorista não encontrado"));
    }

    // teste salvamento com aplicativo inexistente deve lançar exceção
    @Test
    public void testSalvarCorridaComAplicativoInexistente() {
        corridaDTOValida.setId_aplicativo(999);
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> corridaService.salvar(corridaDTOValida)
        );
        
        assertTrue(exception.getMessage().contains("Aplicativo não encontrado"));
    }

    // buscar corrida por ID existente retorna dados corretos
    @Test
    public void testBuscarPorIdExistente() {
        CorridaDTO salva = corridaService.salvar(corridaDTOValida);
        
        CorridaDTO resultado = corridaService.buscarPorId(salva.getId_corrida());
        
        assertNotNull(resultado);
        assertEquals(salva.getId_corrida(), resultado.getId_corrida());
        assertEquals("Rua A", resultado.getOrg_corrida());
    }

    // buscar por ID inexistente deve lançar ResourceNotFound
    @Test
    public void testBuscarPorIdNaoExistente() {
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> corridaService.buscarPorId(999)
        );
        
        assertTrue(exception.getMessage().contains("Corrida não encontrada"));
    }

    // listar todas as corridas deve retornar lista completa
    @Test
    public void testListarTodos() {
        corridaService.salvar(corridaDTOValida);
        
        CorridaDTO corrida2 = new CorridaDTO();
        corrida2.setDat_corrida(LocalDate.now());
        corrida2.setHor_corrida(LocalTime.now());
        corrida2.setOrg_corrida("Rua C");
        corrida2.setDes_corrida("Rua D");
        corrida2.setVal_corrida(new BigDecimal("75.00"));
        corrida2.setId_motorista(idMotoristaTeste);
        corrida2.setId_aplicativo(idAplicativoTeste);
        corridaService.salvar(corrida2);
        
        List<CorridaDTO> resultado = corridaService.listarTodos();
        
        assertEquals(2, resultado.size());
    }

    // atualizar corrida com dados válidos deve alterar campos
    @Test
    public void testAtualizarCorridaValida() {
        CorridaDTO salva = corridaService.salvar(corridaDTOValida);
        
        CorridaDTO atualizacao = new CorridaDTO();
        atualizacao.setDat_corrida(LocalDate.now().plusDays(1));
        atualizacao.setOrg_corrida("Rua X");
        atualizacao.setDes_corrida("Rua Y");
        atualizacao.setVal_corrida(new BigDecimal("100.00"));
        atualizacao.setId_motorista(idMotoristaTeste);
        atualizacao.setId_aplicativo(idAplicativoTeste);
        
        CorridaDTO resultado = corridaService.atualizar(salva.getId_corrida(), atualizacao);
        
        assertNotNull(resultado);
        assertEquals("Rua X", resultado.getOrg_corrida());
        assertEquals("Rua Y", resultado.getDes_corrida());
        assertEquals(new BigDecimal("100.00"), resultado.getVal_corrida());
    }

    // atualizar com valor inválido não deve quebrar o fluxo
    @Test
    public void testAtualizarCorridaComValorInvalido() {
        CorridaDTO salva = corridaService.salvar(corridaDTOValida);
        
        CorridaDTO atualizacao = new CorridaDTO();
        atualizacao.setVal_corrida(new BigDecimal("-50.00"));
        atualizacao.setId_motorista(idMotoristaTeste);
        atualizacao.setId_aplicativo(idAplicativoTeste);
        atualizacao.setOrg_corrida("Rua A");
        atualizacao.setDes_corrida("Rua B");
        atualizacao.setDat_corrida(LocalDate.now());
        atualizacao.setHor_corrida(LocalTime.now());
        
        CorridaDTO resultado = corridaService.atualizar(salva.getId_corrida(), atualizacao);
        
        assertNotNull(resultado);
    }

    // atualizar corrida inexistente deve lançar ResourceNotFound
    @Test
    public void testAtualizarCorridaInexistente() {
        CorridaDTO atualizacao = new CorridaDTO();
        atualizacao.setOrg_corrida("Rua Z");
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> corridaService.atualizar(999, atualizacao)
        );
        
        assertTrue(exception.getMessage().contains("Corrida não encontrada"));
    }

    // alterar motorista em corrida existente deve atualizar referência
    @Test
    public void testAtualizarMotoristaEmCorrida() {
        CorridaDTO salva = corridaService.salvar(corridaDTOValida);
        
        MotoristaDTO novoMotorista = new MotoristaDTO();
        novoMotorista.setNrm_motorista("Maria Silva");
        novoMotorista.setCpf_motorista("98765432101");
        novoMotorista.setTel_motorista("11912345678");
        novoMotorista.setPla_veiculo("XYZ9876");
        MotoristaDTO motoristaResult = motoristaService.salvar(novoMotorista);
        
        CorridaDTO atualizacao = new CorridaDTO();
        atualizacao.setId_motorista(motoristaResult.getId_motorista());
        atualizacao.setDat_corrida(salva.getDat_corrida());
        atualizacao.setHor_corrida(salva.getHor_corrida());
        atualizacao.setOrg_corrida(salva.getOrg_corrida());
        atualizacao.setDes_corrida(salva.getDes_corrida());
        atualizacao.setVal_corrida(salva.getVal_corrida());
        atualizacao.setId_aplicativo(idAplicativoTeste);
        
        CorridaDTO resultado = corridaService.atualizar(salva.getId_corrida(), atualizacao);
        
        assertEquals(motoristaResult.getId_motorista(), resultado.getId_motorista());
    }

    // excluir corrida existente e verificar remoção
    @Test
    public void testExcluirCorridaExistente() {
        CorridaDTO salva = corridaService.salvar(corridaDTOValida);
        
        corridaService.excluir(salva.getId_corrida());
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> corridaService.buscarPorId(salva.getId_corrida())
        );
        
        assertTrue(exception.getMessage().contains("Corrida não encontrada"));
    }

    // tentativa de excluir corrida inexistente lança exceção
    @Test
    public void testExcluirCorridaInexistente() {
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> corridaService.excluir(999)
        );
        assertTrue(exception.getMessage().contains("Corrida não encontrada"));
    }

    // salvar corrida com valor muito grande é permitido
    @Test
    public void testCorridaComValorGrande() {
        corridaDTOValida.setVal_corrida(new BigDecimal("9999.99"));
        
        CorridaDTO resultado = corridaService.salvar(corridaDTOValida);
        
        assertNotNull(resultado);
        assertEquals(new BigDecimal("9999.99"), resultado.getVal_corrida());
    }
}
