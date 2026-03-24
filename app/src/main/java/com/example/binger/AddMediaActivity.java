package com.example.binger;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddMediaActivity extends AppCompatActivity {

    private android.net.Uri selectedImageUri = null;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            android.net.Uri imageUri = data.getData();
            ImageView ivMedia = findViewById(R.id.iv_media_image);
            ivMedia.setImageURI(imageUri);
            selectedImageUri = imageUri;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_media);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView IVmedia = findViewById(R.id.iv_media_image);
        EditText etName = findViewById(R.id.ET_Name);
        EditText etCategory = findViewById(R.id.ET_Category);
        Button btnSave = findViewById(R.id.btn_save);

        IVmedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String category = etCategory.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(AddMediaActivity.this, "הכנס שם סדרה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("NAME", name);
                    resultIntent.putExtra("CATEGORY", category);
                    if (selectedImageUri != null) {
                        resultIntent.putExtra("IMAGE", selectedImageUri.toString());
                    }
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}