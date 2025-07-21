package com.sujeetk.bmicalculatorjava;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * BMI Calculator App
 * Calculates BMI based on user input and gives gender-specific interpretation.
 */
public class MainActivity extends AppCompatActivity {

    // UI components
    private EditText ageInput, heightFeetInput, heightInchInput, weightInput;
    private RadioGroup genderGroup;
    private TextView resultText;
    private Button calculateButton;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState previous state if app was restored
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Full screen layout
        setContentView(R.layout.activity_main);

        // Adjust padding for system bars (status/nav)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI references
        genderGroup = findViewById(R.id.genderGroup);
        ageInput = findViewById(R.id.ageInput);
        heightFeetInput = findViewById(R.id.heightFeetInput);
        heightInchInput = findViewById(R.id.heightInchInput);
        weightInput = findViewById(R.id.weightInput);
        resultText = findViewById(R.id.resultText);
        calculateButton = findViewById(R.id.calculateButton);

        // Set button click listener
        calculateButton.setOnClickListener(v -> calculateBMI());
    }

    /**
     * Reads user inputs, validates them, calculates BMI,
     * and displays personalized interpretation based on gender.
     */
    private void calculateBMI() {
        // Get string inputs
        String ageStr = ageInput.getText().toString().trim();
        String feetStr = heightFeetInput.getText().toString().trim();
        String inchStr = heightInchInput.getText().toString().trim();
        String weightStr = weightInput.getText().toString().trim();

        // Input validation
        if (ageStr.isEmpty() || feetStr.isEmpty() || inchStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate gender selection
        int selectedGenderId = genderGroup.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert values
        int age = Integer.parseInt(ageStr);
        int feet = Integer.parseInt(feetStr);
        int inches = Integer.parseInt(inchStr);
        float weight = Float.parseFloat(weightStr);

        // Convert height to meters
        int totalInches = (feet * 12) + inches;
        float heightInMeters = totalInches * 0.0254f;

        // Calculate BMI
        float bmi = weight / (heightInMeters * heightInMeters);

        // Get gender string
        RadioButton selectedGender = findViewById(selectedGenderId);
        String gender = selectedGender.getText().toString();

        // Show result
        String category = getBMICategory(bmi, gender);
        String result = String.format("BMI: %.1f\n%s", bmi, category);
        resultText.setText(result);
    }

    /**
     * Returns health interpretation based on BMI and gender.
     *
     * @param bmi    Calculated BMI value
     * @param gender "Male" or "Female"
     * @return Interpretation message
     */
    private String getBMICategory(float bmi, String gender) {
        if (bmi < 18.5f) {
            return gender.equals("Female")
                    ? "You are underweight. Consider iron-rich foods and vitamin checks."
                    : "You are underweight. Try adding more protein and calories.";
        } else if (bmi < 24.9f) {
            return gender.equals("Female")
                    ? "You are a healthy weight. Keep it up with yoga or walks."
                    : "You are a healthy weight. Maintain it with regular workouts.";
        } else if (bmi < 29.9f) {
            return gender.equals("Female")
                    ? "You are overweight. Focus on balanced meals and daily activity."
                    : "You are overweight. Try strength training and clean eating.";
        } else {
            return gender.equals("Female")
                    ? "You are obese. Consider professional guidance and dietary control."
                    : "You are obese. Adopt a workout routine and avoid processed food.";
        }
    }
}
