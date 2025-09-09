package com.coffee.test;

import com.coffee.common.GenerateData;
import com.coffee.entity.Product;
import com.coffee.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProductTest {
    @Autowired
    private ProductRepository productRepository ;

    @Test
    public void testCreateProducts(){
        List<String> imageFileList = GenerateData.getImageFileNames();

        for(int i = 0 ; i < imageFileList.size() ; i++ ){
            Product bean = GenerateData.createProduct(i, imageFileList.get(i));

            productRepository.save(bean) ; // 데이터 베이스에 저장
        }
    }
}
