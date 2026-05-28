/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.anhembi.weepkyncoffee.model;

/**
 *
 * @author Annapnascimento
 */
public class Comanda {

    private int    id;
    private String nomeCliente;
    private String dataAbertura;
    private String status;
    private String formaPagamento;

    public Comanda() {
    }

    public Comanda(String nomeCliente) {
        this.nomeCliente = nomeCliente;
        this.status      = "Aberta";
    }

    public Comanda(int id, String nomeCliente, String dataAbertura,
                   String status, String formaPagamento) {
        this.id             = id;
        this.nomeCliente    = nomeCliente;
        this.dataAbertura   = dataAbertura;
        this.status         = status;
        this.formaPagamento = formaPagamento;
    }

    public int getId() {
        return id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public String getStatus() {
        return status;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}