package msk.android.academy.javatemplate.ui.photoBook;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import msk.android.academy.javatemplate.R;

public class PhotoBookAdapter extends RecyclerView.Adapter<PhotoBookAdapter.PhotoBookViewHolder> {

    private List<String> mPhotoPaths;

    public PhotoBookAdapter(List<String> photoPaths) {
        mPhotoPaths = new ArrayList<>(photoPaths);
    }

    @Override
    public PhotoBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.photo_book_list_item, parent, false);
        return new PhotoBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotoBookViewHolder holder, int position) {

        Context context = holder.itemView.getContext();

        Glide.with(holder.itemView)
             .load("file://" + mPhotoPaths.get(position))
             .into(holder.ivImage);

        final GestureDetector mDetector = new GestureDetector(context,
                new MyGestureListener(context, mPhotoPaths.get(position)));

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Pass the events to the gesture detector. A return value of true means the detector is
                // handling it. A return value of false means the detector didn't recognize the event.
                return mDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotoPaths.size();
    }

    public void addPhotoPaths(List<String> photoPaths) {
        int firstPositionToInsert = mPhotoPaths.size();
        mPhotoPaths.addAll(photoPaths);
        notifyItemRangeInserted(firstPositionToInsert, photoPaths.size());
    }

    public void updatePhotosPath(List<String> photoPaths) {
        mPhotoPaths.clear();
        mPhotoPaths.addAll(photoPaths);
        notifyDataSetChanged();
    }

    static class PhotoBookViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;

        private ImageView ivStatus;
        private Status status = Status.PLUS;

        PhotoBookViewHolder(View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.iv_image);
            ivStatus = itemView.findViewById(R.id.iv_photo_book_status);
            ivStatus.setBackgroundResource(getImageResource(status));
            ivStatus.setOnClickListener(v -> {
                switch (status) {
                    case PLUS:
                        status = Status.CHECK;
                        break;
                    case CHECK:
                        status = Status.PLUS;
                        break;
                }
                ivStatus.setBackgroundResource(getImageResource(status));
            });
        }

        private int getImageResource(Status status) {
            switch (status) {
                case PLUS:
                    return R.drawable.ic_plus;
                case CHECK:
                    return R.drawable.ic_check;
            }
            return -1;
        }

        private enum Status {
            PLUS,
            CHECK
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private Context mContext;
        private String mPhotoPath;

        public MyGestureListener(Context context, String photoPath) {
            mContext = context;
            mPhotoPath = photoPath;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            // Only returning of true here can make the other gestures to work.
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("TAG", "onSingleTapConfirmed: ");

            Dialog dialog = createFullScreenPhotoDialog(mContext, mPhotoPath);
            dialog.show();

            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

    }

    private static Dialog createFullScreenPhotoDialog(Context context, String photoPath) {
        // Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);

        // Get the layout inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rooView = inflater.inflate(R.layout.photo_book_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(rooView);

        ImageView image = rooView.findViewById(R.id.iv_dialog_image);

        // Set the resource for the image view
        Glide.with(image)
                .load("file://" + photoPath)
                .into(image);

        return builder.create();
    }
}
