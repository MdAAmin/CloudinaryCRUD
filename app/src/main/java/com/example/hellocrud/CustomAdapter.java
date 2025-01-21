package com.example.hellocrud;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<Model> data;
    private Context context;

    public CustomAdapter(ArrayList<Model> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model model = data.get(position);

        // Set the title and subtitle
        holder.title.setText(model.getTitle());
        holder.subtitle.setText(model.getSubtitle());

        // Handle item click to open the pop-up menu
        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.popup_menu); // Define popup_menu.xml in res/menu folder

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.update) {
                    // Handle update action
                    Intent intent = new Intent(context, UpdateActivity.class);
                    intent.putExtra("key", model.getKey());
                    intent.putExtra("title", model.getTitle());
                    intent.putExtra("subtitle", model.getSubtitle());
                    intent.putExtra("pdfUrl", model.getPdfUrl());
                    context.startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.delete) {
                    // Handle delete action
                    FirebaseDatabase.getInstance().getReference("PDFs")
                            .child(model.getKey())
                            .removeValue()
                            .addOnSuccessListener(unused ->
                                    Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(context, "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    return true;
                }
                return false;
            });

            popupMenu.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }
    }
}
