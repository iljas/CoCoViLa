package ee.ioc.cs.vsle.util;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;

import ee.ioc.cs.vsle.editor.*;

/**
 * User: Ando
 * Date: 28.03.2005
 * Time: 21:45:37
 */
public class FileFuncs {

    /**
     * Buffer size for file read and write operations.
     */
    private static final int BUFSIZE = 10240;
    private static final int BUFMAXSIZE = Integer.MAX_VALUE / 2;
    /**
     * Interface for different storage types for generated data.
     * The effect of these functions is implementation dependant, for example
     * the files might be kept in memory or written to disk.
     * It could be the case that only files written using writeFile()
     * can be later successfully read.
     */
    public static interface GenStorage {
        public boolean writeFile(String fileName, String data);
        public byte[] getFileContents(String fileName);
        public char[] getCharFileContents(String fileName);
    }

    /**
     * Filesystem backed GenStorage implementation.
     */
    public static class FileSystemStorage implements GenStorage {

        private File path;

        public FileSystemStorage(String defaultPath) {
            if (defaultPath == null) {
                defaultPath = RuntimeProperties.getWorkingDirectory();
            }
            setPath( new File(defaultPath) );
        }

        @Override
        public byte[] getFileContents(String fileName) {
            File file = new File(fileName);
            if (!file.isAbsolute()) {
                file = new File(getPath(), fileName);
            }
            if (file.isFile() && file.canRead()) {
                try {
                    return getByteStreamContents(new FileInputStream(file), 
                            file.length() > BUFMAXSIZE ? BUFMAXSIZE : (int)file.length());
                } catch (FileNotFoundException e) {
                    db.p(e);
                }
            }
            return null;
        }

        @Override
        public boolean writeFile(String fileName, String data) {
            File file = new File(fileName);
            if (!file.isAbsolute()) {
                file = new File(getPath(), fileName);
            }
            return FileFuncs.writeFile(file, data);
        }

        @Override
        public String toString() {
            return "FileSystemStorage(" + getPath() + ")";
        }

        @Override
        public char[] getCharFileContents(String fileName) {
            File file = new File(fileName);
            if (!file.isAbsolute()) {
                file = new File(getPath(), fileName);
            }
            //TODO find the reason for those exceptions
            try {
                if (file.canRead() && file.isFile())
                    return getCharStreamContents(new FileInputStream(file), 
                            file.length() > BUFMAXSIZE ? BUFMAXSIZE : (int)file.length());
            } catch (FileNotFoundException e) {
                db.p("FileSystemStorage.getCharFileContents(): " + e.getMessage());
            } catch (SecurityException e) {
                db.p("FileSystemStorage.getCharFileContents(): " + e.getMessage());
            }
            return null;
        }

        /**
         * @return the path
         */
        private File getPath() {
            checkFolderAndCreate( path );
            return path;
        }

        /**
         * @param path the path to set
         */
        private void setPath( File path ) {
            this.path = path;
        }
    }

    /**
     * Memory backed GenStorage implementation.
     * This storage knows nothing about filesystems and files. All the
     * data is kept in memory as byte arrays corresponding to the specified
     * names. Only the files written using writeFile() can be later read.
     */
    public static class MemoryStorage implements GenStorage {

        private Map<String, byte[]> fileMap;

        @Override
        public byte[] getFileContents(String fileName) {
            byte[] data = null;
            if (fileMap != null) {
                data = fileMap.get(fileName);
            }
            return data;
        }

        @Override
        public boolean writeFile(String fileName, String data) {
            if (fileMap == null) {
                fileMap = new HashMap<String, byte[]>();
            }
            fileMap.put(fileName, data.getBytes());
            return true;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append("MemoryStorage (");
            s.append(this.hashCode());
            s.append("):");
            if (fileMap != null && fileMap.size() > 0) {
                s.append("\n");
                for (Map.Entry<String, byte[]> v : fileMap.entrySet()) {
                    s.append('\t');
                    s.append(v.getKey());
                    s.append(" (");
                    s.append(v.getValue().length);
                    s.append(")\n");
                }
            } else {
                s.append(" <empty>");
            }
            return s.toString();
        }

        @Override
        public char[] getCharFileContents(String fileName) {
            byte[] contents = getFileContents(fileName);
            if (contents != null) {
                return new String(contents).toCharArray();
            }
            return null;
        }
    }

