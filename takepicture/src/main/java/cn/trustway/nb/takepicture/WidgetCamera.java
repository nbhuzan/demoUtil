package cn.trustway.nb.takepicture;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.R.attr.width;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by huzan on 2017/6/21.
 * 描述：相机控件
 */

public class WidgetCamera extends DialogFragment implements View.OnClickListener, SurfaceHolder.Callback, CompoundButton.OnCheckedChangeListener {
    SurfaceView sv;
    ImageButton imageButton_takepic;
    ImageButton imageButton_changePre;
    CheckBox checkBox_light;
    ImageView imageView_photo;
    TextView textView_submit;
    TextView textView_cancel;

    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头
    private String picPath;  //保存文件夹
    private SurfaceHolder holder;
    private boolean isLightOn = false; //是否开启闪光灯

    private Camera mCamera;

    private Context context;

    private OnToolListener onToolListener;

    private String filePath;//保存的文件路径
    private Bitmap bitmap;
    private View view;


    public void setOnToolListener(OnToolListener onToolListener) {
        this.onToolListener = onToolListener;
    }

    public void init(Context context) {
        this.context = context;
//        initView();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            Uri selectImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(selectImageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            byte[] bytes = getBytes(picturePath);

            //压缩
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
            int height = options.outHeight;
            int width = options.outWidth;
            int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
            int minLen = Math.min(height, width); // 原图的最小边长
            if (minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
                float ratio = (float) minLen / 100.0f; // 计算像素压缩比例
                inSampleSize = (int) ratio;
            }
            options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
            options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
            Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

            filePath = picPath + "/" + System.currentTimeMillis() + ".jpeg";
            File mFile = new File(filePath);
            FileOutputStream fos = null;
            BufferedOutputStream os = null;
            try {
                boolean newFile = mFile.createNewFile();
                if (newFile) {
                    fos = new FileOutputStream(mFile);
                    os = new BufferedOutputStream(fos);
                    Matrix m = new Matrix();


                    Bitmap transformed = Bitmap.createBitmap(
                            mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, true
                    );
                    transformed.compress(Bitmap.CompressFormat.JPEG, 50, os);
                    os.flush();
                    os.close();
                    fos.close();

                    bitmap = transformed;
                    mBitmap.recycle();
                    transformed.recycle();

                    imageButton_changePre.setVisibility(View.GONE);
                    textView_submit.setVisibility(View.VISIBLE);
                    imageButton_takepic.setVisibility(View.INVISIBLE);
                    imageButton_takepic.setEnabled(false);
                }
            } catch (IOException e) {
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                submit();
            }
        }




    }

    public static byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.widget_camera, container);
        initView();
        setPreview();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    public void setPreview() {
        try {
            doCamera();
        } catch (Exception e) {
            System.out.println("相机初始化失败");
        }
    }


    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    private void initView() {
        sv = view.findViewById(R.id.sv);
        sv.setZOrderOnTop(true);

        imageButton_takepic = view.findViewById(R.id.imagebutton_camera_takepic);
        imageButton_changePre = view.findViewById(R.id.imagebutton_camera_changepre);
        checkBox_light = view.findViewById(R.id.checkbox_camera_light);
        imageView_photo = view.findViewById(R.id.imagebutton_camera_photo);
        textView_submit = view.findViewById(R.id.textview_camera_submit);
        textView_cancel = view.findViewById(R.id.textview_camera_cancel);
//        imageView_photo.setVisibility(View.GONE);
        checkBox_light.setVisibility(View.VISIBLE);
        textView_cancel.setOnClickListener(this);
        imageButton_takepic.setOnClickListener(this);
        imageButton_changePre.setOnClickListener(this);
        checkBox_light.setOnCheckedChangeListener(this);
        textView_submit.setOnClickListener(this);
        sv.setZOrderMediaOverlay(true);
        imageView_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1000);
            }
        });
    }


    /**
     * 获取相机资源
     *
     * @return carema
     */
    private Camera getCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

        int cameraIndex = 0;
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                cameraIndex = i;
                cameraPosition = 1;
                break;
            }
        }

        Camera camera = null;

        try {
            camera = Camera.open(cameraIndex);
        } catch (Exception e) {
            Log.d(TAG, "getCamera: 打开相机失败");
        }

        return camera;//打开当前选中的摄像头
    }

    /**
     * 创建时间：2017/6/16
     * 创建者：huzan
     * 描述：c初始化camera
     */
    public void doCamera() throws Exception {
        mCamera = getCamera();
        sv.setVisibility(View.VISIBLE);
        holder = sv.getHolder();
        holder.addCallback(this);
        sv.setFocusable(true);
        sv.setFocusableInTouchMode(true);

        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Camera.Parameters param = mCamera.getParameters();
//                    List<Camera.Size> supportedPictureSizes = param.getSupportedPreviewSizes();
                    param.setPictureFormat(ImageFormat.JPEG);


                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sv.getLayoutParams();
                    layoutParams.width = width;
//                    layoutParams.height = height;
                    sv.setLayoutParams(layoutParams);
//                    param.setPreviewSize(width, height);
//                    param.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//
//                    mCamera.autoFocus(null);
                    mCamera.setParameters(param);
                } catch (Exception e) {
                }
            }
        });
    }


    /**
     * 预览
     */
    private void startPreview(Camera c, SurfaceHolder h) {
        try {
            if (sv.getVisibility() == View.GONE) {
                sv.setVisibility(View.VISIBLE);
            }
            c.setPreviewDisplay(holder);
            c.setDisplayOrientation(90);
            c.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "startPreview: " + "预览失败");
        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
            sv.setVisibility(View.GONE);
        }
    }

    /**
     * Create by Zheming.xin on 2017/7/13 13:45
     * param:
     * description: 强制转换前置摄像头
     */
    public void changePreCameraFront() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                releaseCamera();
                mCamera = Camera.open(i);//打开当前选中的摄像头
                startPreview(mCamera, holder);//通过surfaceview显示取景画面

                mCamera.startPreview();//开始预览
                cameraPosition = 1;
                break;
            }
        }
    }

    /**
     * 创建时间：2017/6/16
     * 创建者：huzan
     * 描述：切换前后摄像头
     */
    public void changePreCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraPosition == 1) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    releaseCamera();
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    startPreview(mCamera, holder);//通过surfaceview显示取景画面

                    mCamera.startPreview();//开始预览
                    cameraPosition = 0;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    releaseCamera();
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    startPreview(mCamera, holder);//通过surfaceview显示取景画面

                    mCamera.startPreview();//开始预览
