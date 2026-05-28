/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.anhembi.weepkyncoffee.model;

/**
 *
 * @author Annapnascimento
 */
public class Produto {

    private int    id;
    private String nome;
    private double preco;
    private String categoria;
    private int    quantidade;

    public Produto() {
    }

    public Produto(String nome, double preco, String categoria, int quantidade) {
        this.nome       = nome;
        this.preco      = preco;
        this.categoria  = categoria;
        this.quantidade = quantidade;
    }

    public Produto(int id, String nome, double preco, String categoria, int quantidade) {
        this.id         = id;
        this.nome       = nome;
        this.preco      = preco;
        this.categoria  = categoria;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    // precisei colocar pra aparecer o nome certo no combo
    @Override
    public String toString() {
        return nome;
    }
}