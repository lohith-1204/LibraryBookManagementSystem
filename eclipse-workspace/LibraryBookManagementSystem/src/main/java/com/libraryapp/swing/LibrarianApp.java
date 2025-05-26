package com.libraryapp.swing;

import com.libraryapp.dao.BookDAO;
import com.libraryapp.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LibrarianApp extends JFrame {
    private BookDAO bookDAO;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    private JTextField tfTitle;
    private JTextField tfAuthor;
    private JComboBox<String> cbStatus;

    private JButton btnAdd, btnUpdate, btnDelete;

    public LibrarianApp() {
        bookDAO = new BookDAO();
        setTitle("Library Book Management - Librarian");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI();
        loadBooks();
    }

    private void createUI() {
        setLayout(new BorderLayout());

        // Table setup
        bookTable = new JTable();
        String[] columnNames = {"ID", "Title", "Author", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return false; // prevent editing in table directly
            }
        };
        bookTable.setModel(tableModel);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // Form input panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        tfTitle = new JTextField();
        tfAuthor = new JTextField();
        cbStatus = new JComboBox<>(new String[]{"available", "issued"});

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(tfTitle);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(tfAuthor);
        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(cbStatus);

        // Buttons
        btnAdd = new JButton("Add Book");
        btnUpdate = new JButton("Update Book");
        btnDelete = new JButton("Delete Book");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // Event listeners
        btnAdd.addActionListener(e -> addBook());
        btnUpdate.addActionListener(e -> updateBook());
        btnDelete.addActionListener(e -> deleteBook());

        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bookTable.getSelectedRow() != -1) {
                int row = bookTable.getSelectedRow();
                tfTitle.setText((String) tableModel.getValueAt(row, 1));
                tfAuthor.setText((String) tableModel.getValueAt(row, 2));
                cbStatus.setSelectedItem((String) tableModel.getValueAt(row, 3));
            }
        });
    }

    private void loadBooks() {
        tableModel.setRowCount(0);
        List<Book> books = bookDAO.getAllBooks();
        for (Book b : books) {
            tableModel.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getStatus()});
        }
    }

    private void addBook() {
        String title = tfTitle.getText().trim();
        String author = tfAuthor.getText().trim();
        String status = (String) cbStatus.getSelectedItem();

        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Author cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = new Book(0, title, author, status);
        if (bookDAO.addBook(book)) {
            JOptionPane.showMessageDialog(this, "Book added successfully.");
            loadBooks();
            clearInputs();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String title = tfTitle.getText().trim();
        String author = tfAuthor.getText().trim();
        String status = (String) cbStatus.getSelectedItem();

        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Author cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = new Book(id, title, author, status);
        if (bookDAO.updateBook(book)) {
            JOptionPane.showMessageDialog(this, "Book updated successfully.");
            loadBooks();
            clearInputs();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this book?", "Confirm", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            if(bookDAO.deleteBook(id)) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully.");
                loadBooks();
                clearInputs();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearInputs() {
        tfTitle.setText("");
        tfAuthor.setText("");
        cbStatus.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibrarianApp app = new LibrarianApp();
            app.setVisible(true);
        });
    }
}
