import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import org.apache.lucene.document.Document;

import LanguageIdentification.TrainerTester;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;

@SuppressWarnings("serial")
public class TransliterationFrame extends JFrame {
	private JTextField openTextField;
	private JButton labelFileButton;
	private JTextField searchTextField;
	private JTable resultsTable;

	private JRadioButton rdbtnDevanagari, rdbtnRoman;
	private ButtonGroup btnGroup;

	private ArrayList<Document> results = null;
	private TrainerTester tt = new TrainerTester("F:\\Eclipse Workspace\\Retrieval\\HindiEnglishTrain\\train.txt", "maxent", "F:\\Eclipse Workspace\\Retrieval\\Dict\\English.txt", "F:\\Eclipse Workspace\\Retrieval\\Dict\\Hindi.txt");
	
	private String readFilePath;
	private String saveFilePath;
	private JTextArea songPane;
	private JTextArea queryTextField;
	private JTextArea queryLabelledTextField;
	private JTextArea transliteratedTextField;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TransliterationFrame frame = new TransliterationFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Create the frame.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public TransliterationFrame() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Transliterate@KGP");
//		setSize(1371, 638);
		setSize(1366, 768);
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		tt.train();
		getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 1350, 750);
		getContentPane().add(tabbedPane);

		final JPanel subtask1 = new JPanel();
		tabbedPane.addTab("Label Language", null, subtask1, "");
		tabbedPane.setBackgroundAt(0, Color.WHITE);
		subtask1.setLayout(null);

		labelFileButton = new JButton("Label the File");
		labelFileButton.setEnabled(false);
		labelFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser jfc = new JFileChooser();
				int returnVal = jfc.showSaveDialog(TransliterationFrame.this);

				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					saveFilePath = file.getAbsolutePath();
					System.err.println(saveFilePath);

					try {
						tt.writeClassification(readFilePath, saveFilePath);
						JOptionPane.showMessageDialog(null, "File has been successfully labelled!!", "Success", JOptionPane.INFORMATION_MESSAGE);
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(null, "File could not be labbeled!!", "Failure", JOptionPane.ERROR_MESSAGE);
					}
					

				}				
			}
		});
		labelFileButton.setBounds(867, 532, 141, 41);
		subtask1.add(labelFileButton);

		JButton btnOpenFile = new JButton("Open File");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setMultiSelectionEnabled(false);
				int returnVal = jfc.showOpenDialog(TransliterationFrame.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					readFilePath = file.getAbsolutePath();
					System.err.println(readFilePath);

					openTextField.setText(readFilePath);
					labelFileButton.setEnabled(true);            
				}

			}
		});
		btnOpenFile.setBounds(283, 532, 141, 41);
		subtask1.add(btnOpenFile);

		openTextField = new JTextField();
		openTextField.setEditable(false);
		openTextField.setBounds(450, 532, 392, 40);
		subtask1.add(openTextField);
		openTextField.setColumns(10);
		
		queryTextField = new JTextArea();
		queryTextField.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		queryTextField.setLineWrap(true);
		queryTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				labelQuery();
			}
		});
		queryTextField.setBounds(37, 41, 1267, 117);
		subtask1.add(queryTextField);
		queryTextField.setColumns(10);
		
		queryLabelledTextField = new JTextArea();
		queryLabelledTextField.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		queryLabelledTextField.setBackground(SystemColor.inactiveCaption);
		queryLabelledTextField.setLineWrap(true);
		queryLabelledTextField.setEditable(false);
		queryLabelledTextField.setBounds(37, 202, 1267, 117);
		subtask1.add(queryLabelledTextField);
		queryLabelledTextField.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 483, 1325, 2);
		subtask1.add(separator);
		
		transliteratedTextField = new JTextArea();
		transliteratedTextField.setLineWrap(true);
		transliteratedTextField.setFont(new Font("Monospaced", Font.PLAIN, 18));
		transliteratedTextField.setBackground(SystemColor.inactiveCaption);
		transliteratedTextField.setEditable(false);
		transliteratedTextField.setBounds(37, 345, 1267, 117);
		subtask1.add(transliteratedTextField);


		JPanel subtask2 = new JPanel();
		tabbedPane.addTab("Find Song", null, subtask2, null);
		subtask2.setLayout(null);

		searchTextField = new JTextField();
		searchTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					queryIndex();
				}				
			}
		});
		searchTextField.setBounds(24, 46, 516, 32);
		subtask2.add(searchTextField);
		searchTextField.setColumns(10);

		JButton searchBtn = new JButton("Search");
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queryIndex();
			}
		});
		searchBtn.setBounds(550, 47, 135, 30);
		subtask2.add(searchBtn);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(24, 141, 661, 508);
		subtask2.add(scrollPane);		

		resultsTable = new JTable();
		resultsTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Rank", "Devanagari Title", "Roman Title"
			}
		));
		resultsTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		resultsTable.getColumnModel().getColumn(1).setPreferredWidth(280);
		resultsTable.getColumnModel().getColumn(2).setPreferredWidth(280);

		scrollPane.setViewportView(resultsTable);
		resultsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();
					int selectedRowIndex = target.getSelectedRow();
					Document result = results.get(selectedRowIndex);
					
					System.out.println(result.get("doc_title"));
					String songLyrics = readSongDocument(result.get("doc_title"));
					songPane.setText(songLyrics);
					songPane.setCaretPosition(0);					
				}
			}
		});

		rdbtnDevanagari = new JRadioButton("Devanagari");
		rdbtnDevanagari.setFont(new Font("Tahoma", Font.BOLD, 14));
		rdbtnDevanagari.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				queryIndex();
			}
		});

		rdbtnDevanagari.setSelected(true);
		rdbtnDevanagari.setBounds(27, 95, 117, 23);
		subtask2.add(rdbtnDevanagari);

		rdbtnRoman = new JRadioButton("Roman");
		rdbtnRoman.setFont(new Font("Tahoma", Font.BOLD, 14));
		rdbtnRoman.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				queryIndex();
			}
		});
		rdbtnRoman.setBounds(171, 97, 109, 23);
		subtask2.add(rdbtnRoman);

		btnGroup = new ButtonGroup();
		btnGroup.add(rdbtnDevanagari);
		btnGroup.add(rdbtnRoman);


		JPanel panel = new JPanel();
		panel.setBounds(695, 126, 640, 523);
		panel.setLayout(new BorderLayout());

		//Create a text area.
		songPane = new JTextArea();
		songPane.setLineWrap(true);
		songPane.setWrapStyleWord(true);
		songPane.setCaretPosition(0);
		JScrollPane areaScrollPane = new JScrollPane(songPane);
		areaScrollPane.setPreferredSize(new Dimension(250, 250));
		areaScrollPane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder("Song Lyrics"),
								BorderFactory.createEmptyBorder(0,0,0,0)),
								areaScrollPane.getBorder()));
		panel.add(areaScrollPane, BorderLayout.CENTER);

		subtask2.add(panel);	
		setLocationRelativeTo(null);
	}

	private void queryIndex()
	{
		String user_query = searchTextField.getText();
		if(user_query.isEmpty()) return;
		//corpus_type
		//3: Devanagari
		//1: Roman				
		int corpus_type = 3;//default

		//Get selected button
		for (Enumeration<AbstractButton> buttons = btnGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();

			if (button.isSelected()) {
				corpus_type = (button.getText().equalsIgnoreCase("Devanagari"))?3:1;;
			}
		}

		results = IndexFiles.searchWrapper(user_query, corpus_type);
		fillResultsTable();
	}

	private void fillResultsTable()
	{
		if(results == null)
			return;

		songPane.setText("");
		int corpus_type = 3;
		//Get selected button
		for (Enumeration<AbstractButton> buttons = btnGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();

			if (button.isSelected()) {
				corpus_type = (button.getText().equalsIgnoreCase("Devanagari"))?3:1;;
			}
		}

		DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();
		if(model.getRowCount() > 0)
			resultsTable.removeRowSelectionInterval(0, model.getRowCount()-1);

		model.setRowCount(0);//Clear existing results
		for (int i = 0; i < results.size(); i++) {
			Document result = results.get(i);
			String rank = ""+(i+1);
			String devaTitle = result.get("song_title_hindi");
			String romanTitle = result.get("song_title_english");

			model.addRow(new Object[]{rank, devaTitle, romanTitle});
		}
	}

	private void labelQuery()
	{
		String query = queryTextField.getText();
		String labelledQuery = tt.ClassifyQuery(query);
		queryLabelledTextField.setText(labelledQuery);
		
		if(labelledQuery.isEmpty()) {
			transliteratedTextField.setText("");
			return;
		}
			
		Transliterator t = new Transliterator("hindi_syllables.txt");
		String transliteratedQuery = "";
		for (String word : labelledQuery.split(" ")) {
			String result;
			if(word.indexOf("\\H") != -1) {
				word = word.substring(0, word.indexOf("\\H"));
				result = t.transliterate(word);
				if(result.indexOf("\\B=") != -1)
					result = result.substring(result.indexOf("\\B") + 3);
				else 
					result = result.substring(0 , result.indexOf("\\E"));
			} else {
				result = word.substring(0, word.indexOf("\\E"));
			}
				
			transliteratedQuery += result + " ";
		}
		transliteratedTextField.setText(transliteratedQuery);
	}

	private String readSongDocument(String doc_title)
	{
		try {
			String path = "documents/" + doc_title;
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
	}
}

