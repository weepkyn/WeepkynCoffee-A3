/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.anhembi.weepkyncoffee.model;

/**
 *
 * @author Annapnascimento
 */
public class ItemComanda {

    private int    id;
    private int    comandaId;
    private int    produtoId;
    private String nomeProduto;
    private int    quantidade;
    private double precoUnitario;

    public ItemComanda() {
    }

    public ItemComanda(int comandaId, int produtoId, int quantidade, double precoUnitario) {
        this.comandaId     = comandaId;
        this.produtoId     = produtoId;
        this.quantidade    = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public ItemComanda(int id, int comandaId, int produtoId, String nomeProduto,
                       int quantidade, double precoUnitario) {
        this.id            = id;
        this.comandaId     = comandaId;
        this.produtoId     = produtoId;
        this.nomeProduto   = nomeProduto;
        this.quantidade    = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public double getTotal() {
        return quantidade * precoUnitario;
    }

    public int getId() {
        return id;
    }

    public int getComandaId() {
        return comandaId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setComandaId(int comandaId) {
        this.comandaId = comandaId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}