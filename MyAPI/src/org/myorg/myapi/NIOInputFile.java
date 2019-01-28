/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.myapi;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.util.Log;

/**
 *
 * @author user
 */
public class NIOInputFile {

    public RandomAccessFile fileInput;
    public FileChannel fCH = null;
    public int type;
    public File f;
    public int number;
    public int pointer[];
    private double sommaabs = 0;

    public NIOInputFile(File file) throws java.util.zip.DataFormatException, FileNotFoundException {

        try {
            f = file;
            fileInput = new RandomAccessFile(file, "r");
            fCH = fileInput.getChannel();
            try {
                //check if really seg2
                //fileInput.seek(0);
                fCH.position(0);
            } catch (IOException ex) {
                Logger.getLogger(InputFileSg2.class.getName()).log(Level.SEVERE, null, ex);
            }
            ByteBuffer bb = ByteBuffer.allocate(2);
            fCH.read(bb);
            bb.flip();
            byte by = bb.get();
            if (by == 85) {
            }
            int b1 = (int) (by);//
            //int b11=fileInput.readUnsignedByte();

            //fCH.read(bb);
            //bb.flip();
            int b2 = (int) bb.get();//fileInput.readUnsignedByte();

            int b3 = 0;
            int b4 = 0;
            if (((b2 << 8) + b1) != 0x3a55) {
                throw new java.util.zip.DataFormatException();
            }

            //find number of traces in file
            // fileInput.seek(6);
            fCH.position(6);
            bb.flip();
            fCH.read(bb);
            bb.flip();
            b1 = bb.get();
            b2 = bb.get();
            //  b1 = fileInput.readUnsignedByte();
            //  b2 = fileInput.readUnsignedByte();
            number = (b2 << 8) + b1;
            pointer = new int[number];
            Log.warn("" + bb);
            //fileInput.seek(12);
            fCH.position(12);
            bb.flip();
            fCH.read(bb);
            bb.flip();
            b1 = bb.get();//fileInput.readUnsignedByte();
            type = b1;


            //find beginning of each trace
            //fileInput.seek(32);
            bb = ByteBuffer.allocate(number * 4);
            fCH.position(32);
            fCH.read(bb);
            bb.flip();
            for (int i = 0; i < number; i++) {
                b1 = this.byte2UnsignedInt(bb.get());//fileInput.readUnsignedByte();
                b2 = this.byte2UnsignedInt(bb.get());//fileInput.readUnsignedByte();
                b3 = this.byte2UnsignedInt(bb.get());//fileInput.readUnsignedByte();
                b4 = this.byte2UnsignedInt(bb.get());//fileInput.readUnsignedByte();

                int res = ((b3 << 16) | (b2 << 8) | (b4 << 24)) + b1;
                pointer[i] = res;
            }


        } catch (FileNotFoundException fnf) {
            System.err.println(fnf);
        } catch (EOFException eof) {
            System.out.println("End of File");
        } catch (IOException e) {
            System.out.println("IO error: " + e);
        }
    }

    public int byte2UnsignedInt(byte b) {
        return 0x00 << 24 | b & 0xff;
    }

