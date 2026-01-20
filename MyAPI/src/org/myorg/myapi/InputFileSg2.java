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
public class InputFileSg2 {

    public RandomAccessFile fileInput;
    public int type;
    public File f;
    public int number;
    public int pointer[];
    private double sommaabs=0;

    public InputFileSg2(File file) throws java.util.zip.DataFormatException, FileNotFoundException {

        try {
            f = file;
            fileInput = new RandomAccessFile(file, "r");
            try {
                //check if really seg2
                fileInput.seek(0);
            } catch (IOException ex) {
                Logger.getLogger(InputFileSg2.class.getName()).log(Level.SEVERE, null, ex);
            }
            int b1 = fileInput.readUnsignedByte();
            int b2 = fileInput.readUnsignedByte();
            int b3 = 0;
            int b4 = 0;
            if (((b2 << 8) + b1) != 0x3a55) {
                throw new java.util.zip.DataFormatException();
            }

            //find number of traces in file
            fileInput.seek(6);
            b1 = fileInput.readUnsignedByte();
            b2 = fileInput.readUnsignedByte();
            number = (b2 << 8) + b1;
            pointer = new int[number];
            System.out.println("File header - Number of traces: " + number);

            fileInput.seek(12);
            b1 = fileInput.readUnsignedByte();
            type = b1;
            System.out.println("File header - Data format type: " + type);

            //find beginning of each trace
            fileInput.seek(32);
            System.out.println("Reading trace pointers starting at offset 32:");
            for (int i = 0; i < number; i++) {
                b1 = fileInput.readUnsignedByte();
                b2 = fileInput.readUnsignedByte();
                b3 = fileInput.readUnsignedByte();
                b4 = fileInput.readUnsignedByte();

                int res = (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
                pointer[i] = res;
                System.out.println("  Trace " + i + ": pointer = 0x" + Integer.toHexString(pointer[i]) + " (" + pointer[i] + ")");
            }


        } catch (FileNotFoundException fnf) {
            System.err.println("FileNotFoundException: " + fnf);
        } catch (EOFException eof) {
            System.err.println("EOFException: " + eof);
        } catch (IOException e) {
            System.err.println("IOException: " + e);
            e.printStackTrace();
        }
        System.out.println("InputFileSg2 constructor completed successfully with " + number + " traces");
    }

    Trace getTrace(int traceNum) {
        Trace p = new Trace(0);
        try {
            System.out.println("=== Starting to read trace " + traceNum + " ===");
            long beginPos = pointer[traceNum];
            System.out.println("Trace pointer: 0x" + Long.toHexString(beginPos) + " (" + beginPos + ")");

            //check trace magic header "X1"
            fileInput.seek(beginPos);
            int b1 = fileInput.readUnsignedByte();
            int b2 = fileInput.readUnsignedByte();
            int b3 = 0;
            int b4 = 0;
            if (b1 != 0x58 || b2 != 0x31) {  // "X1"
                System.err.println("Invalid trace header at 0x" + Long.toHexString(beginPos) + 
                                 ": expected 0x5831, got 0x" + Integer.toHexString((b2 << 8) | b1));
                return p;
            }

            //read size of trace header (2 bytes)
            fileInput.seek(beginPos + 2);
            b1 = fileInput.readUnsignedByte();
            b2 = fileInput.readUnsignedByte();
            int sizeHeader = (b2 << 8) + b1;

            //read size of data block (Standard says offset 4, 4 bytes)
            fileInput.seek(beginPos + 4);
            b1 = fileInput.readUnsignedByte();
            b2 = fileInput.readUnsignedByte();
            b3 = fileInput.readUnsignedByte();
            b4 = fileInput.readUnsignedByte();
            int sizeData = (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
            
            p = new Trace(sizeData); // Start with safe allocation, will update later
            p.setNum(traceNum);
            fileInput.seek(beginPos + 12);
            //b1 = fileInput.readUnsignedByte();
            type = fileInput.readUnsignedByte();
            
            // Determine sample size based on type
            int sampleSize = 4; // default
            if (type == 1) {
                sampleSize = 2; // 16-bit fixed point
            } else if (type == 4) {
                sampleSize = 4; // 32-bit float
            } else if (type == 2) {
                sampleSize = 4; // 32-bit fixed point
            } else if (type == 10) {
                sampleSize = 2; // Assuming 16-bit
            } else if (type == 3) {
                 sampleSize = 20; // 20-bit, tricky, let's assume not used or handle later
             }

            int numSamples = sizeData / sampleSize;
            p = new Trace(numSamples);
            p.setNum(traceNum);

            System.out.println("Trace " + traceNum + ": type=" + type + ", sizeHeader=" + sizeHeader + 
                             ", sizeData=" + sizeData + ", sampleSize=" + sampleSize + ", numSamples=" + numSamples);

            //read header into a string contained in the Trace object
            // Header text starts after: 2(X1) + 2(header size) + 4(data size) + 2(sample size) + 1(format) = 11 bytes
            long headerTextStart = beginPos + 11;
            fileInput.seek(headerTextStart);
            System.out.println("  Reading header from 0x" + Long.toHexString(headerTextStart) + " for " + sizeHeader + " bytes");
            for (int i = 0; i < sizeHeader; i++) {
                b1 = fileInput.readByte();
                char c = (char) b1;
                //System.out.println(c);
                p.sb.append(c);
            }

            //decode the header string
            p.buildMetadata();
            System.out.println(p.shotLocation);
            double somma=0.0;
            sommaabs=0.0;
            //read in the data
            // Data starts after header text
            long dataStart = beginPos + 11 + sizeHeader;
            fileInput.seek(dataStart);
            System.out.println("  Reading data from 0x" + Long.toHexString(dataStart) + " for " + numSamples + " samples");
            for (int i = 0; i < numSamples; i++) {
                if (type == 4) {
                    b1 = fileInput.readUnsignedByte();
                    b2 = fileInput.readUnsignedByte();
                    b3 = fileInput.readUnsignedByte();
                    b4 = fileInput.readUnsignedByte();
                    int res = (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
                    double f = Float.intBitsToFloat(res);
                    somma=somma+f; 
                    sommaabs=sommaabs+Math.abs(f);
                    //keep track of largest value for normalization later
                    if (f > p.getMaxValue()) {
                        p.setMaxValue(f);
                    }
                    p.set(i, f);
                } else if (type == 1) {
                    b1 = fileInput.readUnsignedByte();
                    b2 = fileInput.readUnsignedByte();
                     short res = (short) ((b2 << 8) | b1);
                    short f = res;
                    //keep track of largest value for normalization later
                    somma=somma+f; 
                    sommaabs=sommaabs+Math.abs(f);
                    if (f > p.getMaxValue()) {
                        p.setMaxValue(f);
                    }
                    p.set(i, f);
                } else if (type == 10) {
                     b1 = fileInput.readUnsignedByte();
                    b2 = fileInput.readUnsignedByte();
                     short res = (short) ((b2 << 8) | b1);
                    short f = res;
                    //keep track of largest value for normalization later
                    somma=somma+f;
                    sommaabs=sommaabs+Math.abs(f);
                    if (f > p.getMaxValue()) {
                        p.setMaxValue(f);
                    }
                    p.set(i, f);
                  
                }
                else if (type == 2) {
                    b1 = fileInput.readUnsignedByte();
                    b2 = fileInput.readUnsignedByte();
                    b3 = fileInput.readUnsignedByte();
                    b4 = fileInput.readUnsignedByte();
                    int res = (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
                     int f = res;
                    somma=somma+f;
                    sommaabs=sommaabs+Math.abs(f);
                    
                    //keep track of largest value for normalization later
                    if (f > p.getMaxValue()) {
                        p.setMaxValue(f);
                    }
                    p.set(i, f);
                }
            }
            p.setmedia(somma, numSamples);
            p.setMediaAbs(sommaabs, numSamples);

        } catch (java.nio.BufferUnderflowException bue) {
            System.err.println("BufferUnderflowException in trace " + traceNum + ": " + bue);
            bue.printStackTrace();
        } catch (EOFException eof) {
            System.err.println("EOFException in trace " + traceNum + ": " + eof);
        } catch (Exception e) {
            System.err.println("Exception in trace " + traceNum + ": " + e);
            e.printStackTrace();
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
            fileInput.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
