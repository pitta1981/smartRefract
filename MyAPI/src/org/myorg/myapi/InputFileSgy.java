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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class InputFileSgy {

    public RandomAccessFile fileInput;
    public File f;
    public int number;
    public int pointer[];
    public int type = 0;
    private float somma = 0;
    private float sommaabs = 0;

    public InputFileSgy(File file) throws java.util.zip.DataFormatException, FileNotFoundException {

        try {
            f = file;
            fileInput = new RandomAccessFile(file, "r");
            try {
                //check if really seg2
                fileInput.seek(3714);
            } catch (IOException ex) {
                Logger.getLogger(InputFileSgy.class.getName()).log(Level.SEVERE, null, ex);
            }
            int b1 = fileInput.readUnsignedShort();
            int b2 = fileInput.readUnsignedShort();
            int b3 = 0;//fileInput.readByte();
            int b4 = 0;//fileInput.readByte();
            /*if (((b2 << 8) + b1) != 0x3a55) {
             throw new java.util.zip.DataFormatException();
             }*/
            int size = b1;
            System.out.println(" " + b1 + " " + b2 + " " + (short) ((b2 << 8) + b1));


            fileInput.seek(3224);
            b1 = fileInput.readUnsignedShort();
            System.out.println(b1);
            type = b1;
            fileInput.seek(3840);
            System.out.println(fileInput.length() + " " + (fileInput.length() - 3600) / (240 + (size * 4)));
            /* for (int i=0;i<1024;i++)
             {
             float b5=fileInput.readFloat();
             System.out.println(b5);
             }*/

            //find number of traces in file
            fileInput.seek(6);
            b1 = fileInput.readUnsignedByte();
            b2 = fileInput.readUnsignedByte();
            number = (int) (fileInput.length() - 3600) / (240 + (size * 4));
            pointer = new int[number];

            fileInput.seek(12);
            b1 = fileInput.readUnsignedByte();


            //find beginning of each trace
            fileInput.seek(32);
            for (int i = 0; i < number; i++) {
                /*  b1 = fileInput.readUnsignedByte();
                 b2 = fileInput.readUnsignedByte();
                 b3 = fileInput.readUnsignedByte();
                 b4 = fileInput.readUnsignedByte();*/

                int res = 3840 + (i * (240 + (size) * 4));
                System.out.println(i + " " + res);
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

    Trace getTrace(int traceNum) {
        Trace p = new Trace(0);
        try {
            long beginPos = pointer[traceNum];

            //read size of trace header
            fileInput.seek(3714);
            int b1 = fileInput.readUnsignedShort();
            int b2 = fileInput.readUnsignedShort();
            int b3 = 0;
            int b4 = 0;
            fileInput.seek(beginPos + 8);
            int sizeData = b1;

            p = new Trace(sizeData);
            p.setNum(traceNum);

            p.sampleInterval = b2 / 1000000.0;
            p.length = b1;

            int sizeHeader = (b2 << 8) + b1;
            sizeHeader = 240;
            //read size of data block
            //fileInput.seek(beginPos+8);
            // b1 = fileInput.readUnsignedByte();
            // b2 = fileInput.readUnsignedByte();









            //read header into a string contained in the Trace object
            fileInput.seek(beginPos + 32);
            /* for(int i = 32; i < sizeHeader; i++){
             b1 = fileInput.readByte();
             char c = (char)b1;
             //System.out.println(c);
             p.sb.append(c);
             }*/

            //decode the header string
            // p.buildMetadata();

            //read in the data
            fileInput.seek(beginPos);
            for (int i = 0; i < sizeData - 1; i++) {
                //b1 = fileInput.readUnsignedByte();
                //b2 = fileInput.readUnsignedByte();
                //b3 = fileInput.readUnsignedByte();
                //b4 = 0;fileInput.readUnsignedByte();
                if (type == 5) {
                    float b5 = fileInput.readFloat();
                    if (b5 > p.getMaxValue()) {
                        p.setMaxValue(b5);
                    }
                    p.set(i, b5);

                }

                if (type == 2) {
                    int b5 = fileInput.readInt();
                    if (b5 > p.getMaxValue()) {
                        p.setMaxValue(b5);
                    }
                    somma = somma + b5;
                    sommaabs = sommaabs + Math.abs(b5);
                    p.set(i, b5);

                }
                if (type == 1) {
                    float b5 = fileInput.readFloat();
                    if (b5 > p.getMaxValue()) {
                        p.setMaxValue(b5);
                    }
                    somma = somma + b5;
                    sommaabs = sommaabs + Math.abs(b5);
                    p.set(i, b5);

                }


                //                System.out.println("b5 "+b5);
                int res = ((b3 << 16) | (b2 << 8)) + b1;
                //int res = ((b3 << 16) | (b2 << 8)  |(b4 << 24)) + b1;
                //short res=(short)((b2 << 8)  |b1);


                //double f = Float.intBitsToFloat(res);
                int f = res;
                //keep track of largest value for normalization later
                //System.out.println("#"+i+" "+b5 +" "+p.get(i));;
            }

        } catch (Exception e) {
            System.err.println(e);
        }
        p.setMediaAbs(sommaabs, p.length);
        p.setmedia(somma, p.length);
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
            fileInput.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
