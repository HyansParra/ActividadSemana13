package com.example.actividadsemana13;

import android.os.Bundle;
import android.view.View; // Importa View
import android.widget.Button; // Importa Button
import android.widget.EditText; // Importa EditText
import android.widget.Toast; // Para mostrar mensajes

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Importa las clases de Firebase Database
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap; // Para crear la estructura de datos
import java.util.Map; // Para crear la estructura de datos

public class MainActivity extends AppCompatActivity {

    // Declara las variables de la UI
    EditText txtNombre, txtApellido, txtEdad;
    Button btnGuardar;

    // Declara la referencia a la base de datos de Firebase
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); // Esto enlaza tu layout XML

        // Inicializa la referencia a la base de datos de Firebase
        // Esto apunta a la raíz de tu Realtime Database
        database = FirebaseDatabase.getInstance().getReference();

        // Enlaza las variables de la UI con los elementos del XML
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtEdad = findViewById(R.id.txtEdad);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Configura el listener de clic para el botón
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama a la función para guardar datos
                guardarDatosEnFirebase();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void guardarDatosEnFirebase() {
        String nombre = txtNombre.getText().toString();
        String apellido = txtApellido.getText().toString();
        String edad = txtEdad.getText().toString();

        // Valida que los campos no estén vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || edad.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crea una estructura de datos (Map) para los datos del usuario
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("username", nombre);
        usuario.put("apellido", apellido); // Campo personalizado
        usuario.put("email", "ejemplo@correo.com"); //Ejemplo
        usuario.put("edad", Integer.parseInt(edad)); // Ejemplo

        // Genera una clave única para el nuevo usuario (similar a 'userId' o 'newPostKey'
        String userId = database.child("users").push().getKey();

        if (userId != null) {
            // Escribe los datos en la base de datos en la ruta 'users/[userId]'
            database.child("users").child(userId).setValue(usuario)
                    .addOnSuccessListener(aVoid -> {
                        // Éxito
                        Toast.makeText(MainActivity.this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show();
                        // Limpia los campos
                        txtNombre.setText("");
                        txtApellido.setText("");
                        txtEdad.setText("");
                    })
                    .addOnFailureListener(e -> {
                        // Error
                        Toast.makeText(MainActivity.this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}