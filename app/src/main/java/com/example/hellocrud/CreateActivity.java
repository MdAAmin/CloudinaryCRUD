package com.example.hellocrud;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class CreateActivity extends AppCompatActivity {

    private ImageView imageView;
    private static final int IMAGE_REQ = 1;
    private EditText titleEditText, subtitleEditText;
    private String title, subtitle, pdfUrl;
    private Uri pdfUri;
    private Button button;
    private DatabaseReference reference;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create);

        Button goToReadActivity = findViewById(R.id.goToReadActivity);
        Button goToUpdateActivity = findViewById(R.id.goToUpdateActivity);

        goToReadActivity.setOnClickListener(v -> {
            // Navigate to Read Activity
            Intent intent = new Intent(CreateActivity.this, ReadActivity.class);
            startActivity(intent);
        });

        goToUpdateActivity.setOnClickListener(v -> {
            // Navigate to Update Activity
            Intent intent = new Intent(CreateActivity.this, UpdateActivity.class);
            startActivity(intent);
        });

        imageView = findViewById(R.id.imageView);
        titleEditText = findViewById(R.id.title);
        subtitleEditText = findViewById(R.id.subtitle);
        button = findViewById(R.id.add);
        progressBar = findViewById(R.id.progressBar);
        reference = FirebaseDatabase.getInstance().getReference().child("PDFs");

        imageView.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(CreateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_REQ);
            } else {
                ActivityCompat.requestPermissions(CreateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_REQ);
            }
        });

        button.setOnClickListener(v -> {
            title = titleEditText.getText().toString().trim();
            subtitle = subtitleEditText.getText().toString().trim();

            if (pdfUri == null) {
                Toast.makeText(CreateActivity.this, "Please Select a PDF", Toast.LENGTH_SHORT).show();
            } else if (title.isEmpty()) {
                titleEditText.setError("Empty");
                titleEditText.requestFocus();
            } else if (subtitle.isEmpty()) {
                subtitleEditText.setError("Empty");
                subtitleEditText.requestFocus();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                uploadPdfToCloudinary(pdfUri);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQ && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            Toast.makeText(CreateActivity.this, "PDF selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPdfToCloudinary(Uri pdfUri) {
        MediaManager.get().upload(pdfUri)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) { }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) { }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        pdfUrl = (String) resultData.get("secure_url");
                        uploadData(pdfUrl);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(CreateActivity.this, "Error uploading PDF: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) { }
                }).dispatch();
    }

    private void uploadData(String pdfUrl) {
        String key = reference.push().getKey();
        Model data = new Model(title, subtitle, pdfUrl, key);
        reference.child(key).setValue(data)
                .addOnSuccessListener(unused -> {
                    titleEditText.setText("");
                    subtitleEditText.setText("");
                    Toast.makeText(CreateActivity.this, "PDF Added Successfully!!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });
    }
}
