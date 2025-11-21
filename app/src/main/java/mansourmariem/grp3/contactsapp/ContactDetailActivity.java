package mansourmariem.grp3.contactsapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ContactDetailActivity extends AppCompatActivity {

    // UI Components
    private TextView nameTV, numberTV;
    private ImageView callBtn, messageBtn;
    private Button editBtn, deleteBtn;

    // Contact information
    private String contactName, contactNumber;
    private int contactPosition; // Important pour identifier le contact dans la liste principale

    // Permission request codes
    private static final int REQUEST_CALL_PERMISSION = 100;
    private static final int REQUEST_SMS_PERMISSION = 101;

    /**
     * Ce launcher gère le retour de EditContactActivity.
     * Si le résultat est positif (RESULT_OK), il met à jour l'UI
     * et prépare les données pour les renvoyer à MainActivity.
     */
    private final ActivityResultLauncher<Intent> editContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    // Récupérer les données mises à jour
                    contactName = data.getStringExtra("UPDATED_NAME");
                    contactNumber = data.getStringExtra("UPDATED_NUMBER");

                    // Mettre à jour l'affichage dans cette activité
                    nameTV.setText(contactName);
                    numberTV.setText(contactNumber);

                    // Préparer un résultat pour MainActivity afin qu'elle puisse mettre à jour sa liste
                    setResult(Activity.RESULT_OK, data);
                    Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        // Initialize views
        nameTV = findViewById(R.id.name);
        numberTV = findViewById(R.id.number);
        callBtn = findViewById(R.id.callButton);
        messageBtn = findViewById(R.id.messageButton);
        editBtn = findViewById(R.id.editButton);
        deleteBtn = findViewById(R.id.deleteButton);

        // Get contact data from intent
        Intent intent = getIntent();
        contactName = intent.getStringExtra("name"); // "name" correspond à ce que MainActivity envoie
        contactNumber = intent.getStringExtra("contact"); // "contact" correspond aussi
        contactPosition = intent.getIntExtra("position", -1); // Récupérer la position

        // Display contact information
        if (contactName != null && contactNumber != null && contactPosition != -1) {
            nameTV.setText(contactName);
            numberTV.setText(contactNumber);
        } else {
            Toast.makeText(this, "Error loading contact details", Toast.LENGTH_SHORT).show();
            finish();
            return; // Arrêter l'exécution si les données sont invalides
        }

        // Set click listener for call button
        callBtn.setOnClickListener(v -> makePhoneCall());

        // Set click listener for message button
        messageBtn.setOnClickListener(v -> sendSMS());

        // --- ACTION BOUTON EDIT ---
        editBtn.setOnClickListener(v -> {
            // Créer un Intent pour démarrer EditContactActivity
            Intent editIntent = new Intent(ContactDetailActivity.this, EditContactActivity.class);

            // Passer les données actuelles du contact à l'activité d'édition
            editIntent.putExtra("CONTACT_NAME", contactName);
            editIntent.putExtra("CONTACT_NUMBER", contactNumber);
            editIntent.putExtra("CONTACT_POSITION", contactPosition);

            // Lancer l'activité et attendre un résultat
            editContactLauncher.launch(editIntent);
        });

        // --- ACTION BOUTON DELETE ---
        deleteBtn.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    // Show confirmation dialog before deleting contact
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete " + contactName + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // L'utilisateur confirme la suppression
                    Intent resultIntent = new Intent();
                    // On renvoie la position du contact à supprimer
                    resultIntent.putExtra("CONTACT_POSITION", contactPosition);
                    // On ajoute un "drapeau" pour que MainActivity sache qu'il s'agit d'une suppression
                    resultIntent.putExtra("ACTION_TYPE", "DELETE");

                    // Définir le résultat et fermer l'activité
                    setResult(Activity.RESULT_OK, resultIntent);
                    Toast.makeText(this, contactName + " has been deleted.", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()) // L'utilisateur annule
                .show();
    }

    // Le reste de votre code (makePhoneCall, sendSMS, etc.) reste inchangé...
    private void makePhoneCall() {
        if (contactNumber == null || contactNumber.isEmpty()) {
            Toast.makeText(this, "No phone number available", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            startCall();
        }
    }

    private void startCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + contactNumber));
        try {
            startActivity(callIntent);
        } catch (SecurityException e) {
            Toast.makeText(this, "Permission denied to make calls", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS() {
        if (contactNumber == null || contactNumber.isEmpty()) {
            Toast.makeText(this, "No phone number available", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
        } else {
            openSMSApp();
        }
    }

    private void openSMSApp() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:" + contactNumber));
        try {
            startActivity(smsIntent);
        } catch (Exception e) {
            Toast.makeText(this, "No SMS app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCall();
            } else {
                Toast.makeText(this, "Permission denied to make calls", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openSMSApp();
            } else {
                Toast.makeText(this, "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
