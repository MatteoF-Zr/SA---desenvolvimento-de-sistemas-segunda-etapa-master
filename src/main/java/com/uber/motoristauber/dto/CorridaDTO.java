package com.uber.motoristauber.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class CorridaDTO {
    private Integer id_corrida;
    private LocalDate dat_corrida;
    private LocalTime hor_corrida;
    private String org_corrida;
    private String des_corrida;
    private BigDecimal val_corrida;
    private Integer id_motorista;
    private Integer id_aplicativo;

    public CorridaDTO() {
    }

    public CorridaDTO(Integer id_corrida, LocalDate dat_corrida, LocalTime hor_corrida, String org_corrida, String des_corrida, BigDecimal val_corrida, Integer id_motorista, Integer id_aplicativo) {
        this.id_corrida = id_corrida;
        this.dat_corrida = dat_corrida;
        this.hor_corrida = hor_corrida;
        this.org_corrida = org_corrida;
        this.des_corrida = des_corrida;
        this.val_corrida = val_corrida;
        this.id_motorista = id_motorista;
        this.id_aplicativo = id_aplicativo;
    }

    public Integer getId_corrida() { return id_corrida; }
    public void setId_corrida(Integer id_corrida) { this.id_corrida = id_corrida; }

    public LocalDate getDat_corrida() { return dat_corrida; }
    public void setDat_corrida(LocalDate dat_corrida) { this.dat_corrida = dat_corrida; }

    public LocalTime getHor_corrida() { return hor_corrida; }
    public void setHor_corrida(LocalTime hor_corrida) { this.hor_corrida = hor_corrida; }

    public String getOrg_corrida() { return org_corrida; }
    public void setOrg_corrida(String org_corrida) { this.org_corrida = org_corrida; }

    public String getDes_corrida() { return des_corrida; }
    public void setDes_corrida(String des_corrida) { this.des_corrida = des_corrida; }

    public BigDecimal getVal_corrida() { return val_corrida; }
    public void setVal_corrida(BigDecimal val_corrida) { this.val_corrida = val_corrida; }

    public Integer getId_motorista() { return id_motorista; }
    public void setId_motorista(Integer id_motorista) { this.id_motorista = id_motorista; }

    public Integer getId_aplicativo() { return id_aplicativo; }
    public void setId_aplicativo(Integer id_aplicativo) { this.id_aplicativo = id_aplicativo; }
}