/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.anhembi.weepkyncoffee.dao;

import br.anhembi.weepkyncoffee.model.Produto;
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
public class ProdutoDAO {

    public static boolean inserir(Produto produto) {
        String sql = "insert into produtos (nome, preco, categoria, quantidade) values (?,?,?,?)";

        try {
            Connection con = Conexao.getConnection();
            PreparedStatement preparar = con.prepareStatement(sql);

            preparar.setString(1, produto.getNome());
            preparar.setDouble(2, produto.getPreco());
            preparar.setString(3, produto.getCategoria());
            preparar.setInt(4, produto.getQuantidade());

            preparar.executeUpdate();

            preparar.close();
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao inserir produto");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "select * from produtos where ativo = 1 order by categoria, nome";

        try {
            Connection con = Conexao.getConnection();
            PreparedStatement preparar = con.prepareStatement(sql);
            ResultSet resultado = preparar.executeQuery();

            while(resultado.next()) {
                int id           = resultado.getInt("id");
                String nome      = resultado.getString("nome");
                double preco     = resultado.getDouble("preco");
                String categoria = resultado.getString("categoria");
                int quantidade   = resultado.getInt("quantidade");

                Produto produto = new Produto(id, nome, preco, categoria, quantidade);
                lista.add(produto);
            }

            resultado.close();
            preparar.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Erro ao listar produtos");
            System.out.println(e.getMessage());
        }

        return lista;
    }

    public static List<Produto> listarDisponiveis() {
        List<Produto> lista = new ArrayList<>();
        String sql = "select * from produtos where ativo = 1 and quantidade > 0 order by nome";

        try {
            Connection con = Conexao.getConnection();
            PreparedStatement preparar = con.prepareStatement(sql);
            ResultSet resultado = preparar.executeQuery();

            while(resultado.next()) {
                int id           = resultado.getInt("id");
                String nome      = resultado.getString("nome");
                double preco     = resultado.getDouble("preco");
                String categoria = resultado.getString("categoria");
                int quantidade   = resultado.getInt("quantidade");

                Produto produto = new Produto(id, nome, preco, categoria, quantidade);
                lista.add(produto);
            }

            resultado.close();
            preparar.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Erro ao listar produtos disponiveis");
            System.out.println(e.getMessage());
        }

        return lista;
    }

    public static List<Produto> listarEstoqueBaixo(int limite) {
        List<Produto> lista = new ArrayList<>();
        String sql = "select * from produtos where ativo = 1 and quantidade <= ? order by quantidade";

        try {
            Connection con = Conexao.getConnection();
            PreparedStatement preparar = con.prepareStatement(sql);
            preparar.setInt(1, limite);
            ResultSet resultado = preparar.executeQuery();

            while(resultado.next()) {
                int id           = resultado.getInt("id");
                String nome      = resultado.getString("nome");
                double preco     = resultado.getDouble("preco");
                String categoria = resultado.getString("categoria");
                int quantidade   = resultado.getInt("quantidade");

                Produto produto = new Produto(id, nome, preco, categoria, quantidade);
                lista.add(produto);
            }

            resultado.close();
            preparar.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Erro ao listar estoque baixo");
            System.out.println(e.getMessage());
        }

        return lista;
    }

    public static boolean atualizar(Produto produto) {
        String sql = "update produtos set nome=?, preco=?, categoria=?, quantidade=? where id=?";

        try {
            Connection con = Conexao.getConnection();
            PreparedStatement preparar = con.prepareStatement(sql);

            preparar.setString(1, produto.getNome());
            preparar.setDouble(2, produto.getPreco());
            preparar.setString(3, produto.getCategoria());
            preparar.setInt(4, produto.getQuantidade());
            preparar.setInt(5, produto.getId());

            preparar.executeUpdate();

            preparar.close();
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar produto");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean apagar(int id) {
        String sql = "update produtos set ativo = 0 where id = ?";

        try {
            Connection con = Conexao.getConnection();
            PreparedStatement preparar = con.prepareStatement(sql);
            preparar.setInt(1, id);
            preparar.executeUpdate();
            preparar.close();
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao apagar produto");
            System.out.println(e.getMessage());
            return false;
        }
    }
}