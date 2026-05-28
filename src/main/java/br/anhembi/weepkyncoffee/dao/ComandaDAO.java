/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.anhembi.weepkyncoffee.dao;

import br.anhembi.weepkyncoffee.model.Comanda;
import br.anhembi.weepkyncoffee.util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Annapnascimento
 */
// DAO = Data Access Object
public class ComandaDAO {

    public static boolean inserir(Comanda comanda) {
        String sql = "insert into comandas (nome_cliente) values (?)";

        try {
            Connection con = Conexao.getConnection();
            PreparedStatement preparar = con.prepareStatement(sql);
            preparar.setString(1, comanda.getNomeCliente());
            preparar.executeUpdate();
            preparar.close();
            con.close();
            System.out.println("Comanda criada para: " + comanda.getNomeCliente());
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao inserir comanda");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static List<Comanda> listarTodas() {
        List<Comanda> lista = new ArrayList<>();
        String sql = "select * from comandas order by status asc, data_abertura desc";

        try {
            Connection con = Conexao.getConnection();
            PreparedStatement preparar = con.prepareStatement(sql);
            ResultSet resultado = preparar.executeQuery();

            while(resultado.next()) {
                int id                = resultado.getInt("id");
                String nomeCliente    = resultado.getString("nome_cliente");
                String dataAbertura   = resultado.getString("data_abertura");
                String status         = resultado.getString("status");
                String formaPagamento = resultado.getString("forma_pagamento");

                Comanda comanda = new Comanda(id, nomeCliente, dataAbertura, status, formaPagamento);
                lista.add(comanda);
            }

            resultado.close();
            preparar.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Erro ao listar comandas");
            System.out.println(e.getMessage());
        }

        return lista;
    }

    public static int contarAbertas() {
        String sql = "select count(*) from comandas where status = 'Aberta'";

        try {
            Connection con = Conexao.getConnection();
            PreparedStatement preparar = con.prepareStatement(sql);
            ResultSet resultado = preparar.executeQuery();

            if(resultado.next()) {
                int total = resultado.getInt(1);
                resultado.close();
                preparar.close();
                con.close();
                return total;
            }

            resultado.close();
            preparar.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Erro ao contar comandas");
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public static double receitaHoje() {
        String sql = "select coalesce(sum(ic.quantidade * ic.preco_unitario), 0) " +
                     "from itens_comanda ic join comandas c on ic.comanda_id = c.id " +
                     "where date(c.data_abertura) = curdate() and c.status = 'Paga'";

        try {
            Connection con = Conexao.getConnection();
            PreparedStatement preparar = con.prepareStatement(sql);
            ResultSet resultado = preparar.executeQuery();

            if(resultado.next()) {
                double receita = resultado.getDouble(1);
                resultado.close();
                preparar.close();
                con.close();
                return receita;
            }

            resultado.close();
            preparar.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Erro ao calcular receita");
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public static double calcularTotal(int comandaId) {
        String sql = "select coalesce(sum(quantidade * preco_unitario), 0) " +
                     "from itens_comanda where comanda_id = ?";

        try {
            Connection con = Conexao.getConnection();
            PreparedStatement preparar = con.prepareStatement(sql);
            preparar.setInt(1, comandaId);
            ResultSet resultado = preparar.executeQuery();

            if(resultado.next()) {
                double total = resultado.getDouble(1);
                resultado.close();
                preparar.close();
                con.close();
                return total;
            }

            resultado.close();
            preparar.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Erro ao calcular total da comanda");
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public static boolean fechar(int id, String formaPagamento) {
        String sql = "update comandas set status = 'Paga', forma_pagamento = ? where id = ?";

        try {
            Connection con = Conexao.getConnection();
            PreparedStatement preparar = con.prepareStatement(sql);
            preparar.setString(1, formaPagamento);
            preparar.setInt(2, id);
            preparar.executeUpdate();
            preparar.close();
            con.close();
            System.out.println("Comanda " + id + " fechada via " + formaPagamento);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao fechar comanda");
            System.out.println(e.getMessage());
            return false;
        }
    }
}