package com.example.wandersyncteam10.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wandersyncteam10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ViewTripsActivity extends AppCompatActivity {

    private ListView travelPostsListView;
    private Button backButton;
    private FirebaseUser currentUser;
    private DatabaseReference travelPostsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trips);

        travelPostsListView = findViewById(R.id.travelPostsListView);
        backButton = findViewById(R.id.backButton);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            travelPostsRef = FirebaseDatabase.getInstance().getReference("communityPosts").child(currentUser.getUid());
            loadTravelPosts();
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewTripsActivity.this, CommunityActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadTravelPosts() {
        travelPostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> formattedPosts = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    TravelPost post = data.getValue(TravelPost.class);
                    if (post != null) {
                        String postDetails = "Destination: " + post.getDestination() + "\n" +
                                "Start Date: " + post.getStartDate() + "\n" +
                                "End Date: " + post.getEndDate() + "\n" +
                                "Accommodation: " + post.getAccommodation() + "\n" +
                                "Dining: " + post.getDining() + "\n" +
                                "Transportation: " + post.getTransportation() + "\n" +
                                "Notes: " + post.getNotes();
                        formattedPosts.add(postDetails);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewTripsActivity.this, android.R.layout.simple_list_item_1, formattedPosts);
                travelPostsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewTripsActivity.this, "Failed to load posts.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
