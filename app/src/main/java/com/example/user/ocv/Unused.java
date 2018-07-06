package com.example.user.ocv;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by user on 20/04/18.
 */

public class Unused {

    private boolean deleteFile(String inputdir) {
        try {
            // delete the original file
            new File(inputdir).delete();
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
        //try {

        //} catch (FileNotFoundException e) {
        //    e.printStackTrace();
        //}
        return true;
    }

    public void ClearStr() throws FileNotFoundException {
        //PrintWriter writer = new PrintWriter(imgtxtfile);
        //writer.print("");
        //writer.close();
    }

    public static String stringToHex(String base)
    {
        StringBuffer buffer = new StringBuffer();
        int intValue;
        for(int x = 0; x < base.length(); x++)
        {
            int cursor = 0;
            intValue = base.charAt(x);
            String binaryChar = new String(Integer.toBinaryString(base.charAt(x)));
            for(int i = 0; i < binaryChar.length(); i++)
            {
                if(binaryChar.charAt(i) == '1')
                {
                    cursor += 1;
                }
            }
            if((cursor % 2) > 0)
            {
                intValue += 128;
            }
            buffer.append(Integer.toHexString(intValue) + " ");
        }
        return buffer.toString();
    }

    public static void main(String[] args) throws IOException {
        String s = "The cat in the hat";
        System.out.println(s);
        //System.out.println(stringToHex(s));
    }

    private void zipSubFolder(ZipOutputStream out, File folder,
                              int basePathLength) throws IOException {
        final int BUFFER = 2048;
        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else {
                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(basePathLength);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }

    /*
     * gets the last path component
     *
     * Example: getLastPathComponent("downloads/example/fileToZip");
     * Result: "fileToZip"
     */

     /*
 *
 * Zips a file at a location and places the resulting zip file at the toLocation
 * Example: zipFileAtPath("downloads/myfolder", "downloads/myFolder.zip");
 */

    public boolean zipFileAtPath(String sourcePath, String toLocation) {
        final int BUFFER = 2048;

        File sourceFile = new File(sourcePath);
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            if (sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length());
            } else {
                byte data[] = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
     *
     * Zips a subfolder
     *
     */

    public String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        if (segments.length == 0)
            return "";
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }
    public static void copyDirectoryOneLocationToAnotherLocation(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {

                copyDirectoryOneLocationToAnotherLocation(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);

            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }

    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();

        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }


    private void runShellCommand(String command) throws Exception {
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }

    private void runTouchCommand(final int toX, final int toY){
        Thread t1 = new Thread(new Runnable(){
            public void run(){
                try {
                    if(m_process != null && m_dataOut != null){
                        String cmd = "/system/bin/input touch "+toX+" "+toY+"\n";
                        m_dataOut.writeBytes(cmd);
                        //Const.v(TAG, "Command executed: "+cmd);
                    }
                } catch (IOException e) { e.printStackTrace();}
            }
        });
        t1.start();
    }

    private void runSwipeCommand(final int fromX, final int toX, final int fromY, final int toY, final int duration){
        Thread t1 = new Thread(new Runnable(){
            public void run(){
                try {
                    if(m_process != null && m_dataOut != null){
                        String cmd = "/system/bin/input swipe "+fromX+" "+fromY+" "+toX+" "+toY+" "+ duration+"\n";
                        m_dataOut.writeBytes(cmd);
                        //Const.v(TAG, "Command executed: "+cmd);
                    }
                } catch (IOException e) { e.printStackTrace();}
            }
        });
        t1.start();
    }

    public static final String MIME_TYPE_CONTACT = "vnd.android.cursor.item/vnd.example.contact";
    Process m_process = null;
    DataOutputStream m_dataOut = null;


    private void drag(float fromX, float toX, float fromY, float toY, int stepCount){
        runSwipeCommand((int)fromX, (int)toX, (int)fromY, (int)toY, stepCount);
    }
}
