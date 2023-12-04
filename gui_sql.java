//Nama	: Gilang Abimanyu
//NIM 	: A11.2019.11753
//kelas	: A11.4413
import java.awt.*;
import java.sql.*;
import javax.sql.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.event.*;

class gui extends JFrame {
    // Koneksi MySQL
    private String user = "root"; 
    private String pass = ""; 
    private String db = "tastn"; 
    private String url = "jdbc:mysql://localhost:3306/" + db;

    // Swing
    private Container cA;
    private JPanel lPanel, buttonPanel, panel1, panel2, bttshpanel, spshowpanel;
    private JLabel text1, text2, text3, text4;
    private JTextField list1, list2, list3, list4;
    private JButton button1, button2, button3, buttonshow;
    private JTextArea area;
    private JScrollPane showdata;

    // Setting Frame
    public gui() {
        // Setting Frame
        setTitle("Data Barang TASTN Production");
        ImageIcon icon = new ImageIcon("icon-tastn.png");
        setIconImage(icon.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setKoneksiDB();
        setContent();
        makeLayout();
        setListener();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method setting koneksi DB
    public void setKoneksiDB() {
        Connection con = null; // berikan nilai default null
        try {
            // pengaktifan driver JDBC
            Class.forName("com.mysql.jdbc.Driver");
            // membuat koneksi
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Connect Succesfully");
        } catch (Exception e) {
            System.out.println("Error = " + e);
        }
    }

    // Method setContent untuk mempersiapkan konten frame
    public void setContent() {
        cA = getContentPane();
        lPanel = new JPanel();
		lPanel.setBackground(Color.lightGray);
        lPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        buttonPanel = new JPanel();
        panel1 = new JPanel();
        bttshpanel = new JPanel();
        spshowpanel = new JPanel();
        panel2 = new JPanel();
        text1 = new JLabel("Kode		:");
        text2 = new JLabel("Nama Barang	:");
        text3 = new JLabel("Ukuran		:");
        text4 = new JLabel("Harga		:");
        list1 = new JTextField(15);
        list2 = new JTextField(15);
        list3 = new JTextField(15);
        list4 = new JTextField(15);
        button1 = new JButton("Simpan");
        button2 = new JButton("Ubah");
        button3 = new JButton("Hapus");
        area = new JTextArea(9, 48);
        showdata = new JScrollPane(area);
        showdata.setBorder(BorderFactory.createTitledBorder("Data"));
        showdata.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        showdata.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        buttonshow = new JButton("Tampil / Resfresh Data");

    }

    // Method makeLayout untuk menempelkan komponen pada layout
    public void makeLayout() {
        // JLabel & JTextField
        lPanel.setLayout(new GridLayout(4, 4));
        lPanel.add(text1);
        lPanel.add(list1);
        lPanel.add(text2);
        lPanel.add(list2);
        lPanel.add(text3);
        lPanel.add(list3);
        lPanel.add(text4);
        lPanel.add(list4);

        // JButton
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 20));
        buttonPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);

        // JScrollPane
        spshowpanel.add(showdata);

        // JButton Tampil Data
        bttshpanel.add(buttonshow);

        // Panel 1 add
        panel1.setLayout(new BorderLayout());
        panel1.add(lPanel, BorderLayout.CENTER);
        panel1.add(buttonPanel, BorderLayout.PAGE_END);
        panel2.setLayout(new BorderLayout());
        panel2.add(bttshpanel, BorderLayout.PAGE_END);
        panel2.add(spshowpanel, BorderLayout.PAGE_START);

