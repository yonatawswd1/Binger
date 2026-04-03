package com.example.binger;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.Gravity;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.MenuItem;


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
                ViewGroup.LayoutParams.MATCH_PARENT
        );

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
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(0, 480, 1);
        textLayout.setLayoutParams(textLayoutParams);
        textLayout.setPadding(40, 0, 0, 0);

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

        TextView tvInfo = new TextView(this);
        tvInfo.setText("Status: Watching");
        tvInfo.setTextSize(14);
        tvInfo.setTextColor(0xFFCCCCCC);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        tvInfo.setLayoutParams(infoParams);

        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentStatus = tvInfo.getText().toString();
                if (currentStatus.equals("Status: Watching")) {
                    tvInfo.setText("Status: Completed");
                    tvInfo.setTextColor(0xFF4CAF50); // ירוק
                } else if (currentStatus.equals("Status: Completed")) {
                    tvInfo.setText("Status: On Hold");
                    tvInfo.setTextColor(0xFFFFC107); // צהוב
                } else if (currentStatus.equals("Status: On Hold")) {
                    tvInfo.setText("Status: Plan to Watch");
                    tvInfo.setTextColor(0xFF03A9F4); // כחול
                } else {
                    tvInfo.setText("Status: Watching");
                    tvInfo.setTextColor(0xFFCCCCCC); // אפור
                }
            }
        });
        textLayout.addView(tvInfo);

        LinearLayout bottomRow = new LinearLayout(this);
        bottomRow.setOrientation(LinearLayout.HORIZONTAL);
        bottomRow.setGravity(Gravity.CENTER_VERTICAL);
        bottomRow.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout epLayout = new LinearLayout(this);
        epLayout.setOrientation(LinearLayout.HORIZONTAL);
        epLayout.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams epParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        epLayout.setLayoutParams(epParams);

        TextView tvEpLabel = new TextView(this);
        tvEpLabel.setText("Ep: ");
        tvEpLabel.setTextColor(0xFFFFFFFF);
        epLayout.addView(tvEpLabel);

        LinearLayout.LayoutParams mathBtnParams = new LinearLayout.LayoutParams(100, 100);

        Button btnMinus = new Button(this);
        btnMinus.setText("-");
        btnMinus.setLayoutParams(mathBtnParams);
        btnMinus.setPadding(0, 0, 0, 0);

        TextView tvEpCount = new TextView(this);
        tvEpCount.setText("0"); // Starting value
        tvEpCount.setTextColor(0xFF03DAC5);
        tvEpCount.setPadding(15, 0, 15, 0);

        Button btnPlus = new Button(this);
        btnPlus.setText("+");
        btnPlus.setLayoutParams(mathBtnParams);
        btnPlus.setPadding(0, 0, 0, 0);

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = Integer.parseInt(tvEpCount.getText().toString());
                if (current > 0) {
                    tvEpCount.setText(String.valueOf(current - 1));
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = Integer.parseInt(tvEpCount.getText().toString());
                tvEpCount.setText(String.valueOf(current + 1));
            }
        });

        epLayout.addView(btnMinus);
        epLayout.addView(tvEpCount);
        epLayout.addView(btnPlus);

        Button btnDelete = new Button(this);
        btnDelete.setText("Delete");
        btnDelete.setBackgroundResource(R.drawable.rounded_bg);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainPageActivity.this)
                        .setTitle("מחיקת פריט").setMessage("האם אתה בטוח שברצונך למחוק את הפריט הזה?").setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                container.removeView(card);
                            }
                        }).setNegativeButton("ביטול", null).show();
            }
        });

        bottomRow.addView(epLayout);
        bottomRow.addView(btnDelete);

        textLayout.addView(bottomRow);

        card.addView(textLayout);
        container.addView(card);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_help) {
            new AlertDialog.Builder(this)
                    .setTitle("איך להשתמש באפליקציה")
                    .setMessage("1. לחץ על 'הוסף' כדי להוסיף סדרה/סרט.\n2. לחץ על כפתור המחיקה כדי להסיר פריט (תתבקש לאשר).\n3. לחץ על סטטוס הצפייה כדי לשנות אותו.")
                    .setPositiveButton("הבנתי", null)
                    .show();
            return true;
        } else if (id == R.id.menu_exit) {
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }










}