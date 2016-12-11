package photo_renamer;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Directory {
    File directory;
    File logfile;
    String[] existing_tags;
    
    Directory(String path) {
        directory=new File(path);
        logfile=new File(path+"//"+"logfile.txt");
        if (!logfile.exists()) {
            logfile=this.generateLogFile(this.directory);
            existing_tags=new String[0];
        }
        else {
            BufferedReader r;
            try {
                r = new BufferedReader(new FileReader(logfile));
                existing_tags=r.readLine().split(",");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * The first line of logfile represents the available tags that were added previously in the directory.
     * The list of tags are represented in a string.
     * If there are previously added tags, the function checks to see if the given tag is new, and if it is, it adds
     * it in the first line, adds it to the string of tags.
     *
     * @param tag
     *            the name of the tag.
     * @throws IOException
     *                     when the log file does not exists.
     */
    void addTagtoDirectory(String tag) {
        BufferedReader reader;
        
        try {
            reader = new BufferedReader(new FileReader(logfile));
            Object[] lines=reader.lines().toArray();
            if(lines[0].toString().length()==0){
                lines[0]=tag;
            }
            else {
                //Check to see if tag already exists in log file, if not, add to logfile
                String line = (String) lines[0];
                if (!line.contains(tag+",")&&!line.contains(tag+",")) {
                    lines[0]=lines[0]+","+tag;
                }
            }
            PrintWriter writer=new PrintWriter(logfile,"UTF-8");
            
            for(int i=0;i<lines.length;i++) {
                writer.println(lines[i].toString());
            }
            writer.close();
        }
        
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * The first line of logfile represents the available tags that were added previously in the directory.
     * The list of tags are represented in a string.
     * This function takes the first instance where the specified tag appears, and replaces it and the comma
     * in front with an empty string.
     *
     * @param tag
     *            the name of the tag.
     * @throws IOException
     *                     when the log file does not exists.
     */
    void deleteTagFromDirectory(String tag) {
        BufferedReader reader;
        
        try {
            reader = new BufferedReader(new FileReader(logfile));
            Object[] lines=reader.lines().toArray();
            String firstline=lines[0].toString();
            lines[0]=firstline.replaceFirst(","+tag, "");
            PrintWriter writer=new PrintWriter(logfile,"UTF-8");
            for(int i=0;i<lines.length;i++){
                writer.println(lines[i].toString());
            }
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Generates a log file in the current directory the images directory.
     *
     * @param directory
     *            the name of the directory.
     * @throws IOException
     *                     when the file is not found.
     * @return the file with a given pathname, else, return an error.
     */
    private File generateLogFile(File directory) {
        String pathname=directory.getAbsolutePath()+"//"+"logfile.txt";
        
        try {
            PrintWriter writer = new PrintWriter(pathname, "UTF-8");
            writer.println("");
            String[] dirlist=directory.list();
            
            for (int i=0;i<dirlist.length;i++) {
                String name=dirlist[i];
                if(name.contains("gif")||name.contains("jpg")
                   ||name.contains("jpeg")||name.contains("png")||name.contains("bmp")
                   ||name.contains("wbmp") ) {
                    writer.println(name.split("//.")[0]);
                }
            }
            writer.close();
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return new File(pathname);
    }
    
    /**
     * Opens the directory in the desktop.
     *
     * @throws IOException
     *                     when the directory cannot be open the in the desktop.
     */
    public void openInDesktop() {
        try {
            Desktop.getDesktop().open(this.directory);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
