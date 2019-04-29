package com.example.locustask.form;

import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.locustask.R;
import com.example.locustask.data.pojo.FormItem;
import com.google.gson.JsonArray;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

public class FormAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "FromAdapter";

    private List<FormItem> mList;
    private final ListItemListener mListener;


    FormAdapter(List<FormItem> mList, ListItemListener mListener) {
        this.mList = mList;
        this.mListener = mListener;
    }

    void updateItem(RecyclerView rv, int position) {
        rv.post(() -> {
            notifyItemChanged(position);
        });
    }

    void setFormItems(List<FormItem> newItems) {
        mList = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case FormItem.TYPE_PHOTO_INT:
                return new PhotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_photo_item, parent, false));
            case FormItem.TYPE_COMMENT_INT:
                return new CommentsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false), mListener);
            case FormItem.TYPE_SINGLE_CHOICE_INT:
                return new OptionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_options_item, parent, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof PhotoViewHolder) {
            bindPhotoViewHolder((PhotoViewHolder) holder, position);
        } else if(holder instanceof CommentsViewHolder) {
            bindCommentViewHolder((CommentsViewHolder) holder, position);
        } else if(holder instanceof OptionViewHolder) {
            bindOptionViewHolder((OptionViewHolder) holder, position);
        }
    }

    private void bindPhotoViewHolder(@NonNull PhotoViewHolder holder, int position) {

        final FormItem item = mList.get(position);

        if(item.isImageExists()) {
            Glide.with(holder.pic.getContext()).load(
                    item.getPicFilePath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.pic);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.pic.setVisibility(View.VISIBLE);
            holder.instruction.setVisibility(View.INVISIBLE);
            holder.deleteButton.setOnClickListener(view -> {
                mListener.onClickDelete(item.getId(), position);
            });
        } else {
            holder.deleteButton.setVisibility(View.INVISIBLE);
            holder.pic.setVisibility(View.INVISIBLE);
            holder.instruction.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(view -> {
                Log.d(TAG, "Inside adapter, the click position is: " + position);
                mListener.onClickTakePicture(item.getId(), position);
            });
        }
    }

    private void bindCommentViewHolder(@NonNull CommentsViewHolder holder, int position) {
        final FormItem item = mList.get(position);

        // Updating the current position ....
        holder.currPosition = position;

        holder.title.setText(item.getTitle());
        holder.toggleSwitch.setChecked(item.isCommentActive());

        holder.toggleSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            mListener.toggleComment(b, position);
            if(b) {
                holder.comment.setVisibility(View.VISIBLE);
            } else {
                holder.comment.setVisibility(View.INVISIBLE);
            }
        });

        if(item.getComment() != null) {
            holder.comment.setText(item.getComment());
        } else {
            holder.comment.setText("");
        }

        if(item.isCommentActive()) {
            holder.comment.setVisibility(View.VISIBLE);
        } else {
            holder.comment.setVisibility(View.INVISIBLE);
        }
    }

    private void bindOptionViewHolder(@NonNull OptionViewHolder holder, int position) {

        final FormItem item = mList.get(position);

        // Removing the existing children from radio group ....
        holder.title.setText(item.getTitle());

        // Deleting the old children ...
        Log.d(TAG, "The number of child are:"+ holder.radioGroup.getChildCount());
        int count = holder.radioGroup.getChildCount();
        if(count>0) {
            for (int i=count-1;i>=0;i--) {
                View o = holder.radioGroup.getChildAt(i);
                if (o instanceof RadioButton) {
                    holder.radioGroup.removeViewAt(i);
                }
            }
        }

        // Now populating new children ...
        final JsonArray options = item.getDataMap().getAsJsonArray("options");
        for(int i = 0; i < options.size(); i++) {
            final RadioButton button = new RadioButton(holder.itemView.getContext());
            button.setText(options.get(i).toString());
            holder.radioGroup.addView(button);

            Log.d(TAG, "i  is " + i + " and item.getChoiceIndex() is: " + item.getChoiceIndex());
            if(item.getChoiceIndex() != null && item.getChoiceIndex() == i) {
                Log.d(TAG, "We enter here........");
                button.setChecked(true);
            }
        }

        Log.d(TAG, "The choiceIndex is: " + item.getChoiceIndex());

        // Now ... adding listener to change ...
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                if(radioButtonID >= 0) {
                    View radioButton = radioGroup.findViewById(radioButtonID);
                    int idx = radioGroup.indexOfChild(radioButton);

                    mListener.updateRadioButton(idx, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getTypeInt();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivImage)
        ImageView pic;
        @BindView(R.id.ibClose)
        View deleteButton;
        @BindView(R.id.tvDirection)
        TextView instruction;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            setIsRecyclable(false);
            ButterKnife.bind(this, itemView);
        }
    }

    static class OptionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rbButtonGroup)
        RadioGroup radioGroup;
        @BindView(R.id.tvTitle)
        TextView title;

        OptionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class CommentsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView title;
        @BindView(R.id.swToggleComment)
        SwitchCompat toggleSwitch;
        @BindView(R.id.etComment)
        AppCompatEditText comment;

        int currPosition;

        PublishSubject<String> subject;

        ListItemListener listener;

        CommentsViewHolder(@NonNull View itemView, @NonNull ListItemListener listItemListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            subject = PublishSubject.create();

            listener = listItemListener;

            comment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    //Log.d(TAG, "The text has changed to a new text: " + editable.toString());
                    // the user is done typing.
                    listener.updateComment(editable.toString(), currPosition);
                }
            });

            comment.setImeOptions(EditorInfo.IME_ACTION_DONE);
            comment.setRawInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    interface ListItemListener {
        void onClickDelete(String id, int position);
        void onClickTakePicture(String id, int position);

        void updateComment(String comment, int position);
        void toggleComment(boolean newState, int position);

        void updateRadioButton(int newSelectedIndex, int position);
    }
}
