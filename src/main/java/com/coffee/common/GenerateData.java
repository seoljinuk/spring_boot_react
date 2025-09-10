package com.coffee.common;

import com.coffee.constant.Category;
import com.coffee.entity.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class GenerateData {
    //  Spring Boot 테스트가 아닌 일반 JUnit 테스트라면 application.properties가 로드되지 않습니다.
    private String imageDir = "C:\\\\shop\\\\images"; // 예: C:\\shop\\images

    public Product createProduct(int index, String imageName) {
        Product product = new Product();

        switch (index % 3) {
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
        return product;
    }

    private int getRandomDataRange(int start, int end) {
        // start <= somedata <= end
        return new Random().nextInt(end) + start;
    }

    private String getDescriptionData(String name) {
        String[] description = {"엄청 달아요.", "맛있어요.", "맛없어요.", "떫어요.", "아주 떫어요.", "새콤해요.", "아주 상큼해요."};
        return name + "는(은) " + description[new Random().nextInt(description.length)];
    }

    private String getProductName() {
        String[] fruits = {"아메리카노", "바닐라라떼", "우유", "에스프레소", "크로아상", "치아바타", "당근케익"};
        return fruits[new Random().nextInt(fruits.length)];
    }

    public List<String> getImageFileNames() {

        File folder = new File(imageDir);
        List<String> imageFiles = new ArrayList<>();

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("해당 폴더가 존재하지 않습니다: " + imageDir);
            return imageFiles;
        }

        String[] imageExtensions = {".jpg", ".jpeg", ".png"};
        File[] fileList = folder.listFiles();

        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile() && Arrays.stream(imageExtensions)
                        .anyMatch(ext -> file.getName().toLowerCase().endsWith(ext))) {
                    imageFiles.add(file.getName());
                }
            }
        }
        return imageFiles;
    }
}
