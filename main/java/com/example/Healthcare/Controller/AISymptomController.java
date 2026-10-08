
package com.example.Healthcare.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AISymptomController {

    @PostMapping("/symptoms")
    public ResponseEntity<?> checkSymptoms(
            @RequestBody Map<String, String> request) {

        String symptoms = request.get("symptoms");

        if (symptoms == null || symptoms.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Please enter your symptoms."
                    ));
        }

        String text = symptoms.toLowerCase();

        String department = "General Medicine";
        String urgency = "Normal";

        if (text.contains("chest pain")
                || text.contains("breathing difficulty")
                || text.contains("shortness of breath")) {

            department = "Cardiology";
            urgency = "High";

        } else if (text.contains("headache")
                || text.contains("migraine")
                || text.contains("dizziness")) {

            department = "Neurology";
            urgency = "Moderate";

        } else if (text.contains("stomach")
                || text.contains("abdominal pain")
                || text.contains("vomiting")
                || text.contains("diarrhea")) {

            department = "Gastroenterology";
            urgency = "Moderate";

        } else if (text.contains("skin")
                || text.contains("rash")
                || text.contains("itching")) {

            department = "Dermatology";
            urgency = "Normal";

        } else if (text.contains("joint pain")
                || text.contains("bone pain")
                || text.contains("back pain")) {

            department = "Orthopedics";
            urgency = "Moderate";

        } else if (text.contains("fever")
                || text.contains("cold")
                || text.contains("cough")
                || text.contains("sore throat")) {

            department = "General Medicine";
            urgency = "Normal";
        }

        Map<String, String> response = new HashMap<>();

        response.put("department", department);
        response.put("urgency", urgency);

        return ResponseEntity.ok(response);
    }
}

