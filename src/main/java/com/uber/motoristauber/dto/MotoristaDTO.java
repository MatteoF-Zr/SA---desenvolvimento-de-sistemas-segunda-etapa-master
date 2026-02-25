package com.uber.motoristauber.dto;

public class MotoristaDTO {
    private Integer id_motorista;
    private String nrm_motorista;
    private String cpf_motorista;
    private String tel_motorista;
    private String pla_veiculo;

    public MotoristaDTO() {
    }

    public MotoristaDTO(Integer id_motorista, String nrm_motorista, String cpf_motorista, String tel_motorista, String pla_veiculo) {
        this.id_motorista = id_motorista;
        this.nrm_motorista = nrm_motorista;
        this.cpf_motorista = cpf_motorista;
        this.tel_motorista = tel_motorista;
        this.pla_veiculo = pla_veiculo;
    }

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
}