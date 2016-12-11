package photo_renamer;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.regex.Pattern;

public class PRImage {
    String name;
    File imgfile;
    Directory directory;
    
    PRImage(String path) {
        imgfile=new File(path);
        if(!imgfile.exists()){System.out.println("ERROR FILE DOES NOT EXIST");
            return;
        }
        name=imgfile.getName();
        directory=new Directory(imgfile.getParent());
    }
    
    /**
     * Deletes a specified tag from the selected image name.
     *
     * @param tag
     *            the name of the tag.
     */
    void delete_tag(String tag) {
        //Updates both name and tags properties
        //Image.name = "13 @summer @boat"
        //Image.delete_tag("boat") -> Image.name= "13 @summer"
        //Print error if tag not found
        String newname="";
        //Pattern.quote removes the problem that the first arg of replaceFirst is a reg exp, which may cause errors
        if(name.contains(" @"+tag+" ")){newname=name.replaceFirst(Pattern.quote(" @"+tag+" "), " ");}
        else if(name.contains(" @"+tag+".")){newname=name.replaceFirst(Pattern.quote(" @"+tag+"."), ".");}
        else {return;}
        this.rename(newname);
    }
    
    /**
     * Adds a specified tag to the selected image name.
     *
     * @param tag
     *            the name of the tag.
     */
    void add_tag(String tag){
        this.rename(name.split("\\.")[0]+" @"+tag+"."+name.split("\\.")[1]);
        directory.addTagtoDirectory(tag);
    }
    
    /**
     * Renames the image to the specified new name and also changes the log file to account
     * for the rename.
     *
     * @param newname
     *            the name of the new desired name.
     * @throws IOException
     *                     when the log file does not exists.
     */
    void rename(String newname) {
        //Rename to name. name includes all tags and spaces, but not a comma or extension
        //This will change the name of the object, and add the new name to the logfile.
        
        //Update logfile
        File logfile=this.directory.logfile;
        BufferedReader reader;
        
        try {
            reader = new BufferedReader(new FileReader(logfile));
            Object[] lines=reader.lines().toArray();
            
            for(int i=0;i<lines.length;i++) {
                String line=lines[i].toString();
                if(line.contains(name)){
                    lines[i]=lines[i]+","+newname+"|"+new Date().getTime();
                }
            }
            
            PrintWriter writer=new PrintWriter(logfile,"UTF-8");
            for(int i=0;i<lines.length;i++){
                writer.println(lines[i].toString());
            }
            writer.close();
        }
        catch (IOException e) {
            
            e.printStackTrace();
        }
        //change the name property
        this.name=newname;
        //rename the file itself
        String newfilename=this.directory.directory.getAbsolutePath()+"//"+newname;
        this.imgfile.renameTo(new File(newfilename));
        this.imgfile=new File(newfilename);
    }
    
    /**
     * Reverts the image name back to previous name.
     *
     * @param index
     *            the index of the previous name that the user wants to go to.
     * @throws IOException
     *                     when the log file does not exists.
     */
    void revertName(int index) {
        File logfile=this.directory.logfile;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(logfile));
            //Creates array for the lines of the logfile
            Object[] lines=reader.lines().toArray();
            
            for(int fileLineIndex=1;fileLineIndex<lines.length;fileLineIndex++) {
                String line=lines[fileLineIndex].toString();
                for(String nameAndStamp:line.split(",")) {
                    String name=nameAndStamp.split("\\|")[0];
                    //Find the line which corresponds to the image
                    if(this.name.equals(name)){
                        //Given the line, rename the file based on the index argument
                        String newName=line.split(",")[index].split("\\|")[0];
                        this.rename(newName);
                        return;
                    }
                }
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Returns a String of previous names of the specified image file.
     *
     * @throws IOException
     *                     when the log file does not exists.
     * @return a String of previous names of the chosen  image file.
     */
    String previousNames(){
        //Returns line of previous names and timestamp of when name was changed.
        File logfile=this.directory.logfile;
        
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(logfile));
            Object[] lines=reader.lines().toArray();
            for(int fileLineIndex=1;fileLineIndex<lines.length;fileLineIndex++) {
                String line=lines[fileLineIndex].toString();
                for(String nameAndStamp:line.split(",")) {
                    String name=nameAndStamp.split("\\|")[0];
                    if(this.name.equals(name)){
                        //if the name of the file has been found in the logfile
                        //return the line on which it's been found
                        return line;
                    }
                }
            }
        }
        
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Error has occurred
        return "";
    }
    
    /**
     * Opens image in the desktop.
     *
     * @throws IOException
     *                     when the image cannot be opened in the desktop.
     */
    public void openInDesktop(){
        try {
            Desktop.getDesktop().open(imgfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
