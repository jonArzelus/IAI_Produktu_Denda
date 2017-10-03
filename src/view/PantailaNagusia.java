package view;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Panel;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.phidget22.PhidgetException;
import com.phidget22.RFID;
import com.phidget22.RFIDTagEvent;
import com.phidget22.RFIDTagListener;
import com.phidget22.RFIDTagLostEvent;
import com.phidget22.RFIDTagLostListener;

import model.Eroslea;
import model.Produktua;
import model.Saltzailea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PantailaNagusia {

	private JFrame frmPhidgetDenda;
	private JPasswordField pwdAdminPwd;
	private JTextField txtAdminErab;
	private JTable tblInbentarioa;
	private JTextField txtInbIzena;
	private JTextField txtInbKopurua;
	private JTextField txtInbBalioa;
	
	//bestelako konponenteak
	JLabel lblAdminIzena;
	JLabel lblAdminArgazkia;
	JLabel lblAdminMota;
	JLabel lblAdminSarrera;
	JLabel lblAdminInfo;
	JLabel lblAdminEr1;
	JLabel lblAdminEr2;
	JLabel lblAdminId;
	JLabel lblInbEr;
	
	JButton btnAdminAmaitu;
	JButton btnAdminHasi;
	JButton btnInbEzabatu;
	JButton btnInbIzena;
	JButton btnInbBalioa;
	JButton btnInbKopurua;
	
	//panelak
	JTabbedPane tabbedPane;
	JPanel panelAdmin;
	JPanel panelDenda;
	JPanel panelInbentarioa;
	
	//dendako itemak, erabiltzaileak...
	private HashMap<String, Saltzailea> saltzaileZerrenda = new HashMap<String, Saltzailea>();
	private HashMap<String, Eroslea> erosleZerrenda = new HashMap<String, Eroslea>();
	private HashMap<String, Produktua> produktuZerrenda = new HashMap<String, Produktua>();
	
	//saioaren datuak
	private Saltzailea saioSaltzailea;
	
	//txartel irakurtzailea
	RFID ch;

	public PantailaNagusia() {
		initialize();
		saltzaileZerrenda.put("Giltzak1", new Saltzailea("Giltzak1", "Jon Arzelus Rodriguez", "Saltzaile Arrunta", "1234"));
		saltzaileZerrenda.put("Giltzak2", new Saltzailea("Giltzak2", "Julen Diez Diez", "Saltzaile Arrunta", "1234"));
		saioSaltzailea=null;
		try {
			ch = new RFID();
			ch.addTagListener(new RFIDTagListener() {
				public void onTag(RFIDTagEvent e) {
					if(tabbedPane.getSelectedIndex()==0) {
						if(saioSaltzailea==null) {
							System.out.println("Tag read: " + e.getTag());
							txtAdminErab.setText(e.getTag());
							pwdAdminPwd.setEnabled(true);
							btnAdminHasi.setEnabled(true);
							lblAdminInfo.setText("Giltza detektatuta, mesedez, sartu pasahitza saioa hasteko");
						}
					}
				}
	        });
			ch.addTagLostListener(new RFIDTagLostListener() {
				public void onTagLost(RFIDTagLostEvent e) {
					if(tabbedPane.getSelectedIndex()==0) {
						if(saioSaltzailea==null) {
							System.out.println("Tag lost: " + e.getTag());
							txtAdminErab.setText("");
							pwdAdminPwd.setText("");
							lblAdminEr1.setText("");
							lblAdminEr2.setText("");
							pwdAdminPwd.setEnabled(false);
							btnAdminHasi.setEnabled(false);
							lblAdminInfo.setText("Giltza detekzioa galduta. Mesedez, urbildu identifikazio giltza saioa hasteko");
						}
					}
				}
	        });
			
			ch.open();
			
		} catch(PhidgetException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPhidgetDenda = new JFrame();
		frmPhidgetDenda.setResizable(false);
		frmPhidgetDenda.setTitle("Phidget Denda");
		frmPhidgetDenda.setBounds(100, 100, 675, 385);
		frmPhidgetDenda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmPhidgetDenda.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		panelAdmin = new JPanel();
		tabbedPane.addTab("Administrazioa", null, panelAdmin, null);
		panelAdmin.setLayout(null);
		
		btnAdminAmaitu = new JButton("Saioa Amaitu");
		btnAdminAmaitu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saioaAmaitu();
			}
		});
		btnAdminAmaitu.setEnabled(false);
		btnAdminAmaitu.setBounds(28, 191, 234, 64);
		panelAdmin.add(btnAdminAmaitu);
		
		lblAdminIzena = new JLabel("");
		lblAdminIzena.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdminIzena.setBounds(359, 11, 267, 14);
		panelAdmin.add(lblAdminIzena);
		
		lblAdminArgazkia = new JLabel("");
		lblAdminArgazkia.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdminArgazkia.setBounds(359, 36, 267, 169);
		panelAdmin.add(lblAdminArgazkia);
		
		JLabel lblErabiltzaileMota = new JLabel("Erabiltzaile Mota:");
		lblErabiltzaileMota.setBounds(359, 216, 127, 14);
		panelAdmin.add(lblErabiltzaileMota);
		
		JLabel lblNewLabel = new JLabel("Azken Sarrera:");
		lblNewLabel.setBounds(359, 266, 127, 14);
		panelAdmin.add(lblNewLabel);
		
		lblAdminMota = new JLabel("");
		lblAdminMota.setBounds(496, 216, 158, 14);
		panelAdmin.add(lblAdminMota);
		
		lblAdminSarrera = new JLabel("");
		lblAdminSarrera.setBounds(496, 266, 158, 14);
		panelAdmin.add(lblAdminSarrera);
		
		JLabel lblPasahitza = new JLabel("Pasahitza:");
		lblPasahitza.setBounds(28, 91, 84, 14);
		panelAdmin.add(lblPasahitza);
		
		pwdAdminPwd = new JPasswordField();
		pwdAdminPwd.setEnabled(false);
		pwdAdminPwd.setBounds(120, 88, 142, 20);
		panelAdmin.add(pwdAdminPwd);
		
		btnAdminHasi = new JButton("Saioa Hasi");
		btnAdminHasi.setEnabled(false);
		btnAdminHasi.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				Saltzailea temp;
				if(saltzaileZerrenda.get(txtAdminErab.getText())!=null) {
					if(saltzaileZerrenda.get(txtAdminErab.getText().toString()).getPasahitza().equals(pwdAdminPwd.getText().toString())) {
						saioaHasi();
					} else {
						lblAdminEr1.setText("");
						lblAdminEr2.setText("Pasahitza okerra, saiatu berriro");
					}
				} else {
					lblAdminEr2.setText("");
					lblAdminEr1.setText("Erabiltzailea ez da aurkitu");
				}
			}
		});
		btnAdminHasi.setBounds(28, 138, 234, 23);
		panelAdmin.add(btnAdminHasi);
		
		JLabel lblErabiltzailea = new JLabel("Erabiltzailea:");
		lblErabiltzailea.setBounds(26, 36, 84, 19);
		panelAdmin.add(lblErabiltzailea);
		
		txtAdminErab = new JTextField();
		txtAdminErab.setEditable(false);
		txtAdminErab.setBounds(120, 36, 142, 20);
		panelAdmin.add(txtAdminErab);
		txtAdminErab.setColumns(10);
		
		lblAdminEr1 = new JLabel("");
		lblAdminEr1.setForeground(Color.RED);
		lblAdminEr1.setBounds(28, 66, 234, 14);
		panelAdmin.add(lblAdminEr1);
		
		lblAdminEr2 = new JLabel("");
		lblAdminEr2.setForeground(Color.RED);
		lblAdminEr2.setBounds(28, 113, 234, 14);
		panelAdmin.add(lblAdminEr2);
		
		JLabel lblId = new JLabel("ID:");
		lblId.setBounds(359, 241, 111, 14);
		panelAdmin.add(lblId);
		
		lblAdminId = new JLabel("");
		lblAdminId.setBounds(496, 241, 158, 14);
		panelAdmin.add(lblAdminId);
		
		lblAdminInfo = new JLabel("Mesedez, urbildu identifikazio giltza saioa hasteko");
		lblAdminInfo.setForeground(Color.BLUE);
		lblAdminInfo.setBounds(10, 303, 644, 14);
		panelAdmin.add(lblAdminInfo);
		
		panelDenda = new JPanel();
		tabbedPane.addTab("Denda", null, panelDenda, null);
		tabbedPane.setEnabledAt(1, false);
		panelDenda.setLayout(null);
		
		panelInbentarioa = new JPanel();
		tabbedPane.addTab("Inbentarioa", null, panelInbentarioa, null);
		tabbedPane.setEnabledAt(2, false);
		panelInbentarioa.setLayout(null);
		
		JLabel lblDendako = new JLabel("Dendako Inbentarioa");
		lblDendako.setHorizontalAlignment(SwingConstants.CENTER);
		lblDendako.setBounds(10, 11, 455, 14);
		panelInbentarioa.add(lblDendako);
		
		tblInbentarioa = new JTable();
		tblInbentarioa.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"ID", "Izena", "Kopurua", "Balioa"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, Integer.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblInbentarioa.setBounds(20, 54, 444, 192);
		panelInbentarioa.add(tblInbentarioa);
		
		JLabel lblNewLabel_1 = new JLabel("ID");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(20, 36, 111, 14);
		panelInbentarioa.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Izena");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(130, 36, 111, 14);
		panelInbentarioa.add(lblNewLabel_2);
		
		JLabel lblBalioa = new JLabel("Balioa");
		lblBalioa.setHorizontalAlignment(SwingConstants.CENTER);
		lblBalioa.setBounds(354, 36, 111, 14);
		panelInbentarioa.add(lblBalioa);
		
		JLabel lblKopurua = new JLabel("Kopurua");
		lblKopurua.setHorizontalAlignment(SwingConstants.CENTER);
		lblKopurua.setBounds(244, 36, 111, 14);
		panelInbentarioa.add(lblKopurua);
		
		btnInbEzabatu = new JButton("Ezabatu");
		btnInbEzabatu.setBounds(474, 256, 180, 23);
		panelInbentarioa.add(btnInbEzabatu);
		
		txtInbIzena = new JTextField();
		txtInbIzena.setBounds(474, 61, 180, 20);
		panelInbentarioa.add(txtInbIzena);
		txtInbIzena.setColumns(10);
		
		btnInbIzena = new JButton("Izena Aldatu");
		btnInbIzena.setBounds(474, 92, 180, 23);
		panelInbentarioa.add(btnInbIzena);
		
		txtInbKopurua = new JTextField();
		txtInbKopurua.setBounds(474, 126, 180, 20);
		panelInbentarioa.add(txtInbKopurua);
		txtInbKopurua.setColumns(10);
		
		btnInbKopurua = new JButton("Kopurua Aldatu");
		btnInbKopurua.setBounds(474, 157, 180, 23);
		panelInbentarioa.add(btnInbKopurua);
		
		txtInbBalioa = new JTextField();
		txtInbBalioa.setBounds(474, 191, 180, 20);
		panelInbentarioa.add(txtInbBalioa);
		txtInbBalioa.setColumns(10);
		
		btnInbBalioa = new JButton("Balioa Aldatu");
		btnInbBalioa.setBounds(474, 222, 180, 23);
		panelInbentarioa.add(btnInbBalioa);
		
		JLabel lblNewLabel_3 = new JLabel("Konfigurazioa");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setBounds(475, 36, 179, 14);
		panelInbentarioa.add(lblNewLabel_3);
		
		lblInbEr = new JLabel("errore mezua");
		lblInbEr.setForeground(Color.RED);
		lblInbEr.setBounds(10, 303, 644, 14);
		panelInbentarioa.add(lblInbEr);
	}
	
	public void setVisible(boolean b) {
		frmPhidgetDenda.setVisible(b);
	}
	
	public void erakutsiLogin() {
		
	}
	
	public void saioaHasi() {
		saioSaltzailea = saltzaileZerrenda.get(txtAdminErab.getText());
		lblAdminId.setText(saioSaltzailea.getId());
		lblAdminIzena.setText(saioSaltzailea.getIzena());
		lblAdminMota.setText(saioSaltzailea.getMota());
		if(saioSaltzailea.getAzkenSarrera()!=null) {
			lblAdminSarrera.setText(saioSaltzailea.getAzkenSarrera().toString());
		} else {
			lblAdminSarrera.setText(LocalDateTime.now().toString());
		}
		btnAdminHasi.setEnabled(false);
		txtAdminErab.setEnabled(false);
		pwdAdminPwd.setEnabled(false);
		lblAdminInfo.setText("Saioa hasita. Ez ahaztu saioa amaitzen joan aurretikan");
		lblAdminEr1.setText("");
		lblAdminEr2.setText("");
		btnAdminAmaitu.setEnabled(true);
	}
	
	public void saioaAmaitu() {
		saioSaltzailea = null;
		lblAdminId.setText("");
		lblAdminIzena.setText("");
		lblAdminMota.setText("");
		lblAdminSarrera.setText("");
		pwdAdminPwd.setText("");
		txtAdminErab.setText("");
		//btnAdminHasi.setEnabled(true);
		txtAdminErab.setEnabled(true);
		//pwdAdminPwd.setEnabled(true);
		lblAdminInfo.setText("Mesedez, urbildu identifikazio giltza saioa hasteko");
		lblAdminEr1.setText("");
		lblAdminEr2.setText("");
		btnAdminAmaitu.setEnabled(false);
	}
}
