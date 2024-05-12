package com.example.assignment2

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val signupButton = findViewById<Button>(R.id.signupButton)
        val agreeCheckBox = findViewById<CheckBox>(R.id.agreeCheckBox)

        signupButton.setOnClickListener {
            if (agreeCheckBox.isChecked) {
                val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
                val emailEditText = findViewById<EditText>(R.id.emailEditText)
                val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
                val rePasswordEditText = findViewById<EditText>(R.id.rePasswordEditText)

                val username = usernameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                val rePassword = rePasswordEditText.text.toString()

                // Check if username contains only alphanumeric characters
                if (!username.matches("[a-zA-Z0-9]+".toRegex())) {
                    usernameEditText.error = "Username should contain only letters and numbers"
                    return@setOnClickListener
                }

                // Check if email is valid
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.error = "Invalid email address"
                    return@setOnClickListener
                }

                // Check if password has at least 6 characters
                if (password.length < 6) {
                    passwordEditText.error = "Password must be at least 6 characters long"
                    return@setOnClickListener
                }

                // Check if re-entered password matches the original password
                if (password != rePassword) {
                    rePasswordEditText.error = "Passwords do not match"
                    return@setOnClickListener
                }

                // Display success message
                val message = "You Registered Successfully!"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
        }
    }
}
