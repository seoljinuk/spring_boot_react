package com.coffee.common;

import com.coffee.constant.Category;
import com.coffee.entity.Product;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GenerateData {
    // 윈도우 폴더 구분자는 \인데 특수 문자이므로 반드시 \\로 표기해야 합니다.
    private static final String IMAGE_DIR = "c:\\boot\\images"; // 이미지가 있는 폴더 경로


    public static Product createProduct(int index, String imageName) { // make Product bean object
        Product product = new Product();

        switch (index%3){
            case 0:
                product.setCategory(Category.BREAD);
                break;
            case 1:
                product.setCategory(Category.BEVERAGE);
                break;
            case 2:
                product.setCategory(Category.CAKE);
                break;
        }

        String productName = getProductName();
        product.setName(productName);
        String description = getDescriptionData(productName);
        product.setDescription(description);
        product.setImage(imageName);
        product.setPrice(1000 * getRandomDataRange(1, 10));
        product.setStock(111 * getRandomDataRange(1, 9));
        LocalDate sysdate = LocalDate.now();
        product.setInputdate(sysdate.minusDays(index));
        return product ;
    }

    private static int getRandomDataRange(int start, int end){
        // start <= somedata <= end
        return new Random().nextInt(end) + start ;
    }


    private static String getDescriptionData(String name){
        String[] description = {"엄청 달아요.", "맛있어요.", "맛없어요.", "떫어요.", "아주 떫어요.", "새콤해요.", "아주 상큼해요."};
        return name + "는(은) " + description[new Random().nextInt(description.length)] ;
    }

    private static String getProductName(){
        String[] fruits = {"아메리카노", "바닐라라떼", "우유", "에스프레소", "크로아상", "치아바타", "당근케익"};
        return fruits[new Random().nextInt(fruits.length)] ;
    }


    public static List<String> getImageFileNames() {
        // 특정 폴더 내에 들어 있는 모든 이미지 파일을 문자열 List 형식으로 반환합니다.
        File folder = new File(IMAGE_DIR) ;
        List<String> imageFiles = new ArrayList<String>();

        if(folder.exists() == false && folder.isFile() ){
            System.out.println("해당 폴더가 존재하지 않습니다. " + IMAGE_DIR );
            return imageFiles ;
        }

        // 확장자가 다음 항목인 파일들만 추출
        String[] imageExtensions = {".jpg", ".jpeg", ".png"} ;

        File[] fileList = folder.listFiles() ;

        for(File  file : fileList){
            // 자바의 stream API 공부해야 합니다.
            if (file.isFile() && Arrays.stream(imageExtensions)
                    .anyMatch(ext -> file.getName().toLowerCase().endsWith(ext))) {
                imageFiles.add(file.getName());
            }
        }

        return imageFiles ;
    }
}
