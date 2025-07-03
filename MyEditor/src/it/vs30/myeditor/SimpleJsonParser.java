/*
 * Simple JSON Parser for OpenRefract format
 * This is a lightweight JSON parser specifically designed for SmartRefract
 * to avoid external dependencies.
 */
package it.vs30.myeditor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple JSON parser implementation for OpenRefract format
 * 
 * @author SmartRefract Team
 */
public class SimpleJsonParser {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Parses JSON string into OpenRefractProject
     */
    public static OpenRefractProject parseProject(String json) throws Exception {
        Map<String, Object> root = parseObject(json.trim());
        return mapToProject(root);
    }
    
    /**
     * Converts project to JSON string
     */
    public static String projectToJson(OpenRefractProject project) {
        StringBuilder sb = new StringBuilder();
        writeProject(sb, project, 0);
        return sb.toString();
    }
    
    // Parsing methods
    private static Map<String, Object> parseObject(String json) throws Exception {
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new Exception("Invalid JSON object");
        }
        
        Map<String, Object> result = new HashMap<>();
        String content = json.substring(1, json.length() - 1).trim();
        
        if (content.isEmpty()) {
            return result;
        }
        
        List<String> tokens = tokenize(content);
        
        for (int i = 0; i < tokens.size(); i += 3) {
            if (i + 2 >= tokens.size()) break;
            
            String key = unquote(tokens.get(i));
            String colon = tokens.get(i + 1);
            String valueToken = tokens.get(i + 2);
            
            if (!":".equals(colon)) {
                throw new Exception("Expected ':' after key: " + key);
            }
            
            Object value = parseValue(valueToken);
            result.put(key, value);
        }
        
