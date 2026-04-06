package com.example.binger;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainPageActivity extends AppCompatActivity {

    private LinearLayout container;
    private final int ID_TITLE = 1001;
    private final int ID_CAT = 1002;
    private final int ID_EP = 1003;
    private final int ID_IMG_STR = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.view_contain_media);
        initView();
        loadAllCards();
    }

    public void initView() {
        Button addMediaBtn = findViewById(R.id.btn_add_new);
        addMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, AddMediaActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        findViewById(R.id.show1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uriPath = "android.resource://com.example.binger/drawable/show1";
                askToAddShow("Breaking Bad", "Crime Drama", uriPath);
            }
        });

        findViewById(R.id.show2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uriPath = "android.resource://com.example.binger/drawable/show2";
                askToAddShow("Rick And Morty", "Animation", uriPath);
            }
        });

        findViewById(R.id.show3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uriPath = "android.resource://com.example.binger/drawable/show3";
                askToAddShow("Invincible", "Action", uriPath);
            }
        });

        findViewById(R.id.show4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uriPath = "android.resource://com.example.binger/drawable/show4";
                askToAddShow("Dexter Resurrection", "Thriller", uriPath);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            addCard(data.getStringExtra("NAME"), data.getStringExtra("CATEGORY"), data.getStringExtra("IMAGE"), "0", true);
        }
    }

    public void addCard(String name, String category, String imageStr, String epCount, boolean shouldSave) {
        findViewById(R.id.empty_text).setVisibility(View.GONE);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setBackgroundResource(R.drawable.rounded_bg);
        card.setPadding(25, 25, 25, 25);
        card.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ImageView img = new ImageView(this);
        img.setLayoutParams(new LinearLayout.LayoutParams(320, 480));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setClipToOutline(true);
        if (imageStr != null && !imageStr.isEmpty()) {
            try {
                img.setImageURI(Uri.parse(imageStr));
            } catch (Exception e) {
                img.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }
        card.addView(img);

        TextView tvImgData = new TextView(this);
        tvImgData.setId(ID_IMG_STR);
        tvImgData.setText(imageStr != null ? imageStr : "");
        tvImgData.setVisibility(View.GONE);
        card.addView(tvImgData);

        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(0, 480, 1));
        textLayout.setPadding(40, 0, 0, 0);

        TextView tvTitle = new TextView(this);
        tvTitle.setId(ID_TITLE);
        tvTitle.setText(name);
        tvTitle.setTextSize(22);
        tvTitle.setTextColor(0xFFFFFFFF);
        textLayout.addView(tvTitle);

        TextView tvCat = new TextView(this);
        tvCat.setId(ID_CAT);
        tvCat.setText(category);
        tvCat.setTextColor(0xFFBB86FC);
        textLayout.addView(tvCat);

        TextView tvInfo = new TextView(this);
        tvInfo.setText("סטטוס: צופה כעת");
        tvInfo.setTextColor(0xFFCCCCCC);
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = tvInfo.getText().toString();
                if (s.equals("סטטוס: צופה כעת")) tvInfo.setText("סטטוס: הושלם");
                else if (s.equals("סטטוס: הושלם")) tvInfo.setText("סטטוס: בהמתנה");
                else tvInfo.setText("סטטוס: צופה כעת");
            }
        });
        textLayout.addView(tvInfo);

        LinearLayout bottomRow = new LinearLayout(this);
        bottomRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvEp = new TextView(this);
        tvEp.setId(ID_EP);
        tvEp.setText(epCount);
        tvEp.setTextColor(0xFF03DAC5);

        Button btnMinus = new Button(this);
        btnMinus.setText("-");
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c = Integer.parseInt(tvEp.getText().toString());
                if (c > 0) tvEp.setText(String.valueOf(c - 1));
            }
        });

        Button btnPlus = new Button(this);
        btnPlus.setText("+");
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvEp.setText(String.valueOf(Integer.parseInt(tvEp.getText().toString()) + 1));
            }
        });

        Button btnDelete = new Button(this);
        btnDelete.setText("מחק");
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainPageActivity.this)
                        .setTitle("מחיקה").setMessage("למחוק?")
                        .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                container.removeView(card);
                                refreshStorage();
                            }
                        }).setNegativeButton("לא", null).show();
            }
        });

        bottomRow.addView(btnMinus);
        bottomRow.addView(tvEp);
        bottomRow.addView(btnPlus);
        bottomRow.addView(btnDelete);
        textLayout.addView(bottomRow);
        card.addView(textLayout);
        container.addView(card);

        if (shouldSave) {
            android.content.SharedPreferences.Editor editor = getSharedPreferences("BingerPref", MODE_PRIVATE).edit();
            saveCard(name, category, imageStr, epCount, container.indexOfChild(card), editor);
            editor.putInt("total", container.getChildCount());
            editor.apply();
        }    }

    private void saveCard(String name, String category, String imageStr, String ep, int index, android.content.SharedPreferences.Editor editor) {
        String finalPath = imageStr;
        if (imageStr != null && imageStr.startsWith("content://")) {
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(imageStr));
                File file = new File(getFilesDir(), "img_" + System.currentTimeMillis() + "_" + index + ".jpg");
                FileOutputStream fos = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                fos.close();
                finalPath = file.getAbsolutePath();
            } catch (IOException e) { finalPath = ""; }
        }

        editor.putString("n_" + index, name)
                .putString("c_" + index, category)
                .putString("i_" + index, finalPath)
                .putString("e_" + index, ep);
    }

    private void refreshStorage() {
        android.content.SharedPreferences.Editor editor = getSharedPreferences("BingerPref", MODE_PRIVATE).edit();
        editor.clear();

        int cardCount = 0;

        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);

            if (v instanceof LinearLayout) {
                LinearLayout card = (LinearLayout) v;

                if (card.findViewById(ID_TITLE) != null) {
                    String title = ((TextView) card.findViewById(ID_TITLE)).getText().toString();
                    String cat = ((TextView) card.findViewById(ID_CAT)).getText().toString();
                    String imgPath = ((TextView) card.findViewById(ID_IMG_STR)).getText().toString();
                    String ep = ((TextView) card.findViewById(ID_EP)).getText().toString();

                    saveCard(title, cat, imgPath, ep, cardCount, editor);
                    cardCount++;
                }
            }
        }

        editor.putInt("total", cardCount);
        editor.apply();
    }

    private void loadAllCards() {
        android.content.SharedPreferences prefs = getSharedPreferences("BingerPref", MODE_PRIVATE);
        int total = prefs.getInt("total", 0);
        for (int i = 0; i < total; i++) {
            String n = prefs.getString("n_" + i, "");
            if (!n.isEmpty()) {
                addCard(n, prefs.getString("c_" + i, ""), prefs.getString("i_" + i, ""), prefs.getString("e_" + i, "0"), false);
            }
        }
    }


    public void askToAddShow(String showName, String category, String imgUri){
        new AlertDialog.Builder(this).setTitle("הוספה מהירה").setMessage("האם ברצונך להוסיף את " + showName + " לרשימה שלך?").setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addCard(showName, category, imgUri, "0", true);
                    }
                })
                .setNegativeButton("לא", null)
                .show();
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_help) {
            new AlertDialog.Builder(this).setTitle("עזרה").setMessage("הוסף סדרות ולחץ מחק להסרה.").setPositiveButton("OK", null).show();
            return true;
        } else if (item.getItemId() == R.id.menu_exit) {
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}