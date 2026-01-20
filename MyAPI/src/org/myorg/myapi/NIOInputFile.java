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
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.util.Log;

/**
 * SEG2 input reader using NIO.
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
                fCH.position(0);
            } catch (IOException ex) {
                Logger.getLogger(InputFileSg2.class.getName()).log(Level.SEVERE, null, ex);
            }
            ByteBuffer bb = ByteBuffer.allocate(2);
            fCH.read(bb);
            bb.flip();
            int b1 = bb.get() & 0xFF;
            int b2 = bb.get() & 0xFF;
            if (((b2 << 8) + b1) != 0x3a55) {
                throw new java.util.zip.DataFormatException();
            }

            // find number of traces in file
            fCH.position(6);
            bb.clear();
            fCH.read(bb);
            bb.flip();
            b1 = bb.get() & 0xFF;
            b2 = bb.get() & 0xFF;
            number = (b2 << 8) + b1;
            pointer = new int[number];
            Log.warn("Number of traces: " + number);

            // data format code (global)
            fCH.position(12);
            bb.clear();
            fCH.read(bb);
            bb.flip();
            b1 = bb.get() & 0xFF;
            type = b1;
            Log.warn("Global data format type: " + type);

            // find beginning of each trace
            bb = ByteBuffer.allocate(number * 4);
            fCH.position(32);
            fCH.read(bb);
            bb.flip();
            System.out.println("Reading trace pointers starting at offset 32:");
            for (int i = 0; i < number; i++) {
                int p1 = bb.get() & 0xFF;
                int p2 = bb.get() & 0xFF;
                int p3 = bb.get() & 0xFF;
                int p4 = bb.get() & 0xFF;
                int res = (p4 << 24) | (p3 << 16) | (p2 << 8) | p1;
                pointer[i] = res;
                System.out.println("  Trace " + i + ": pointer = 0x" + Integer.toHexString(pointer[i]) + " (" + pointer[i] + ")");
            }

        } catch (FileNotFoundException fnf) {
            System.err.println(fnf);
        } catch (EOFException eof) {
            System.out.println("End of File");
        } catch (IOException e) {
            System.out.println("IO error: " + e);
        }
        System.out.println("NIOInputFile constructor completed successfully with " + number + " traces");
    }

    public int byte2UnsignedInt(byte b) {
        return b & 0xFF;
    }

    Trace getTrace(int traceNum) {
        Trace p = new Trace(0);
        try {
            System.out.println("=== Starting to read trace " + traceNum + " ===");
            long beginPos = pointer[traceNum];
            System.out.println("Trace pointer: 0x" + Long.toHexString(beginPos) + " (" + beginPos + ")");

            // Trace layout observed:
            // +0: 2 bytes trace ID/unused
            // +2: 2 bytes header size
            // +4: 4 bytes data size (bytes)
            // +8: 4 bytes num samples
            // +12:1 byte type (data format)
            // +13..15: padding/reserved
            // +16: header text (length = headerSize - 16)

            fileInput.seek(beginPos);
            int b1 = fileInput.readUnsignedByte();
            int b2 = fileInput.readUnsignedByte();
            int traceId = (b2 << 8) | b1;

            fileInput.seek(beginPos + 2);
            b1 = fileInput.readUnsignedByte();
            b2 = fileInput.readUnsignedByte();
            int sizeHeader = (b2 << 8) + b1;

            fileInput.seek(beginPos + 4);
            b1 = fileInput.readUnsignedByte();
            b2 = fileInput.readUnsignedByte();
            int b3 = fileInput.readUnsignedByte();
            int b4 = fileInput.readUnsignedByte();
            int sizeData = (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;

            fileInput.seek(beginPos + 8);
            b1 = fileInput.readUnsignedByte();
            b2 = fileInput.readUnsignedByte();
            b3 = fileInput.readUnsignedByte();
            b4 = fileInput.readUnsignedByte();
            int numSamples = (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;

            fileInput.seek(beginPos + 12);
            type = fileInput.readUnsignedByte();

            int sampleSize = (numSamples > 0) ? (sizeData / numSamples) : 0;
            if (sampleSize == 0) {
                sampleSize = 2; // fallback
            }

            p = new Trace(numSamples);
            p.setNum(traceNum);

            System.out.println("Trace " + traceNum + ": traceId=" + traceId + ", type=" + type +
                             ", sizeHeader=" + sizeHeader + ", sizeData=" + sizeData +
                             ", numSamples=" + numSamples + ", sampleSize=" + sampleSize);

            int headerTextLen = Math.max(0, sizeHeader - 16);
            long headerTextStart = beginPos + 16;
            fileInput.seek(headerTextStart);
            System.out.println("  Reading header from 0x" + Long.toHexString(headerTextStart) + " for " + headerTextLen + " bytes");
            for (int i = 0; i < headerTextLen; i++) {
                b1 = fileInput.readByte();
                char c = (char) b1;
                p.sb.append(c);
            }

            p.buildMetadata();
            System.out.println(p.shotLocation);
            double somma = 0.0;
            sommaabs = 0.0;

            long dataStart = beginPos + sizeHeader;
            System.out.println("  Reading data from 0x" + Long.toHexString(dataStart) + " for " + numSamples + " samples");

            ByteBuffer dataBuf = ByteBuffer.allocate(sizeData);
            dataBuf.order(ByteOrder.LITTLE_ENDIAN);
            fCH.position(dataStart);
            fCH.read(dataBuf);
            dataBuf.flip();

            for (int i = 0; i < numSamples; i++) {
                double val = 0;
                if (type == 4) { // 32-bit float
                    val = dataBuf.getFloat();
                } else if (type == 1 || type == 10) { // 16-bit signed
                    val = dataBuf.getShort();
                } else if (type == 2) { // 32-bit signed
                    val = dataBuf.getInt();
                } else {
                    // fallback: try sampleSize
                    if (sampleSize == 2) {
                        val = dataBuf.getShort();
                    } else if (sampleSize == 4) {
                        val = dataBuf.getInt();
                    } else {
                        val = dataBuf.get();
                    }
                }

                somma += val;
                sommaabs += Math.abs(val);
                if (val > p.getMaxValue()) {
                    p.setMaxValue(val);
                }
                p.set(i, val);
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
