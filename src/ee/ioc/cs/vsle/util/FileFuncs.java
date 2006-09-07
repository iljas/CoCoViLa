package ee.ioc.cs.vsle.util;

import java.io.*;

/**
 * User: Ando
 * Date: 28.03.2005
 * Time: 21:45:37
 */
public class FileFuncs {
	public static String getFileContents(String fileName) {
		String fileString = new String();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String lineString = new String();

			while ((lineString = in.readLine()) != null) {
				fileString += lineString+"\n";
			}
			in.close();
		} catch (IOException ioe) {
			db.p("Couldn't open file "+ fileName);
		}

		return fileString;
	}

	public static void writeFile(String fileName, String text) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
			out.println(text);
			out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			db.p("Couldn't write to file "+ fileName);
		}
	}

	public static void writeFile( String prog, String mainClassName, String ext, String dir, boolean append ) {
        try {
        	if( !dir.endsWith( System.getProperty( "file.separator" ) ) ) {
        		dir += System.getProperty( "file.separator" );
        	}
        	String path = dir + System.getProperty( "file.separator" ) + mainClassName + "." + ext;
        	
        	File file = new File( path );
        	
        	if( !append && file.exists() ) {
        		file.delete();
        	}
        	
            PrintWriter out = new PrintWriter( new BufferedWriter( new FileWriter( path, append ) ) );

            out.println( prog );
            out.close();
        } catch ( Exception e ) {
            db.p( e );
        }
    }
    
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