    /**
     * Reads the contents of the specified (text) file into memory.
     * Reading is done line by line using a buffered character stream.
     * Files bigger than 2GB cannot currently be read with this method.
     * @param file the file to be read
     * @return the contents of the file as a string,
     * or the empty string on error
     */
    public static String getFileContents(File file) {
        if (RuntimeProperties.isLogDebugEnabled()) {
            db.p("Retrieving " + file);
        }

        StringBuilder fileString = null;
        if (file != null && file.exists() && !file.isDirectory()) {
            BufferedReader in = null;
            try {
                long len = file.length();
                if (len > Integer.MAX_VALUE) {
                    db.p("File " + file.getAbsolutePath() + " is too large!");
                    return "";
                }
                // StringBuilder can handle the value 0L for special files
                fileString = new StringBuilder((int) file.length());

                in = new BufferedReader(new FileReader(file));
                String lineString;

                while ((lineString = in.readLine()) != null) {
                    fileString.append(lineString);
                    fileString.append("\n");
                }
            } catch (IOException ioe) {
                db.p("Couldn't open file "+ file.getAbsolutePath());
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        db.p(e);
                    }
                    in = null;
                }
            }
        }

        return fileString == null ? "" : fileString.toString();
    }

	/**
	 * Writes the text to the specified file. The file is created if it
	 * does not exist yet. A newline is appended to the text unless the text
	 * already ends with a newline.
	 * @param file the output file
	 * @param text the text to be written
	 * @return true on success, false on error
	 */
	public static boolean writeFile(File file, String text) {
		boolean status = false;
		if (file != null && !file.isDirectory()) {
			PrintWriter out = null;
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
				out.print(text);
				if (text != null && !text.endsWith("\n")) {
				    out.println();
				}
				out.close();
				out = null;
				status = true;
			} catch (Exception e) {
				db.p(e);
				db.p("Couldn't write to file "+ file.getAbsolutePath());
			} finally {
				if (out != null) {
					out.close();
					out = null;
				}
			}
		}
		return status;
	}

    public static void writeFile( String prog, String mainClassName, String ext, String dir, boolean append ) {
        try {
            if (!dir.endsWith(File.separator)) {
                dir += File.separator;
            }
            String path = dir + mainClassName + "." + ext;
            File file = new File( path );

            if( !append && file.exists() ) {
                file.delete();
            }

            PrintWriter out = new PrintWriter( new BufferedWriter(new FileWriter( path, append ) ) );

            out.println( prog );
            out.close();
        } catch ( Exception e ) {
            db.p( e );
        }
    }
    
    public static String getName(File f) {
        String s = f.getName();
        int i = s.lastIndexOf( '.' );
        if( i > 0 && i < s.length() )
            return s.substring( 0, i );
        return s;
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
    
    public static URL getResource( String res, boolean checkFileIfNull ) {
    	
    	URL url = Thread.currentThread().getContextClassLoader().getResource( res );
    	
    	if( url != null || !checkFileIfNull ) {
    		return url;
    	}
    	
    	File file = new File( res );
    	
    	if( file.exists() ) {
    		try {
				return file.toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
    	}
    	
    	return null;
    }

    public static ImageIcon getImageIcon( String icon, boolean isAbsolutePath ) {
    	
    	if (!isAbsolutePath) {
    		URL url = getResource( icon, false );
    		
    		if( url != null )
    		{
    			return new ImageIcon( url );
    		}
    	}
    	
    	return new ImageIcon( icon );
    }
    
    
    /**
     * Makes sure that all file separators in path are valid for current OS.
     * If the path is already valid the same string is returned.
     * 
     * @param path a string representing a path name that possibly contains
     * path name separators of a different platform
     * @return a string where foreign path name separators are replaced with
     * the separators of current platform; or the original string if it did not
     * contain any foreign path name separators
     */
    public static String preparePathOS( String path ) {
        return File.separatorChar == '/'
                ? path.replace('\\', '/')
                : path.replace('/', '\\');
    }

    /**
     * Reads everything from a character input stream into a char array.
     * @param charStream Finite character stream
     */
    public static char[] getCharStreamContents(InputStream charStream) { 
        return getCharStreamContents( charStream, BUFSIZE );
    }
    
    public static char[] getCharStreamContents(InputStream charStream, int size) {
        InputStreamReader reader = new InputStreamReader(charStream);
        char[] buf = null;
        int readTotal = 0;
        int read = 0;

        try {
            buf = new char[size];

            while (true) {
                if (buf.length == readTotal) {
                    // a linked list of chunks could be more efficient
                    buf = Arrays.copyOf(buf, readTotal + size);
                }
                read = reader.read(buf, readTotal, buf.length - readTotal);
                if (read > -1) {
                    readTotal += read;
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            db.p(e);
            buf = null;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // ignore
            }
            try {
                charStream.close();
            } catch (IOException e) {
                // ignore
            }
        }

        if (buf != null && buf.length > readTotal) {
            buf = Arrays.copyOf(buf, readTotal);
        }
        return buf;
    }
    
    /**
    * Copies files using byte streams
    * @param fileIn Input file
    * @param fileOut Output file
    */
    public static void copyImageFile(File fileIn, File fileOut) {
    	try {
    		ImageInputStream in = new FileImageInputStream(fileIn);
    		ImageOutputStream out = new FileImageOutputStream(fileOut);
			int b;
			while ((b = in.read()) != -1) {
				out.write((byte)b);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			db.p(e);
		} 
    }

    /**
     * Reads everything from a byte input stream into a byte array.
     * @param byteStream Finite byte stream
     * @return the contents of the stream as byte array; null in case of errors
     */
    public static byte[] getByteStreamContents(InputStream byteStream) {
        return getByteStreamContents( byteStream, BUFSIZE );
    }
    
    public static byte[] getByteStreamContents(InputStream byteStream, int size) {
        byte[] buf = null;
        int readTotal = 0;
        int read = 0;
        
        try {
            buf = new byte[size];

            while (true) {
                if (buf.length == readTotal) {
                    // a linked list of chunks could be more efficient
                    buf = Arrays.copyOf(buf, readTotal + size);
                }
                read = byteStream.read(buf, readTotal, buf.length - readTotal);
                if (read > -1) {
                    readTotal += read;
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            db.p(e);
            buf = null;
        } finally {
            try {
                byteStream.close();
            } catch (IOException e) {
                // ignore
            }
        }

        if (buf != null && buf.length > readTotal) {
            buf = Arrays.copyOf(buf, readTotal);
        }
        return buf;
    }

    /**
     * Translates arbitrary non-null strings to strings that should be suitable
     * for usage in file names.
     * @param s non-null string
     * @return a lowercase string with non-word characters replaced with
     * underscores
     */
    public static String toFileName(String s) {
        s = s.toLowerCase();
        s = s.replaceAll("[^-\\w]+", "_");
        return s;
    }
    
    public static File showFileChooser( String path, File selectedFile, CustomFileFilter filter, Component parent, boolean showSaveDialog ) {
        
        JFileChooser fc = new JFileChooser( path );
        fc.setSelectedFile( selectedFile );
        fc.setFileFilter( filter );
        int returnVal = showSaveDialog ? fc.showSaveDialog( parent ) : fc.showOpenDialog( parent );
        
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {

            File file = fc.getSelectedFile();

            if ( showSaveDialog 
                    && filter != null 
                    && !file.getAbsolutePath().toLowerCase().endsWith( filter.getExtension() ) ) {

                file = new File( file.getAbsolutePath() + "." + filter.getExtension() );
            }
            
            return file;
        }
        
        return null;
    }
    
    public static boolean askToOverwriteFile( File f, Component parent ) {
        
        return JOptionPane.showConfirmDialog( 
                    parent, 
                    "File \"" + f.getName() + "\" exists, overwrite?", 
                    "File exists", 
                    JOptionPane.YES_NO_OPTION ) == JOptionPane.OK_OPTION;
    }
    
    /**
     * Checks if folders exists, if yes, does nothing and returns false;
     * if not, creates new folder and returns true
     * 
     * @param dir
     * @return
     */
    public static boolean checkFolderAndCreate( File dir ) {
        if (!dir.exists()) {
            dir.mkdirs();
            return true;
        }
        return false;
    }
    
    public static boolean checkFolderAndCreate( String path ) {
        return checkFolderAndCreate( new File(path) );
    }
}
