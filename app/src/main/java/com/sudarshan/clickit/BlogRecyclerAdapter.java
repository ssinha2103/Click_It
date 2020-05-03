package com.sudarshan.clickit;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {


    public List<BlogPost> blog_list;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private TextView blogUserName;
    private CircleImageView blogUserImage;
    private TextView blogLikeCount;


    public BlogRecyclerAdapter(List<BlogPost> blog_list){

        this.blog_list = blog_list;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item,parent,false);
        context = parent.getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final String blogPostId = blog_list.get(position).BlogPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        String desc_data = blog_list.get(position).getDesc();
        holder.setDescText(desc_data);
        String blog_title = blog_list.get(position).getTitle();
        holder.setBlogTitle(blog_title);
        String image_url = blog_list.get(position).getImage_url();
        holder.setBlogImage(image_url);

        long millisecond = blog_list.get(position).getTime_stamp().getTime();
        String dateString = DateFormat.format("E, dd-M-yyyy hh:mm:ss", new Date(millisecond)).toString();
        holder.setTime(dateString);

        String user_id = blog_list.get(position).getUser_id();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");
                    holder.setUserData(userName,userImage);



                }else{
//                    Toast.makeText(MainActivity.this, "Error :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


        //get likes realtime
        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).addSnapshotListener((Activity) context,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists())
                {
                    holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.drawable.action_like_red));
                }else {
                    holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.drawable.action_like_black));
                }
            }
        });

        // get likes count

        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty())
                {
                    int count = queryDocumentSnapshots.size();
                    holder.updateLikesCount(count);
                }
                else {
                    holder.updateLikesCount(0);
                }
            }
        });

        holder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Posts/" +  blogPostId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                         if (!task.getResult().exists())
                         {
                             Map<String , Object> likesMap = new HashMap<>();
                             likesMap.put("timeStamp", FieldValue.serverTimestamp());

                             firebaseFirestore.collection("Posts/").document(blogPostId).collection("Likes").document(currentUserId).set(likesMap);
                         }else {
                             firebaseFirestore.collection("Posts/").document(blogPostId).collection("Likes").document(currentUserId).delete();
                         }
                    }
                });

            }
        });





    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView descView;
        private TextView blogTitle;
        private ImageView blogImageView;
        private TextView userName;
        private TextView blogDate;
        private ImageView blogLikeBtn;
        private TextView blogLikeCount;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            blogLikeBtn = mView.findViewById(R.id.blog_like_button);
        }

        public void setDescText(String descText)
        {
            descView = mView.findViewById(R.id.blog_desc);
            descView.setText(descText);
        }

        public void setBlogImage(String downloadUri){
            blogImageView = mView.findViewById(R.id.blog_image);
            RequestOptions placeHolderOption = new RequestOptions();
            placeHolderOption.placeholder(R.drawable.blog_loading_image);
            Glide.with(context).applyDefaultRequestOptions(placeHolderOption).load(downloadUri).into(blogImageView);
        }

        public void setBlogTitle(String blogtitle){
            blogTitle = mView.findViewById(R.id.blog_title);
            blogTitle.setText(blogtitle);
        }


        public void setTime(String date)
        {
            blogDate = mView.findViewById(R.id.blog_date);
            blogDate.setText(date);
        }

        public void setUserData(String name, String image){
            blogUserImage = mView.findViewById(R.id.blog_user_image);
            blogUserName = mView.findViewById(R.id.blog_user_name);

            blogUserName.setText(name);

            RequestOptions placeHolderOption = new RequestOptions();
            placeHolderOption.placeholder(R.drawable.default_icon);
            Glide.with(context).applyDefaultRequestOptions(placeHolderOption).load(image).into(blogUserImage);
        }

        public void updateLikesCount(int count)
        {
            blogLikeCount = mView.findViewById(R.id.blog_like_count);
            blogLikeCount.setText(count + " Likes");
        }
    }

}
