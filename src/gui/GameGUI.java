package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

import assets.register.instruction.Register;
import main.Computer;

@SuppressWarnings("serial")
public class GameGUI extends JFrame {
	
	public Computer   computer;
    private JTextArea   codeBox;
    private JScrollPane pane1;
    private JTextArea values;
    private String output;
    public Thread queryThread;
    private ArrayList<JTextArea> textAreaRegisters;
    private JTextArea textAreaCpyCycles;
	private JTextArea gameWorld;
	private JTextArea description_box;
	
	public JPanel gamegui;
	public JPanel puzzlemenu;
	public JPanel mainmenu;
	public JPanel settingsmenu;
	public String puzzleName;
	public Color backgroundColor;
	
	private MainMenuGUI menu;
	private PuzzleMenuGUI puzzle;
	private SettingsMenuGUI settings;
	
	public JPanel resourcePanel;

	private boolean started;
	
	public GameGUI(Computer computer) {
		
		super("Game GUI");
		this.backgroundColor = Color.DARK_GRAY;
		this.computer = computer;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setUndecorated(true);
		this.setVisible(true);
		this.setResizable(true);
		
		this.gamegui = new JPanel();
		this.puzzlemenu = new JPanel(new GridBagLayout());
		this.mainmenu = new JPanel(new GridBagLayout());
		this.settingsmenu = new JPanel(new GridBagLayout());
		buildGUI();
		
		this.menu = new MainMenuGUI(this);
		menu.buildMainMenu();
		
		this.puzzle = new PuzzleMenuGUI(this);
		puzzle.buildPuzzleMenu();
		
		this.settings = new SettingsMenuGUI(this);
		settings.buildSettingsMenu();
		
		this.add(mainmenu);
		this.puzzleName = "";
        setVisible(true);
        started = false;
        SwingUtilities.isEventDispatchThread();
	}
	
	public void buildGUI() {
		       
        // Code Panel
        JPanel codePanel = new JPanel(new GridBagLayout());
        JLabel codeBoxInformation = new JLabel("Enter code:");
     	codeBox = new JTextArea("", 15, 40);
        codeBox.setEditable(true);
        this.pane1 = new JScrollPane(codeBox);
        GridBagConstraints codePanelConstraints = new GridBagConstraints();
        codePanelConstraints.gridx = 0;
        codePanelConstraints.gridy = 0;
        codePanel.add(codeBoxInformation, codePanelConstraints);
        codePanelConstraints.gridx = 0;
        codePanelConstraints.gridy = 1;
        codePanel.add(this.pane1, codePanelConstraints);
        codePanelConstraints.gridx = 1;
        codePanelConstraints.gridy = 1;
        codePanel.add(createRegisterPanel(), codePanelConstraints);
        
        // Output panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(this.backgroundColor);
		JLabel value_message = new JLabel("Values:");
		this.values = new JTextArea("", 5, 40);
		this.values.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(this.values);
		GridBagConstraints outputPanelConstraints = new GridBagConstraints();
		outputPanelConstraints.gridx = 0;
		outputPanelConstraints.gridy = 0;
		panel.add(value_message, outputPanelConstraints);
		outputPanelConstraints.gridx = 0;
		outputPanelConstraints.gridy = 1;
		panel.add(scrollPane, outputPanelConstraints);
		panel.setSize(300, 300);
		
		
		// Add each panel to the JFrame with the 'right' GridBagConstraints
		JPanel mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setName("Main");
		GridBagConstraints mainPanelConstraints = new GridBagConstraints();
		mainPanelConstraints.gridx = 0;
		mainPanelConstraints.gridy = 1;
		mainPanel.add(codePanel, mainPanelConstraints);
		mainPanelConstraints.gridx = 0;
		mainPanelConstraints.gridy = 2;
		mainPanel.add(panel, mainPanelConstraints);
		mainPanel.setBackground(this.backgroundColor);
		
		// gameworld
        this.gameWorld = new JTextArea("", 15, 40);
        this.gameWorld.setEditable(false);
        this.gameWorld.setSize(800, 500);
        JPanel gameworld_panel = new JPanel();
        gameworld_panel.add(gameWorld);
		
		// description box
        JPanel description_panel = new JPanel(new GridBagLayout());
		JLabel description = new JLabel("Description");
		this.description_box = new JTextArea("", 5, 40);
		this.description_box.setSize(800, 500);
		this.description_box.setEditable(false);
		this.description_box.setLineWrap(true);
		this.description_box.setText(this.computer.puzzle.getDescription());
		GridBagConstraints descriptionPanelConstraints = new GridBagConstraints();
		descriptionPanelConstraints.gridx = 0;
		descriptionPanelConstraints.gridy = 0;
		description_panel.add(description, descriptionPanelConstraints);
		descriptionPanelConstraints.gridx = 0;
		descriptionPanelConstraints.gridy = 1;
		description_panel.add(this.description_box, descriptionPanelConstraints);
		description_panel.setSize(300, 300);
		
		// Set layout of the right side
		mainPanelConstraints.gridx = 1;
		mainPanelConstraints.gridy = 1;
		mainPanel.add(gameworld_panel, mainPanelConstraints);
		mainPanelConstraints.gridx = 1;
		mainPanelConstraints.gridy = 2;
		mainPanel.add(createButtonPanel(), mainPanelConstraints);
//		mainPanelConstraints.gridx = 2;
//		mainPanelConstraints.gridy = 2;
////		this.resourcePanel = createResourceWidgets();
//		mainPanel.add(createResourceWidgets(), mainPanelConstraints);

		
		mainPanelConstraints.gridx = 1;
		mainPanelConstraints.gridy = 4;
		mainPanel.add(description_panel, mainPanelConstraints);
		mainPanel.setVisible(true);
		
		this.gamegui.add(mainPanel);
		this.gamegui.setBackground(this.backgroundColor);
    }
	
