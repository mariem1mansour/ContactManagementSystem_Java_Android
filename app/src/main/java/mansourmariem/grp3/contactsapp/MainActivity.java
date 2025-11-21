package mansourmariem.grp3.contactsapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // List to store contacts
    private final ArrayList<Contacts> contactsArrayList = new ArrayList<>();

    // RecyclerView for displaying contacts
    private RecyclerView contactRV;

    // Adapter for RecyclerView
    private Adapter adapter;


    // --- AJOUT : ACTIVITY RESULT LAUNCHER ---
    // Ce launcher écoute les résultats renvoyés par ContactDetailActivity.
    private final ActivityResultLauncher<Intent> contactDetailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // On vérifie si le résultat est positif (RESULT_OK) et contient des données
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    int position = data.getIntExtra("CONTACT_POSITION", -1);

                    // Si la position est invalide, on ne fait rien
                    if (position == -1) {
                        return;
                    }

                    // On vérifie s'il s'agit d'une action de suppression
                    String actionType = data.getStringExtra("ACTION_TYPE");
                    if ("DELETE".equals(actionType)) {
                        // --- Logique de Suppression ---
                        // Supprimer le contact de la liste
                        contactsArrayList.remove(position);
                        // Notifier l'adaptateur que l'élément a été supprimé pour mettre à jour l'UI
                        adapter.notifyItemRemoved(position);
                    } else {
                        // --- Logique de Modification ---
                        // Récupérer les nouvelles données
                        String updatedName = data.getStringExtra("UPDATED_NAME");
                        String updatedNumber = data.getStringExtra("UPDATED_NUMBER");

                        // Mettre à jour l'objet contact dans la liste
                        Contacts contactToUpdate = contactsArrayList.get(position);
                        contactToUpdate.setUserName(updatedName);
                        contactToUpdate.setContactNumber(updatedNumber); // Assurez-vous que le setter correspond au nom de la variable

                        // Notifier l'adaptateur que l'élément a changé
                        adapter.notifyItemChanged(position);
                    }
                }
            }
    );
    // --- AJOUT : LAUNCHER POUR LA CRÉATION DE CONTACT ---
    // Ce launcher gère le retour de CreateNewContactActivity
    private final ActivityResultLauncher<Intent> createContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Si l'activité s'est terminée avec succès (RESULT_OK), on rafraîchit la liste.
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(this, "Refreshing contacts...", Toast.LENGTH_SHORT).show();
                    getContacts(); // Recharge la liste des contacts depuis le téléphone
                }
            }
    );
    // --- FIN DE L'AJOUT ---



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactRV = findViewById(R.id.rv);
        FloatingActionButton addNewContactFAB = findViewById(R.id.addButton);

        prepareContactRV();
        requestPermissions();

        // --- MODIFICATION : Utiliser le nouveau launcher ---
        addNewContactFAB.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateNewContactActivity.class);
            createContactLauncher.launch(intent); // Lancer pour un résultat
        });
        // --- FIN DE LA MODIFICATION ---
    }

    // Le `onResume` n'est plus nécessaire pour recharger toute la liste car
    // les mises à jour se font maintenant via le launcher.
    // Vous pouvez le supprimer ou le laisser, mais sa logique actuelle
    // peut causer des rechargements inutiles.
    @Override
    protected void onResume() {
        super.onResume();
        // Optionnel : si vous voulez toujours recharger la liste à chaque fois.
        // contactsArrayList.clear();
        // getContacts();
    }

    private void prepareContactRV() {
        // --- MODIFICATION : PASSER LE LAUNCHER À L'ADAPTATEUR ---
        // L'adaptateur a besoin du launcher pour démarrer ContactDetailActivity
        adapter = new Adapter(this, contactsArrayList, contactDetailLauncher);
        contactRV.setLayoutManager(new LinearLayoutManager(this));
        contactRV.setAdapter(adapter);
    }

    // Le reste de votre code (requestPermissions, showSettingsDialog, getContacts)
    // reste identique.
    // ... (votre code existant pour requestPermissions, showSettingsDialog, getContacts)
    private void requestPermissions() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.WRITE_CONTACTS
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(MainActivity.this,
                                    "All permissions granted",
                                    Toast.LENGTH_SHORT).show();

                            getContacts();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(
                            List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(error ->
                        Toast.makeText(getApplicationContext(),
                                "Error occurred! " + error.toString(),
                                Toast.LENGTH_SHORT).show()
                )
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void getContacts() {
        contactsArrayList.clear(); // Vider la liste pour éviter les doublons lors du rafraîchissement

        // Le reste de la méthode est identique
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI, null, null, null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            );
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    String displayName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    int hasPhoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    if (hasPhoneNumber > 0) {
                        Cursor phoneCursor = null;
                        try {
                            phoneCursor = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null
                            );
                            if (phoneCursor != null && phoneCursor.moveToNext()) {
                                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                contactsArrayList.add(new Contacts(displayName, phoneNumber));
                            }
                        } finally {
                            if (phoneCursor != null) phoneCursor.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Notifier l'adaptateur que toute la liste a changé
        }
    }

}
