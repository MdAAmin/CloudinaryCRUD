package com.example.hello.crud;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hello.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.holder> {

    ArrayList<Model> data;
    Context context;

    public CustomAdapter(ArrayList<Model> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.sample_view, parent, false);
        return new CustomAdapter.holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        Model model = data.get(position);
        holder.title.setText(model.getTitle());
        holder.subtitle.setText(model.getSubtitle());

        // Handle item click to open PDF
        holder.itemView.setOnClickListener(v -> {
            // Open the PDF URL in a browser or PDF viewer
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(model.getPdfUrl()));
            holder.itemView.getContext().startActivity(intent);
        });

        // Long click to show the popup menu
        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
            popupMenu.inflate(R.menu.popup_menu); // Ensure this points to the correct menu file
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.edit:
                        // Open UpdateActivity to update PDF details
                        Intent intent = new Intent(holder.itemView.getContext(), UpdateActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("subtitle", model.getSubtitle());
                        intent.putExtra("pdfUrl", model.getPdfUrl());
                        intent.putExtra("key", model.getKey());
                        holder.itemView.getContext().startActivity(intent);
                        break;

                    case R.id.delete:
                        // Show confirmation dialog before deleting the item
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Are you sure?")
                                .setPositiveButton("Delete", (dialog, which) -> {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Tech Items")
                                            .child(model.getKey())
                                            .removeValue();
                                    Toast.makeText(holder.itemView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("Cancel", (dialog, which) -> {
                                    Toast.makeText(holder.itemView.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                                }).show();
                        break;
                }
                return false;
            });
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class holder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        CardView cardView;

        public holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.main);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }
    }
}
