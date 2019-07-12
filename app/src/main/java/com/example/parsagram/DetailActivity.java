package com.example.parsagram;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.parsagram.Model.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    Post post;

    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvUsername = findViewById(R.id.tvDetailUsername);
        ivImage = findViewById(R.id.tvDetailImage);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvTimestamp = findViewById(R.id.tvTimeStamp);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.instagram_word_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());

        tvTimestamp.setText(post.getCreatedAt().toString());

        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImage);
        }
    }
//
//    public String getRelativeTimeAgo(String rawDate) {
//        String twitterFormat = "EEE MMM dd HH:mm:ss Z yyyy";
//        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
//        sf.setLenient(true);
//
//        String relativeDate = "";
//        try {
//            long dateMillis = sf.parse(rawDate).getTime();
//            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
//                    System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_ALL).toString();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return relativeDate;
//    }
}
