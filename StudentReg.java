import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class StudentRegistrationGUI extends JFrame {
    // Form components
    private JTextField txtName, txtEmail, txtPhone, txtAge;
    private JComboBox<String> cmbGender, cmbCourse;
    private JTextArea txtAddress;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    private StudentDAO studentDAO;
    private int selectedStudentId = -1;

    public StudentRegistrationGUI() {
        studentDAO = new StudentDAO();
        initComponents();
        loadStudentData();
    }

    private void initComponents() {
        setTitle("Student Registration Form");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(33, 128, 141));
        JLabel lblTitle = new JLabel("Student Registration System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.WEST);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setPreferredSize(new Dimension(400, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(20);
        panel.add(txtName, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        txtPhone = new JTextField(20);
        panel.add(txtPhone, gbc);

        // Age
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        txtAge = new JTextField(20);
        panel.add(txtAge, gbc);

        // Gender
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        panel.add(cmbGender, gbc);

        // Course
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        String[] courses = {"Computer Science", "Information Technology",
                "Electronics", "Mechanical", "Civil"};
        cmbCourse = new JComboBox<>(courses);
        panel.add(cmbCourse, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        txtAddress = new JTextArea(3, 20);
        txtAddress.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(txtAddress);
        panel.add(scrollPane, gbc);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        btnAdd = new JButton("Add Student");
        btnAdd.setBackground(new Color(33, 128, 141));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> addStudent());

        btnUpdate = new JButton("Update");
        btnUpdate.setBackground(new Color(245, 158, 11));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.addActionListener(e -> updateStudent());

        btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(192, 21, 47));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> deleteStudent());

        btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        panel.add(btnPanel, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));

        // Table
        String[] columns = {"ID", "Name", "Email", "Phone", "Age", "Gender", "Course"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedStudent();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private boolean validateInputs() {
        // Name validation
        if (txtName.getText().trim().isEmpty()) {
            showError("Name cannot be empty!");
            return false;
        }

        // Email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(emailRegex, txtEmail.getText().trim())) {
            showError("Invalid email format!");
            return false;
        }

        // Phone validation (10 digits)
        String phoneRegex = "^[0-9]{10}$";
        if (!Pattern.matches(phoneRegex, txtPhone.getText().trim())) {
            showError("Phone number must be 10 digits!");
            return false;
        }

        // Age validation
        try {
            int age = Integer.parseInt(txtAge.getText().trim());
            if (age < 18 || age > 100) {
                showError("Age must be between 18 and 100!");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Age must be a valid number!");
            return false;
        }

        // Address validation
        if (txtAddress.getText().trim().isEmpty()) {
            showError("Address cannot be empty!");
            return false;
        }

        return true;
    }

    private void addStudent() {
        if (!validateInputs()) return;

        Student student = new Student(
                txtName.getText().trim(),
                txtEmail.getText().trim(),
                txtPhone.getText().trim(),
                Integer.parseInt(txtAge.getText().trim()),
                cmbGender.getSelectedItem().toString(),
                cmbCourse.getSelectedItem().toString(),
                txtAddress.getText().trim()
        );

        if (studentDAO.addStudent(student)) {
            showSuccess("Student added successfully!");
            loadStudentData();
            clearForm();
        } else {
            showError("Failed to add student!");
        }
    }

    private void updateStudent() {
        if (selectedStudentId == -1) {
            showError("Please select a student to update!");
            return;
        }

        if (!validateInputs()) return;

        Student student = new Student(
                txtName.getText().trim(),
                txtEmail.getText().trim(),
                txtPhone.getText().trim(),
                Integer.parseInt(txtAge.getText().trim()),
                cmbGender.getSelectedItem().toString(),
                cmbCourse.getSelectedItem().toString(),
                txtAddress.getText().trim()
        );
        student.setId(selectedStudentId);

        if (studentDAO.updateStudent(student)) {
            showSuccess("Student updated successfully!");
            loadStudentData();
            clearForm();
        } else {
            showError("Failed to update student!");
        }
    }

    private void deleteStudent() {
        if (selectedStudentId == -1) {
            showError("Please select a student to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (studentDAO.deleteStudent(selectedStudentId)) {
                showSuccess("Student deleted successfully!");
                loadStudentData();
                clearForm();
            } else {
                showError("Failed to delete student!");
            }
        }
    }

    private void loadStudentData() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents();

        for (Student student : students) {
            Object[] row = {
                    student.getId(),
                    student.getName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getAge(),
                    student.getGender(),
                    student.getCourse()
            };
            tableModel.addRow(row);
        }
    }

    private void loadSelectedStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            selectedStudentId = (int) table.getValueAt(selectedRow, 0);
            txtName.setText(table.getValueAt(selectedRow, 1).toString());
            txtEmail.setText(table.getValueAt(selectedRow, 2).toString());
            txtPhone.setText(table.getValueAt(selectedRow, 3).toString());
            txtAge.setText(table.getValueAt(selectedRow, 4).toString());
            cmbGender.setSelectedItem(table.getValueAt(selectedRow, 5).toString());
            cmbCourse.setSelectedItem(table.getValueAt(selectedRow, 6).toString());

            // Load full address from database
            List<Student> students = studentDAO.getAllStudents();
            for (Student s : students) {
                if (s.getId() == selectedStudentId) {
                    txtAddress.setText(s.getAddress());
                    break;
                }
            }
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAge.setText("");
        txtAddress.setText("");
        cmbGender.setSelectedIndex(0);
        cmbCourse.setSelectedIndex(0);
        selectedStudentId = -1;
        table.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRegistrationGUI());
    }
}
