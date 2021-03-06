#include <jni.h>
#include <string>

#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>

using namespace cv;
using namespace std;




//#define INPUT_FILE              "1.jpg"
//#define OUTPUT_FOLDER_PATH      string("")


//JNIEXPORT jint JNICALL
//Java_com_example_user_ocv_test1_convertNativeGray(JNIEnv *env, jobject instance, jlong matAddrRgba,
                                                  //jlong matAddrGray) {

    // TODO

//}






//int argc,
extern "C"
JNIEXPORT jstring JNICALL Java_com_example_user_ocv_SecondActivity_stringFromJNI(JNIEnv *env, jobject, jstring path);

JNIEXPORT jstring JNICALL Java_com_example_user_ocv_SecondActivity_stringFromJNI(JNIEnv *env, jobject, jstring path) {

    const char *nativeString = env->GetStringUTFChars(path, 0);

    //char buf[128];
    //const char *str = (*env) -> GetStringUTFChars(env, path, 0);

    //char *strcat(char *path, const char *src);
    //string imgname="/images.jpg";
    //const helloWorld2 = path + 'World!';

    string imgname=nativeString+'/img.jpg';

    Mat large = imread(imgname);
    Mat rgb;
    // downsample and use it for processing
    pyrDown(large, rgb);
    Mat small;
    cvtColor(rgb, small, CV_BGR2GRAY);
    // morphological gradient
    Mat grad;
    Mat morphKernel = getStructuringElement(MORPH_ELLIPSE, Size(3, 3));
    morphologyEx(small, grad, MORPH_GRADIENT, morphKernel);
    // binarize
    Mat bw;
    threshold(grad, bw, 0.0, 255.0, THRESH_BINARY | THRESH_OTSU);
    // connect horizontally oriented regions
    Mat connected;
    morphKernel = getStructuringElement(MORPH_RECT, Size(9, 1));
    morphologyEx(bw, connected, MORPH_CLOSE, morphKernel);
    // find contours
    Mat mask = Mat::zeros(bw.size(), CV_8UC1);
    vector<vector<Point> > contours;
    vector<Vec4i> hierarchy;
    findContours(connected, contours, hierarchy, CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE, Point(0, 0));
    // filter contours
    for(int idx = 0; idx >= 0; idx = hierarchy[idx][0])
    {
        Rect rect = boundingRect(contours[idx]);
        Mat maskROI(mask, rect);
        maskROI = Scalar(0, 0, 0);
        // fill the contour
        drawContours(mask, contours, idx, Scalar(255, 255, 255), CV_FILLED);
        // ratio of non-zero pixels in the filled region
        double r = (double)countNonZero(maskROI)/(rect.width*rect.height);

        if (r > .45 /* assume at least 45% of the area is filled if it contains text */
            &&
            (rect.height > 8 && rect.width > 8) /* constraints on region size */
            /* these two conditions alone are not very robust. better to use something
            like the number of significant peaks in a horizontal projection as a third condition */
                )
        {
            rectangle(rgb, rect, Scalar(0, 255, 0), 2);
        }
    }

    //string imgname2=strcat(path, "/abc1.jpg");
    string imgname2=nativeString+'/abc1.jpg';

    imwrite(imgname2, rgb);

    //imwrite(OUTPUT_FOLDER_PATH + string("img.jpg"), rgb);

    (env)->ReleaseStringUTFChars(path, nativeString);


    return 0;
}
//, _TCHAR* argv[]




