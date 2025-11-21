// Dans le fichier CreateNewContactActivity.java

package mansourmariem.grp3.contactsapp;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent; // <<< AJOUT
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CreateNewContactActivity extends AppCompatActivity {

    private EditText nameEdt, phoneEdt, emailEdt;
    private Button saveContactBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_contact);

        nameEdt = findViewById(R.id.enterName);
        phoneEdt = findViewById(R.id.enterNumber);
        emailEdt = findViewById(R.id.enterEmail);
        saveContactBtn = findViewById(R.id.saveButton);
        cancelBtn = findViewById(R.id.cancelButton);

        saveContactBtn.setOnClickListener(v -> {
            String name = nameEdt.getText().toString().trim();
            String phone = phoneEdt.getText().toString().trim();
            String email = emailEdt.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                nameEdt.setError("Please enter name");
                nameEdt.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(phone)) {
                phoneEdt.setError("Please enter phone number");
                phoneEdt.requestFocus();
                return;
            }

            if (addContact(name, phone, email)) {
                Toast.makeText(CreateNewContactActivity.this,
                        "Contact saved successfully!",
                        Toast.LENGTH_SHORT).show();

                // --- MODIFICATION ---
                // Préparer un résultat positif pour MainActivity
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent); // RESULT_OK indique le succès
                finish(); // Fermer l'activité
                // --- FIN DE LA MODIFICATION ---

            } else {
                Toast.makeText(CreateNewContactActivity.this,
                        "Failed to save contact",
                        Toast.LENGTH_SHORT).show();
            }
        });

        cancelBtn.setOnClickListener(v -> finish());
    }

    // La méthode addContact reste inchangée
    private boolean addContact(String name, String phone, String email) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        int rawContactID = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        if (!TextUtils.isEmpty(email)) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                            ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                    .build());
        }

        try {
            ContentResolver contentResolver = getContentResolver();
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
            return true;
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
            return false;
        }
    }
}
