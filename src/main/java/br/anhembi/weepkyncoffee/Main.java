/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.anhembi.weepkyncoffee;

import br.anhembi.weepkyncoffee.dao.ComandaDAO;
import br.anhembi.weepkyncoffee.dao.ItemComandaDAO;
import br.anhembi.weepkyncoffee.dao.ProdutoDAO;
import br.anhembi.weepkyncoffee.model.Comanda;
import br.anhembi.weepkyncoffee.model.ItemComanda;
import br.anhembi.weepkyncoffee.model.Produto;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author Annapnascimento
 */
public class Main extends JFrame {

    private static final Color COR_CAFE     = new Color(190, 115, 50);
    private static final Color COR_FUNDO    = new Color(243, 238, 230);
    private static final Color COR_VERDE    = new Color(55, 148, 78);
    private static final Color COR_VERMELHO = new Color(198, 58, 58);
    private static final Color COR_AMARELO  = new Color(210, 148, 28);
    private static final Color COR_CINZA    = new Color(130, 110, 90);
    private static final Font  FONTE_PADRAO = new Font("Segoe UI", Font.PLAIN, 13);

    // cardapio
    private JTextField inputId    = new JTextField(5);
    private JTextField inputNome  = new JTextField(20);
    private JTextField inputPreco = new JTextField(8);
    private JTextField inputQtd   = new JTextField(6);
    private JComboBox<String> cbCategoria = new JComboBox<>(
        new String[]{"Bebidas Quentes", "Bebidas Frias", "Lanches", "Doces", "Outros"});
    private DefaultTableModel modelProd;
    private JTable tabelaProd;

    // comandas
    private DefaultTableModel modelComandas, modelItens;
    private JTable tabelaComandas, tabelaItens;
    private JLabel lblCliente  = new JLabel("---");
    private JLabel lblAbertura = new JLabel("---");
    private JLabel lblStatus   = new JLabel("---");
    private JLabel lblTotal    = new JLabel("R$ 0,00");
    private JComboBox<Produto> cbProduto = new JComboBox<>();
    private JSpinner spinQtd = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private JButton btnPagar, btnRemover;
    private int cmdId = -1;

    // dashboard
    private JLabel lblComandasAbertas = new JLabel("0");
    private JLabel lblReceitaHoje     = new JLabel("R$ 0,00");
    private JLabel lblEstoqueBaixo    = new JLabel("0");
    private DefaultTableModel modelEstoqueBaixo;

    public Main() {
        setTitle("WeepkynCoffee");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        inputId.setEditable(false);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        abas.setBackground(COR_FUNDO);

        abas.addTab("  Dashboard  ", criarAbaDashboard());
        abas.addTab("  Cardapio   ", criarAbaCardapio());
        abas.addTab("  Comandas   ", criarAbaComandas());

        abas.addChangeListener(e -> {
            if (abas.getSelectedIndex() == 0) carregarDashboard();
        });

        add(abas);

        buscarProdutos();
        carregarComandas();
        carregarDashboard();
        carregarComboProdutos();
    }

    // ================================================================
    // DASHBOARD
    // ================================================================
    private JPanel criarAbaDashboard() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(COR_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setBackground(COR_FUNDO);
        cards.add(criarCard("Comandas Abertas", lblComandasAbertas, COR_CAFE));
        cards.add(criarCard("Receita Hoje",     lblReceitaHoje,     COR_VERDE));
        cards.add(criarCard("Estoque Critico",  lblEstoqueBaixo,    COR_VERMELHO));
        p.add(cards, BorderLayout.NORTH);

        modelEstoqueBaixo = new DefaultTableModel(
                new String[]{"Produto", "Categoria", "Restam"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabelaDash = new JTable(modelEstoqueBaixo);
        estilizarTabela(tabelaDash);

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBackground(Color.WHITE);
        painelTabela.setBorder(BorderFactory.createLineBorder(new Color(220, 210, 195)));

        JLabel titulo = new JLabel("  Produtos com estoque critico (5 ou menos)");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titulo.setForeground(COR_CINZA);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 4, 8, 0));
        painelTabela.add(titulo, BorderLayout.NORTH);
        painelTabela.add(scroll(tabelaDash), BorderLayout.CENTER);

        p.add(painelTabela, BorderLayout.CENTER);
        return p;
    }

