/*
 * Simple test to verify seismic data preservation in OpenRefract format
 */
package it.vs30.myeditor;

import java.io.File;
import org.myorg.myapi.*;

/**
 * Test class to verify that seismic trace data is properly preserved
 */
public class TestSeismicDataPreservation {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Testing Seismic Data Preservation ===");
            
            // Create a simple project with known seismic data
            APIObject originalProject = createSimpleTestProject();
            
            // Save and load
            File tempFile = new File("seismic_test.orefract");
            tempFile.deleteOnExit();
            
            try {
                OpenRefractWriter.saveOpenRefractProject(tempFile, originalProject);
                System.out.println("Project saved successfully");
            } catch (Exception e) {
                System.err.println("Failed to save project: " + e.getMessage());
                return;
            }
            
            APIObject loadedProject = OpenRefractLoader.loadOpenRefractProject(tempFile);
            if (loadedProject == null) {
                System.err.println("Failed to load project");
                return;
            }
            
            // Verify data preservation
            verifySeismicData(originalProject, loadedProject);
            
            // Clean up
            tempFile.delete();
            
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static APIObject createSimpleTestProject() {
        APIObject project = new APIObject();
        project.TraceGroup = new java.util.ArrayList<>();
        
        // Create a simple trace group
        FirstBrakeList fbl = new FirstBrakeList();
        fbl.ch = 2;
        fbl.spaz = 2.0;
        fbl.spaz_in = 1.0;
        fbl.scoppio = 4.0;
        
        // Create simple traces with known data
        fbl.tr = new org.myorg.myapi.Trace[2];
        for (int i = 0; i < 2; i++) {
            fbl.tr[i] = new org.myorg.myapi.Trace(10); // 10 samples
            fbl.tr[i].number = i;
            fbl.tr[i].sampleInterval = 0.001;
            
            // Fill with simple test data
            for (int j = 0; j < 10; j++) {
                double value = (i + 1) * 100 + j; // Simple pattern: 100,101,102... or 200,201,202...
                fbl.tr[i].set(j, value);
            }
            
            // Set a test pick
            fbl.tr[i].setPick(0.005 * (i + 1));
        }
        
        // Initialize dromo arrays
        fbl.dromo = new JRetta[3];
        fbl.dromoR = new JRetta[3];
        for (int i = 0; i < 3; i++) {
            fbl.dromo[i] = new JRetta();
            fbl.dromoR[i] = new JRetta();
        }
        
        // Create first breaks
        fbl.setChanel(2);
        for (int i = 0; i < 2; i++) {
            fbl.fb[i].chan = i;
            fbl.fb[i].time = fbl.tr[i].getPick() * 1000;
            fbl.fb[i].posx = i * 2.0;
            fbl.fb[i].offset = i * 2.0;
            fbl.fb[i].enabled = true;
        }
        
        project.TraceGroup.add(fbl);
        project.fb = fbl;
        project.tr = fbl.tr;
        
        System.out.println("Created test project with 2 traces containing known data patterns");
        return project;
    }
    
    private static void verifySeismicData(APIObject original, APIObject loaded) {
        System.out.println("\n=== Verifying Seismic Data ===");
        
        if (loaded.TraceGroup == null || loaded.TraceGroup.size() == 0) {
            System.err.println("✗ No trace groups loaded");
            return;
        }
        
        FirstBrakeList originalFbl = original.TraceGroup.get(0);
        FirstBrakeList loadedFbl = loaded.TraceGroup.get(0);
        
        if (loadedFbl.tr == null) {
            System.err.println("✗ No traces loaded");
            return;
        }
        
        if (originalFbl.tr.length != loadedFbl.tr.length) {
            System.err.println("✗ Trace count mismatch: " + originalFbl.tr.length + " vs " + loadedFbl.tr.length);
            return;
        }
        
        System.out.println("✓ Trace count preserved: " + loadedFbl.tr.length);
        
        // Check each trace
        for (int i = 0; i < originalFbl.tr.length; i++) {
            org.myorg.myapi.Trace originalTrace = originalFbl.tr[i];
            org.myorg.myapi.Trace loadedTrace = loadedFbl.tr[i];
            
            System.out.println("\nTesting trace " + i + ":");
            
            // Check sample interval
            if (Math.abs(originalTrace.sampleInterval - loadedTrace.sampleInterval) < 1e-6) {
                System.out.println("  ✓ Sample interval preserved: " + loadedTrace.sampleInterval);
            } else {
                System.err.println("  ✗ Sample interval mismatch: " + originalTrace.sampleInterval + " vs " + loadedTrace.sampleInterval);
            }
            
            // Check pick
            if (Math.abs(originalTrace.getPick() - loadedTrace.getPick()) < 1e-6) {
                System.out.println("  ✓ Pick preserved: " + loadedTrace.getPick());
            } else {
                System.err.println("  ✗ Pick mismatch: " + originalTrace.getPick() + " vs " + loadedTrace.getPick());
            }
            
            // Check seismic data values
            if (originalTrace.value != null && loadedTrace.value != null) {
                if (originalTrace.value.length == loadedTrace.value.length) {
                    boolean dataMatches = true;
                    for (int j = 0; j < originalTrace.value.length; j++) {
                        if (Math.abs(originalTrace.value[j] - loadedTrace.value[j]) > 1e-10) {
                            System.err.println("    ✗ Sample " + j + " mismatch: " + originalTrace.value[j] + " vs " + loadedTrace.value[j]);
                            dataMatches = false;
                        }
                    }
                    
                    if (dataMatches) {
                        System.out.println("  ✓ All " + originalTrace.value.length + " seismic data samples preserved exactly");
                        
                        // Show first few values as proof
                        System.out.print("    First 5 values: ");
                        for (int j = 0; j < Math.min(5, loadedTrace.value.length); j++) {
                            System.out.print(loadedTrace.value[j] + " ");
                        }
                        System.out.println();
                        
                    } else {
                        System.err.println("  ✗ Seismic data contains errors");
                    }
                } else {
                    System.err.println("  ✗ Data length mismatch: " + originalTrace.value.length + " vs " + loadedTrace.value.length);
                }
            } else {
                System.err.println("  ✗ Seismic data arrays are null");
            }
        }
        
        // Check first breaks
        if (originalFbl.fb != null && loadedFbl.fb != null) {
            if (originalFbl.fb.length == loadedFbl.fb.length) {
                System.out.println("\n✓ First break count preserved: " + loadedFbl.fb.length);
                
                for (int i = 0; i < originalFbl.fb.length; i++) {
                    if (Math.abs(originalFbl.fb[i].time - loadedFbl.fb[i].time) < 1e-3) {
                        System.out.println("  ✓ First break " + i + " time: " + loadedFbl.fb[i].time + "ms");
                    } else {
                        System.err.println("  ✗ First break " + i + " time mismatch: " + originalFbl.fb[i].time + " vs " + loadedFbl.fb[i].time);
                    }
                }
            }
        }
        
        System.out.println("\n=== Verification Complete ===");
    }
}
