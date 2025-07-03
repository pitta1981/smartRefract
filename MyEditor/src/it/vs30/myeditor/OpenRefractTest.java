/*
 * Test class for OpenRefract format functionality
 * This demonstrates how to use the new JSON-based .orefract format
 */
package it.vs30.myeditor;

import java.io.File;
import java.io.IOException;
import org.myorg.myapi.APIObject;

/**
 * Utility class for testing OpenRefract format
 * 
 * @author SmartRefract Team
 */
public class OpenRefractTest {
    
    /**
     * Tests saving and loading a project in OpenRefract format
     */
    public static void testOpenRefractFormat() {
        try {
            // Create a test project
            APIObject testProject = createTestProject();
            
            // Save in OpenRefract format
            File testFile = new File("test_project.orefract");
            OpenRefractWriter.saveOpenRefractProject(testFile, testProject);
            System.out.println("Test project saved to: " + testFile.getAbsolutePath());
            
            // Load the project back
            APIObject loadedProject = OpenRefractLoader.loadOpenRefractProject(testFile);
            System.out.println("Test project loaded successfully!");
            
            // Verify some basic properties
            System.out.println("Original trace groups: " + testProject.TraceGroup.size());
            System.out.println("Loaded trace groups: " + loadedProject.TraceGroup.size());
            System.out.println("Original trace index: " + testProject.trace_index);
            System.out.println("Loaded trace index: " + loadedProject.trace_index);
            
            // Don't delete the test file so we can inspect it
            // testFile.delete();
            System.out.println("Test file preserved at: " + testFile.getAbsolutePath());
            
        } catch (IOException e) {
            System.err.println("Error testing OpenRefract format: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a comprehensive test project with seismic data for testing purposes
     */
    private static APIObject createTestProject() {
        APIObject project = new APIObject();
        
        // Set basic properties
        project.is_white = false;
        project.proporz = true;
        project.trace_index = 0;
        project.FORMAT = 1;
        
        // Create investigation
        project.proj = new org.myorg.myapi.Indagine();
        project.proj.index = 1;
        project.proj.xy = 10;
        project.proj.xy2 = 20;
        // Note: licenza, max3, maxR3 types need to be checked in actual Indagine class
        project.proj.max3 = 150;
        project.proj.maxR3 = 300;
        
        // Create trace group with actual seismic data
        project.TraceGroup = new java.util.ArrayList<>();
        
        // Create FirstBrakeList with test traces
        org.myorg.myapi.FirstBrakeList fbl = new org.myorg.myapi.FirstBrakeList();
        fbl.ch = 4; // 4 channels
        fbl.spaz = 2.5; // 2.5m spacing
        fbl.spaz_in = 1.0;
        fbl.primo = 0.0;
        fbl.scoppio = 6.0; // Shot at 6m
        fbl.AR = 1;
        fbl.xsc = 0.0;
        fbl.tAB = 0.1;
        fbl.fbp = "test_seismic_data.sgy";
        
        // Initialize dromo arrays
        fbl.dromo = new org.myorg.myapi.JRetta[3];
        for (int i = 0; i < 3; i++) {
            fbl.dromo[i] = new org.myorg.myapi.JRetta();
        }
        
        fbl.dromoR = new org.myorg.myapi.JRetta[3];
        for (int i = 0; i < 3; i++) {
            fbl.dromoR[i] = new org.myorg.myapi.JRetta();
        }
        
        // Create test traces with realistic seismic data
        fbl.tr = new org.myorg.myapi.Trace[4];
        for (int i = 0; i < 4; i++) {
            fbl.tr[i] = new org.myorg.myapi.Trace(500); // 500 samples per trace
            fbl.tr[i].number = i;
            fbl.tr[i].sampleInterval = 0.0005; // 0.5ms sample interval
            fbl.tr[i].media = 0.0;
            
            // Create realistic seismic data: arrival wave with different travel times
            double arrivalTime = 0.015 + i * 0.003; // Arrival gets later with distance
            for (int j = 0; j < 500; j++) {
                double time = j * 0.0005;
                double amplitude = 0.0;
                
                // Add first arrival if past arrival time
                if (time >= arrivalTime) {
                    double relTime = time - arrivalTime;
                    // Simple decaying sine wave instead of Ricker wavelet to avoid infinity
                    if (relTime < 0.1) { // Limit to prevent infinity
                        amplitude = Math.sin(2 * Math.PI * 50 * relTime) * Math.exp(-relTime * 10);
                    }
                }
                
                // Add some background noise
                amplitude += 0.02 * (Math.random() - 0.5);
                
                // Ensure finite values
                if (!Double.isFinite(amplitude)) {
                    amplitude = 0.0;
                }
                
                fbl.tr[i].set(j, amplitude);
            }
            
            // Set pick at arrival time
            fbl.tr[i].setPick(arrivalTime);
        }
        
        // Create first breaks matching the picks
        fbl.setChanel(4);
        for (int i = 0; i < 4; i++) {
            fbl.fb[i].chan = i;
            fbl.fb[i].time = fbl.tr[i].getPick() * 1000; // Convert to milliseconds
            fbl.fb[i].posx = i * 2.5; // Position based on spacing
            fbl.fb[i].enabled = true;
            fbl.fb[i].ar = 1;
            fbl.fb[i].layer = 1;
            fbl.fb[i].z = 0.0;
            fbl.fb[i].offset = i * 2.5; // Same as position to avoid infinity
        }
        
        project.TraceGroup.add(fbl);
        project.fb = fbl;
        project.tr = fbl.tr;
        
        System.out.println("Created test project with " + fbl.tr.length + " traces containing realistic seismic data");
        return project;
    }
    
    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        System.out.println("Testing OpenRefract format...");
        testOpenRefractFormat();
        System.out.println("OpenRefract format test completed.");
    }
}