    private JPanel criarCard(String titulo, JLabel lblValor, Color cor) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.setColor(cor);
                g2.fillRoundRect(0, 0, 6, getHeight(), 3, 3);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(0, 88));
        card.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 14));

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(cor);

        JLabel lTitulo = new JLabel(titulo);
        lTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lTitulo.setForeground(COR_CINZA);

        card.add(lblValor,  BorderLayout.CENTER);
        card.add(lTitulo,   BorderLayout.SOUTH);
        return card;
    }

    private void carregarDashboard() {
        int totalAbertas = ComandaDAO.contarAbertas();
        lblComandasAbertas.setText(String.valueOf(totalAbertas));

        double receita = ComandaDAO.receitaHoje();
        lblReceitaHoje.setText(String.format("R$ %.2f", receita));

        List<Produto> criticos = ProdutoDAO.listarEstoqueBaixo(5);
        lblEstoqueBaixo.setText(String.valueOf(criticos.size()));

        modelEstoqueBaixo.setRowCount(0);
        for (Produto prod : criticos) {
            modelEstoqueBaixo.addRow(new Object[]{
                prod.getNome(), prod.getCategoria(), prod.getQuantidade()
            });
        }
    }

    // ================================================================
    // CARDAPIO
    // ================================================================
    private JPanel criarAbaCardapio() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(COR_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        modelProd = new DefaultTableModel(
                new String[]{"ID", "Nome", "Preco", "Categoria", "Estoque"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaProd = new JTable(modelProd);
        estilizarTabela(tabelaProd);

        // coluna estoque fica vermelha quando critico
        tabelaProd.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

                int qtd;
                if(v != null) {
                    qtd = Integer.parseInt(v.toString());
                } else {
                    qtd = 0;
                }

                if(!sel) {
                    if(qtd <= 5) {
                        setBackground(new Color(255, 235, 235));
                        setForeground(COR_VERMELHO);
                        setFont(new Font("Segoe UI", Font.BOLD, 12));
                    } else {
                        if(row % 2 == 0) {
                            setBackground(Color.WHITE);
                        } else {
                            setBackground(new Color(250, 246, 240));
                        }
                        setForeground(new Color(28, 18, 8));
                        setFont(FONTE_PADRAO);
                    }
                }
                return this;
            }
        });

        tabelaProd.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaProd.getSelectedRow() >= 0) {
                int row = tabelaProd.getSelectedRow();
                inputId.setText(modelProd.getValueAt(row, 0).toString());
                inputNome.setText(modelProd.getValueAt(row, 1).toString());
                inputPreco.setText(modelProd.getValueAt(row, 2).toString());
                cbCategoria.setSelectedItem(modelProd.getValueAt(row, 3).toString());
                inputQtd.setText(modelProd.getValueAt(row, 4).toString());
            }
        });

        p.add(scroll(tabelaProd), BorderLayout.CENTER);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createLineBorder(new Color(220, 210, 195)));
        form.add(rotulo("ID:"));        form.add(inputId);
        form.add(rotulo("Nome:"));      form.add(inputNome);
        form.add(rotulo("Preco:"));     form.add(inputPreco);
        form.add(rotulo("Categoria:")); form.add(cbCategoria);
        form.add(rotulo("Estoque:"));   form.add(inputQtd);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        botoes.setBackground(Color.WHITE);

        // usando ActionListener igual o professor ensinou
        JButton btnGravar = criarBotao("Gravar", COR_CAFE);
        btnGravar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gravarProduto();
            }
        });

        JButton btnBuscar    = criarBotao("Buscar Todos", new Color(80, 120, 180));
        JButton btnAtualizar = criarBotao("Atualizar",    new Color(80, 120, 180));
        JButton btnApagar    = criarBotao("Apagar",       COR_VERMELHO);
        JButton btnLimpar    = criarBotao("Limpar",       COR_CINZA);

        btnBuscar.addActionListener(e    -> buscarProdutos());
        btnAtualizar.addActionListener(e -> atualizarProduto());
        btnApagar.addActionListener(e    -> apagarProduto());
        btnLimpar.addActionListener(e    -> limparProduto());

        botoes.add(btnGravar); botoes.add(btnBuscar); botoes.add(btnAtualizar);
        botoes.add(btnApagar); botoes.add(btnLimpar);

        JPanel sul = new JPanel(new BorderLayout());
        sul.add(form,   BorderLayout.NORTH);
        sul.add(botoes, BorderLayout.CENTER);
        p.add(sul, BorderLayout.SOUTH);

        return p;
    }

    private void gravarProduto() {
        // TODO: verificar se ja existe produto com esse nome
        if(inputNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome do produto.");
            return;
        }
        try {
            String nome      = inputNome.getText().trim();
            double preco     = Double.parseDouble(inputPreco.getText().replace(",", "."));
            String categoria = (String) cbCategoria.getSelectedItem();
            int estoque      = Integer.parseInt(inputQtd.getText().trim());

            Produto novoProduto = new Produto(nome, preco, categoria, estoque);
            Boolean gravou = ProdutoDAO.inserir(novoProduto);

            if(gravou) {
                JOptionPane.showMessageDialog(this, "Produto gravado com sucesso!");
                limparProduto();
                buscarProdutos();
                carregarComboProdutos();
            }
        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preco ou estoque invalido.");
        }
    }

    private void buscarProdutos() {
        modelProd.setRowCount(0);
        List<Produto> lista = ProdutoDAO.listarTodos();
        for(Produto prod : lista) {
            Object[] linha = {
                prod.getId(),
                prod.getNome(),
                String.format("%.2f", prod.getPreco()),
                prod.getCategoria(),
                prod.getQuantidade()
            };
            modelProd.addRow(linha);
        }
    }

    private void atualizarProduto() {
        if(inputId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }
        try {
            int id           = Integer.parseInt(inputId.getText());
            String nome      = inputNome.getText().trim();
            double preco     = Double.parseDouble(inputPreco.getText().replace(",", "."));
            String categoria = (String) cbCategoria.getSelectedItem();
            int estoque      = Integer.parseInt(inputQtd.getText().trim());

            if(preco <= 0) {
                JOptionPane.showMessageDialog(this, "Preco deve ser maior que zero.");
                return;
            }

            Produto produtoAtualizado = new Produto(id, nome, preco, categoria, estoque);
            Boolean atualizou = ProdutoDAO.atualizar(produtoAtualizado);

            if(atualizou) {
                JOptionPane.showMessageDialog(this, "Produto atualizado!");
                limparProduto();
                buscarProdutos();
                carregarComboProdutos();
            }
        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preco ou estoque invalido.");
        }
    }

    private void apagarProduto() {
        if(inputId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }
        int confirma = JOptionPane.showConfirmDialog(this,
            "Confirma a exclusao do produto?", "Apagar", JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(inputId.getText());
            ProdutoDAO.apagar(id);
            JOptionPane.showMessageDialog(this, "Produto removido do cardapio!");
            limparProduto();
            buscarProdutos();
            carregarComboProdutos();
        }
    }

    private void limparProduto() {
        inputId.setText("");
        inputNome.setText("");
        inputPreco.setText("");
        inputQtd.setText("");
        cbCategoria.setSelectedIndex(0);
        tabelaProd.clearSelection();
    }

    // ================================================================
    // COMANDAS
    // ================================================================
    private JPanel criarAbaComandas() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(COR_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        modelComandas = new DefaultTableModel(new String[]{"#", "Cliente", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaComandas = new JTable(modelComandas);
        estilizarTabela(tabelaComandas);
        tabelaComandas.getColumnModel().getColumn(0).setPreferredWidth(35);
        tabelaComandas.getColumnModel().getColumn(2).setPreferredWidth(70);

        tabelaComandas.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

                String s = "";
                if(v != null) {
                    s = v.toString();
                }

                if(!sel) {
                    if(row % 2 == 0) {
                        setBackground(Color.WHITE);
                    } else {
                        setBackground(new Color(250, 246, 240));
                    }

                    if("Aberta".equals(s)) {
                        setForeground(COR_CAFE);
                    } else {
                        setForeground(COR_VERDE);
                    }
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                }
                return this;
            }
        });

        tabelaComandas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaComandas.getSelectedRow() >= 0) {
                cmdId = (int) modelComandas.getValueAt(tabelaComandas.getSelectedRow(), 0);
                carregarItens(cmdId);
            }
        });

        JButton btnNova      = criarBotao("+ Nova Comanda", COR_CAFE);
        JButton btnHistorico = criarBotao("Historico",      COR_CINZA);
        btnNova.addActionListener(e      -> novaComanda());
        btnHistorico.addActionListener(e -> abrirHistorico());

        JPanel topBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        topBotoes.setBackground(Color.WHITE);
        topBotoes.add(btnHistorico);
        topBotoes.add(btnNova);

        JPanel topLista = new JPanel(new BorderLayout());
        topLista.setBackground(Color.WHITE);
        JLabel lLista = new JLabel("  Comandas");
        lLista.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lLista.setForeground(COR_CINZA);
        lLista.setBorder(BorderFactory.createEmptyBorder(8, 4, 6, 0));
        topLista.add(lLista,    BorderLayout.WEST);
        topLista.add(topBotoes, BorderLayout.EAST);

        JPanel painelLista = new JPanel(new BorderLayout());
        painelLista.setBackground(Color.WHITE);
        painelLista.add(topLista,               BorderLayout.NORTH);
        painelLista.add(scroll(tabelaComandas),  BorderLayout.CENTER);

        // barra de info da comanda selecionada
        JPanel infoBar = new JPanel(new GridLayout(1, 4, 8, 0));
        infoBar.setBackground(new Color(248, 244, 238));
        infoBar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        lblCliente.setFont(FONTE_PADRAO);
        lblAbertura.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblAbertura.setForeground(COR_CINZA);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(COR_CAFE);

        infoBar.add(painelInfo("Cliente:",  lblCliente));
        infoBar.add(painelInfo("Abertura:", lblAbertura));
        infoBar.add(painelInfo("Status:",   lblStatus));
        infoBar.add(painelInfo("Total:",    lblTotal));

        modelItens = new DefaultTableModel(
                new String[]{"#", "Produto", "Qtd", "Preco unit.", "Total"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaItens = new JTable(modelItens);
        estilizarTabela(tabelaItens);
        tabelaItens.getColumnModel().getColumn(0).setPreferredWidth(35);
        tabelaItens.getColumnModel().getColumn(2).setPreferredWidth(40);

        cbProduto.setFont(FONTE_PADRAO);
        cbProduto.setPreferredSize(new Dimension(200, 28));
        cbProduto.addActionListener(e -> {
            Produto prod = (Produto) cbProduto.getSelectedItem();
            if (prod != null)
                ((SpinnerNumberModel) spinQtd.getModel()).setMaximum(prod.getQuantidade());
        });
        spinQtd.setFont(FONTE_PADRAO);
        spinQtd.setPreferredSize(new Dimension(60, 28));

        JButton btnAdd = criarBotao("Adicionar", COR_CAFE);
        btnAdd.addActionListener(e -> adicionarItem());

        JPanel addForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        addForm.setBackground(Color.WHITE);
        addForm.add(rotulo("Produto:")); addForm.add(cbProduto);
        addForm.add(rotulo("Qtd:"));    addForm.add(spinQtd);
        addForm.add(btnAdd);

        btnRemover = criarBotao("Remover Item",   COR_AMARELO);
        btnPagar   = criarBotao("Fechar e Pagar", COR_VERDE);
        btnRemover.addActionListener(e -> removerItem());
        btnPagar.addActionListener(e   -> fecharComanda());

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        acoes.setBackground(Color.WHITE);
        acoes.add(btnRemover);
        acoes.add(btnPagar);

        JPanel rodapeDetalhe = new JPanel(new BorderLayout());
        rodapeDetalhe.setBackground(Color.WHITE);
        rodapeDetalhe.add(addForm, BorderLayout.NORTH);
        rodapeDetalhe.add(acoes,   BorderLayout.SOUTH);

        JPanel painelDetalhe = new JPanel(new BorderLayout());
        painelDetalhe.setBackground(Color.WHITE);
        painelDetalhe.add(infoBar,             BorderLayout.NORTH);
        painelDetalhe.add(scroll(tabelaItens), BorderLayout.CENTER);
        painelDetalhe.add(rodapeDetalhe,       BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelLista, painelDetalhe);
        split.setDividerLocation(310); // tentei 300 mas ficou pequenininho
        split.setDividerSize(5);
        split.setBorder(null);

        p.add(split);
        return p;
    }

    private JPanel painelInfo(String label, JLabel valor) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setBackground(new Color(248, 244, 238));
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(COR_CINZA);
        p.add(l);
        p.add(valor);
        return p;
    }

    private void novaComanda() {
        String nomeCliente = JOptionPane.showInputDialog(this,
            "Nome do cliente:", "Nova Comanda", JOptionPane.PLAIN_MESSAGE);
        if(nomeCliente == null || nomeCliente.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome nao pode ser vazio!");
            return;
        }
        System.out.println("Criando comanda para: " + nomeCliente.trim());

        Comanda novaComanda = new Comanda(nomeCliente.trim());
        Boolean criou = ComandaDAO.inserir(novaComanda);

        if(criou) {
            List<Comanda> lista = ComandaDAO.listarTodas();
            if(!lista.isEmpty()) cmdId = lista.get(0).getId();
            carregarComandas();
        }
    }

    private void carregarComandas() {
        int idAtual = cmdId;
        modelComandas.setRowCount(0);

        List<Comanda> todasComandas = ComandaDAO.listarTodas();
        for (Comanda c : todasComandas) {
            if ("Aberta".equals(c.getStatus())) {
                modelComandas.addRow(new Object[]{c.getId(), c.getNomeCliente(), c.getStatus()});
            }
        }

        for (int i = 0; i < modelComandas.getRowCount(); i++) {
            if ((int) modelComandas.getValueAt(i, 0) == idAtual) {
                tabelaComandas.setRowSelectionInterval(i, i);
                return;
            }
        }
    }

    private void carregarItens(int id) {
        // System.out.println("carregando itens da comanda " + id);

        Comanda comandaSelecionada = null;
        List<Comanda> todasComandas = ComandaDAO.listarTodas();
        for (Comanda comanda : todasComandas) {
            if (comanda.getId() == id) {
                comandaSelecionada = comanda;
                break;
            }
        }
        if (comandaSelecionada == null) return;

        lblCliente.setText(comandaSelecionada.getNomeCliente());

        String dataAbertura = comandaSelecionada.getDataAbertura();
        if(dataAbertura != null) {
            lblAbertura.setText(dataAbertura.substring(0, 16));
        } else {
            lblAbertura.setText("---");
        }

        lblStatus.setText(comandaSelecionada.getStatus());
        if("Aberta".equals(comandaSelecionada.getStatus())) {
            lblStatus.setForeground(COR_CAFE);
        } else {
            lblStatus.setForeground(COR_VERDE);
        }

        boolean aberta = "Aberta".equals(comandaSelecionada.getStatus());
        btnPagar.setEnabled(aberta);
        btnRemover.setEnabled(aberta);
        cbProduto.setEnabled(aberta);
        spinQtd.setEnabled(aberta);

        modelItens.setRowCount(0);
        List<ItemComanda> itens = ItemComandaDAO.listarPorComanda(id);
        for (ItemComanda it : itens) {
            modelItens.addRow(new Object[]{
                it.getId(), it.getNomeProduto(), it.getQuantidade(),
                String.format("R$ %.2f", it.getPrecoUnitario()),
                String.format("R$ %.2f", it.getTotal())
            });
        }

        double totalComanda = ComandaDAO.calcularTotal(id);
        lblTotal.setText(String.format("R$ %.2f", totalComanda));

        carregarComboProdutos();
    }

    private void carregarComboProdutos() {
        // salvando o que tava selecionado pra nao perder depois de recarregar
        Produto atual = (Produto) cbProduto.getSelectedItem();
        cbProduto.removeAllItems();

        List<Produto> disponiveis = ProdutoDAO.listarDisponiveis();
        for (Produto prod : disponiveis) {
            cbProduto.addItem(prod);
            if (atual != null && prod.getId() == atual.getId()) {
                cbProduto.setSelectedItem(prod);
            }
        }
    }

    private void adicionarItem() {
        if(cmdId == -1) {
            JOptionPane.showMessageDialog(this, "Nenhuma comanda selecionada!");
            return;
        }
        Produto produtoSelecionado = (Produto) cbProduto.getSelectedItem();
        if(produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Escolha um produto primeiro.");
            return;
        }
        int quantidade = (int) spinQtd.getValue();
        System.out.println("Adicionando: " + produtoSelecionado.getNome() + " x" + quantidade);

        ItemComanda novoItem = new ItemComanda(cmdId, produtoSelecionado.getId(), quantidade, produtoSelecionado.getPreco());
        Boolean adicionou = ItemComandaDAO.adicionar(novoItem);

        if(adicionou) {
            carregarItens(cmdId);
            buscarProdutos();
            carregarDashboard();
        } else {
            JOptionPane.showMessageDialog(this, "Sem estoque suficiente para essa quantidade.");
        }
    }

    private void removerItem() {
        if(tabelaItens.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Clique em um item da lista.");
            return;
        }
        int itemId = (int) modelItens.getValueAt(tabelaItens.getSelectedRow(), 0);
        String motivo = JOptionPane.showInputDialog(this,
            "Qual o motivo da remocao?", "Remover Item", JOptionPane.WARNING_MESSAGE);
        if(motivo == null || motivo.trim().isEmpty()) return;

        Boolean removeu = ItemComandaDAO.remover(itemId, motivo.trim());
        if(removeu) {
            carregarItens(cmdId);
            buscarProdutos();
        }
    }

    private void fecharComanda() {
        if(cmdId == -1) return;

        double total = ComandaDAO.calcularTotal(cmdId);
        if(total == 0) {
            JOptionPane.showMessageDialog(this, "A comanda esta vazia!");
            return;
        }

        // TODO: gerar comprovante em PDF futuramente
        String[] formas = {"Dinheiro", "Cartao de Credito", "Cartao de Debito", "Pix"};
        String formaSelecionada = (String) JOptionPane.showInputDialog(
            this,
            String.format("Total da comanda: R$ %.2f\n\nForma de pagamento:", total),
            "Fechar e Pagar",
            JOptionPane.QUESTION_MESSAGE,
            null, formas, formas[0]
        );

        if(formaSelecionada == null) return;

        System.out.println("Fechando comanda " + cmdId + " - forma: " + formaSelecionada);

        if("Dinheiro".equals(formaSelecionada)) {
            String valorStr = JOptionPane.showInputDialog(this,
                String.format("Total: R$ %.2f\nValor recebido pelo cliente:", total),
                "Calcular Troco",
                JOptionPane.PLAIN_MESSAGE);

            if(valorStr == null) return;

            try {
                double valorRecebido = Double.parseDouble(valorStr.replace(",", "."));

                if(valorRecebido < total) {
                    JOptionPane.showMessageDialog(this,
                        String.format("Valor insuficiente! Faltam R$ %.2f", total - valorRecebido),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double troco = valorRecebido - total;
                JOptionPane.showMessageDialog(this,
                    String.format("Pagamento recebido!\n\nValor:  R$ %.2f\nTroco:  R$ %.2f", valorRecebido, troco),
                    "Troco", JOptionPane.INFORMATION_MESSAGE);

            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor invalido. Ex: 15.00 ou 15,00");
                return;
            }
        } else {
            int confirma = JOptionPane.showConfirmDialog(this,
                String.format("Confirmar pagamento de R$ %.2f via %s?", total, formaSelecionada),
                "Confirmar Pagamento",
                JOptionPane.YES_NO_OPTION);
            if(confirma != JOptionPane.YES_OPTION) return;
        }

        Boolean fechou = ComandaDAO.fechar(cmdId, formaSelecionada);
        if(fechou) {
            JOptionPane.showMessageDialog(this, "Venda finalizada com sucesso!");
            cmdId = -1;
            carregarComandas();
            modelItens.setRowCount(0);
            lblCliente.setText("---");
            lblAbertura.setText("---");
            lblStatus.setText("---");
            lblTotal.setText("R$ 0,00");
            carregarDashboard();
        }
    }

    private void abrirHistorico() {
        // TODO: adicionar filtro por data
        JDialog dialog = new JDialog(this, "Historico de Vendas", true);
        dialog.setSize(860, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        DefaultTableModel modelHist = new DefaultTableModel(
                new String[]{"#", "Cliente", "Data", "Total", "Pagamento"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabelaHist = new JTable(modelHist);
        estilizarTabela(tabelaHist);

        DefaultTableModel modelHistItens = new DefaultTableModel(
                new String[]{"Produto", "Qtd", "Preco unit.", "Total"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabelaHistItens = new JTable(modelHistItens);
        estilizarTabela(tabelaHistItens);

        List<Comanda> todasComandas = ComandaDAO.listarTodas();
        for (Comanda c : todasComandas) {
            if ("Paga".equals(c.getStatus())) {
                double totalCmd = ComandaDAO.calcularTotal(c.getId());

                String pgto;
                if(c.getFormaPagamento() != null) {
                    pgto = c.getFormaPagamento();
                } else {
                    pgto = "-";
                }

                modelHist.addRow(new Object[]{
                    c.getId(), c.getNomeCliente(),
                    c.getDataAbertura().substring(0, 16),
                    String.format("R$ %.2f", totalCmd),
                    pgto
                });
            }
        }

        tabelaHist.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaHist.getSelectedRow() >= 0) {
                int id = (int) modelHist.getValueAt(tabelaHist.getSelectedRow(), 0);
                modelHistItens.setRowCount(0);
                List<ItemComanda> itensHist = ItemComandaDAO.listarPorComanda(id);
                for (ItemComanda it : itensHist) {
                    modelHistItens.addRow(new Object[]{
                        it.getNomeProduto(), it.getQuantidade(),
                        String.format("R$ %.2f", it.getPrecoUnitario()),
                        String.format("R$ %.2f", it.getTotal())
                    });
                }
            }
        });

        JPanel esq = new JPanel(new BorderLayout());
        esq.setBackground(Color.WHITE);
        JLabel lEsq = new JLabel("  Comandas Pagas");
        lEsq.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lEsq.setForeground(COR_CINZA);
        lEsq.setBorder(BorderFactory.createEmptyBorder(10, 4, 6, 0));
        esq.add(lEsq, BorderLayout.NORTH);
        esq.add(scroll(tabelaHist), BorderLayout.CENTER);

        JPanel dir = new JPanel(new BorderLayout());
        dir.setBackground(Color.WHITE);
        JLabel lDir = new JLabel("  Itens");
        lDir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lDir.setForeground(COR_CINZA);
        lDir.setBorder(BorderFactory.createEmptyBorder(10, 4, 6, 0));
        dir.add(lDir, BorderLayout.NORTH);
        dir.add(scroll(tabelaHistItens), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, esq, dir);
        split.setDividerLocation(420);
        split.setDividerSize(5);
        split.setBorder(null);

        JButton btnFechar = criarBotao("Fechar", COR_CINZA);
        btnFechar.addActionListener(e -> dialog.dispose());
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        rodape.setBackground(COR_FUNDO);
        rodape.add(btnFechar);

        dialog.add(split,  BorderLayout.CENTER);
        dialog.add(rodape, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ================================================================
    // UTILITARIOS
    // ================================================================
    private void estilizarTabela(JTable tabela) {
        tabela.setFont(FONTE_PADRAO);
        tabela.setRowHeight(30);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(new Color(235, 228, 218));
        tabela.getTableHeader().setForeground(COR_CINZA);
        tabela.setSelectionBackground(new Color(220, 175, 115));
        tabela.setSelectionForeground(new Color(28, 18, 8));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setFont(FONTE_PADRAO);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if(!sel) {
                    if(row % 2 == 0) {
                        setBackground(Color.WHITE);
                    } else {
                        setBackground(new Color(250, 246, 240));
                    }
                }
                return this;
            }
        };
        for (int i = 0; i < tabela.getColumnCount(); i++)
            tabela.getColumnModel().getColumn(i).setCellRenderer(renderer);
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if(getModel().isPressed()) {
                    g2.setColor(cor.darker());
                } else if(getModel().isRollover()) {
                    g2.setColor(cor.brighter());
                } else {
                    g2.setColor(cor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(cor);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel rotulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(COR_CINZA);
        return l;
    }

    private JScrollPane scroll(JTable tabela) {
        JScrollPane sp = new JScrollPane(tabela);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        return sp;
    }

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (InstantiationException ex) {
            System.out.println(ex.getMessage());
        } catch (IllegalAccessException ex) {
            System.out.println(ex.getMessage());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            System.out.println(ex.getMessage());
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}