//                    cameraPosition = 0;
                    cameraPosition = 1;
                    break;
                }
            }

        }
    }


    /**
     * 创建时间：2017/6/21
     * 创建者：huzan
     * 描述：拍照
     */
    private void takePic() {
        File file = new File(picPath);
        if (!file.exists()) {
            boolean isMake = file.mkdirs();
            if (!isMake) {
                Toast.makeText(context, "文件夹创建失败", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        try {
            final Camera.Parameters param = mCamera.getParameters();
            List<Camera.Size> supportedPictureSizes = param.getSupportedPictureSizes();
            param.setPictureFormat(ImageFormat.JPEG);
            param.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            Camera.Size size = supportedPictureSizes.get(supportedPictureSizes.size() / 2);
            param.setPictureSize(size.width, size.height);
            if (isLightOn) {
                param.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            }
            mCamera.setParameters(param);
        } catch (Exception e) {
            System.out.println(1);
        }
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, Camera camera) {
                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {

                        //压缩
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
                        int height = options.outHeight;
                        int width = options.outWidth;
                        int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
                        int minLen = Math.min(height, width); // 原图的最小边长
                        if (minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
                            float ratio = (float) minLen / 100.0f; // 计算像素压缩比例
                            inSampleSize = (int) ratio;
                        }
                        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
                        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
                        Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                        filePath = picPath + "/" + System.currentTimeMillis() + ".jpeg";
                        File mFile = new File(filePath);
                        FileOutputStream fos = null;
                        BufferedOutputStream os = null;
                        try {
                            boolean newFile = mFile.createNewFile();
                            if (newFile) {
                                fos = new FileOutputStream(mFile);
                                os = new BufferedOutputStream(fos);
                                Matrix m = new Matrix();

                                if (cameraPosition == 0) {
                                    m.setRotate(270);
                                } else {
                                    m.setRotate(90);
                                }
                                Bitmap transformed = Bitmap.createBitmap(
                                        mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, true
                                );
                                transformed.compress(Bitmap.CompressFormat.JPEG, 50, os);
                                os.flush();
                                os.close();
                                fos.close();

                                bitmap = transformed;
                                mBitmap.recycle();
                                transformed.recycle();

                                imageButton_changePre.setVisibility(View.GONE);
                                textView_submit.setVisibility(View.VISIBLE);
                                imageButton_takepic.setVisibility(View.INVISIBLE);
                                imageButton_takepic.setEnabled(false);
                            }
                        } catch (IOException e) {
                        } finally {
                            try {
                                if (fos != null) {
                                    fos.close();
                                }
                                if (os != null) {
                                    os.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

        });
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        imageButton_takepic.setEnabled(true);
        startPreview(mCamera, holder);
//        changePreCameraFront();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mCamera.stopPreview();
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.imagebutton_camera_takepic) {
            takePic();

        } else if (i == R.id.imagebutton_camera_changepre) {
            changePreCamera();

        } else if (i == R.id.textview_camera_submit) {
            submit();

        } else if (i == R.id.textview_camera_cancel) {//需要判断是否已经拍照
            if (textView_submit.getVisibility() == View.VISIBLE) {
                imageButton_takepic.setVisibility(View.VISIBLE);
                imageButton_takepic.setEnabled(true);
                imageButton_changePre.setVisibility(View.VISIBLE);
                textView_submit.setVisibility(View.GONE);
                startPreview(mCamera, holder);
            } else {
                //退出
                releaseCamera();
                if (onToolListener != null) {
                    onToolListener.onExit();
                }
            }
            dismiss();

        } else {
        }
    }

    /**
     * 创建时间：2017/6/24
     * 创建者：huzan
     * 描述：使用照片
     */
    private void submit() {
        releaseCamera();
        imageButton_takepic.setVisibility(View.VISIBLE);
        imageButton_takepic.setEnabled(true);
        imageButton_changePre.setVisibility(View.VISIBLE);
        textView_submit.setVisibility(View.GONE);
        dismiss();
        if (onToolListener != null) {
            onToolListener.onSubmit(filePath, bitmap);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(context, "你的手机没有闪光灯!", Toast.LENGTH_LONG).show();
            return;
        }
        if (mCamera != null) {
            isLightOn = isChecked;
        }
    }

    public interface OnToolListener {
        void onExit();

        void onSubmit(String path, Bitmap bitmap);
    }
}
