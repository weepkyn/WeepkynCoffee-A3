/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.anhembi.weepkyncoffee.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Annapnascimento
 */
public class Conexao {

    // jdbc:mysql = protocolo de conexao com o MySQL
    // localhost  = servidor rodando na nossa maquina
    // 3306       = porta padrao do MySQL
    // db_cafeteria = nome do banco de dados
    private static final String URL      = "jdbc:mysql://localhost:3306/db_cafeteria";
    private static final String USER     = "root";
    private static final String PASSWORD = "admin";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}