package com.uber.motoristauber.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "MOTORISTA")
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_motorista") // alterado
    private Integer id_motorista;

    @Column(name = "nrm_motorista", nullable = false)
    private String nrm_motorista;

    @Column(name = "cpf_motorista", nullable = false, unique = true, length = 11)
    private String cpf_motorista;

    @Column(name = "tel_motorista")
    private String tel_motorista;

    @Column(name = "pla_veiculo")
    private String pla_veiculo;

    @OneToMany(mappedBy = "motorista")
    private List<Corrida> corridas;

    public Motorista() {
    }

    public Motorista(List<Corrida> corridas, String cpf_motorista, Integer id_motorista, String nrm_motorista, String pla_veiculo, String tel_motorista) {
        this.corridas = corridas;
        this.cpf_motorista = cpf_motorista;
        this.id_motorista = id_motorista;
        this.nrm_motorista = nrm_motorista;
        this.pla_veiculo = pla_veiculo;
        this.tel_motorista = tel_motorista;
    }


    // getters / setters
    public Integer getId_motorista() { return id_motorista; }
    public void setId_motorista(Integer id_motorista) { this.id_motorista = id_motorista; }

    public String getNrm_motorista() { return nrm_motorista; }
    public void setNrm_motorista(String nrm_motorista) { this.nrm_motorista = nrm_motorista; }

    public String getCpf_motorista() { return cpf_motorista; }
    public void setCpf_motorista(String cpf_motorista) { this.cpf_motorista = cpf_motorista; }

    public String getTel_motorista() { return tel_motorista; }
    public void setTel_motorista(String tel_motorista) { this.tel_motorista = tel_motorista; }

    public String getPla_veiculo() { return pla_veiculo; }
    public void setPla_veiculo(String pla_veiculo) { this.pla_veiculo = pla_veiculo; }

    public List<Corrida> getCorridas() { return corridas; }
    public void setCorridas(List<Corrida> corridas) { this.corridas = corridas; }
}
