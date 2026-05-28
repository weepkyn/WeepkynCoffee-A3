/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.anhembi.weepkyncoffee.dao;

import br.anhembi.weepkyncoffee.model.ItemComanda;
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
public class ItemComandaDAO {

    public static boolean adicionar(ItemComanda item) {
        String sqlItem    = "insert into itens_comanda (comanda_id, produto_id, quantidade, preco_unitario) values (?,?,?,?)";
        String sqlEstoque = "update produtos set quantidade = quantidade - ? where id = ? and quantidade >= ?";

        Connection con = null;
        try {
            con = Conexao.getConnection();
            con.setAutoCommit(false);

            PreparedStatement prepararItem = con.prepareStatement(sqlItem);
            prepararItem.setInt(1, item.getComandaId());
            prepararItem.setInt(2, item.getProdutoId());
            prepararItem.setInt(3, item.getQuantidade());
            prepararItem.setDouble(4, item.getPrecoUnitario());
            prepararItem.executeUpdate();
            prepararItem.close();

            PreparedStatement prepararEstoque = con.prepareStatement(sqlEstoque);
            prepararEstoque.setInt(1, item.getQuantidade());
            prepararEstoque.setInt(2, item.getProdutoId());
            prepararEstoque.setInt(3, item.getQuantidade());
            int linhasAfetadas = prepararEstoque.executeUpdate();
            prepararEstoque.close();

            if(linhasAfetadas == 0) {
                con.rollback();
                con.close();
                System.out.println("Rollback - estoque insuficiente para produto " + item.getProdutoId());
                return false;
            }

            con.commit();
            con.close();
            System.out.println("Item adicionado ok - comanda " + item.getComandaId());
            return true;

        } catch (Exception e) {
            System.out.println("Erro ao adicionar item");
            System.out.println(e.getMessage());
            try { if(con != null) con.rollback(); } catch(Exception ignored) {}
            try { if(con != null) con.close();    } catch(Exception ignored) {}
            return false;
        }
    }

    public static boolean remover(int itemId, String motivo) {
        String sqlBuscar  = "select * from itens_comanda where id = ?";
        String sqlLog     = "insert into itens_removidos (comanda_id, produto_id, quantidade, motivo) values (?,?,?,?)";
        String sqlDeletar = "delete from itens_comanda where id = ?";

        Connection con = null;
        try {
            con = Conexao.getConnection();
            con.setAutoCommit(false);

            PreparedStatement prepararBusca = con.prepareStatement(sqlBuscar);
            prepararBusca.setInt(1, itemId);
            ResultSet resultado = prepararBusca.executeQuery();

            if(!resultado.next()) {
                con.rollback();
                con.close();
                return false;
            }

            int comandaId = resultado.getInt("comanda_id");
            int produtoId = resultado.getInt("produto_id");
            int quantidade = resultado.getInt("quantidade");
            prepararBusca.close();

            PreparedStatement prepararLog = con.prepareStatement(sqlLog);
            prepararLog.setInt(1, comandaId);
            prepararLog.setInt(2, produtoId);
            prepararLog.setInt(3, quantidade);
            prepararLog.setString(4, motivo);
            prepararLog.executeUpdate();
            prepararLog.close();

            PreparedStatement prepararDeletar = con.prepareStatement(sqlDeletar);
            prepararDeletar.setInt(1, itemId);
            prepararDeletar.executeUpdate();
            prepararDeletar.close();

            con.commit();
            con.close();
            System.out.println("Item " + itemId + " removido - motivo: " + motivo);
            return true;

        } catch (Exception e) {
            System.out.println("Erro ao remover item");
            System.out.println(e.getMessage());
            try { if(con != null) con.rollback(); } catch(Exception ignored) {}
            try { if(con != null) con.close();    } catch(Exception ignored) {}
            return false;
        }
    }

    public static List<ItemComanda> listarPorComanda(int comandaId) {
        List<ItemComanda> lista = new ArrayList<>();
        String sql = "select ic.*, p.nome as nome_produto from itens_comanda ic " +
                     "join produtos p on ic.produto_id = p.id where ic.comanda_id = ?";

        try {
            Connection con = Conexao.getConnection();
            PreparedStatement preparar = con.prepareStatement(sql);
            preparar.setInt(1, comandaId);
            ResultSet resultado = preparar.executeQuery();

            while(resultado.next()) {
                int id             = resultado.getInt("id");
                int cmdId          = resultado.getInt("comanda_id");
                int prodId         = resultado.getInt("produto_id");
                String nomeProd    = resultado.getString("nome_produto");
                int qtd            = resultado.getInt("quantidade");
                double precoUnit   = resultado.getDouble("preco_unitario");

                ItemComanda item = new ItemComanda(id, cmdId, prodId, nomeProd, qtd, precoUnit);
                lista.add(item);
            }

            resultado.close();
            preparar.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Erro ao listar itens da comanda");
            System.out.println(e.getMessage());
        }

        return lista;
    }
}