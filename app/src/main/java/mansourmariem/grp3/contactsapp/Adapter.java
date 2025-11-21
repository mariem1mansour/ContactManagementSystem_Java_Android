package mansourmariem.grp3.contactsapp;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// Adapter class for handling the display
// of contacts in a RecyclerView
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final Context context;
    private ArrayList<Contacts> contactsArrayList;

    private final ActivityResultLauncher<Intent> contactDetailLauncher;

    // --- MODIFICATION : Le constructeur accepte maintenant le launcher ---
    public Adapter(Context context, ArrayList<Contacts> contactsArrayList, ActivityResultLauncher<Intent> launcher) {
        this.context = context;
        this.contactsArrayList = contactsArrayList;
        this.contactDetailLauncher = launcher;
    }

    // Creates and returns a ViewHolder object
    // for each item in the RecyclerView.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contacts_rv_item, parent, false);
        return new ViewHolder(view);
    }

    // Updates the contact list with a filtered
    // list and notifies the adapter.
    public void filterList(ArrayList<Contacts> filterList) {
        this.contactsArrayList = filterList;

        // Notify adapter about dataset change
        notifyItemRangeChanged(0, getItemCount());
    }

    // Binds data to the ViewHolder for a specific position.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contacts contact = contactsArrayList.get(position);
        holder.contactTV.setText(contact.getUserName());
//        holder.contactTV.setText(contact.getContactNumber());


        // Set click listener to open ContactDetailActivity
        // with selected contact details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContactDetailActivity.class);
            intent.putExtra("name", contact.getUserName());
            intent.putExtra("contact", contact.getContactNumber());
            intent.putExtra("position", position);

            // --- MODIFICATION : Démarrer le launcher ---
//            context.startActivity(intent);
            contactDetailLauncher.launch(intent);
        });
    }

    /**
     * Returns the total number of items in the list.
     */
    @Override
    public int getItemCount() {
        return contactsArrayList.size();
    }

    /**
     * ViewHolder class to hold and manage views
     * for each RecyclerView item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView contactTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactTV = itemView.findViewById(R.id.contactName);
        }
    }
}