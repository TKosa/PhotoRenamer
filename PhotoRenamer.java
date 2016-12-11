package photo_renamer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PhotoRenamer  {
    JFrame frame=new JFrame("Photo Renamer App");
    JPanel imgPanel=new JPanel();
    JPanel renamePanel=new JPanel();
    
    static boolean hasBeenInstantiated=false;
    
    PRImage image;
    
    //Most components have been instantiated in the code below beside their
    //grid layout specifications. JComboBox is accessed higher up in the code
    //though, so it is defined here.
    JComboBox prevNames;
    
    private PhotoRenamer() {
        
        GridBagConstraints constraints = new GridBagConstraints();
        
        //making panels in the frame a Grid Bag Layout
        imgPanel.setLayout(new GridBagLayout());
        renamePanel.setLayout(new GridBagLayout());
        
        //setting frame to be Flow Layout and specifying size for panels
        frame.setLayout(new FlowLayout());
        imgPanel.setPreferredSize(new Dimension(650, 100));
        renamePanel.setPreferredSize(new Dimension(650, 100));
        frame.add(imgPanel);
        frame.add(renamePanel);
        
        //Everything below are all added to the imgPanel
        
        //putting path label into panel
        JLabel pathName = new JLabel("File Name:");
        pathName.setForeground(Color.BLUE);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        imgPanel.add(pathName, constraints);
        
        //Different label than above
        JLabel ActualPathName = new JLabel("Actual path");
        ActualPathName.setForeground(Color.BLUE);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        imgPanel.add(ActualPathName, constraints);
        
        //putting tag drop down menu into panel
        JComboBox tagsComboBox = new JComboBox();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weightx = 0;
        constraints.weighty = 1;
        imgPanel.add(tagsComboBox, constraints);
        
        //add tag button into panel
        JButton addTagButton = new JButton("add tag");
        addTagButton.addActionListener(new ActionListener() {
            
            /**
             * When the "add tag" button is clicked, the tag selected in the combo box will be added to
             * the image name, change the path name in the GUI, and create a new previous name.
             *
             * @param e
             *            an ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String tag=tagsComboBox.getSelectedItem().toString();
                image.add_tag(tag);
                ActualPathName.setText(image.name);
                prevNames.addItem(image.name);
                
            }
        });
        
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.weighty = 2;
        imgPanel.add(addTagButton, constraints);
        
        JButton deleteTagButton = new JButton("Delete tag");
        
        deleteTagButton.addActionListener(new ActionListener() {
            /**
             * When the "Delete tag" button is clicked, the tag selected in the combo box will be removed from
             * the image name and change the path name in the GUI.
             *
             * @param e
             *            an ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                //image.directory.deleteTagFromDirectory(tagsComboBox.getSelectedItem().toString());
                image.delete_tag(tagsComboBox.getSelectedItem().toString());
                ActualPathName.setText(image.name);
            }
        });
        
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.weighty = 2;
        imgPanel.add(deleteTagButton, constraints);
        
        JTextField newTagTextField = new JTextField();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.weightx = 0;
        constraints.weighty = 1;
        imgPanel.add(newTagTextField, constraints);
        
        JButton addNewTag = new JButton("add new tag to above menu");
        addNewTag.addActionListener(new ActionListener() {
            /**
             * When the "add new tag to above menu" button is clicked, the text in the text field
             * will be added to the combo box of existing tags.
             *
             * @param e
             *            an ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                image.directory.addTagtoDirectory(newTagTextField.getText());
                /*If user tries to add a tag that already exists,
                  It will be added to the combobox of this instance of the gui, but not the actual
                  master list of tags. On next instantiation, duplicates will not persist.
                 */
                tagsComboBox.addItem(newTagTextField.getText());
            }
        });
        
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 2;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.weighty = 2;
        imgPanel.add(addNewTag, constraints);
        
        //Everything below are all added to the renamePanel
        //putting previous name title into renamePanel
        JLabel previousNames = new JLabel("Previous Names");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        renamePanel.add(previousNames, constraints);
        
        //putting previous names drop down menu into panel
        prevNames = new JComboBox();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weightx = 0;
        constraints.weighty = 1;
        renamePanel.add(prevNames, constraints);
        
        //add revert name button into panel
        //add revert name button into panel
        JButton revertName = new JButton("Revert to previous name");
        revertName.addActionListener(new ActionListener(){
            /**
             * When the "Revert to previous name" button is clicked, the image name will be changed
             * back to a previous name that the user selected in the previous names combo box.
             *
             * @param e
             *            an ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e){
            	try{String previousName=prevNames.getSelectedItem().toString();       	
                image.rename(previousName);
                ActualPathName.setText(image.name);
            	}
            	catch(NullPointerException ex){/*Do nothing*/}
            	
            }
            
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.weighty = 2;
        renamePanel.add(revertName, constraints);
        
        //choose button photo to panel
        JButton choosePhoto = new JButton("Choose Photo");
        choosePhoto.addActionListener(new ActionListener() {
            /**
             * When the "choose photo" button is clicked, it will open a GUI for the user to select
             * a photo from their local directories.
             *
             * @param e
             *            an ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "gif","jpeg","png","bmp","wbmp");
                fileChooser.setFileFilter(filter);
                int returnVal = fileChooser.showOpenDialog(null);
                
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    
                    File file = fileChooser.getSelectedFile();
                    ActualPathName.setText(file.getName());
                    image=new PRImage(file.getPath());
                    tagsComboBox.removeAllItems();
                    for(String tag:image.directory.existing_tags){
                        tagsComboBox.addItem(tag);
                    }
                    prevNames.removeAllItems();
                    if(image.previousNames().length()>0){
	                    for(String s: image.previousNames().split(",") ){
	                        prevNames.addItem(s.replace("/", "\\").split("\\|")[0]);
	                    }
                    }
                }
            }
            
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.weighty = 1;
        renamePanel.add(choosePhoto, constraints);
        
        //open photo button to panel
        JButton openPhoto = new JButton("Open Photo");
        openPhoto.addActionListener(new ActionListener() {
            /**
             * When the "Open Photo" button is clicked, it will open the the photo the
             * user has selected on his or her computer.
             *
             * @param e
             *            an ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                image.openInDesktop();
            }
        });
        
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.weighty = 1;
        renamePanel.add(openPhoto, constraints);
        
        frame.setVisible(true);
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * Makes a frame if the frame has never been made.
     */
    public static void makeFrame(){
        if(PhotoRenamer.hasBeenInstantiated){return;}
        else{
            PhotoRenamer.hasBeenInstantiated=true;
            new PhotoRenamer();}
    }
    
    /**
     * Calls the makeFrame() function to make a frame.
     *
     * @param args
     *            a list of Strings. This parameter is not used.
     */
    public static void main(String[] args) {
        makeFrame();
    }
}
