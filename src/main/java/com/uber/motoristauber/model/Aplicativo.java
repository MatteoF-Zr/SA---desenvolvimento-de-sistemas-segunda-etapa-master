package com.uber.motoristauber.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "APLICATIVO")
public class Aplicativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aplicativo")
    private Integer id_aplicativo;

    @Column(name = "nom_aplicativo", nullable = false)
    private String nom_aplicativo;

    @Column(name = "tax_plataforma")
    private BigDecimal tax_plataforma;

    @Column(name = "ema_suporte")
    private String ema_suporte;

    @Column(name = "tax_cancelamento")
    private BigDecimal tax_cancelamento;

    @OneToMany(mappedBy = "aplicativo")
    private List<Corrida> corridas;

    // getters / setters

    public Aplicativo() {

    }

    public Aplicativo(List<Corrida> corridas, String ema_suporte, Integer id_aplicativo, String nom_aplicativo, BigDecimal tax_cancelamento, BigDecimal tax_plataforma) {
        this.corridas = corridas;
        this.ema_suporte = ema_suporte;
        this.id_aplicativo = id_aplicativo;
        this.nom_aplicativo = nom_aplicativo;
        this.tax_cancelamento = tax_cancelamento;
        this.tax_plataforma = tax_plataforma;
    }
    


    public Integer getId_aplicativo() { return id_aplicativo; }
    public void setId_aplicativo(Integer id_aplicativo) { this.id_aplicativo = id_aplicativo; }

    public String getNom_aplicativo() { return nom_aplicativo; }
    public void setNom_aplicativo(String nom_aplicativo) { this.nom_aplicativo = nom_aplicativo; }

    public BigDecimal getTax_plataforma() { return tax_plataforma; }
    public void setTax_plataforma(BigDecimal tax_plataforma) { this.tax_plataforma = tax_plataforma; }

    public String getEma_suporte() { return ema_suporte; }
    public void setEma_suporte(String ema_suporte) { this.ema_suporte = ema_suporte; }

    public BigDecimal getTax_cancelamento() { return tax_cancelamento; }
    public void setTax_cancelamento(BigDecimal tax_cancelamento) { this.tax_cancelamento = tax_cancelamento; }

    public List<Corrida> getCorridas() { return corridas; }
    public void setCorridas(List<Corrida> corridas) { this.corridas = corridas; }
}
