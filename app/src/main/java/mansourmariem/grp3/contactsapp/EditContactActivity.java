package mansourmariem.grp3.contactsapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class EditContactActivity extends AppCompatActivity {

    private EditText editName, editNumber;
    private Button saveButton;
    private int contactPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        editName = findViewById(R.id.editName);
        editNumber = findViewById(R.id.editNumber);
        saveButton = findViewById(R.id.saveButton);

        // Récupérer les données envoyées depuis ContactDetailActivity
        Intent intent = getIntent();
        String currentName = intent.getStringExtra("CONTACT_NAME");
        String currentNumber = intent.getStringExtra("CONTACT_NUMBER");
        contactPosition = intent.getIntExtra("CONTACT_POSITION", -1); // Pour savoir quel contact mettre à jour

        // Pré-remplir les champs
        editName.setText(currentName);
        editNumber.setText(currentNumber);

        saveButton.setOnClickListener(v -> {
            // Récupérer les nouvelles valeurs
            String updatedName = editName.getText().toString();
            String updatedNumber = editNumber.getText().toString();

            // Créer un Intent pour renvoyer les données mises à jour
            Intent resultIntent = new Intent();
            resultIntent.putExtra("UPDATED_NAME", updatedName);
            resultIntent.putExtra("UPDATED_NUMBER", updatedNumber);
            resultIntent.putExtra("CONTACT_POSITION", contactPosition);

            // Définir le résultat comme OK et terminer l'activité
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