    Trace getTrace(int traceNum) {
        Trace p = new Trace(0);
        try {
            long beginPos = pointer[traceNum];

            //read size of trace header
            fileInput.seek(beginPos + 2);
            int b1 = fileInput.readUnsignedByte();
            int b2 = fileInput.readUnsignedByte();
            int b3 = 0;
            int b4 = 0;
            int sizeHeader = (b2 << 8) + b1;

            //read size of data block
            fileInput.seek(beginPos + 8);
            b1 = fileInput.readUnsignedByte();
            b2 = fileInput.readUnsignedByte();
            int sizeData = (b2 << 8) + b1;


            p = new Trace(sizeData);
            p.setNum(traceNum);
            fileInput.seek(beginPos + 12);
            //b1 = fileInput.readUnsignedByte();
            type = fileInput.readUnsignedByte();

            //read header into a string contained in the Trace object
            fileInput.seek(beginPos + 32);
            for (int i = 32; i < sizeHeader; i++) {
                b1 = fileInput.readByte();
                char c = (char) b1;
                //System.out.println(c);
                p.sb.append(c);
            }

            //decode the header string
            p.buildMetadata();
            System.out.println(p.shotLocation);
            double somma = 0.0;
            sommaabs = 0.0;
            //read in the data
            //fileInput.seek(beginPos + sizeHeader);

            ByteBuffer bb = ByteBuffer.allocate((sizeData - 1) * 4);
            fCH.position(beginPos + sizeHeader);
            fCH.read(bb);
            bb.flip();



            for (int i = 0; i < sizeData - 1; i++) {
                if (type == 4) {
                    b1 = this.byte2UnsignedInt(bb.get());
                    b2 = this.byte2UnsignedInt(bb.get());
                    b3 = this.byte2UnsignedInt(bb.get());
                    b4 = this.byte2UnsignedInt(bb.get());
              
                    
                    
                    /*
                    b1 = fileInput.readUnsignedByte();
                    b2 = fileInput.readUnsignedByte();
                    b3 = fileInput.readUnsignedByte();
                    b4 = fileInput.readUnsignedByte();*/
                    int res = 0;
                    //int res = ((b3 << 16) | (b2 << 8) ) + b1;
                    res = ((b3 << 16) | (b2 << 8) | (b4 << 24)) + b1;
                    //short res=(short)((b2 << 8)  |b1);
                    double f = Float.intBitsToFloat(res);
                    somma = somma + f;
                    sommaabs = sommaabs + Math.abs(f);
                    // int f=res;
                    //keep track of largest value for normalization later
                    if (f > p.getMaxValue()) {
                        p.setMaxValue(f);
                    }
                    p.set(i, f);
                } else if (type == 1) {
                    b1 = this.byte2UnsignedInt(bb.get());
                    b2 = this.byte2UnsignedInt(bb.get());
                    b3 =0;
                    this.byte2UnsignedInt(bb.get());
                    b4 =0;
                            this.byte2UnsignedInt(bb.get());
              /*
                    b1 = fileInput.readUnsignedByte();
                    b2 = fileInput.readUnsignedByte();
                    b3 = 0;
                    fileInput.readByte();
                    b4 = 0;
                    fileInput.readUnsignedByte();
*/
                    //b3 = fileInput.readUnsignedByte();
                    //b4 = fileInput.readUnsignedByte();

                    short res = (short) ((b2 << 8) | b1);
                    //    double f = Float.intBitsToFloat(res);
                    short f = res;
                    //short f = res;
                    //keep track of largest value for normalization later
                    somma = somma + f;
                    sommaabs = sommaabs + Math.abs(f);
                    if (f > p.getMaxValue()) {
                        p.setMaxValue(f);
                    }
                    p.set(i, f);
                } else if (type == 10) {
                    b1 = this.byte2UnsignedInt(bb.get());
                    b2 = this.byte2UnsignedInt(bb.get());
                    bb.get();
                    bb.get();
                    b3=0;
                    b4=0;
                    /*b1 = fileInput.readUnsignedByte();
                    b2 = fileInput.readUnsignedByte();
                    b3 = 0;
                    fileInput.readByte();
                    b4 = 0;
                    fileInput.readUnsignedByte();
*/
                    //b3 = fileInput.readUnsignedByte();
                    //b4 = fileInput.readUnsignedByte();

                    short res = (short) ((b2 << 8) | b1);
                    //    double f = Float.intBitsToFloat(res);
                    short f = res;
                    //short f = res;
                    //keep track of largest value for normalization later
                    somma = somma + f;
                    sommaabs = sommaabs + Math.abs(f);
                    if (f > p.getMaxValue()) {
                        p.setMaxValue(f);
                    }
                    p.set(i, f);

                } else if (type == 2) {
                    
                    b1 = this.byte2UnsignedInt(bb.get());
                    b2 = this.byte2UnsignedInt(bb.get());
                    b3 = this.byte2UnsignedInt(bb.get());
                    b4 = this.byte2UnsignedInt(bb.get());
                   
                    //b3 = fileInput.readUnsignedByte();
                    //b4 = fileInput.readUnsignedByte();

                    //int res = ((b3 << 16) | (b2 << 8) ) + b1;
                    // res = ((b3 << 16) | (b2 << 8)  |(b4 << 24)) + b1;

                    int res = 0;
                    //int res = ((b3 << 16) | (b2 << 8) ) + b1;
                    res = ((b3 << 16) | (b2 << 8) | (b4 << 24)) + b1;
                    //short res = (short) ((b2 << 8) | b1);
                    int f = res;
                    somma = somma + f;
                    sommaabs = sommaabs + Math.abs(f);

                    //keep track of largest value for normalization later
                    if (f > p.getMaxValue()) {
                        p.setMaxValue(f);
                    }
                    p.set(i, f);
                }
            }
            p.setmedia(somma, p.value.length);
            p.setMediaAbs(sommaabs, p.value.length);

        } catch (Exception e) {
            System.err.println(e);
        }
        // System.out.println("done reading trace");
        return p;
    }

    public TraceSet getTraceSet() {
        TraceSet s = new TraceSet(number);
        for (int i = 0; i < number; i++) {
            s.setTrace(i, getTrace(i));
        }
        return s;
    }

    public void close() {
        try {
            fCH.close();
            fileInput.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
