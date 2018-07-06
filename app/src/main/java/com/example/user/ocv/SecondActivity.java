package com.example.user.ocv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

//import com.vistrav.ask.annotations.AskDenied;
//import com.vistrav.ask.annotations.AskGranted;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

//@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class SecondActivity extends Activity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    Process sh = Runtime.getRuntime().exec("su");
    //int permissionCheck1= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    //int permissionCheck2= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    //int permissionCheck3= ContextCompat.checkSelfPermission(this, Manifest.permission.KILL_BACKGROUND_PROCESSES);
    //static {
    //    System.loadLibrary("native-lib");
    //}
    //int pause=0;
    Integer tclick = 0;
    Integer objno2;
    Integer mode=1;
    public Integer checklatest=1;
    Integer X, Y, X1, Y1, X2, Y2;
    PointF f = new PointF();
    SparseArray<PointF> mActivePointers;
    String contentahex;
    String settext;
    Pattern patternobjno2=Pattern.compile("(0302010201)(.{2})(0A|29)?");
    //{0,1}
    Pattern patternobjno2complex=Pattern.compile("(0302010201)(.{2})?(.{2})(0A|29)");
    Pattern patternpic=Pattern.compile("(010AC480C391C391C391(?!.*010AC480C391C391C391))(.*?)(01C88A)(.{36})(.{28})(.{2})(.{2})(.{2})(.{2})(.{36})(0303)(.{102,})");
    Pattern patternindexno=Pattern.compile("(01)(.{8})(.{4})(011A)");
    Pattern patternindexappend=Pattern.compile("(1123236E6F7465732F2323756E66696C6564(?!.*1123236E6F7465732F2323756E66696C6564))(.*?)(00\\d\\d\\d\\d000\\d)(2323)");
    Matcher match;
    String replacedwith;
    String replaced;
    String[] diffdatehex;
    String diffDayHex;
    String diffMonthHex;

    //Filedir
    public String storageDir = Environment.getExternalStorageDirectory().toString();
    public String fnapp="com.fiistudio.fiinote";
    public String fndir=storageDir+"/fiinote/notes";
    public String folderName0;
    public String folderName;
    public String imgdir=storageDir+"/img.jpg";
    public String imgtxtdir=storageDir+"/img.txt";
    public String indexdir=fndir+"/index.nti";
    //public String oldindexdir=fndir+"/oldindex.nti";
    public String curnotzname, curnotename, foldernamehex;
    public String curnotzdir;
    public String curnotedir;
    public String curattachdir;
    //public File indexfile = new File(indexdir);
    public File curnotzfile;
    public File curnotefile;
    public File curattachfile;
    public File imgfile= new File(imgdir);
    public File imgtxtfile= new File(imgtxtdir);
    public Bitmap bmp1;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    public ImageView imageView;
    public TextView tv2;
    public TextView tv3;
    
    //public native String stringFromJNI(String i);
    public SecondActivity() throws IOException {
    }
    //Basic-INTERFACE(TOUCH/KEY)
    @Override public boolean onTouchEvent(MotionEvent event) {
        //if (pause==0) {
            mActivePointers = new SparseArray<>();
            int pointerIndex = event.getActionIndex();
            // get pointer ID
            int pointerId = event.getPointerId(pointerIndex);
            // get masked (not specific to a pointer) action
            int maskedAction = event.getActionMasked();
            f.x = event.getX(pointerIndex);
            f.y = event.getY(pointerIndex);
            X = Math.round(f.x);
            Y = Math.round(f.y);
            switch (maskedAction) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN: {
                    // We have a new pointer. Lets add it to the list of pointers
                    mActivePointers.put(pointerId, f);
                    break;
                }
                case MotionEvent.ACTION_MOVE: { // a pointer was moved
                    for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                        PointF point = mActivePointers.get(event.getPointerId(i));
                        if (point != null) {
                            point.x = event.getX(i);
                            point.y = event.getY(i);
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                }
                case MotionEvent.ACTION_POINTER_UP: {
                }
                case MotionEvent.ACTION_CANCEL: {
                    mActivePointers.remove(pointerId);
                    break;
                }
            }
            //invalidate();
            this.mDetector.onTouchEvent(event);
            // Be sure to call the superclass implementation
        //}
        return super.onTouchEvent(event);
    }
    @Override public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDown: " + event.toString());
        return true;
    }
    @Override public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());

    }
    @Override public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        //\Toast.makeText(this, "SingTap", Toast.LENGTH_SHORT).show ();
        return true;
    }
    @Override public boolean onSingleTapConfirmed(MotionEvent event) {
        //File myFile = new File(imgdir);
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        try {
            Runtime.getRuntime().exec("adb shell killall com.fiistudio.fiinote");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //if (pause==0){
            if (tclick == 0) {
                checkfile();
                settext="c1-"+String.valueOf(objno2);
                tv2.setText(settext);
                //Toast.makeText(this, "c1 ", Toast.LENGTH_LONG).show();
                tclick = 1;
                X1 = X;
                Y1 = Y;
            }
            else if (tclick==1){
                checkfile();
                X2 = X - X1;
                Y2 = Y - Y1;
                if (X > X1 && Y > Y1) {
                    if (mode==1)
                    {
                        if (imgtxtfile.exists()) {
                            String contenthex = readbytestoahex(curnotedir);
                            match = patternobjno2.matcher(contenthex);
                            while (match.find()) {
                                //objno2=Integer.parseInt(matcher5.group(2),16);
                                String objno2hex = match.group(2);
                                objno2 = (int) Long.parseLong(objno2hex, 16);
                            }
                        }
                        settext="c2 "+String.valueOf(objno2);
                        tv2.setText(settext);

                        String picName = new SimpleDateFormat("yyyyMMddHHmmss",Locale.US).format(new Date());
                        //imageView = findViewById(R.id.iv1);
                        //View v1 = getWindow().getDecorView().getRootView();
                        //v1.setDrawingCacheEnabled(true);
                        //Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                        //v1.setDrawingCacheEnabled(false);
                        Bitmap screen = Bitmap.createBitmap(bmp1, X1, Y1, X2, Y2, null, false);

                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        screen.compress(Bitmap.CompressFormat.JPEG, 25, bytes);
                        //File f = new File(Environment.getExternalStorageDirectory() + File.separator + String.valueOf(fileName) + "abcdefghijklmno.jpg");

                        //File myDir = new File(storageDir + fndir + "/" + String.valueOf(newdir2));
                        String filename = String.valueOf(picName) + "abcdefghijklmno.jpg";
                        File f = new File(curattachdir, filename);

                        //File f = new File(new File(storageDir+folderName+".notz/"), String.valueOf(picName)+"abcdefghijklmno.jpg");
                        FileOutputStream fo;

                        int w=screen.getWidth();
                        int h=screen.getHeight();

                        attachpicname(filename, w, h);

                        try {
                            if (f.createNewFile()){
                                fo = new FileOutputStream(f);
                                fo.write(bytes.toByteArray());
                                fo.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    else if (mode==2){
                        String settext="c2";
                        tv2.setText(settext);
                        //String picName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        imageView = findViewById(R.id.iv1);
                        View v1 = getWindow().getDecorView().getRootView();
                        v1.setDrawingCacheEnabled(true);
                        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                        v1.setDrawingCacheEnabled(false);
                        Bitmap screen = Bitmap.createBitmap(bitmap, X1, Y1, X2, Y2, null, false);

                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        screen.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                        File f = new File(imgdir);
                        FileOutputStream fo;
                        try {
                            if (f.createNewFile()){
                                fo = new FileOutputStream(f);
                                fo.write(bytes.toByteArray());
                                fo.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        detectPicsandLet();
                    }
                }
                else {
                    settext="try";
                    tv2.setText(settext);
                    //tclick = 0;
                }
                tclick = 0;
            }
        //}
        return true;
    }
    @Override public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }
    @Override public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        //setContentView(R.xml.preferences);
        //Intent globalService2 = new Intent(SecondActivity.this,SettingsActivity.class);
        //startActivity(globalService2);
        //Intent i = new Intent(this, MyPreferencesActivity.class);
        //startActivity(i);
        return true;
    }
    @Override public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }
    @Override public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        return true;
    }
    @Override public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: " + event1.toString() + event2.toString());
        System.exit(0);
        return true;
    }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            try {
                Runtime.getRuntime().exec("adb shell killall com.fiistudio.fiinote");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mode==1){
                mode=2;
                Toast.makeText(this, "Mode2", Toast.LENGTH_SHORT).show();
            }
            else if (mode==2){
                mode=1;
                Toast.makeText(this, "Mode1", Toast.LENGTH_SHORT).show();
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
            System.exit(0);
        }
        return true;
    }
    //AndroidProcessor(askroot)
    public void runapp(String app) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(app);
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }
    public void killapp(String app) {
        try {
            Runtime.getRuntime().exec("adb shell killall "+app);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void rootscreenshot(String picdir) {
        try {
            //sh = Runtime.getRuntime().exec("su", null,null);
            OutputStream os = sh.getOutputStream();
            os.write(("/system/bin/screencap -p " + picdir).getBytes("ASCII"));
            os.flush();
            os.close();
            //sh.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Basic-Fileprocessor
    private void copyFile(String inputPath, String inputFile, String outputPath) {
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
            //in = null;
            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            //out = null;
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }
    public String getlastmodfile(String dirPath) {
        File dir = new File(dirPath);
        String[] files = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return (new File(file, s).isDirectory() && s.matches(".*notz.*"));
            }
        });
        //logging(files.toString());
        File filename;
        File lastModifiedFile = new File (dirPath+"/"+files[0]);
        //logging(files1.toString());
        for (int i = 1; i < files.length; i++) {
            filename=new File(dirPath+"/"+files[i]);
            if (lastModifiedFile.lastModified() < filename.lastModified()) {
                if (lastModifiedFile.isDirectory() && filename.toString().matches(".*notz.*")){
                    lastModifiedFile = filename;
                }
            }
        }
        logging(lastModifiedFile.toString());
        return lastModifiedFile.toString();
    }
    //Basic-Stringprocessor
    //read
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        try (FileInputStream fis = new FileInputStream(f)) {
            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }
        //FileInputStream fis= new FileInputStream(f);
        //try {
        //    int read = fis.read(bytes, 0, size);
        //    if (read < size) {
        //        int remain = size - read;
        //        while (remain > 0) {
        //            read = fis.read(tmpBuff, 0, remain);
        //            System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
        //            remain -= read;
        //        }
        //    }
        //}  catch (IOException e){
        //    throw e;
        //} finally {
        //    fis.close();
        //}
        return bytes;
    }
    public static String convertbytestoahex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    public String readbytestoahex(String filedir){
        File file=new File (filedir);
        byte[] bytesArray= null;
        try {
            bytesArray = fullyReadFileToBytes(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertbytestoahex(bytesArray);
    }
    //translate 
    private static String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char aChar : chars) {
            hex.append(String.format(Locale.ENGLISH, "%02X", (int) aChar));
        }
        //for (int i = 0; i < chars.length; i++)
        //{
        //    hex.append(String.format(Locale.ENGLISH,"%02X",(int) chars[i]));
        //}
        return hex.toString();
    }
    public int convertahexintegertoint(String ahex){
        return (int) Long.parseLong(ahex, 16);
    }
    //matchingwregex
    public Matcher regexp2(String searchforpattern, Pattern pattern1) throws IOException {
        Matcher matcher1;
        matcher1 = pattern1.matcher(searchforpattern);
        return matcher1;
    }
    //write
    public void appendahextofile(String content, String filedir){
        try{
            int len = content.length();
            byte[] data2 = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data2[i / 2] = (byte) ((Character.digit(content.charAt(i), 16) << 4)
                        + Character.digit(content.charAt(i + 1), 16));
            }
            RandomAccessFile file2ram = new RandomAccessFile(filedir, "rw");
            file2ram.write(data2);
            file2ram.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        checkfile();
        killapp(fnapp);
        rootscreenshot(imgdir);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        imageView = findViewById(R.id.iv1);
        bmp1 = BitmapFactory.decodeFile(imgfile.getAbsolutePath());
        //imageView.setImageBitmap(bmp1);
        tv2 = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        tv3.setText(String.valueOf(objno2));
        Button prefbutton= findViewById(R.id.button);
        prefbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SecondActivity.this, MyPreferencesActivity.class);
                startActivity(i);
            }
        });
        Button newnotzbutton= findViewById(R.id.button2);
        newnotzbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgtxtfile.delete();
                newnotzfolder();
                Intent i = new Intent(SecondActivity.this, SecondActivity.class);
                startActivity(i);
                settext="nf";
                tv2.setText(settext);
            }
        });
        mDetector = new GestureDetectorCompat(this, this);
        mDetector.setOnDoubleTapListener(this);
    }
    //Functionspecifictoapp
    public void newnotzfolder() {
        if(!imgtxtfile.exists()) {
            System.out.println("We had to make a imgtxt file.");
            try {
                imgtxtfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            folderName0 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            folderName = "AOWNAND00000"+folderName0;
            //Toast.makeText(this, folderName, Toast.LENGTH_SHORT).show();
            setcurfnname(folderName);
            if(!curnotzfile.exists()) {
                curnotzfile.mkdirs();
            }
            if(!curattachfile.exists()) {
                curattachfile.mkdirs();
            }
            checknote(folderName);
        }
    }
    public String getlatestnotzfromdir(){
        String regex;
        String lastnotz=getlastmodfile(fndir);
        //else if(f.isFile()){
        //}
        regex = fndir;
        lastnotz = lastnotz.replaceAll(regex, "");
        regex = "/";
        lastnotz = lastnotz.replaceAll(regex, "");
        return lastnotz;
    }
    public void setcurfnname(String folderName){
        curnotzname=folderName+".notz";
        curnotename=folderName+".note";
        curnotzdir=fndir+"/"+curnotzname;
        curnotedir=curnotzdir+"/"+curnotename;
        curattachdir=curnotzdir+"/attach";
        curnotzfile= new File(curnotzdir);
        curnotefile= new File(curnotedir);
        curattachfile= new File(curattachdir);
        foldernamehex=asciiToHex(folderName);
        //return true;
    }
    public String[] getdateandtimediffinahex(){
        //Date c = Calendar.getInstance().getTime();
        String date1="05/11/2009";
        Date startdate;
        Date enddate;
        Integer diffYear=null;
        Integer diffMonth=null;
        Integer diffDay=null;
        //Calendar cnow = Calendar.getInstance();
        //@SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        //String date2 = df.format(cnow);
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date2 = df.format(todayDate);
        try {
            startdate = df.parse(date1);
            enddate = df.parse(date2);
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(startdate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(enddate);
            diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            diffMonth = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)+(diffYear*12);
            diffDay = endCalendar.get(Calendar.DAY_OF_MONTH) - startCalendar.get(Calendar.DAY_OF_MONTH);
            //+(diffYear*365)
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert diffDay != null;
        String diffdayhex=String.format("%02X",diffDay);
        String diffmonthhex=String.format("%02X",diffMonth);
        String diffyearhex=String.format("%02X",diffYear);
        String datediffhex[] = new String[3];
        datediffhex[0]= diffdayhex;
        datediffhex[1] =  diffmonthhex;
        datediffhex[2] =  diffyearhex;

        return datediffhex;
    }
    public void checkobjno2() throws IOException {
        logging(curnotedir);
        contentahex = readbytestoahex(curnotedir);
        match=regexp2(contentahex, patternobjno2complex);
        if (match.find()) {
            objno2=convertahexintegertoint(match.group(3));
        }
        else{
            try {
                match=regexp2(contentahex,patternobjno2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (match.find()) {
                objno2=convertahexintegertoint(match.group(2));
            }
            }
        //tv3.setText(String.valueOf(objno2));
        //return objno2;
        logging(objno2.toString());
    }
    public void updateobjno2(){
        int prefix;
        String prefixhex="";
        String totalobjhex="";
        String a;
        if (curnotefile.exists()){
            contentahex = readbytestoahex(curnotedir);
            try {
                match=regexp2(contentahex,patternobjno2complex);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (match.find()){
                objno2=convertahexintegertoint(match.group(3));
                objno2=objno2+1;
                logging(objno2.toString()+"last");
                if (objno2==128){
                    prefixhex="c2";
                }
                if (objno2>128){
                    prefix=Math.round((objno2-128)/64);
                    prefix=194+prefix;
                    prefixhex=String.format(Locale.ENGLISH,"%02X",prefix);
                }
                if (objno2>=192){
                    prefix=Math.round((objno2-192)/64);
                    prefix=195+prefix;
                    prefixhex=String.format(Locale.ENGLISH,"%02X",prefix);
                }
                logging(prefixhex);
                if (objno2<192) {
                    totalobjhex = String.format(Locale.ENGLISH, "%02X", objno2);
                }
                else if (objno2>=192) {
                    totalobjhex = String.format(Locale.ENGLISH,"%02X", (128 + ((objno2 - 192) % 64)));
                }
                if (match.group(4)==null){
                    a="";
                }
                else {
                    a=match.group(4);
                }
                String replacedcontent = contentahex.replaceAll(patternobjno2complex.toString(), match.group(1)+prefixhex+totalobjhex+a);
                appendahextofile(replacedcontent,curnotedir);
            }
            else{
                try {
                    match=regexp2(contentahex,patternobjno2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (match.find()){
                    objno2=convertahexintegertoint(match.group(2));
                    objno2=objno2+1;
                    totalobjhex = String.format(Locale.ENGLISH,"%02X", objno2);
                    String replacedcontent = contentahex.replaceAll(patternobjno2complex.toString(), match.group(1)+totalobjhex);
                    appendahextofile(replacedcontent,curnotedir);
                }
            }
        }
    }
    public void appendfilenamenotz(String fn) throws IOException {
        imgtxtfile.delete();
        try{
            if(!imgtxtfile.exists()){
                System.out.println("We had to make a new file.");
                imgtxtfile.createNewFile();
            }
            PrintWriter out = new PrintWriter(new FileWriter(imgtxtfile, true));
            out.append(fn).append(".notz\n");
            out.close();
        }catch(IOException e){
            System.out.println("COULD NOT LOG!!");
        }
    }
    public void appendnewnotetoindex() throws IOException {
        (new File (storageDir+"/log.txt")).delete();
        logging("appendindexstart");
        contentahex=readbytestoahex(indexdir);
        //logging(contentahex);
        diffdatehex=getdateandtimediffinahex();
        diffDayHex=diffdatehex[0];
        diffMonthHex=diffdatehex[1];
        String newfolderhex1="011A"+foldernamehex+"00" +
        "00" +
        "04" +
        "00" +
        "00" +
        "01" + diffMonthHex + diffDayHex + "FFFFFF" + "0000" +
        "01" + diffMonthHex + diffDayHex + "FFFFFF" + "0000" +
        "001A" + foldernamehex +
        "00" + "1123236E6F7465732F2323756E66696C6564" + "05" +
        "00"+
        "000000" +
        "000000" +
        "000000" +
        "01" + diffMonthHex + diffDayHex + "FFFFFF" + "0000" +
        "01" + diffMonthHex + diffDayHex + "FFFFFF";

        match=regexp2(contentahex,patternindexno);
        if(match.find()){
            Integer indexno=convertahexintegertoint(match.group(3));
            logging("ino="+indexno.toString());
            String indexnohex=String.format(Locale.ENGLISH,"%04X", (indexno+1));
            replacedwith=match.group(1)+match.group(2)+indexnohex+match.group(4);
            replaced=contentahex.replaceAll(patternindexno.pattern(),replacedwith);
            logging("appendindexstart1");
            logging(replacedwith);
            match=regexp2(replaced,patternindexappend);
            if (match.find()){
                replacedwith=match.group(1)+match.group(2)+newfolderhex1+match.group(3)+match.group(4);
                replaced=replaced.replaceAll(patternindexappend.pattern(),replacedwith);
                appendahextofile(replaced,indexdir);
                logging("appendindexstart2");
                logging(replacedwith);
                logging("\n");
                logging(replaced+"\n");
            }
        }

        logging("appendindexstart3");
    }
    @SuppressLint("SimpleDateFormat")
    public void checkfile(){
        try{
            if (checklatest==1){
                folderName=getlatestnotzfromdir();
                folderName=folderName.replaceAll(".notz","");
                logging(folderName);
            }
            else if (checklatest==0){
                if(imgtxtfile.exists()){
                    StringBuilder text = new StringBuilder();
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(imgtxtfile));
                        String line;
                        while ((line = br.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                            if (line.matches(".*notz.*")) {
                                folderName = String.valueOf(line);
                                folderName = folderName.replace(".notz", "");
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(!imgtxtfile.exists()) {
                    newnotzfolder();
                }
            }
            appendfilenamenotz(folderName);
            setcurfnname(folderName);
            if(!curnotzfile.exists()) {
                curnotzfile.mkdirs();
            }
            if(!curattachfile.exists()) {
                curattachfile.mkdirs();
            }
            checkobjno2();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void checknote(String folderName1){
        setcurfnname(folderName1);
        curnotzname=folderName1+".notz";
        //+".note";
        curnotename=folderName1+".note";;
        curnotzdir=fndir+"/"+curnotzname;
        curnotedir=curnotzdir+"/"+curnotename;
        curattachdir=curnotzdir+"/attach";
        curnotzfile= new File(curnotzdir);
        curnotefile= new File(curnotedir);
        curattachfile= new File(curattachdir);
        foldernamehex=asciiToHex(folderName1);
        try{
            if(!new File(curnotedir).exists()){
                System.out.println("We had to make a note file");
                curnotefile.createNewFile();
                //append...
                String[] diffdatehex=getdateandtimediffinahex();
                String diffMonthHex=diffdatehex[1];
                String diffDayHex=diffdatehex[0];
                logging(diffDayHex+","+diffMonthHex);
                //+ foldernamehex +
                String filetypehex="060000" +
                "01" + diffMonthHex + diffDayHex + "FFFFFF" + "0000" +
                "01" + diffMonthHex + diffDayHex + "FFFFFF" +
                "001A" + foldernamehex +
                "000000" + "ffff" +
                "000000" + "000000" +
                "000000" + "000000" +
                "000000" + "000000" +
                "01" +
                "000000" + "000000" +
                "000000" + "000000" +
                "01" + diffMonthHex + diffDayHex + "FFFFFF" +
                "010302010201"+
                "01";
                appendahextofile(filetypehex,curnotedir);
                appendnewnotetoindex();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void attachpicname(String filename, int w, int h){
        contentahex=readbytestoahex(curnotedir);
        int objnonow;
        int quot;
        int rem;
        int objno2c1=objno2/14;
        String objnohex;
        int prefixposy=169;
        String prefixposyhex;

        String newlinehex="0AC480C391C391C39101";
        String secondobjhex="C88A";
        String posyhex;
        String xpixshex="";
        String ypixshex="";
        String yscalehexs;
        String xscalehexs;
        String ysuffix="";
        String xlochex="E5A5AAE5AB81E5A5A9E19E81E5A5A9E19E81";
        if (objno2c1<32){
            prefixposy=169;
        }
        else if (objno2c1>=32){
            prefixposy=(Math.round(objno2c1)/34)+169;
        }
        prefixposyhex= String.format(Locale.ENGLISH,"%02X",prefixposy);
        quot=objno2/2;
        rem=objno2%2;
        if (quot==0)
        {
            if (rem>0){
                posyhex="9E";
            }
            else {
                posyhex="81";
            }
        }
        else {
            if (rem != 0) {
                posyhex = "9E";
            } else {
                posyhex = "81";
            }
        }
        if (quot>=31){
            int quotc1=objno2%31;
            objnonow=224+quotc1+1;
        }
        else{
            objnonow=224+quot+1;
        }
        objnohex=String.format(Locale.US,"%02X",objnonow);

        try {
            match=regexp2(contentahex,patternpic);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (checklatest==1 && match.find()){
            xlochex=match.group(4);
            prefixposyhex=match.group(6);
            objnohex=match.group(7);
            int posy=convertahexintegertoint(match.group(8))+5;
            posyhex=String.format(Locale.ENGLISH,"%02X",posy);
            logging("pph"+prefixposyhex+","+objnohex+","+posyhex);
        }
        logging("pph"+prefixposyhex+","+objnohex+","+posyhex);

        String ylochex="E5A5A9E19E81E5A5AAE5AB81E5A5"+prefixposyhex+objnohex+posyhex+"81";
        String zlochex="E5A5A9E19E81E5A5A9E19E81E5A5AAE5AB81";
        if (w<128)
        {
            xpixshex=String.format(Locale.ENGLISH,"%02X",w);
        }
        else if (w>=128)
        {
            int xquothexint=192+(w/64);
            int xremhexint=128+(w%64);
            String xquothexs=String.format(Locale.ENGLISH,"%02X",xquothexint);
            String xremhexs= String.format(Locale.ENGLISH,"%02X",xremhexint);
            xpixshex=xquothexs+xremhexs;
        }
        if (h<128)
        {
            ypixshex=String.format(Locale.ENGLISH,"%02X",h);
        }
        else if (h>=128)
        {
            int yquothexint=192+(h/64);
            int yremhexint=128+(h%64);
            String yquothexs=String.format(Locale.ENGLISH,"%02X",yquothexint);
            String yremhexs= String.format(Locale.ENGLISH,"%02X",yremhexint);
            ypixshex=yquothexs+yremhexs;
        }
        if (w<h)
        {
            int a=2717*w;
            int div=h*64;
            int xscalequotinta=a/div;
            int xscalequotint=148+(a/div);
            String xscalequothexs=String.format(Locale.ENGLISH,"%02X",xscalequotint);
            int xrem=(((a/div)-xscalequotinta)*64);
            int xscaleremint=128+xrem;
            String xscaleremhexs=String.format(Locale.ENGLISH,"%02X",xscaleremint);
            xscalehexs="E2"+xscalequothexs+xscaleremhexs;
            yscalehexs="E2BE9D";
        }
        else if (w>h)
        {
            xscalehexs="E38EBF";
            int a=3711*h;
            int div=w*64;
            int yscalequotint=0;
            int yscalequotinta=(a/div);

            if (yscalequotinta>=43)
            {
                ysuffix="E3";
                yscalequotint=128+(((3711*h)/(w*64))-43);
            }
            else if (yscalequotinta<43)
            {
                ysuffix="E2";
                yscalequotint=148+((3711*h)/(w*64));
            }
            String yscalequothexs=String.format(Locale.ENGLISH,"%02X",yscalequotint);
            int yrem=(((a/div)-yscalequotinta)*64);
            int yscaleremint=128+yrem;
            String yscaleremhexs=String.format(Locale.ENGLISH,"%02X",yscaleremint);
            yscalehexs=ysuffix+yscalequothexs+yscaleremhexs;
        }
        else
        {
            xscalehexs="E2BAA3";
            yscalehexs="E2BAA3";
        }
        String objscalehex="0303E293B903E293B903"+xscalehexs+"03"+yscalehexs+"22";
        String picnamehex=asciiToHex(filename);
        String hex = newlinehex+secondobjhex+xlochex+ylochex+zlochex+objscalehex+picnamehex+xpixshex+ypixshex+"01";

        logging("\n\n"+newlinehex+"\n"+secondobjhex+"\n"+xlochex+"\n"+ylochex+"\n"+zlochex+"\n"+objscalehex+"\n"+picnamehex+"\n"+xpixshex+"\n"+ypixshex+"01"+"\n\n");
        String appendtonote=contentahex+hex;
        appendahextofile(appendtonote,curnotedir);
        updateobjno2();

        if (checklatest==0){
            if (objno2>=30)
            {
                imgtxtfile.delete();
                checkfile();
                settext="newf";
                tv3.setText(settext);
                //runapp(fnapp);
            }
            else if (objno2<30)
            {
                tv3.setText(String.valueOf(objno2));
                try {
                    imgtxtfile.delete();
                    appendfilenamenotz(folderName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //public void detectpicchanges() {
        //copyFile(curnotedir,curnotedir.bk)

        //if (mode==pic){
//
//duplicatenotz
//                detectpiclocationchanges
//        run detectLetternPic
//#delete duplicate
//    }
//}
    public void logging(String data) {
        File logfile = new File(storageDir,  "log.txt");
        try {
            if (!logfile.exists()) {
                System.out.println("We had to make a new file.");
                logfile.createNewFile();
            }
            PrintWriter out = new PrintWriter(new FileWriter(logfile, true));
            out.append(data).append("\n");
            out.close();
        } catch (IOException e) {
            System.out.println("COULD NOT LOG!!");
            e.printStackTrace();
        }
    }
    //OPENCV
    public void detectPicsandLet(){
        //stringFromJNI(imgpath);
        if (imgfile.exists()) {
            Mat img1=Imgcodecs.imread(imgdir);
            List<Rect> letterBBoxes1= detectLetters(img1);
            for(int i=0; i< letterBBoxes1.size(); i++) {
                Imgproc.rectangle(img1,letterBBoxes1.get(i).br(), letterBBoxes1.get(i).tl(),new Scalar(255,255,255), -1);
            }
            //Imgproc.rectangle(img1,letterBBoxes1.get(i).br(), letterBBoxes1.get(i).tl(),new Scalar(0,255,0),3,8,0);
            Imgcodecs.imwrite(storageDir + "/abc1.jpg", img1);

        }
        //abc1
        String imgpath2 = storageDir + "/img1.jpg";
        File imgfile2 = new File(imgpath2);
        if (imgfile2.exists()) {

            Mat img2=Imgcodecs.imread(imgpath2);
            List<Rect> letterBBoxes2= detectPics(img2);
            for(int i=0; i< letterBBoxes2.size(); i++) {
                Imgproc.rectangle(img2,letterBBoxes2.get(i).br(), letterBBoxes2.get(i).tl(),new Scalar(255,255,255), -1);
            }
            Imgcodecs.imwrite(storageDir + "/abc2.jpg", img2);
            Bitmap myBitmap = BitmapFactory.decodeFile(storageDir + "/abc2.jpg");
        }
    }
    public List<Rect> detectLetters(Mat img){
        List<Rect> boundRect=new ArrayList<>();
        //Mat rgb =new Mat();
        Mat small=new Mat(), kernel, kernel2, grad=new Mat(), bw=new Mat(), connected=new Mat();
        //Imgproc.pyrDown(img,rgb);
        Imgproc.cvtColor(img, small, Imgproc.COLOR_RGB2GRAY);

        kernel=Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3,3));
        Imgproc.morphologyEx(small,grad, Imgproc.MORPH_GRADIENT,kernel);

        Imgproc.threshold(grad, bw, 0.0, 255.0, Imgproc.THRESH_OTSU);

        kernel2=Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9,1));
        Imgproc.morphologyEx(bw,connected,Imgproc.MORPH_CLOSE, kernel2);

        Mat hierarchy=new Mat();
        List<MatOfPoint> contours = new ArrayList<>();

        Imgproc.findContours(connected, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        for( int i = 0; i < contours.size(); i++ ){
            Rect appRect = Imgproc.boundingRect(contours.get(i));
            String fncount;
            @SuppressLint("SimpleDateFormat") String picName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            if (appRect.width>appRect.height && appRect.width > 10 && appRect.height > 10) {
                if (i>0){
                    boundRect.add(appRect);
                    Mat boundingSquare = img.submat(appRect);
                    fncount = String.format(Locale.ENGLISH,"%05d", i);
                    String filename = String.valueOf(picName)+fncount+"fghijklmno.jpg";
                    Imgcodecs.imwrite(curattachdir+"/"+filename, boundingSquare);
                    int w=boundingSquare.width();
                    int h=boundingSquare.height();
                    attachpicname(filename, w, h);
                }
            }
        }
        return boundRect;
    }
    public List<Rect> detectPics(Mat img){
        List<Rect> boundRect=new ArrayList<>();

        Mat gray=new Mat(), thresh=new Mat(), dilated=new Mat(), kernel;

        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(gray, thresh, 150,255,Imgproc.THRESH_BINARY_INV);
        kernel=Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(3,3));
        Imgproc.dilate(thresh, dilated, kernel);

        Mat hierarchy=new Mat();
        List<MatOfPoint> contours = new ArrayList<>();

        Imgproc.findContours(dilated, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        String fncount;
        @SuppressLint("SimpleDateFormat") String picName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        for( int i = 0; i < contours.size(); i++ ){
            Rect appRect = Imgproc.boundingRect(contours.get(i));

            if (appRect.width <12 || appRect.height <12) {
                String text="next";
            }
            else {
                if (i>0){
                    boundRect.add(appRect);
                    Mat boundingSquare = img.submat(appRect);
                    fncount = String.format(Locale.ENGLISH,"%05d", i);
                    String filename = String.valueOf(picName)+fncount+"t2hijklmno.jpg";
                    Imgcodecs.imwrite(curattachdir+"/"+filename, boundingSquare);
                    int w = boundingSquare.width();
                    int h = boundingSquare.height();
                    attachpicname(filename, w, h);
                }
            }
        }
        return boundRect;
    }
}
