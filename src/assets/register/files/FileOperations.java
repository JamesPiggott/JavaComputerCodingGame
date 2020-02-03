package assets.register.files;

import java.util.HashMap;

/**
 * FileOperations determines the functionality possible by files. Files can be created, fetched, iterated through, altered, wiped and dropped.
 * Files have permanence, unlike registers, they can be transmitted to other computers and will remain even after a computer has been rebooted, 
 * unlike main memory. A file is basically a dictionary of key value pairs. The key is an integer between 0 and 9999 while the value is any,
 * alphanumeric string with a length of no more than 10. 
 * @author Kane
 *
 */
public class FileOperations {
	
	private String file_name;
	private HashMap<Integer, String> file_contents;
	private Integer location_handler;
	
	// FCH or fetch creates a new file if one does not already exist
	public FileOperations(String name) {
		this.file_name = name;
		this.file_contents = new HashMap<Integer, String>();
	}
	
	public void wipeContents() {
		this.file_contents.clear();
	}

}