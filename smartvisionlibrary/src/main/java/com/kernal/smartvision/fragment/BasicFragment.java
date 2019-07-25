package com.kernal.smartvision.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.isseiaoki.simplecropview.util.Logger;
import com.kernal.smartvision.R;
import com.kernal.smartvision.activity.PictureRecogActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by WenTong on 2018/12/29.
 */

 public class BasicFragment extends Fragment {

    private static final String KEY_FRAME_RECT = "FrameRect";
    private static final String KEY_SOURCE_URI = "SourceUri";

    private CropImageView mCropView;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    private RectF mFrameRect = null;
    private Uri mSourceUri = null;

    FragmentSelectPicture fragmentSelectPicture;
    private boolean isCropping = false;

    public BasicFragment() {
    }

    public static BasicFragment newInstance() {
        BasicFragment fragment = new BasicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crop_layout, null, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FragmentSelectPicture) {
            fragmentSelectPicture = (FragmentSelectPicture) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentSelectPicture");
        }
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        mCropView.setDebug(false);
        mCropView.setHandleShowMode(CropImageView.ShowMode.SHOW_ALWAYS);
        mCropView.setCropMode(CropImageView.CropMode.FREE);

        /**
         * 定义裁切框样式
         */
        //定义边框宽度
        mCropView.setFrameStrokeWeightInDp(2);
        //定义边框颜色
        mCropView.setFrameColor(ContextCompat.getColor(this.getContext().getApplicationContext(),R.color.cropFrameColor));
        //定义四角圆点的大小
        mCropView.setHandleSizeInDp((int) getResources().getDimension(R.dimen.handle_size));
        //定义四角圆点颜色
        mCropView.setHandleColor(ContextCompat.getColor(this.getContext().getApplicationContext(),R.color.cropFrameColor));


        if (savedInstanceState != null) {
            // restore data
            mFrameRect = savedInstanceState.getParcelable(KEY_FRAME_RECT);
            mSourceUri = savedInstanceState.getParcelable(KEY_SOURCE_URI);
        }

        Bundle bundle = getArguments();
        String picPath = bundle.getString("DATA");

        mSourceUri = Uri.parse(picPath);
        mCropView.load(mSourceUri)
                .initialFrameRect(mFrameRect)
                .useThumbnail(true)
                .execute(mLoadCallback);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save data
        outState.putParcelable(KEY_FRAME_RECT, mCropView.getActualCropRect());
        outState.putParcelable(KEY_SOURCE_URI, mCropView.getSourceUri());
    }

    private void bindViews(View view) {
        mCropView = (CropImageView) view.findViewById(R.id.cropImageView);
        view.findViewById(R.id.buttonDone).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonFree).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonRotateLeft).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonRotateRight).setOnClickListener(btnListener);
    }

 public void cropImage() {
        mCropView.crop(mSourceUri).execute(mCropCallback);
    }

    public Uri createSaveUri() {
        return createNewUri(getContext(), mCompressFormat);
    }

    public static String getDirPath() {
        String dirPath = "";
        File imageDir = null;
        File extStorageDir = Environment.getExternalStorageDirectory();
        if (extStorageDir.canWrite()) {
            imageDir = new File(extStorageDir.getPath() + "/DCIM/Camera/");
        }
        if (imageDir != null) {
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            if (imageDir.canWrite()) {
                dirPath = imageDir.getPath();
            }
        }
        return dirPath;
    }

    public static Uri createNewUri(Context context, Bitmap.CompressFormat format) {
        long currentTimeMillis = System.currentTimeMillis();
        Date today = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String title = dateFormat.format(today);
        String dirPath = getDirPath();
        String fileName = "scv" + title + "." + getMimeType(format);
        String path = dirPath + "/" + fileName;
        File file = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + getMimeType(format));
        values.put(MediaStore.Images.Media.DATA, path);
        long time = currentTimeMillis / 1000;
        values.put(MediaStore.MediaColumns.DATE_ADDED, time);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, time);
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length());
        }
        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return uri;
    }

    public static String getMimeType(Bitmap.CompressFormat format) {
        switch (format) {
            case JPEG:
                return "jpeg";
            case PNG:
                return "png";
        }
        return "png";
    }

    // Handle button event /////////////////////////////////////////////////////////////////////////
    private final View.OnClickListener btnListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            if (isCropping){
                return;
            }
            int i = v.getId();
            if (i == R.id.buttonDone) {
                isCropping = true;
                cropImage();
            }  else if (i == R.id.buttonFree) {
                mCropView.setCropMode(CropImageView.CropMode.FREE);
            } else if (i == R.id.buttonRotateLeft) {
                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);

            } else if (i == R.id.buttonRotateRight) {
                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
            }
        }
    };


    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override public void onSuccess() {
        }

        @Override public void onError(Throwable e) {
        }
    };

    private final CropCallback mCropCallback = new CropCallback() {
        @Override public void onSuccess(Bitmap cropped) {
            mCropView.save(cropped)
                    .compressFormat(mCompressFormat)
                    .execute(createSaveUri(), mSaveCallback);
        }

        @Override public void onError(Throwable e) {
        }
    };

    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override public void onSuccess(Uri outputUri) {
            if (fragmentSelectPicture != null && outputUri != null){
                String picPathString = com.kernal.smartvision.utils.Utills.getPath(BasicFragment.this.getContext().getApplicationContext(), outputUri);
                fragmentSelectPicture.startRecog(picPathString);
            }
        }

        @Override public void onError(Throwable e) {
        }
    };


    public interface FragmentSelectPicture{
        public void startRecog(String path);
    }
}