	public JPanel createRegisterPanel() {
		this.textAreaRegisters = new ArrayList<JTextArea>();
		JPanel register_panel = new JPanel(new GridBagLayout());
		ArrayList<Register> registers = this.computer.getCPU().getRegisters();
		
		int count = 1;
		GridBagConstraints registerPanelConstraints = new GridBagConstraints();	
		for(Register register : registers) {			
			registerPanelConstraints.gridx = 0;
			registerPanelConstraints.gridy = count;
			JTextArea register_value_box = new JTextArea(" " + register.getRegisterName() + " : " + register.getValue(), 1, 5);
			this.textAreaRegisters.add(register_value_box);
			register_value_box.setEditable(false);
			register_value_box.setVisible(true);
			register_panel.add(register_value_box, registerPanelConstraints);
			count++;
		}
		
		return register_panel;
	}
	
	public JPanel createButtonPanel() {
		
		// Return back to main menu
        JButton menuButton = new JButton("Menu");
        menuButton.setBackground(GUIMarkUp.buttonColor);
        menuButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  SwitchToMainMenu(e);
        	  started = false;
          }
        });
        
        // Reset button stops a game and returns all start values
        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(GUIMarkUp.buttonColor);
        resetButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  resetComputer();
        	  started = false;
          }
        });
        
        // Pause button interrupts a running game
        JButton pauseButton = new JButton("Pause"); 
        pauseButton.setBackground(GUIMarkUp.buttonColor);
        pauseButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  new Thread(new Runnable() {
					public void run() {
						interruptProgram();
					}
				}).start();
          }
        });
        
        // Advance one cycle (perform one line of code)
        JButton stepButton = new JButton("Step"); 
        stepButton.setBackground(GUIMarkUp.buttonColor);
        stepButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  if (started == false) {
        		  setAllMarkPoints(codeBox.getText());
        		  started = true;
        	  }    
        	  sendCodetoGame(codeBox.getText(), true, false);
          }
        });
        
        // Run code indefinitely, but slow enough to observe
        JButton runButton = new JButton("Run");
        runButton.setBackground(GUIMarkUp.buttonColor);
        runButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
        	  if (getInterrupted() == true) {
        		  resumeProgram(false);
        	  } else {
            	  new Thread(new Runnable() {
  					public void run() {
  						setAllMarkPoints(codeBox.getText());
  						sendCodetoGame(codeBox.getText(), false, false);
  						setStepToNotEditable();
  					}
  				}).start();  
        	  }
          }
        });
        
        // Run code indefinitely, but at a much faster rate to quickly pass all tests.
        JButton runFastButton = new JButton("Run Fast");
        runFastButton.setBackground(GUIMarkUp.buttonColor);
        runFastButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        	  if (getInterrupted() == true) {
        		  resumeProgram(true);
        	  } else {		
  				new Thread(new Runnable() {
					public void run() {
						setAllMarkPoints(codeBox.getText());
						sendCodetoGame(codeBox.getText(), false, true);
					}
				}).start();  
        	  }
			}
        });
        
        // Control panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setName("buttonPanel");
        buttonPanel.add(menuButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stepButton);
        buttonPanel.add(runButton);
        buttonPanel.add(runFastButton);
        buttonPanel.add(createResourceWidgets());
        buttonPanel.setBackground(this.backgroundColor);     
        
		return buttonPanel;
	}
	
	public JPanel createResourceWidgets() {
		JPanel resource_panel = new JPanel();
		this.textAreaCpyCycles = new JTextArea(" " + this.computer.getCPUCycleCount(), 1, 5);
		
		// CPU cycle counter
//		JTextArea cpu_cycle_value_box = new JTextArea(" " + this.computer.getCPUCycleCount(), 1, 5);
//		this.textAreaCpyCycles.add(cpu_cycle_value_box);
		this.textAreaCpyCycles.setEditable(false);
		this.textAreaCpyCycles.setVisible(true);
		JLabel cpu_cycle_message = new JLabel("CPU cycles:");
	
//		JTextArea lines_of_code_value_box = new JTextArea(" " + this.computer.instruction_lines.length, 1, 5);
//		this.textAreaRegisters.add(lines_of_code_value_box);
//		lines_of_code_value_box.setEditable(false);
//		lines_of_code_value_box.setVisible(true);
//		JLabel number_of_lines_of_code_message = new JLabel("# lines of code:");
		
		resource_panel.add(cpu_cycle_message);
		resource_panel.add(this.textAreaCpyCycles);
		
//		resource_panel.add(lines_of_code_value_box);
//		resource_panel.add(number_of_lines_of_code_message);
		
//		this.textAreaCpyCycles.setText("" + this.computer.getCPUCycleCount());
		
		
		return resource_panel;
		
	}
	
	public void setAllMarkPoints(String code) {
		this.computer.setAllJumpLines(code);
	}
	
	public void sendCodetoGame(String code, boolean executeSingleLine, boolean runFast) {
		this.computer.retrieveUserInstruction(code, this, executeSingleLine, runFast);
	}

	public void displayOutputValue() {	
		highlightCodeLine(this.codeBox, this.computer);
		this.output = this.values.getText() + " " + this.computer.retrieveCurrentValueCPUs().get(0).getValue();
		this.values.setText(this.output);
		this.values.updateUI();
	}
	
	public void displayPuzzleOutcome(String message) {
		this.values.setText(message);
		this.values.updateUI();
	}
	
	public void interruptProgram() {
		this.computer.interruptProgram();
	}
	
	public boolean getInterrupted() {
		return this.computer.interrupted;
	}
	
	public void resumeProgram(boolean runfast) {
		if (runfast == true) {
			this.computer.resumeProgramRunFast();
		} else {
			this.computer.resumeProgram();
		}
	}
	
	private void setStepToNotEditable() {                                
	    Component[] comp = this.gamegui.getComponents();
	    for (int i = 0; i < comp.length;i++) {
	    	System.out.println(comp[i].getName());
//	        if (comp[i].getName().contentEquals("Main")) {
//	        	Component buttonPanel = comp[i].getComponentAt(0, 0);
//	        	System.out.println(buttonPanel.getName());
//	        }
	    }
	} 
	

	
	public void highlightCodeLine( JTextArea   codeBox, Computer   computer) {
		codeBox.getHighlighter().removeAllHighlights();
		DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.GRAY);
		try {
            int start = codeBox.getLineStartOffset(computer.getCurrentLine());
            int end = codeBox.getLineEndOffset(computer.getCurrentLine());
			codeBox.getHighlighter().addHighlight(start, end, painter);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void displayValueCpuRegisters() {
		ArrayList<Register> registers = this.computer.retrieveCurrentValueCPUs();
		int index = 0;
		for(Register register: registers) {
			this.textAreaRegisters.get(index).setText("" + register.getValueInt());
			index++;
		}
//		System.out.println(this.computer.getCPUCycleCount());
//		this.resourcePanel.getComponent(1).getName();
		this.textAreaCpyCycles.setText("" + this.computer.getCPUCycleCount());
	}
	
	public void resetComputer() {
		this.computer.resetComputer();
		this.values.setText("");
		
		ArrayList<Register> registers = this.computer.retrieveCurrentValueCPUs();
		
		int index = 0;
		for(Register register: registers) {
			this.textAreaRegisters.get(index).setText("");;
			register.setValue("0");
			index++;
		}
		this.textAreaCpyCycles.setText("" + 0);
		highlightCodeLine(this.codeBox, this.computer);
	}
	
	
	private void saveGame() throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter("solutions/" + this.puzzleName + ".txt")) {
		    out.println(codeBox.getText());
		}
	}
	
	public void setCodeBox() {
        if (Files.exists(Paths.get("solutions/" + this.puzzleName + ".txt"))) {
            StringBuilder contentBuilder = new StringBuilder();
	        try (Stream<String> stream = Files.lines(Paths.get("solutions/" + this.puzzleName + ".txt"), StandardCharsets.UTF_8)) {
	            stream.forEach(s -> contentBuilder.append(s).append("\n"));  
	        }        
	        catch (IOException e) {
	            e.printStackTrace();
	        }

	        contentBuilder.delete(contentBuilder.lastIndexOf("\n"), contentBuilder.lastIndexOf("\n")+1);
	        
	        this.codeBox.setText(contentBuilder.toString());
	        highlightCodeLine(this.codeBox, this.computer);
	        this.codeBox.setCaretPosition(0);
        } 
	}
	
	private void SwitchToMainMenu(java.awt.event.ActionEvent evt) {
		try {
			saveGame();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		this.gamegui.setVisible(false);
	    getContentPane().removeAll(); 
	    getContentPane().add(this.mainmenu); 
	    this.mainmenu.setVisible(true);
	    validate(); 
	} 

}