        return result;
    }
    
    private static List<Object> parseArray(String json) throws Exception {
        json = json.trim();
        if (!json.startsWith("[") || !json.endsWith("]")) {
            throw new Exception("Invalid JSON array");
        }
        
        List<Object> result = new ArrayList<>();
        String content = json.substring(1, json.length() - 1).trim();
        
        if (content.isEmpty()) {
            return result;
        }
        
        List<String> tokens = tokenizeArray(content);
        for (String token : tokens) {
            result.add(parseValue(token));
        }
        
        return result;
    }
    
    private static Object parseValue(String token) throws Exception {
        token = token.trim();
        
        if (token.equals("null")) {
            return null;
        } else if (token.equals("true")) {
            return true;
        } else if (token.equals("false")) {
            return false;
        } else if (token.startsWith("\"") && token.endsWith("\"")) {
            return unquote(token);
        } else if (token.startsWith("{")) {
            return parseObject(token);
        } else if (token.startsWith("[")) {
            return parseArray(token);
        } else {
            // Try to parse as number
            try {
                if (token.contains(".")) {
                    return Double.parseDouble(token);
                } else {
                    return Integer.parseInt(token);
                }
            } catch (NumberFormatException e) {
                throw new Exception("Invalid value: " + token);
            }
        }
    }
    
    private static List<String> tokenize(String content) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inString = false;
        boolean escaped = false;
        int braceDepth = 0;
        int bracketDepth = 0;
        
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            
            if (escaped) {
                current.append(c);
                escaped = false;
                continue;
            }
            
            if (c == '\\' && inString) {
                escaped = true;
                current.append(c);
                continue;
            }
            
            if (c == '"') {
                inString = !inString;
                current.append(c);
                continue;
            }
            
            if (inString) {
                current.append(c);
                continue;
            }
            
            if (c == '{') {
                braceDepth++;
                current.append(c);
            } else if (c == '}') {
                braceDepth--;
                current.append(c);
            } else if (c == '[') {
                bracketDepth++;
                current.append(c);
            } else if (c == ']') {
                bracketDepth--;
                current.append(c);
            } else if (c == ',' && braceDepth == 0 && bracketDepth == 0) {
                if (current.length() > 0) {
                    tokens.add(current.toString().trim());
                    current = new StringBuilder();
                }
            } else if (c == ':' && braceDepth == 0 && bracketDepth == 0) {
                if (current.length() > 0) {
                    tokens.add(current.toString().trim());
                    tokens.add(":");
                    current = new StringBuilder();
                }
            } else {
                current.append(c);
            }
        }
        
        if (current.length() > 0) {
            tokens.add(current.toString().trim());
        }
        
        return tokens;
    }
    
    private static List<String> tokenizeArray(String content) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inString = false;
        boolean escaped = false;
        int braceDepth = 0;
        int bracketDepth = 0;
        
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            
            if (escaped) {
                current.append(c);
                escaped = false;
                continue;
            }
            
            if (c == '\\' && inString) {
                escaped = true;
                current.append(c);
                continue;
            }
            
            if (c == '"') {
                inString = !inString;
                current.append(c);
                continue;
            }
            
            if (inString) {
                current.append(c);
                continue;
            }
            
            if (c == '{') {
                braceDepth++;
                current.append(c);
            } else if (c == '}') {
                braceDepth--;
                current.append(c);
            } else if (c == '[') {
                bracketDepth++;
                current.append(c);
            } else if (c == ']') {
                bracketDepth--;
                current.append(c);
            } else if (c == ',' && braceDepth == 0 && bracketDepth == 0) {
                if (current.length() > 0) {
                    tokens.add(current.toString().trim());
                    current = new StringBuilder();
                }
            } else {
                current.append(c);
            }
        }
        
        if (current.length() > 0) {
            tokens.add(current.toString().trim());
        }
        
        return tokens;
    }
    
    private static String unquote(String str) {
        if (str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }
    
    // Mapping methods
    private static OpenRefractProject mapToProject(Map<String, Object> root) throws Exception {
        OpenRefractProject project = new OpenRefractProject();
        
        project.version = getStringValue(root, "version", "1.0");
        project.formatName = getStringValue(root, "formatName", "OpenRefract");
        project.description = getStringValue(root, "description", "");
        
        // Parse dates
        String createdDateStr = getStringValue(root, "createdDate", null);
        if (createdDateStr != null) {
            try {
                project.createdDate = DATE_FORMAT.parse(createdDateStr);
            } catch (ParseException e) {
                project.createdDate = new Date();
            }
        } else {
            project.createdDate = new Date();
        }
        
        String modifiedDateStr = getStringValue(root, "modifiedDate", null);
        if (modifiedDateStr != null) {
            try {
                project.modifiedDate = DATE_FORMAT.parse(modifiedDateStr);
            } catch (ParseException e) {
                project.modifiedDate = new Date();
            }
        } else {
            project.modifiedDate = new Date();
        }
        
        project.traceIndex = getIntValue(root, "traceIndex", 0);
        project.format = getIntValue(root, "format", 0);
        
        // Parse display settings
        Map<String, Object> displayMap = getMapValue(root, "displaySettings");
        if (displayMap != null) {
            project.displaySettings = mapToDisplaySettings(displayMap);
        }
        
        // Parse trace groups
        List<Object> traceGroupsList = getListValue(root, "traceGroups");
        if (traceGroupsList != null) {
            project.traceGroups = new OpenRefractTraceGroup[traceGroupsList.size()];
            for (int i = 0; i < traceGroupsList.size(); i++) {
                @SuppressWarnings("unchecked")
                Map<String, Object> tgMap = (Map<String, Object>) traceGroupsList.get(i);
                project.traceGroups[i] = mapToTraceGroup(tgMap);
            }
        }
        
        // Parse investigation
        Map<String, Object> invMap = getMapValue(root, "investigation");
        if (invMap != null) {
            project.investigation = mapToInvestigation(invMap);
        }
        
        return project;
    }
    
    private static OpenRefractDisplaySettings mapToDisplaySettings(Map<String, Object> map) {
        OpenRefractDisplaySettings settings = new OpenRefractDisplaySettings();
        settings.isWhite = getBooleanValue(map, "isWhite", false);
        settings.proporz = getBooleanValue(map, "proporz", false);
        settings.selectedTab = getIntValue(map, "selectedTab", 0);
        return settings;
    }
    
    private static OpenRefractTraceGroup mapToTraceGroup(Map<String, Object> map) throws Exception {
        OpenRefractTraceGroup tg = new OpenRefractTraceGroup();
        
        tg.channelCount = getIntValue(map, "channelCount", 0);
        tg.spacing = getDoubleValue(map, "spacing", 0.0);
        tg.spacingIn = getDoubleValue(map, "spacingIn", 0.0);
        tg.primo = getDoubleValue(map, "primo", 0.0);
        tg.shotLocation = getDoubleValue(map, "shotLocation", 0.0);
        tg.AR = getIntValue(map, "AR", 0);
        tg.xsc = getDoubleValue(map, "xsc", 999.0);
        tg.tAB = getDoubleValue(map, "tAB", 999.0);
        tg.filePath = getStringValue(map, "filePath", "");
        
        tg.strato1 = getStringValue(map, "strato1", "0-0");
        tg.strato2 = getStringValue(map, "strato2", "0-0");
        tg.strato3 = getStringValue(map, "strato3", "0-0");
        tg.strato1R = getStringValue(map, "strato1R", "0-0");
        tg.strato2R = getStringValue(map, "strato2R", "0-0");
        tg.strato3R = getStringValue(map, "strato3R", "0-0");
        
        // Parse first breaks
        List<Object> fbList = getListValue(map, "firstBreaks");
        if (fbList != null) {
            tg.firstBreaks = new OpenRefractFirstBreak[fbList.size()];
            for (int i = 0; i < fbList.size(); i++) {
                @SuppressWarnings("unchecked")
                Map<String, Object> fbMap = (Map<String, Object>) fbList.get(i);
                tg.firstBreaks[i] = mapToFirstBreak(fbMap);
            }
        }
        
        // Parse dromo
        List<Object> dromoList = getListValue(map, "dromo");
        if (dromoList != null) {
            tg.dromo = new OpenRefractLine[dromoList.size()];
            for (int i = 0; i < dromoList.size(); i++) {
                @SuppressWarnings("unchecked")
                Map<String, Object> lineMap = (Map<String, Object>) dromoList.get(i);
                tg.dromo[i] = mapToLine(lineMap);
            }
        }
        
        // Parse dromoR
        List<Object> dromoRList = getListValue(map, "dromoR");
        if (dromoRList != null) {
            tg.dromoR = new OpenRefractLine[dromoRList.size()];
            for (int i = 0; i < dromoRList.size(); i++) {
                @SuppressWarnings("unchecked")
                Map<String, Object> lineMap = (Map<String, Object>) dromoRList.get(i);
                tg.dromoR[i] = mapToLine(lineMap);
            }
        }
        
        // Parse traces
        List<Object> tracesList = getListValue(map, "traces");
        if (tracesList != null) {
            tg.traces = new OpenRefractTrace[tracesList.size()];
            for (int i = 0; i < tracesList.size(); i++) {
                @SuppressWarnings("unchecked")
                Map<String, Object> traceMap = (Map<String, Object>) tracesList.get(i);
                tg.traces[i] = mapToTrace(traceMap);
            }
        }
        
        // Parse zoom trace utils
        List<Object> zoomList = getListValue(map, "zoomTraceUtils");
        if (zoomList != null) {
            tg.zoomTraceUtils = new OpenRefractZoomTraceUtil[zoomList.size()];
            for (int i = 0; i < zoomList.size(); i++) {
                @SuppressWarnings("unchecked")
                Map<String, Object> zoomMap = (Map<String, Object>) zoomList.get(i);
                tg.zoomTraceUtils[i] = mapToZoomTraceUtil(zoomMap);
            }
        }
        
        return tg;
    }
    
    private static OpenRefractFirstBreak mapToFirstBreak(Map<String, Object> map) {
        OpenRefractFirstBreak fb = new OpenRefractFirstBreak();
        fb.channel = getIntValue(map, "channel", 0);
        fb.ar = getIntValue(map, "ar", 0);
        fb.layer = getIntValue(map, "layer", 0);
        fb.time = getDoubleValue(map, "time", 0.0);
        fb.posX = getDoubleValue(map, "posX", 0.0);
        fb.z = getDoubleValue(map, "z", 0.0);
        fb.offset = getDoubleValue(map, "offset", 0.0);
        fb.enabled = getBooleanValue(map, "enabled", true);
        return fb;
    }
    
    private static OpenRefractLine mapToLine(Map<String, Object> map) {
        OpenRefractLine line = new OpenRefractLine();
        line.a = getDoubleValue(map, "a", 0.0);
        line.b = getDoubleValue(map, "b", 0.0);
        return line;
    }
    
    private static OpenRefractTrace mapToTrace(Map<String, Object> map) throws Exception {
        OpenRefractTrace trace = new OpenRefractTrace();
        trace.number = getIntValue(map, "number", 0);
        trace.length = getIntValue(map, "length", 0);
        trace.sampleInterval = getDoubleValue(map, "sampleInterval", 0.0);
        trace.media = getDoubleValue(map, "media", 0.0);
        trace.pick = getDoubleValue(map, "pick", 0.0);
        trace.isPicked = getBooleanValue(map, "isPicked", false);
        
        // Trace data is already stored as Base64 string
        trace.valueData = getStringValue(map, "valueData", "");
        
        return trace;
    }
    
    private static OpenRefractZoomTraceUtil mapToZoomTraceUtil(Map<String, Object> map) {
        OpenRefractZoomTraceUtil zoom = new OpenRefractZoomTraceUtil();
        zoom.zoomFactor = getDoubleValue(map, "zoomFactor", 1.0);
        zoom.isSelected = getBooleanValue(map, "isSelected", false);
        return zoom;
    }
    
    private static OpenRefractInvestigation mapToInvestigation(Map<String, Object> map) {
        OpenRefractInvestigation inv = new OpenRefractInvestigation();
        // Add investigation mapping as needed
        return inv;
    }
    
    // Writing methods
    private static void writeProject(StringBuilder sb, OpenRefractProject project, int indent) {
        sb.append("{\n");
        
        writeStringField(sb, "version", project.version, indent + 1, true);
        writeStringField(sb, "formatName", project.formatName, indent + 1, true);
        writeStringField(sb, "description", project.description, indent + 1, true);
        writeStringField(sb, "createdDate", DATE_FORMAT.format(project.createdDate), indent + 1, true);
        writeStringField(sb, "modifiedDate", DATE_FORMAT.format(project.modifiedDate), indent + 1, true);
        writeIntField(sb, "traceIndex", project.traceIndex, indent + 1, true);
        writeIntField(sb, "format", project.format, indent + 1, true);
        
        // Display settings
        if (project.displaySettings != null) {
            writeIndent(sb, indent + 1);
            sb.append("\"displaySettings\": ");
            writeDisplaySettings(sb, project.displaySettings, indent + 1);
            sb.append(",\n");
        }
        
        // Trace groups
        if (project.traceGroups != null) {
            writeIndent(sb, indent + 1);
            sb.append("\"traceGroups\": [\n");
            for (int i = 0; i < project.traceGroups.length; i++) {
                writeIndent(sb, indent + 2);
                writeTraceGroup(sb, project.traceGroups[i], indent + 2);
                if (i < project.traceGroups.length - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            writeIndent(sb, indent + 1);
            sb.append("],\n");
        }
        
        // Investigation
        if (project.investigation != null) {
            writeIndent(sb, indent + 1);
            sb.append("\"investigation\": ");
            writeInvestigation(sb, project.investigation, indent + 1);
            sb.append("\n");
        } else {
            // Remove trailing comma
            if (sb.charAt(sb.length() - 2) == ',') {
                sb.setLength(sb.length() - 2);
                sb.append("\n");
            }
        }
        
        writeIndent(sb, indent);
        sb.append("}");
    }
    
    private static void writeDisplaySettings(StringBuilder sb, OpenRefractDisplaySettings settings, int indent) {
        sb.append("{\n");
        writeBooleanField(sb, "isWhite", settings.isWhite, indent + 1, true);
        writeBooleanField(sb, "proporz", settings.proporz, indent + 1, true);
        writeIntField(sb, "selectedTab", settings.selectedTab, indent + 1, false);
        sb.append("\n");
        writeIndent(sb, indent);
        sb.append("}");
    }
    
    private static void writeTraceGroup(StringBuilder sb, OpenRefractTraceGroup tg, int indent) {
        sb.append("{\n");
        
        writeIntField(sb, "channelCount", tg.channelCount, indent + 1, true);
        writeDoubleField(sb, "spacing", tg.spacing, indent + 1, true);
        writeDoubleField(sb, "spacingIn", tg.spacingIn, indent + 1, true);
        writeDoubleField(sb, "primo", tg.primo, indent + 1, true);
        writeDoubleField(sb, "shotLocation", tg.shotLocation, indent + 1, true);
        writeIntField(sb, "AR", tg.AR, indent + 1, true);
        writeDoubleField(sb, "xsc", tg.xsc, indent + 1, true);
        writeDoubleField(sb, "tAB", tg.tAB, indent + 1, true);
        writeStringField(sb, "filePath", tg.filePath, indent + 1, true);
        
        writeStringField(sb, "strato1", tg.strato1, indent + 1, true);
        writeStringField(sb, "strato2", tg.strato2, indent + 1, true);
        writeStringField(sb, "strato3", tg.strato3, indent + 1, true);
        writeStringField(sb, "strato1R", tg.strato1R, indent + 1, true);
        writeStringField(sb, "strato2R", tg.strato2R, indent + 1, true);
        writeStringField(sb, "strato3R", tg.strato3R, indent + 1, true);
        
        // First breaks
        if (tg.firstBreaks != null) {
            writeIndent(sb, indent + 1);
            sb.append("\"firstBreaks\": [\n");
            for (int i = 0; i < tg.firstBreaks.length; i++) {
                writeIndent(sb, indent + 2);
                writeFirstBreak(sb, tg.firstBreaks[i], indent + 2);
                if (i < tg.firstBreaks.length - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            writeIndent(sb, indent + 1);
            sb.append("],\n");
        }
        
        // Dromo
        if (tg.dromo != null) {
            writeIndent(sb, indent + 1);
            sb.append("\"dromo\": [\n");
            for (int i = 0; i < tg.dromo.length; i++) {
                writeIndent(sb, indent + 2);
                writeLine(sb, tg.dromo[i], indent + 2);
                if (i < tg.dromo.length - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            writeIndent(sb, indent + 1);
            sb.append("],\n");
        }
        
        // DromoR
        if (tg.dromoR != null) {
            writeIndent(sb, indent + 1);
            sb.append("\"dromoR\": [\n");
            for (int i = 0; i < tg.dromoR.length; i++) {
                writeIndent(sb, indent + 2);
                writeLine(sb, tg.dromoR[i], indent + 2);
                if (i < tg.dromoR.length - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            writeIndent(sb, indent + 1);
            sb.append("],\n");
        }
        
        // Traces
        if (tg.traces != null) {
            writeIndent(sb, indent + 1);
            sb.append("\"traces\": [\n");
            for (int i = 0; i < tg.traces.length; i++) {
                writeIndent(sb, indent + 2);
                writeTrace(sb, tg.traces[i], indent + 2);
                if (i < tg.traces.length - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            writeIndent(sb, indent + 1);
            sb.append("],\n");
        }
        
        // Zoom trace utils
        if (tg.zoomTraceUtils != null) {
            writeIndent(sb, indent + 1);
            sb.append("\"zoomTraceUtils\": [\n");
            for (int i = 0; i < tg.zoomTraceUtils.length; i++) {
                writeIndent(sb, indent + 2);
                writeZoomTraceUtil(sb, tg.zoomTraceUtils[i], indent + 2);
                if (i < tg.zoomTraceUtils.length - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            writeIndent(sb, indent + 1);
            sb.append("]\n");
        } else {
            // Remove trailing comma
            if (sb.charAt(sb.length() - 2) == ',') {
                sb.setLength(sb.length() - 2);
                sb.append("\n");
            }
        }
        
        writeIndent(sb, indent);
        sb.append("}");
    }
    
    private static void writeFirstBreak(StringBuilder sb, OpenRefractFirstBreak fb, int indent) {
        sb.append("{\n");
        writeIntField(sb, "channel", fb.channel, indent + 1, true);
        writeIntField(sb, "ar", fb.ar, indent + 1, true);
        writeIntField(sb, "layer", fb.layer, indent + 1, true);
        writeDoubleField(sb, "time", fb.time, indent + 1, true);
        writeDoubleField(sb, "posX", fb.posX, indent + 1, true);
        writeDoubleField(sb, "z", fb.z, indent + 1, true);
        writeDoubleField(sb, "offset", fb.offset, indent + 1, true);
        writeBooleanField(sb, "enabled", fb.enabled, indent + 1, false);
        sb.append("\n");
        writeIndent(sb, indent);
        sb.append("}");
    }
    
    private static void writeLine(StringBuilder sb, OpenRefractLine line, int indent) {
        sb.append("{\n");
        writeDoubleField(sb, "a", line.a, indent + 1, true);
        writeDoubleField(sb, "b", line.b, indent + 1, false);
        sb.append("\n");
        writeIndent(sb, indent);
        sb.append("}");
    }
    
    private static void writeTrace(StringBuilder sb, OpenRefractTrace trace, int indent) {
        sb.append("{\n");
        writeIntField(sb, "number", trace.number, indent + 1, true);
        writeIntField(sb, "length", trace.length, indent + 1, true);
        writeDoubleField(sb, "sampleInterval", trace.sampleInterval, indent + 1, true);
        writeDoubleField(sb, "media", trace.media, indent + 1, true);
        writeDoubleField(sb, "pick", trace.pick, indent + 1, true);
        writeBooleanField(sb, "isPicked", trace.isPicked, indent + 1, true);
        
        // Trace data is already a Base64 string
        writeStringField(sb, "valueData", trace.valueData, indent + 1, false);
        
        sb.append("\n");
        writeIndent(sb, indent);
        sb.append("}");
    }
    
    private static void writeZoomTraceUtil(StringBuilder sb, OpenRefractZoomTraceUtil zoom, int indent) {
        sb.append("{\n");
        writeDoubleField(sb, "zoomFactor", zoom.zoomFactor, indent + 1, true);
        writeBooleanField(sb, "isSelected", zoom.isSelected, indent + 1, false);
        sb.append("\n");
        writeIndent(sb, indent);
        sb.append("}");
    }
    
    private static void writeInvestigation(StringBuilder sb, OpenRefractInvestigation inv, int indent) {
        sb.append("{\n");
        // Add investigation fields as needed
        writeIndent(sb, indent);
        sb.append("}");
    }
    
    // Helper writing methods
    private static void writeStringField(StringBuilder sb, String name, String value, int indent, boolean addComma) {
        writeIndent(sb, indent);
        sb.append("\"").append(name).append("\": \"").append(escapeString(value)).append("\"");
        if (addComma) {
            sb.append(",");
        }
        sb.append("\n");
    }
    
    private static void writeIntField(StringBuilder sb, String name, int value, int indent, boolean addComma) {
        writeIndent(sb, indent);
        sb.append("\"").append(name).append("\": ").append(value);
        if (addComma) {
            sb.append(",");
        }
        sb.append("\n");
    }
    
    private static void writeDoubleField(StringBuilder sb, String name, double value, int indent, boolean addComma) {
        writeIndent(sb, indent);
        sb.append("\"").append(name).append("\": ").append(value);
        if (addComma) {
            sb.append(",");
        }
        sb.append("\n");
    }
    
    private static void writeBooleanField(StringBuilder sb, String name, boolean value, int indent, boolean addComma) {
        writeIndent(sb, indent);
        sb.append("\"").append(name).append("\": ").append(value);
        if (addComma) {
            sb.append(",");
        }
        sb.append("\n");
    }
    
    private static void writeIndent(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
    }
    
    private static String escapeString(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }
    
    // Utility methods for type conversion
    private static String getStringValue(Map<String, Object> map, String key, String defaultValue) {
        Object value = map.get(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    private static int getIntValue(Map<String, Object> map, String key, int defaultValue) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }
    
    private static double getDoubleValue(Map<String, Object> map, String key, double defaultValue) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }
    
    private static boolean getBooleanValue(Map<String, Object> map, String key, boolean defaultValue) {
        Object value = map.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }
    
    @SuppressWarnings("unchecked")
    private static Map<String, Object> getMapValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private static List<Object> getListValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof List) {
            return (List<Object>) value;
        }
        return null;
    }
}
