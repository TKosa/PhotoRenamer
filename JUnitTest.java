package photo_renamer;

import junit.framework.TestCase;

public class JUnitTest extends TestCase {
	PRImage img;

   protected void setUp() {
     img=new PRImage("C:\\Users\\Thomas\\Documents\\Scanned Documents\\test.jpg");
        
    }
   
   public void testAddTagToMasterList(){ 
	   //Must ensure logfile does have testTag in its first line already
	   String testTag="Harambae";
	   
	   int numberOfTags=img.directory.existing_tags.length;
	   img.directory.addTagtoDirectory(testTag);
	   //He lives on in our hearts
	   
	   PRImage img2=new PRImage(img.imgfile.getAbsolutePath());
	   //Reinstantiate the logfile file object to reflect the added tag;
	   //This is not done automatically since the GUI can just add the new tag to its combobox directly
	   int numberOfTags2=img2.directory.existing_tags.length;
	   
	   assertEquals(numberOfTags,numberOfTags2-1);
	   assertEquals(img2.directory.existing_tags[numberOfTags],testTag);
	}
   
   public void testName(){
	   assertEquals(img.name,"test.jpg");
	 
   }
   
   public void testAddTag(){
	   img.add_tag("cat");
	   assertEquals(img.name,"test @cat.jpg");
	   
	   img.add_tag("cat");
	   assertEquals(img.name,"test @cat @cat.jpg");
	   //Although some implementations might deem this behavior to be erroneous
	   //We will allow the same tag to be added twice at the user's discretion, since
	   //the 2nd one can always be deleted. This gives the user more freedom for tagging conventions.
   }
   public void testDeleteTag(){
	   img.add_tag("catt");
	   img.delete_tag("cat");
	   //Delete tag should do nothing
	   assertEquals(img.name,"test @catt.jpg");
	   img.delete_tag("catt");
	   assertEquals(img.name,"test.jpg");
   }
   public void testDeleteTag2(){
	   img.add_tag("1");
	   img.add_tag("11");
	   img.add_tag("111");
	   img.add_tag("11");
	   img.add_tag("1");
	   assertEquals(img.name,"test @1 @11 @111 @11 @1.jpg");
	   
	   img.delete_tag("11");
	   assertEquals(img.name,"test @1 @111 @11 @1.jpg");
	   img.delete_tag("11");
	   assertEquals(img.name,"test @1 @111 @1.jpg");
	   img.delete_tag("1");
	   assertEquals(img.name,"test @111 @1.jpg");
	   img.delete_tag("1");
	   assertEquals(img.name,"test @111.jpg");
	   img.delete_tag("111");
	   assertEquals(img.name,"test.jpg");
   }
   
   public void testPreviousNames(){
	   img.add_tag("one");
	   img.add_tag("two");
	   String[] prevNames=img.previousNames().split(",");
	   assertEquals(prevNames[0],"test.jpg");
	   //Expected 2nd name is "test @one.jpg|123timestampdigits456"
	   //So we will parse it using the | character as a delimiter
	   assertEquals(prevNames[1].split("\\|")[0],"test @one.jpg");
	   assertEquals(prevNames[2].split("\\|")[0],"test @one @two.jpg");
   }
   
   public void testRevertName(){
	   img.add_tag("one");
	   img.add_tag("two");
	   
	   img.revertName(0);
	   assertEquals(img.name,"test.jpg");
	   
	   img.revertName(1);
	   assertEquals(img.name,"test @one.jpg");
	   
	   img.revertName(2);
	   assertEquals(img.name,"test @one @two.jpg");
   }
   
   public void testRename(){
	   img.rename("butter.jpg");
	   assertEquals(img.name,"butter.jpg");
	   img.rename("I cant believe its not.jpg");
	   assertEquals(img.name,"I cant believe its not.jpg");
   }
   
   @Override
   public void tearDown(){
	   img.rename("test.jpg");
   }
   
  
}
