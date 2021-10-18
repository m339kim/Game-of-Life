/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
/**
 *
 * @author leannekim. Dec 5, 2019.
 */

/* GameOfLife GUI. Uses GameOfLife class to play the game, then displays the matching user interface. */
public class Main implements ActionListener {
    
    JFrame frame;
    JPanel contentPane, goQuit, choosePreset, setPreset, buttonsPane; // JPanels within a JPanel
    JButton buttons[][], go, quit, save;
    JLabel title, promptChoosePreset, promptSetPreset, message;
    JComboBox presetNames;
    JTextField inputName;
    GameOfLife gol;
    boolean canClickButton = true; // to make all buttons unclickable if "Quit" is clicked
    
    addToPreset a;
    go g;
    countLiveCell c;
    generationToPreset p;
    
    File preset = new File("preset.txt");
    FileReader in;
    BufferedReader readFile;
    FileWriter out;
    BufferedWriter writeFile;
    int numPresets = 1;
    
    /**
     * constructor
     * pre: none
     * post: objects have been initialized.
     */
    public Main(){
        /* initialize objects */
        gol = new GameOfLife();
        a = new addToPreset();          // addToPreset class
        g = new go();                   // go class
        c = new countLiveCell();        // countLiveCell class
        p = new generationToPreset();   // generationToPreset class
        
        /* create preset text file */
        try{
            preset.createNewFile();
        } catch (IOException e){
            System.err.println("IOException: " + e.getMessage());
        }
        
        /* create and set up frame */
        frame = new JFrame("Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /* create content pane with a BoxLayout with empty borders */
        contentPane = new JPanel();
        contentPane.setBackground(Color.white);
        contentPane.setBorder(BorderFactory.createEmptyBorder(20,20,10,10));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
      
            /* title label of game */
            title = new JLabel("~ Conway's Game of Life ~");
            title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            contentPane.add(title);
        
        /* create content pane with a FlowLayout with empty borders */
        goQuit = new JPanel();
        goQuit.setBackground(Color.white);
        
            /* create Go! button */
            go = new JButton("Go!");
            go.setActionCommand("Go!");
            go.addActionListener(g);
            goQuit.add(go);

            /* create Quit button */
            quit = new JButton("Quit");
            quit.setActionCommand("Quit");
            quit.addActionListener(c);
            goQuit.add(quit);
            
            contentPane.add(goQuit);
        
        /* create content pane with a FlowLayout with white background */
        choosePreset = new JPanel();
        choosePreset.setBackground(Color.white);
        
            /* create 'choose preset' prompt */
            promptChoosePreset = new JLabel("Choose a preset: ");
            choosePreset.add(promptChoosePreset);

            /* create combo box with null item */
            presetNames = new JComboBox();
            presetNames.addItem(null);
            presetNames.setSelectedIndex(0);
            presetNames.addActionListener(p);
            choosePreset.add(presetNames);
        
            contentPane.add(choosePreset);
        
        /* create content pane with a FlowLayout with empty borders */   
        setPreset = new JPanel();
        setPreset.setBackground(Color.white);
        
            /* create set preset prompt */
            promptSetPreset = new JLabel("Set preset name: ");
            setPreset.add(promptSetPreset);
            
            /* create text field */
            inputName = new JTextField(10);
            setPreset.add(inputName);
            
            /* create save button */
            save = new JButton("Save as preset");
            save.setActionCommand("Save as preset");
            save.addActionListener(a);
            setPreset.add(save);
            
            contentPane.add(setPreset);
            
        /* create content pane for buttons with a GridLayout and with borders */
        buttonsPane = new JPanel();
        buttonsPane.setLayout(new GridLayout(20,20));
        buttonsPane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));   
        
            /* create 400 buttons. row 20, column 20 */
            buttons = new JButton[20][20];
            for (int i = 0; i < buttons.length; i++){    // row 
                for (int j = 0; j < buttons[0].length; j++){   // column
                    buttons[i][j] = new JButton("O");                
                    buttons[i][j].setMargin(new Insets(0, 0, 0, 0)); // set margin of text in Jbutton 0
                    buttons[i][j].setPreferredSize(new Dimension(20,20));   // set size of JButton as 20x20
                    buttons[i][j].setForeground(Color.white);
                    buttons[i][j].setActionCommand("O");
                    buttons[i][j].addActionListener(this);
                    buttonsPane.add(buttons[i][j]);
                }
            }
            contentPane.add(buttonsPane);
        
        /* create message label */
        message = new JLabel("");
        message.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        message.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        contentPane.add(message);
        
        /* add content pane to frame */
        frame.setContentPane(contentPane);
        
        /* size and then display the frame */
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * if cell is clicked, execute code.
     * pre: none
     * post: Code has been executed when a cell has been clicked.
     * @param event 
     */
    public void actionPerformed(ActionEvent event){
        String eventName = event.getActionCommand();
        
        /* change button text to "X", set foreground colour, change value of cell to 1 */
        if (canClickButton == true){
            if ("O".equals(eventName)){
                for (int i = 0; i < buttons.length; i++){    // row
                    for (int j = 0; j < buttons[0].length; j++){   // column
                        if (event.getSource() == buttons[i][j]){
                            buttons[i][j].setText("X");
                            buttons[i][j].setForeground(Color.black);
                            gol.enliven(i, j);
                        }
                    }
                }
            }
        }
    }

