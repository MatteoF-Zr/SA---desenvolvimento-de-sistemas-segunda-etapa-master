package com.uber.motoristauber.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CORRIDA")
public class Corrida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_corrida") // usar exatamente o nome da coluna PK no seu banco
    private Integer id_corrida;

    @Column(name = "dat_corrida")
    private LocalDate dat_corrida;

    @Column(name = "hor_corrida")
    private LocalTime hor_corrida;

    @Column(name = "org_corrida")
    private String org_corrida;

    @Column(name = "des_corrida")
    private String des_corrida;

    @Column(name = "val_corrida")
    private BigDecimal val_corrida;

    @ManyToOne
    @JoinColumn(name = "id_motorista", referencedColumnName = "id_motorista") // ajuste conforme sua tabela MOTORISTA
    private Motorista motorista;

    @ManyToOne
    @JoinColumn(name = "id_aplicativo", referencedColumnName = "id_aplicativo") // ajuste conforme sua tabela APLICATIVO
    private Aplicativo aplicativo;

    // getters / setters...

    public Corrida() {
    }

    public Corrida(Aplicativo aplicativo, LocalDate dat_corrida, String des_corrida, LocalTime hor_corrida, Integer id_corrida, Motorista motorista, String org_corrida, BigDecimal val_corrida) {
        this.aplicativo = aplicativo;
        this.dat_corrida = dat_corrida;
        this.des_corrida = des_corrida;
        this.hor_corrida = hor_corrida;
        this.id_corrida = id_corrida;
        this.motorista = motorista;
        this.org_corrida = org_corrida;
        this.val_corrida = val_corrida;
    }

    public Integer getId_corrida() {
        return id_corrida;
    }

    public void setId_corrida(Integer id_corrida) {
        this.id_corrida = id_corrida;
    }

    public LocalDate getDat_corrida() {
        return dat_corrida;
    }

    public void setDat_corrida(LocalDate dat_corrida) {
        this.dat_corrida = dat_corrida;
    }

    public LocalTime getHor_corrida() {
        return hor_corrida;
    }

    public void setHor_corrida(LocalTime hor_corrida) {
        this.hor_corrida = hor_corrida;
    }

    public String getOrg_corrida() {
        return org_corrida;
    }

    public void setOrg_corrida(String org_corrida) {
        this.org_corrida = org_corrida;
    }

    public String getDes_corrida() {
        return des_corrida;
    }

    public void setDes_corrida(String des_corrida) {
        this.des_corrida = des_corrida;
    }

    public BigDecimal getVal_corrida() {
        return val_corrida;
    }

    public void setVal_corrida(BigDecimal val_corrida) {
        this.val_corrida = val_corrida;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Aplicativo getAplicativo() {
        return aplicativo;
    }

    public void setAplicativo(Aplicativo aplicativo) {
        this.aplicativo = aplicativo;
    }


}
