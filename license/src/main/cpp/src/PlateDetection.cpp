//
// Created by 庾金科 on 20/09/2017.
//
#include "../include/PlateDetection.h"
#include "util.h"

namespace pr{

    PlateDetection::PlateDetection(std::string filename_cascade){
        cascade.load(filename_cascade);

    };

    void PlateDetection::plateDetectionRough(cv::Mat InputImage,std::vector<pr::PlateInfo>  &plateInfos,int min_w,int max_w){

        cv::Mat processImage;
        cv::cvtColor(InputImage,processImage,cv::COLOR_BGR2GRAY);
        std::vector<cv::Rect> platesRegions;
        cv::Size minSize(min_w,min_w/4);
        cv::Size maxSize(max_w,max_w/4);
        cascade.detectMultiScale(processImage, platesRegions, 1.1, 3, cv::CASCADE_SCALE_IMAGE,minSize,maxSize);
        for(auto plate:platesRegions) {
            int zeroadd_w  = static_cast<int>(plate.width*0.28);
            int zeroadd_h = static_cast<int>(plate.height*1.2);
            int zeroadd_x = static_cast<int>(plate.width*0.14);
            int zeroadd_y = static_cast<int>(plate.height*0.6);
            plate.x-=zeroadd_x;
            plate.y-=zeroadd_y;
            plate.height += zeroadd_h;
            plate.width += zeroadd_w;
            cv::Mat plateImage = util::cropFromImage(InputImage,plate);
            PlateInfo plateInfo(plateImage,plate);
            plateInfos.push_back(plateInfo);
        }
    }
}//namespace pr
