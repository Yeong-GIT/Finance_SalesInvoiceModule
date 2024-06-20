package gityeong.SalesInvoice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api")
public class TriggerScriptController {

    @GetMapping("/generate-data")
    public String triggerPythonScript() {
        try {
            // Log the execution attempt
            System.out.println("Attempting to run Python script...");
            // Update the path if necessary
            ProcessBuilder pb = new ProcessBuilder("python3", "/app/scripts/generateSalesInvoices.py");
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = in.readLine()) != null) {
                output.append(line).append("\n");
            }
            // Log the output
            System.out.println("Python script output: " + output.toString());
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