    /* add to preset to "preset.txt" file */
    class addToPreset implements ActionListener {
        /**
        * if "Save as Preset" is clicked, execute code.
        * pre: none
        * post: Code has been executed when "Save as Preset" has been clicked.
        * @param event 
        */
        public void actionPerformed(ActionEvent event){
            if (canClickButton){
                String input = inputName.getText();     // text field
                
                /* write cell's current condition(O/X) to preset.txt file */
                try {
                    out = new FileWriter(preset, true);
                    writeFile = new BufferedWriter(out);

                    writeFile.write(input);
                    writeFile.newLine();
                    for (int i = 0; i < buttons.length; i++){
                        for (int j = 0; j < buttons[0].length; j++){
                            writeFile.write(buttons[i][j].getText());
                        }
                        writeFile.newLine(); 
                    }
                    writeFile.close();
                    out.close();
                    presetNames.addItem(input); // add to comboBox
                } catch (FileNotFoundException e){
                    System.out.println("File does not exist or could not be found.");
                    System.err.println("FileNotFoundException: " + e.getMessage());
                } catch (IOException e){
                    System.out.println("Problem reading file");
                    System.err.println("IOException: " + e.getMessage());
                }
            }
        }
    }
    
    /* go through array, check if cell is alive or dead, display next generation */
    class go implements ActionListener {
        /**
        * if "Go!" is clicked, execute code.
        * pre: none
        * post: Code has been executed when "Go!" has been clicked.
        * @param event 
        */
        public void actionPerformed (ActionEvent event){
            if (canClickButton){
                gol.nextGen();
                
                /* go through every cells, update condition(O/X) */
                for (int i = 0; i < buttons.length; i++){    // row
                    for (int j = 0; j < buttons[0].length; j++){   // column
                        if (gol.checkAliveOrDead(i, j) == 1){
                            buttons[i][j].setText("X");     // if alive, X
                            buttons[i][j].setForeground(Color.black);
                        } else if (gol.checkAliveOrDead(i, j) == 0){
                            buttons[i][j].setText("O");     // if dead, O
                            buttons[i][j].setForeground(Color.white);
                        }
                    }
                }
            }
        }
    }
    
    /* go through the array and count the number of live cells, then display message */
    class countLiveCell implements ActionListener {
        /**
        * if "Quit" is clicked, execute code.
        * pre: none
        * post: Code has been executed when "Quit" has been clicked.
        * @param event 
        */
        public void actionPerformed (ActionEvent event){
            if (canClickButton){
                int liveCellCounter = 0;
                /* go through cells, count number of live cells */
                for (int i = 0; i < buttons.length; i++){    // row
                    for (int j = 0; j < buttons[0].length; j++){   // column
                        if (gol.checkAliveOrDead(i, j) == 1){
                            liveCellCounter++;
                        }
                    }
                }
                canClickButton = false;     // user can't press any buttons anymore
                message.setText("You have ended the game with " + liveCellCounter + " live cells.");
                preset.delete();    // delete "preset.txt" file
            }
        }
    }
    
    /* change to selected preset's generation */
    class generationToPreset implements ActionListener {
        /**
        * if comboBox item is clicked, execute code.
        * pre: none
        * post: Code has been executed when a comboBox item has been clicked.
        * @param event 
        */
        public void actionPerformed(ActionEvent event){
            JComboBox comboBox = (JComboBox)event.getSource();
            String presetName = (String)comboBox.getSelectedItem();
            char[] array;   // individual rows of cells in text file
            
            if (canClickButton == true){
                /* read file */
                try {
                    in = new FileReader(preset);
                    readFile = new BufferedReader(in);
                    String temp;
                    
                    /* read textfile ,update current cell state as preset cell state */
                    while ((temp = readFile.readLine()) != null){
                        if (temp.equals(presetName)){   
                            temp = readFile.readLine();
                            for (int i = 0; i < buttons.length; i++){
                                for (int j = 0; j < buttons[0].length; j++){
                                    array = temp.toCharArray();     // line of state of cells to char array
                                    buttons[i][j].setText(String.valueOf(array[j]));
                                    if (String.valueOf(array[j]).equals("X")){
                                        gol.enliven(i, j);
                                        buttons[i][j].setForeground(Color.black);
                                    } else if (String.valueOf(array[j]).equals("O")){
                                        gol.kill(i, j);
                                        buttons[i][j].setForeground(Color.white);
                                    }
                                }
                                temp = readFile.readLine();
                            }   
                        }
                    }
                    in.close();
                    readFile.close();
                }catch (FileNotFoundException e){
                    System.out.println("File does not exist or could not be found.");
                    System.err.println("FileNotFoundException: " + e.getMessage());
                } catch (IOException e){
                    System.out.println("Problem reading file");
                    System.err.println("IOException: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Create and show the GUI.
     * pre: none
     * post: GUI has been showed.
     */
    private static void runGUI(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        Main GameOfLife = new Main();
    }
    
    /**
     * Main method
     * @param args 
     */
    public static void main(String[] args){
        /* methods that create and show a GUI should be run from an event-dispatching thread */
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                runGUI();
            }
        });
    }
}