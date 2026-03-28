package com.example.binger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.Gravity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainPageActivity extends AppCompatActivity {

    Button addMediaBtn;
    ImageView IVmedia;
    TextView TVname, TVcategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initView();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void initView(){
        addMediaBtn = findViewById(R.id.btn_add_new);
        addMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, AddMediaActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            String name = data.getStringExtra("NAME");
            String category = data.getStringExtra("CATEGORY");
            String imageUriString = data.getStringExtra("IMAGE");
            addCard(name, category, imageUriString);
        }


    }



    public void addCard(String name, String category, String imageUri) {
        TextView empty_text = findViewById(R.id.empty_text);
        empty_text.setVisibility(View.GONE);

        LinearLayout container = findViewById(R.id.view_contain_media);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setBackgroundResource(R.drawable.rounded_bg);
        card.setPadding(25, 25, 25, 25);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 30);
        card.setLayoutParams(cardParams);

        ImageView img = new ImageView(this);
        img.setLayoutParams(new LinearLayout.LayoutParams(320, 480));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setBackgroundResource(R.drawable.rounded_bg);
        img.setClipToOutline(true);

        if (imageUri != null && !imageUri.isEmpty()) {
            img.setImageURI(android.net.Uri.parse(imageUri));
        } else {
            img.setImageResource(android.R.drawable.ic_menu_gallery);
        }
        card.addView(img);

        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(40, 0, 0, 0);
        textLayout.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvTitle = new TextView(this);
        tvTitle.setText(name);
        tvTitle.setTextSize(22);
        tvTitle.setTextColor(0xFFFFFFFF);
        tvTitle.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        textLayout.addView(tvTitle);

        TextView tvCat = new TextView(this);
        tvCat.setText(category);
        tvCat.setTextSize(16);
        tvCat.setTextColor(0xFFBB86FC);
        textLayout.addView(tvCat);

        card.addView(textLayout);
        container.addView(card);
    }
}