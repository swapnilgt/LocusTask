package com.example.locustask.form;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.locustask.R;
import com.example.locustask.data.AppMockData;
import com.example.locustask.data.pojo.FormItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormFragment extends Fragment implements FormContract.View {

    private static final String TAG = "FormFragment";

    @BindView(R.id.rvFormList)
    RecyclerView recyclerView;

    private OnFragmentInteractionListener mActivityListener;
    private FormContract.Presenter mPresenter;

    private View mView;

    private FormAdapter mAdapter;

    public FormFragment() {
        // Required empty public constructor
    }

    private final FormAdapter.ListItemListener mItemClickLister = new FormAdapter.ListItemListener() {
        @Override
        public void onClickDelete(String id, int position) {
            mPresenter.clickDeletePhoto(id, position);
        }

        @Override
        public void onClickTakePicture(String id, int position) {
            Log.d(TAG, "Inside onClickTakePicture() with position: " + position);
            mPresenter.clickPhotoPlaceholder(id, position);
        }

        @Override
        public void updateComment(String comment, int position) {
            mPresenter.updateComment(comment, position);
        }

        @Override
        public void toggleComment(boolean newState, int position) {
            mPresenter.toggleComment(newState, position);
        }

        @Override
        public void updateRadioButton(int newSelectedIndex, int position) {
            Log.d(TAG, "The new selected index is: " + newSelectedIndex);
            mPresenter.updateSelectedIndex(newSelectedIndex, position);
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FormFragment.
     */
    public static FormFragment newInstance() {
        FormFragment fragment = new FormFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        mAdapter = new FormAdapter(new ArrayList<>(), mItemClickLister);

        mPresenter = new FormPresenter(this, AppMockData.getInstance());

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment_form, container, false);

            ButterKnife.bind(this, mView);

            recyclerView.setAdapter(mAdapter);

            final LinearLayoutManager lm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    lm.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(lm);

            mPresenter.loadData();

        }

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mActivityListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityListener = null;
    }

    @Override
    public void openEnlargedPhoto(String filePath) {
        Log.d(TAG, "Inside openEnlargedPhoto()");
        mActivityListener.openEnlargedPicture(filePath);
    }

    public void printDetails() {
        mPresenter.printDetails();
    }

    @Override
    public void launchCameraForClickingPhoto() {
        mActivityListener.launchCameraForPhoto();
    }


    @Override
    public void setList(List<FormItem> list) {
        mAdapter.setFormItems(list);
    }

    @Override
    public void updateItemInList(int position) {
        mAdapter.updateItem(recyclerView, position);
    }

    public void saveImageBitmap(final Bitmap bitmap) {
        mPresenter.saveImageTaken(bitmap);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void launchCameraForPhoto();
        void openEnlargedPicture(String filePath);
    }
}