        // Menampilkan Panel
        cA.setLayout(new BorderLayout());
        cA.add(panel1, BorderLayout.PAGE_START);
        cA.add(panel2, BorderLayout.PAGE_END);

    }
	//menghandle event
    public void setListener() {
        buttonshow.addActionListener(new TampilDataDB());
        button1.addActionListener(new SimpanDataDB());
        button2.addActionListener(new UbahDataDB());
        button3.addActionListener(new HapusDataDB());
    }
	// menampilkan data barang
    class TampilDataDB implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                area.setText("");
                Connection con = null;
                con = DriverManager.getConnection(url, user, pass);
                Statement stmt = con.createStatement();
                ResultSet rs01 = stmt.executeQuery("SELECT kode, nama_barang, ukuran, harga FROM barang");
                while (rs01.next()) {
                    String kode = rs01.getString("kode");
                    String nama_barang = rs01.getString("nama_barang");
                    String ukuran = rs01.getString("ukuran");
                    int harga = rs01.getInt("harga");
                    area.append("| " + kode + " | " + nama_barang + " | " + ukuran + " | " + harga + "\n");
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                area.append("ERROR = " + e);
            }
        }
    }
	//menyimpan data barang
    class SimpanDataDB implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                Connection con = null;
                con = DriverManager.getConnection(url, user, pass);
                Statement stmt = con.createStatement();
                String kode = list1.getText();
                String nama_barang = list2.getText();
                String ukuran = list3.getText();
                String harga = list4.getText();
                if (kode.equals("") || nama_barang.equals("") || ukuran.equals("") || harga.equals("")) {
                    area.append("data kosong");
                } else {
                    try {
                        String insertString = "INSERT INTO barang(kode, nama_barang, ukuran, harga) VALUES ('" + kode + "', '"
                                + nama_barang + "', '" + ukuran + "', '" + harga + "');";
                        int rs = stmt.executeUpdate(insertString);
                        if (rs == 1) {
                            area.append("Berhasil");
                            ResultSet rs1 = stmt.executeQuery("SELECT kode, nama_barang, ukuran, harga from barang");
                            while (rs1.next()) {
                                String kodee = rs1.getString("kode");
                                String nama_barangg = rs1.getString("nama_barang");
                                String ukurann = rs1.getString("nama_barang");
                                int hargaa = rs1.getInt("harga");
                                area.append("| " + kodee + " | " + nama_barangg + " | " + ukurann + " | " + hargaa + "\n");
                            }
                            if (stmt != null) {
                                stmt.close();
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e) {
                area.append("ERROR = " + e);
            }
        }
    }
	//update data barang
    class UbahDataDB implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                Connection con = null;
                con = DriverManager.getConnection(url, user, pass);
                Statement stmt = con.createStatement();
                String kode = list1.getText();
                String nama_barang = list2.getText();
                String ukuran = list3.getText();
                String harga = list4.getText();
                if (kode.equals("")) {
                    area.append("data kosong");
                } else {
                    try {
                        String updateString = "UPDATE barang SET nama_barang = '" + nama_barang + "', ukuran = '" + ukuran + "', harga = '" + harga+ "' WHERE kode = '" + kode + "';";
                        int rs = stmt.executeUpdate(updateString);
                        if (rs == 1) {
                            area.append("Update Berhasil");
                            list1.setText("");
                            list2.setText("");
                            list3.setText("");
                            list4.setText("");
                        }
                    } catch (Exception e) {
                        area.append("ERROR = " + e);
                    }
                }
            } catch (Exception e) {
                area.append("ERROR = " + e);
            }
        }

    }
	//Menghapus data barang
    class HapusDataDB implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                Connection con = null;
                con = DriverManager.getConnection(url, user, pass);
                Statement stmt = con.createStatement();
                area.setText("");
                String kode = list1.getText();
                if (kode.equals("")) {
                    area.append("data kosong");
                } else {
                    Object[] opsi = { "YES", "NO" };
                    int input = JOptionPane.showOptionDialog(cA, "Data Akan Dihapus?", "Confirm",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opsi, opsi[0]);
                    if (input == JOptionPane.YES_OPTION) {
                        try {
                            String deleteString = "DELETE FROM barang WHERE kode = '" + kode + "';";
                            int rs = stmt.executeUpdate(deleteString);
                            if (rs == 1) {
                                area.append("Delete Berhasil");
                                list1.setText("");
                                list2.setText("");
                                list3.setText("");
                                list4.setText("");
                            }
                        } catch (Exception e) {
                            area.append("ERROR = " + e);
                        }
                    }
                }
            } catch (Exception e) {
                area.append("ERROR = " + e);
            }
        }

    }

    // Main Method
    public static void main(String[] args) {
        gui frame = new gui();
    }
}